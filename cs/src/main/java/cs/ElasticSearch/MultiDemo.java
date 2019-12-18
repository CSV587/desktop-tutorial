package cs.ElasticSearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import java.io.IOException;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/15.
 * @Description :
 */
public class MultiDemo {

    public static void main(String[] args) throws IOException {

        //创建客户端client
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.144.128", 9200, "http")));

        MultiGetRequest request = new MultiGetRequest();
        request.add(new MultiGetRequest.Item(
                "index",         //索引
                "1"));    //文档id
        request.add(new MultiGetRequest.Item("index", "2")); //添加另一个要提取的项目
        request.add(new MultiGetRequest.Item("index", "3")
                .fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE));  //禁用源检索，默认情况下启用
        String[] includes = new String[] {"foo", "*r"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext =
                new FetchSourceContext(true, includes, excludes);
        request.add(new MultiGetRequest.Item("index", "4")
                .fetchSourceContext(fetchSourceContext));  //为特定字段配置源包含
//        String[] includes2 = Strings.EMPTY_ARRAY;
//        String[] excludes2 = new String[] {"foo", "*r"};
//        FetchSourceContext fetchSourceContext2 =
//                new FetchSourceContext(true, includes2, excludes2);
//        request.add(new MultiGetRequest.Item("index", "example_id")
//                .fetchSourceContext(fetchSourceContext2));  //为特定字段配置源排除
        request.add(new MultiGetRequest.Item("index", "example_id")
                .storedFields("foo"));  //配置特定存储字段的检索(要求字段在映射中单独存储)
        MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
        MultiGetItemResponse item = response.getResponses()[0];
        String value = item.getResponse().getField("foo").getValue(); //检索foo存储字段(要求该字段在映射中单独存储)
        request.add(new MultiGetRequest.Item("index", "with_routing")
                .routing("some_routing"));    //路由值
        request.add(new MultiGetRequest.Item("index", "with_version")
                .versionType(VersionType.EXTERNAL)  //版本
                .version(10123L));  //版本类型
        //preference, realtime和refresh可以在主请求上设置，但不能在任何项目上设置:
        request.preference("some_preference");  //偏好值
        request.realtime(false);//将实时标志设置为false(默认true)
        request.refresh(true);//在检索文档之前执行刷新(默认false)

        ActionListener listener = new ActionListener<MultiGetResponse>() {
            @Override
            public void onResponse(MultiGetResponse response) {
                //成功的时候执行
            }
            @Override
            public void onFailure(Exception e) {
                //失败的时候执行
            }
        };
        //异步执行
        client.mgetAsync(request, RequestOptions.DEFAULT, listener); //要执行的多重请求和执行完成时要使用的操作侦听器

    }

}
