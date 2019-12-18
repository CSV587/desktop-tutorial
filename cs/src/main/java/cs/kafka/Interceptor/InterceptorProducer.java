package cs.kafka.Interceptor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/8.
 * @Description :
 */
public class InterceptorProducer {

    public static void main(String[] args) {
        //设置配置信息
        Properties properties = new Properties();
        properties.put("bootstrap.servers","192.168.144.128:9092");
        properties.put("acks","all");
        properties.put("retries","0");
        properties.put("batch.size",16384);
        properties.put("linger.ms",1);
        properties.put("buffer.memory",33554432);
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");

        //构建拦截链
        List<String> interceptors = new ArrayList<>();
        interceptors.add("cs.kafka.Interceptor.TimeInterceptor");
        interceptors.add("cs.kafka.Interceptor.CounterInterceptor");
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,interceptors);
        Producer<String,String> producer = new KafkaProducer<>(properties);
        for(int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<>("first",Integer.toString(i),"Message - "+i));
        }
        //一定要关闭producer,这样才会调用interceptor的close方法
        producer.close();
    }

}
