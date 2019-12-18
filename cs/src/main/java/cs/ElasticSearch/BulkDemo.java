package cs.ElasticSearch;

import org.apache.commons.lang.NullArgumentException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.Date;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/15.
 * @Description :
 */
public class BulkDemo {

    public static void main(String[] args) throws Exception {

        BulkDemo bd = new BulkDemo();
        bd.add(1,1);

    }

    @Transactional(rollbackFor = Exception.class)
    public void add(int a, int b) throws Exception {
        //创建客户端client
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.144.128", 9200, "http")));

        //创建BulkProcessor.Listener
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                //每次执行BulkRequest之前都会调用此方法
                //在每次执行BulkRequest之前调用，这个方法允许知道在BulkRequest中将要执行的操作的数量
                int numberOfActions = request.numberOfActions();
                System.out.println("Executing bulk "+executionId+" with %s requests "+numberOfActions);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  BulkResponse response) {
                //每次执行BulkRequest后都会调用此方法
                if (response.hasFailures()) { //在每次执行BulkRequest后调用，该方法允许知道BulkResponse是否包含错误
                    System.out.println("Bulk "+ executionId +" executed with failures");
                } else {
                    System.out.println("Bulk "+executionId+" completed in "+response.getTook().getMillis()+" milliseconds");
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  Throwable failure) {
                //当批量请求失败时调用此方法
                System.out.println("Failed to execute bulk "+failure.getMessage()); //如果BulkRequest失败，则调用该方法，该方法允许知道失败
            }
        };

        //通过从BulkProcessor.Builder调用build()方法来创建BulkProcessor。client.bulkAsync()方法将用于在机罩下执行BulkRequest。
        BulkProcessor bulkProcessor = BulkProcessor.builder(
                (request, bulkListener) ->
                        client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                listener).build();
        //BulkProcessor.Builder 供了配置批量处理器如何处理请求执行的方法:
//        BulkProcessor.Builder builder1 = BulkProcessor.builder(
//                (request, bulkListener) ->
//                        client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
//                listener);
//        builder1.setBulkActions(500); //根据当前添加的操作数设置刷新新批量请求的时间(默认值为1000，-1禁用)
//        builder1.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB)); //根据当前添加的操作大小设置刷新新批量请求的时间(默认为5Mb，-1禁用)
//        builder1.setConcurrentRequests(0); //设置允许执行的并发请求数(默认为1，0仅允许执行单个请求)
//        builder1.setFlushInterval(TimeValue.timeValueSeconds(10L)); //设置一个刷新间隔，如果间隔过去，刷新任何待处理的批量请求(默认为未设置)
//        builder1.setBackoffPolicy(BackoffPolicy
//                .constantBackoff(TimeValue.timeValueSeconds(1L), 3)); //设置一个恒定的后退策略，最初等待1秒钟，最多重试3次。有关更多选项，请参见BackoffPolicy.noBackoff(), BackoffPolicy.constantBackoff()和BackoffPolicy.exponentialBackoff().
        //一旦批量处理器被创建，可以向它添加请求:
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
//        {
//            builder.field("onState", "c");
//            builder.field("recordEndTime", "2008-05-09 13:39:45");
//            builder.field("channelEndTime", "2008-05-09 13:39:45");
//            builder.field("channelStartTime", "2008-05-09 13:37:12");
//            builder.field("recordStartTime", "2008-05-09 13:37:23");
//            builder.field("callnumber", "15955446965");
//            builder.field("uuid", "2028c7b9-df93-4fb1-ba3d-12d4b7cb4997");
//            builder.field("recordPath", "/home/ccrobot/data/callinfo/finsh/records/2019/05/09/13/2028c7b9-ca50-4fb1-ba3d-12d4.wav");
//        }
        {
            builder.field("a", "A");
            builder.field("b", "B");
            builder.field("c", "C");
        }
        builder.endObject();
        IndexRequest one = new IndexRequest("class").id("7")
                .source(builder);
        IndexRequest two = new IndexRequest("class").id("8")
                .source(builder);
//        IndexRequest three = new IndexRequest("class").id("3")
//                .source(builder);
        bulkProcessor.add(one);
        bulkProcessor.flush();
//        if(a == b) throw new Exception();
        bulkProcessor.add(two);
        bulkProcessor.flush();
//        bulkProcessor.add(three);
//        bulkProcessor.flush();
        bulkProcessor.close();
        Thread.sleep(1000);
        client.close();
    }

}
