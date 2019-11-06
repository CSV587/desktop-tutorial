package cs.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.io.IOException;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/5.
 * @Description :
 */
//使用redis中的滑动时间窗口实现简单的用户行为限制功能，如：限制某用户的某种行为在指定时间内只能允许发生N次（斗鱼一分钟最多只能发5条弹幕）
public class SimpleLimiter {

    private Jedis jedis;
    private Pipeline pipe;
    public SimpleLimiter(Jedis jedis){
        this.jedis= jedis;
        pipe = jedis.pipelined();
    }

    public boolean isActionAllowed(String userId,String actionKey,int period,int maxCount) throws IOException {
        String key = String.format("hist:%s:%s",userId,actionKey);
        //毫秒时间戳
        long nowTs = System.currentTimeMillis();
        //Redis事务，要使用pipeline执行事务,相关方法有：multi()开启事务，exec()执行事务，discard()丢弃事务
        pipe.multi();
        //记录用户行为
        pipe.zadd(key,nowTs,""+nowTs);
        //移除时间窗口之前的行为记录，剩下的都是时间窗口之内的
        pipe.zremrangeByScore(key,0,nowTs - period * 1000);
        //获取时间窗口内的行为数量
        Response<Long> count = pipe.zcard(key);
        pipe.expire(key,period + 1);
        pipe.exec();
        pipe.close();
        return count.get() <= maxCount;
    }

    public static void main(String[] args) throws IOException {
        Jedis jedis = new Jedis("192.168.144.128");
        SimpleLimiter limiter = new SimpleLimiter(jedis);
        for(int i = 0; i < 20; i++){
            System.out.println(limiter.isActionAllowed("cs","sex",60,5));
        }
    }

}
