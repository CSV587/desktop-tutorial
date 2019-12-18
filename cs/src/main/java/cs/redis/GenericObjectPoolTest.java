package cs.redis;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/12/9.
 * @Description :
 */
public class GenericObjectPoolTest extends GenericObjectPool<Jedis> {

        public GenericObjectPoolTest(JedisFactory factory, GenericObjectPoolConfig config){
            super(factory,config);
        }

        public static void main(String[] args){
            GenericObjectPoolConfig config=new GenericObjectPoolConfig();
            config.setMinIdle(10);
            config.setMaxTotal(10);
            config.setMaxWaitMillis(30000);
            JedisFactory jedisFactory=new JedisFactory();
            GenericObjectPoolTest redisPool=new GenericObjectPoolTest(jedisFactory,config);
            Jedis jedis=null;
            try{
                jedis=redisPool.borrowObject();//获取池中对象
                jedis.select(0);
                jedis.set("test","pooltest");
                System.out.println(redisPool.getNumActive());
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                redisPool.returnObject(jedis);//归还池中对象
                System.out.println(redisPool.getNumActive());
            }

        }

}
