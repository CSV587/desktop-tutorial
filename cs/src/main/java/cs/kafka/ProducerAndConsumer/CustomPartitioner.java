package cs.kafka.ProducerAndConsumer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/7.
 * @Description :
 */
public class CustomPartitioner implements Partitioner {

    private int count = 0;

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        //控制分区

        //例：按照奇偶顺序分区
//        String k = (String) o;
//        if(Integer.parseInt(k) % 2 == 0)
//            return 0;
//        else
//            return 1;

        //例：按照可用分区和对key进行散列化取模做负载均衡
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        int num = partitionInfos.size();
        if(key == null) { //若消息没有key，则均衡分布
            int nextValue = ++count;//计数器递增
            List<PartitionInfo> availablePartitions = cluster.availablePartitionsForTopic(topic);
            for(PartitionInfo p : availablePartitions) {
                System.out.println(p.topic()+"---"+p.partition());
            }
            if(availablePartitions.size() > 0) {
                int part = nextValue % availablePartitions.size();
                return availablePartitions.get(part).partition();
            }
        } else { //若消息有key，对key进行散列化后取模确定分区
            return Utils.murmur2(keyBytes) % num;
        }

        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
