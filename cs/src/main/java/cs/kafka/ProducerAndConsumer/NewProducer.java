package cs.kafka.ProducerAndConsumer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/7.
 * @Description :
 */

//创建生产者，同步发送
public class NewProducer {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        //Kafka服务端主机名及端口号
        properties.put("bootstrap.servers","192.168.144.128:9092");
        //等待所有副本节点的应答
        properties.put("acks","all");
        //消息发送最大尝试次数
        properties.put("retries","0");
        //一批消息处理大小
        properties.put("batch.size",16384);
        //增加服务端请求延时
        properties.put("linger.ms",1);
        //发送缓存区内存大小
        properties.put("buffer.memory",33554432);
        //key序列化类
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        //value序列化类
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        //自定义分区策略类
        properties.put("partition.class","cs.kafka.ProducerAndConsumer.CustomPartitioner");
        Producer<String,String> producer = new KafkaProducer<>(properties);
        for(int i = 0; i < 50; i++) {
            producer.send(new ProducerRecord<>("first",Integer.toString(i),"Hello World - "+i)).get();
        }
        producer.close();
    }

}
