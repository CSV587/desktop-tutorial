package cs.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/11.
 * @Description :
 */
public class DistributedLock {

    private static Logger log = LoggerFactory.getLogger(DistributedLock.class);

    public boolean lock(String key, String value, Long lockExpireTimeOut,
                         Long lockWaitTimeOut) {

        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis();
            String realKey = key;
            Long deadTimeLine = System.currentTimeMillis() + lockWaitTimeOut;

            for (;;) {
                //通过set(key,value,NX,EX,timeout)方法合并普通的set()和expire()操作，使其具有原子性
                String result = jedis.set(realKey, value, "NX", "PX", lockExpireTimeOut);

                if ("OK".equals(result)) {
                    return true;
                }

                lockWaitTimeOut = deadTimeLine - System.currentTimeMillis();

                if (lockWaitTimeOut <= 0L) {
                    return false;
                }
            }
        } catch (Exception ex) {
            log.info("lock error");
        } finally {
            RedisUtil.returnResource(jedis);
        }

        return false;
    }

    public boolean unlock(String key, String value) {

        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis();
            String realKey = key;

            //使用lua脚本合并get()和del()操作，使其具有原子性。
            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(luaScript, Collections.singletonList(realKey),
                    Collections.singletonList(value));

            if ("1".equals(result)) {
                return true;
            }

        } catch (Exception ex) {
            log.info("unlock error");
        } finally {
            RedisUtil.returnResource(jedis);
        }
        return false;

    }

}
