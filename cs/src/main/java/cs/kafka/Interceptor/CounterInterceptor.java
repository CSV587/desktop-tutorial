package cs.kafka.Interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/8.
 * @Description :
 */
public class CounterInterceptor implements ProducerInterceptor {

    private int successCounter = 0;
    private int errorCounter = 0;

    @Override
    public ProducerRecord onSend(ProducerRecord producerRecord) {
        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        //统计成功和失败的次数
        if(e == null){
            successCounter++;
        } else {
            errorCounter++;
        }
    }

    @Override
    public void close() {
        //保存结果
        System.out.println("Successful sent:"+successCounter);
        System.out.println("Failed sent:"+errorCounter);
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
