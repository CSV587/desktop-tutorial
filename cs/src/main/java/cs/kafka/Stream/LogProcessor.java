package cs.kafka.Stream;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/8.
 * @Description :
 */
public class LogProcessor implements Processor<byte[],byte[]> {

    private ProcessorContext context;

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
    }

    @Override
    public void process(byte[] bytes, byte[] bytes2) {
        String input = new String(bytes2);
        //如果包含">>>"则只保留该标记后面的内容
        if(input.contains(">>>")){
            input = input.split(">>>")[1].trim();
            //输出到下一个topic
            context.forward("logProcessor".getBytes(),input.getBytes());
        } else {
            context.forward("logProcessor".getBytes(),input.getBytes());
        }
    }

    @Override
    public void close() {

    }
}
