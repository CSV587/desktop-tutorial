package cs.ElasticSearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/15.
 * @Description :
 */
public class CRUDDemo {

    public static void main(String[] args) throws IOException {

        //创建客户端client
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.144.128", 9200, "http")));

        //IndexResponse侦听器
        ActionListener indexListener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {//执行成功的时候调用
                System.out.println("Index Succeed!");
            }
            @Override
            public void onFailure(Exception e) {//执行失败的时候调用
                System.out.println("Index Failed!");
            }
        };
        //GetResponse侦听器
        ActionListener<GetResponse> getListener = new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse getResponse) {//执行成功的时候调用
                System.out.println("Get Succeed!");
            }
            @Override
            public void onFailure(Exception e) {//出错的时候调用
                System.out.println("Get Failed!");
            }
        };

        ActionListener<Boolean> existListener = new ActionListener<Boolean>() {
            @Override
            public void onResponse(Boolean exists) {//执行成功的时候调用
                System.out.println("Get Succeed!");
            }
            @Override
            public void onFailure(Exception e) {//出错的时候调用
                System.out.println("Get Failed!");
            }
        };
        //DeleteResponse侦听器
        ActionListener<DeleteResponse> deleteListener = new ActionListener<DeleteResponse>() {
            @Override
            public void onResponse(DeleteResponse deleteResponse) {//执行成功的时候调用
                System.out.println("Delete Succeed!");
            }
            @Override
            public void onFailure(Exception e) {//出错的时候调用
                System.out.println("Delete Failed!");
            }
        };



        //++++++++++++++++++++++++++++++++++++++++添加新文档++++++++++++++++++++++++++++++++++++++++
        IndexRequest indexRequest = new IndexRequest("posts"); //创建索引

        //可选参数
        indexRequest.routing("routing"); //路由值
        indexRequest.timeout(TimeValue.timeValueSeconds(1)); //设置超时
        indexRequest.timeout("1s"); ////以字符串形式设置超时时间
        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL); //以WriteRequest.RefreshPolicy实例形式设置刷新策略
        indexRequest.setRefreshPolicy("wait_for");//以字符串形式刷新策略
        indexRequest.version(2); //文档版本
        indexRequest.versionType(VersionType.EXTERNAL); //文档类型
        indexRequest.opType(DocWriteRequest.OpType.CREATE); //操作类型
        indexRequest.opType("create"); //操作类型 可选create或update
        indexRequest.setPipeline("pipeline"); //索引文档之前要执行的摄取管道的名称

        //同步执行:当以此方式执行IndexRequest时，客户端在继续执行代码之前，会等待返回索引响应
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

        //检索关于已执行操作的信息
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {//处理创建文档的情况
            System.out.println(indexResponse.getIndex()+"创建了"+indexResponse.getId());
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {//处理文档更新的情况
            System.out.println(indexResponse.getIndex()+"更新了"+indexResponse.getId());
        }

        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {//处理成功的分片数少于总分片数时的情况

        }
        if (shardInfo.getFailed() > 0) {//处理潜在的故障
            for (ReplicationResponse.ShardInfo.Failure failure :
                    shardInfo.getFailures()) {
                String reason = failure.reason();
            }
        }

        //异步执行:异步方法不会阻塞并立即返回。一旦完成，如果执行成功完成，则使用onResponse方法回调操作侦听器，如果执行失败，则使用onFailure方法回调操作侦听器。失败场景和预期异常与同步执行情况相同
        client.indexAsync(indexRequest, RequestOptions.DEFAULT, indexListener);// listener是执行完成时要使用的侦听器

        //方式1:直接传递json数据
        indexRequest.id("1"); //文档id
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2019-11-15\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        indexRequest.source(jsonString, XContentType.JSON); //以字符串形式提供的文档源

        //方式2:以map构建内容
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        indexRequest.id("1").source(jsonMap); //以Map形式提供的文档源，可自动转换为JSON格式

        //方式3:使用XConttentBuilder构建内容
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "kimchy");
            builder.timeField("postDate", new Date());
            builder.field("message", "trying out Elasticsearch");
        }
        builder.endObject();
        indexRequest.id("1").source(builder);

        //方式4:简单粗暴
        indexRequest.id("1")
                .source("user", "kimchy",
                        "postDate", new Date(),
                        "message", "trying out Elasticsearch");



        //++++++++++++++++++++++++++++++++++++++++获取文档++++++++++++++++++++++++++++++++++++++++
        GetRequest getRequest = new GetRequest(
                "posts", //索引名称
                "1");   //文档id
        //可选参数
        getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE); //禁用源检索，默认情况下启用
        //为特定字段配置源包含
        String[] includes1 = new String[]{"message", "*Date"};
        String[] excludes1 = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext1 =
                new FetchSourceContext(true, includes1, excludes1);
        getRequest.fetchSourceContext(fetchSourceContext1);
        //为特定字段配置源排除
        String[] includes2 = Strings.EMPTY_ARRAY;
        String[] excludes2 = new String[]{"message"};
        FetchSourceContext fetchSourceContext2 =
                new FetchSourceContext(true, includes2, excludes2);
        getRequest.fetchSourceContext(fetchSourceContext2);
        getRequest.storedFields("message"); //配置特定存储字段的检索(要求字段在映射中单独存储)
        getRequest.routing("routing"); //路由值
        getRequest.preference("preference"); //偏好值
        getRequest.realtime(false); //将realtime标志设置为false
        getRequest.refresh(true); //在检索文档之前执行刷新(默认为false)
        getRequest.version(2); //版本
        getRequest.versionType(VersionType.EXTERNAL); //版本类型
        //同步执行
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        //检索请求的文档及其元数据和最终存储的字段
        String index = getResponse.getIndex();
        String id = getResponse.getId();
        String message = getResponse.getField("message").getValue(); //检索消息存储字段(要求该字段单独存储在映射中)
        if (getResponse.isExists()) {
            long version = getResponse.getVersion();
            String sourceAsString = getResponse.getSourceAsString(); //以字符串形式检索文档
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap(); //以Map<String, Object>的形式检索文档
            byte[] sourceAsBytes = getResponse.getSourceAsBytes(); //以byte[]形式检索文档
        } else {}
        //异步执行
        client.getAsync(getRequest, RequestOptions.DEFAULT, getListener); //要执行的GetRequest和执行完成时要使用的ActionListener



        //++++++++++++++++++++++++++++++++++++++++检查文档是否存在++++++++++++++++++++++++++++++++++++++++
        GetRequest getRequest2 = new GetRequest(
                "posts", //索引
                "1");    //文档id
        getRequest.fetchSourceContext(new FetchSourceContext(false)); //禁用fetching _source
        getRequest.storedFields("_none_");
        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        client.existsAsync(getRequest, RequestOptions.DEFAULT, existListener); //要执行的GetRequest和执行完成时要使用的ActionListener



        //++++++++++++++++++++++++++++++++++++++++删除文档++++++++++++++++++++++++++++++++++++++++
        DeleteRequest deleteRequest = new DeleteRequest(
                "posts",    //索引
                "1");       //文档id
        //可选参数
        deleteRequest.routing("routing"); //路由值
        deleteRequest.timeout(TimeValue.timeValueMinutes(2)); //以TimeValue形式设置超时
        deleteRequest.timeout("2m");  //以字符串形式设置超时
        deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL); //以WriteRequest.RefreshPolicy实例的形式设置刷新策略
        deleteRequest.setRefreshPolicy("wait_for");  //以字符串的形式设置刷新策略
        deleteRequest.version(2); //版本
        deleteRequest.versionType(VersionType.EXTERNAL); //版本类型
        //同步执行
        DeleteResponse deleteResponse = client.delete(
                deleteRequest, RequestOptions.DEFAULT);
        //异步执行
        client.deleteAsync(deleteRequest, RequestOptions.DEFAULT, deleteListener); //要执行的删除请求和执行完成时要使用的操作侦听器
        String index2 = deleteResponse.getIndex();
        String id2 = deleteResponse.getId();
        long version = deleteResponse.getVersion();
        //碎片分析
        ReplicationResponse.ShardInfo shardInfo2 = deleteResponse.getShardInfo();
        if (shardInfo2.getTotal() != shardInfo2.getSuccessful()) {
            //处理成功分片数少于总分片数的情况
        }
        if (shardInfo2.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure :
                    shardInfo.getFailures()) {//处理潜在的故障
                String reason = failure.reason();
            }
        }



        //++++++++++++++++++++++++++++++++++++++++更新文档++++++++++++++++++++++++++++++++++++++++
        UpdateRequest updateRequest = new UpdateRequest(
                "posts", //索引
                "1");   //文档id

        //可选参数
        updateRequest.routing("routing"); //路由值
        updateRequest.timeout(TimeValue.timeValueSeconds(1)); //设置超时
        updateRequest.timeout("1s"); ////以字符串形式设置超时时间
        updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL); //以WriteRequest.RefreshPolicy实例形式设置刷新策略
        updateRequest.setRefreshPolicy("wait_for");//以字符串形式刷新策略
        updateRequest.retryOnConflict(3); //如果要更新的文档在更新操作的获取和索引阶段之间被另一个操作更改，重试更新操作的次数
        updateRequest.fetchSource(true); //启用源检索，默认情况下禁用
        String[] includes3 = new String[]{"updated", "r*"};
        String[] excludes3 = Strings.EMPTY_ARRAY;
        updateRequest.fetchSource(
                new FetchSourceContext(true, includes3, excludes3)); //为特定字段配置源包含
        String[] includes4 = Strings.EMPTY_ARRAY;
        String[] excludes4 = new String[]{"updated"};
        updateRequest.fetchSource(
                new FetchSourceContext(true, includes4, excludes4)); //为特定字段配置源排除
        updateRequest.setIfSeqNo(2L); //ifSeqNo
        updateRequest.setIfPrimaryTerm(1L); //ifPrimaryTerm
        updateRequest.detectNoop(false); //禁用noop检测
        updateRequest.scriptedUpsert(true); //指出无论文档是否存在，脚本都必须运行，即如果文档不存在，脚本负责创建文档。
        updateRequest.docAsUpsert(true); //指示如果部分文档尚不存在，则必须将其用作upsert文档。
        updateRequest.waitForActiveShards(2); //设置在继续更新操作之前必须活动的碎片副本数量。
        updateRequest.waitForActiveShards(ActiveShardCount.ALL); //ActiveShardCount的碎片副本数。可选值：ActiveShardCount.ALL, ActiveShardCount.ONE或者 ActiveShardCount.DEFAULT

        //方式1:使用json
        String jsonString2 = "{" +
                "\"updated\":\"2017-01-01\"," +
                "\"reason\":\"daily update\"" +
                "}";
        updateRequest.doc(jsonString2, XContentType.JSON); //以JSON格式的字符串形式提供的部分文档源

        //方式2:map
        Map<String, Object> jsonMap2 = new HashMap<>();
        jsonMap2.put("updated", new Date());
        jsonMap2.put("reason", "daily update");
        updateRequest.doc(jsonMap2); //作为Map提供的部分文档源会自动转换为JSON格式

        //方式3:使用XContentBuilder
        XContentBuilder builder2 = XContentFactory.jsonBuilder();
        builder2.startObject();
        {
            builder2.timeField("updated", new Date());
            builder2.field("reason", "daily update");
        }
        builder2.endObject();
        updateRequest.doc(builder2);  //作为XContentBuilder对象提供的部分文档源，弹性搜索内置帮助器，用于生成JSON内容

        //方式4:直接插入键值对
        updateRequest.doc("updated", new Date(),
                        "reason", "daily update"); //作为键值对提供的部分文档源，转换为JSON格式

        //同步异步执行，碎片检查。。。。。。同上

    }

}
