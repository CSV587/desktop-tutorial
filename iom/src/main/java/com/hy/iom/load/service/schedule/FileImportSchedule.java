package com.hy.iom.load.service.schedule;

import com.hy.iom.index.bussiness.FileIndexWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@PropertySource("classpath:scheduler.properties")
public class FileImportSchedule {

    @KafkaListener(topics = "${import.oracle.topic}", groupId = "${import.oracle.groupId}")
    public void processMessage(List<ConsumerRecord> records) {
        long startTime = System.currentTimeMillis();
        List<String> recordList = new ArrayList<>();
        for (ConsumerRecord record : records) {
            recordList.add(record.value().toString());
        }
        log.debug("入库{}条数据", recordList.size());
        FileIndexWorker fileIndexWorker = new FileIndexWorker();
        try {
            fileIndexWorker.process(recordList);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("异常入库数据：{}", recordList);
        }
        long endTime = System.currentTimeMillis();
        log.debug("数据入库成功，共耗时{}", endTime - startTime);
    }

}
