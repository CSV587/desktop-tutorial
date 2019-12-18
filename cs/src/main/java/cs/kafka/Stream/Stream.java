package cs.kafka.Stream;

import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/8.
 * @Description :
 */
public class Stream {

    public static void main(String[] args){
        String from = "first";
        String to = "second";

        Properties setting = new Properties();
        setting.put(StreamsConfig.APPLICATION_ID_CONFIG,"logFilter");
        setting.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.144.128:9092");
        StreamsConfig config = new StreamsConfig(setting);

        //构建拓扑
        Topology topology = new Topology();
        topology.addSource("SOURCE",from)
                .addProcessor("PROCESS",new ProcessorSupplier<byte[],byte[]>(){
                    @Override
                    public Processor<byte[], byte[]> get() {
                        //具体分析处理
                        return new LogProcessor();
                    }
                },"SOURCE")
                .addSink("SINK",to,"PROCESS");

        //创建kafka stream
        KafkaStreams streams = new KafkaStreams(topology,config);
        streams.start();
    }

}
