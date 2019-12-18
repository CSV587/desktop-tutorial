package cs.kafka.Listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;

import java.util.List;
import java.util.Optional;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/8.
 * @Description :
 */
public class MyListener {

    private static Logger log = LoggerFactory.getLogger(MyListener.class);
    private static final String TPOIC = "topic02";

    @KafkaListener(id = "id0", topicPartitions = {
            @TopicPartition(topic = TPOIC, partitions = {"0"},
                    partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "100"))
    })
    public void listenPartition0(List<ConsumerRecord<?, ?>> records) {
        log.info("Id0 Listener, Thread ID: " + Thread.currentThread().getId());
        log.info("Id0 records size " + records.size());

        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            log.info("Received: " + record);
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                String topic = record.topic();
                log.info("p0 Received message={}", message);
            }
        }
    }

    @KafkaListener(id = "id1", topicPartitions = {@TopicPartition(topic = TPOIC, partitions = {"1"})})
    public void listenPartition1(List<ConsumerRecord<?, ?>> records) {
        log.info("Id1 Listener, Thread ID: " + Thread.currentThread().getId());
        log.info("Id1 records size " + records.size());

        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            log.info("Received: " + record);
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                String topic = record.topic();
                log.info("p1 Received message={}", message);
            }
        }
    }

}
