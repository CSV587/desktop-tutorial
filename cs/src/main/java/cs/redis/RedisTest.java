package cs.redis;

import redis.clients.jedis.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/4.
 * @Description :
 */
public class RedisTest {

    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.144.128");
        System.out.println("连接成功");
//        //存储数据到列表中
//        jedis.lpush("site-list", "Runoob");
//        jedis.lpush("site-list", "Google");
//        jedis.lpush("site-list", "Taobao");
//        //获取存储的数据并输出
//        List<String> list = jedis.lrange("site-list", 0 ,2);
//        for(int i=0; i<list.size(); i++) {
//            System.out.println("列表项为: "+list.get(i));
//        }

//        jedis.set("1","ss");
//        jedis.getSet("1","cc");
//        if(jedis.setnx("2","x") == 1){
//            System.out.println("success");
//        } else {
//            System.out.println("failure");
//        }

//        //位图
//        jedis.setbit("hello",1,true);
//        jedis.setbit("hello",2,true);
//        jedis.setbit("hello",4,true);
//        jedis.setbit("hello",9,true);
//        jedis.setbit("hello",10,true);
//        jedis.setbit("hello",13,true);
//        jedis.setbit("hello",15,true);
//        System.out.println(jedis.getbit("hello",1));
//        System.out.println(jedis.getbit("hello",2));
//        System.out.println(jedis.getbit("hello",4));
//        System.out.println(jedis.getbit("hello",3));
//        System.out.println(jedis.getbit("hello",9));
//        System.out.println(jedis.getbit("hello",10));
//        System.out.println(jedis.getbit("hello",13));
//        System.out.println(jedis.getbit("hello",15));
//        System.out.println(jedis.bitcount("hello"));
//        System.out.println(jedis.bitpos("hello",false));
//        System.out.println(jedis.bitfield("hello","get","u4","0","get","u3","2","get","i4","0","set","u8","8","101"));
//        System.out.println(jedis.bitfield("hello","incrby","u4","2","1"));
//        jedis.sadd("user","chenshu1","chenshu2","chenshu3","chenshu1","chenshu4","chenshu3");
//        System.out.println(jedis.scard("user"));

//        jedis.zadd("id",1,"cs1");
//        jedis.zadd("id",2,"cs2");
//        jedis.zadd("id",3,"cs3");
//        jedis.zadd("id",4,"cs3");
//        jedis.zadd("id",5,"cs2");
//        jedis.zadd("id",6,"cs6");
//        Set<String> s  = jedis.zrange("id",0,3);
//        for(String ss : s){
//            System.out.println(ss);
//        }

//        jedis.pfadd("web","web1");
//        jedis.pfadd("web","web2");
//        jedis.pfadd("web","web3");
//        System.out.println(jedis.pfcount("web"));
//        for(int i = 0; i < 100000; i++){
//            jedis.pfadd("web","web"+i);
//        }
//        System.out.println(jedis.pfcount("web"));

//        jedis.geoadd("company",116.48105,39.996794,"juejin");
//        jedis.geoadd("company",116.514203,39.905409,"ireader");
//        jedis.geoadd("company",116.489033,40.007669,"meituan");
//        jedis.geoadd("company",116.562108,39.787602,"jd");
//        jedis.geoadd("company",116.334255,40.027400,"xiaomi");
//        System.out.println(jedis.geodist("company","juejin","ireader", GeoUnit.KM));
//        System.out.println(jedis.geopos("company","meituan","ireader","jd"));
//        GeoRadiusParam g = GeoRadiusParam.geoRadiusParam();
//        g.count(5);
//        g.sortAscending();
//        g.withDist();
//        List<GeoRadiusResponse> r = jedis.georadiusByMember("company","ireader",20,GeoUnit.KM,g);
//        for(GeoRadiusResponse grr : r){
//            System.out.println(grr.getMemberByString()+"---"+grr.getDistance());
//        }
//        System.out.println(jedis.georadiusByMember("company","ireader",10,GeoUnit.KM));

//        System.out.println(jedis.keys("*"));

//        ScanParams sp = new ScanParams();
//        sp.match("*");
//        sp.count(5);
//        ScanResult<String> sr = jedis.scan("0",sp);
//        System.out.println(sr.getResult());

//        System.out.println(jedis.info());

//        jedis.flushDB();
//        jedis.flushAll();
//        jedis.save();

    }

}