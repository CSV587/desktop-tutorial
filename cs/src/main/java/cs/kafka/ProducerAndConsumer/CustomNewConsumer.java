package cs.kafka.ProducerAndConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/7.
 * @Description :
 */

//自动提交位移
public class CustomNewConsumer {

    public static void main(String[] args) {
        Properties properties = new Properties();
        //定义Kafka服务的地址，不需要将所有的broker指定上
        properties.put("bootstrap.servers","192.168.144.128:9092");
        //制定consumer group
        properties.put("group.id","test");
        //是否自动提交offset
        properties.put("enable.auto.commit","true");
        //自动提交offset的时间间隔
        properties.put("auto.commit.interval.ms","1000");
        //key的序列化类
        properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        //value的序列化类
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        //定义consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        //消费者订阅的topic，可以同时订阅多个
        consumer.subscribe(Arrays.asList("first","second","third"));
//        //自定义指定topic的指定分区
//        TopicPartition tp1 = new TopicPartition("first",0);
//        TopicPartition tp2 = new TopicPartition("second",1);
//        //指定消费topic的哪个分区
//        //assign与subscribe的区别：assign的consumer不会拥有kafka的group management机制，也就是当group内消费者数量变化的时候不会有reblance行为发生
//        consumer.assign(Arrays.asList(tp1,tp2));
//        //指定从topic的分区的某个offset开始消费
//        consumer.seek(tp1,4);
//        consumer.seek(tp2,6);
        while(true) {
            //读取数据，读取超时时间为100ms
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String,String> record : records) {
                System.out.printf("offset=%d,key=%s,value=%s%n",record.offset(),record.key(),record.value());
            }
        }
    }

}
