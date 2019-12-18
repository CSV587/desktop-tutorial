package cs.redis;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import redis.clients.jedis.Jedis;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/12/9.
 * @Description :
 */
public class JedisFactory extends BasePooledObjectFactory<Jedis> {

        public PooledObject<Jedis> makeObject(){
            Jedis jedis = new Jedis("192.168.144.128",6379);
            jedis.connect();
            System.out.println(jedis.isConnected());
            return new DefaultPooledObject<Jedis>(jedis);
        }

        public void destroyObject(Jedis jedis){
            jedis.close();
        }

        public boolean validateObject(Jedis jedis) {
            if(jedis.isConnected()){
                return true;
            } else {
                return false;
            }

        }

        @Override
        public PooledObject<Jedis> wrap(Jedis arg0) {
            return null;
        }

        @Override
        public Jedis create() throws Exception {
            return null;
        }

}

