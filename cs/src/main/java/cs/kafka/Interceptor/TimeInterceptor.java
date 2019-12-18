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
public class TimeInterceptor implements ProducerInterceptor<String,String> {
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        //创建一个新的record，把时间戳写入消息体的最前部
        return new ProducerRecord(producerRecord.topic(),producerRecord.partition(),producerRecord.timestamp(),
                producerRecord.key(),System.currentTimeMillis()+","+producerRecord.value());
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
