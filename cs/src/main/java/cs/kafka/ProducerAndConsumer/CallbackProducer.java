package cs.kafka.ProducerAndConsumer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/7.
 * @Description :
 */

//创建生产者带回调函数，实现异步发送，调用send()方法之后可以继续发送消息不用等待
public class CallbackProducer {

    public static void main(String[] args) throws InterruptedException {
        Properties properties = new Properties();
        properties.put("bootstrap.servers","192.168.144.128:9092");
        properties.put("acks","all");
        properties.put("retries","0");
        properties.put("batch.size",16384);
        properties.put("linger.ms",1);
        properties.put("buffer.memory",33554432);
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);
        for(int i = 0; true; i++) {
            producer.send(new ProducerRecord<>("first", Integer.toString(i), "hello - " + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if(recordMetadata != null) {
                        System.err.println(recordMetadata.partition()+"___"+recordMetadata.offset());
                    }
                }
            });
            Thread.sleep(100);
        }
//        producer.close();
    }

}
