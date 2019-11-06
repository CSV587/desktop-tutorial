package cs.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import java.util.List;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/6.
 * @Description :
 */

//Redis事务
public class RedisTransaction {

    public static void main(String[] args) {

        Jedis jedis = new Jedis("192.168.144.128");
        String userId = "abc";
        String key = keyFor(userId);
        jedis.setnx(key,String.valueOf(5));
        System.out.println(doubleAccount(jedis,userId));
        jedis.close();

    }

    public static int doubleAccount(Jedis jedis, String id) {
        String key = keyFor(id);
        //使用watch机制监控变量，实现乐观锁
        while(true) {
            jedis.watch(key);
            int value = Integer.parseInt(jedis.get(key));
            value *= 2;
            Transaction tx = jedis.multi();
            tx.set(key,String.valueOf(value));
            List<Object> ll = tx.exec();
            if(ll != null) {//若事务执行失败，会返回null，以此作为自旋跳出条件
                break;
            }
        }
        return Integer.parseInt(jedis.get(key));
    }

    public static String keyFor(String id) {
        return String.format("account_{%s}",id);
    }

}
