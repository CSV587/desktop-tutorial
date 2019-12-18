package cs.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/9.
 * @Description :
 */
public class KafkaTemplateDemo {

    public static void main(String[] args){

        ApplicationContext applicationContext = SpringApplication.run(KafkaTemplateDemo.class, args);
        KafkaTemplate template = (KafkaTemplate) applicationContext.getBean("kafkaTemplate");
        final ProducerRecord<String, String> record = new ProducerRecord<String, String>("first","1","cs");

        //加入回调，实现异步(非阻塞)发送消息
        ListenableFuture<SendResult<Integer, String>> future = template.send(record);
        future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                //handleSuccess(data);
                System.out.println("Success!");
            }

            @Override
            public void onFailure(Throwable ex) {
                //handleFailure(data, record, ex);
                System.out.println("Failed!");
            }

        });


        //同步(阻塞)发送消息
        try {
            template.send(record).get(10, TimeUnit.SECONDS);
            //handleSuccess(data);
            System.out.println("Success!");
        }
        catch (ExecutionException e) {
            //handleFailure(data, record, e.getCause());
            System.out.println("Success!");
        }
        catch (TimeoutException | InterruptedException e) {
            //handleFailure(data, record, e);
            System.out.println("Failed!");
        }


        //执行事务
        template.executeInTransaction(t -> {
            t.send("1","1");
            t.send("2","2");
            return true;
        });

    }

}
