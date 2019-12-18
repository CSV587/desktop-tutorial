package cs.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/11.
 * @Description :
 */
public class BadLock {

    private static Logger log = LoggerFactory.getLogger(DistributedLock.class);

    public static boolean lock(String key, String value) {
        Jedis jedis = null;
        Long lockWaitTimeOut = 200L;
        Long deadTimeLine = System.currentTimeMillis() + lockWaitTimeOut;

        try {
            jedis = RedisUtil.getJedis();
            String realKey = key;

            for (;;) {
                if (jedis.setnx(realKey, value) == 1) {
                    return true;
                }

                String currentValue = jedis.get(realKey);

                // if lock is expired
                if (!StringUtils.isEmpty(currentValue) &&
                        Long.valueOf(currentValue) < System.currentTimeMillis()) {
                    // gets last lock time
                    String oldValue = jedis.getSet(realKey, value);

                    if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                        return true;
                    }
                }

                lockWaitTimeOut = deadTimeLine - System.currentTimeMillis();

                if (lockWaitTimeOut <= 0L) {
                    return false;
                }
            }
        } finally {
            RedisUtil.returnResource(jedis);
        }
    }

    public void unlock(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis();
            String realKey = key;
            String currentValue = jedis.get(realKey);

            if (!StringUtils.isEmpty(currentValue)
                    && value.equals(currentValue)) {
                jedis.del(realKey);
            }
        } catch (Exception ex) {
            log.info("unlock error");
        } finally {
            RedisUtil.returnResource(jedis);
        }
    }

}
