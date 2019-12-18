package cs.ElasticSearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/12.
 * @Description :
 */
public class ESDemo {

    /**
     * 获得RestHighLevelClient实例
     * @return
     */
    public static RestHighLevelClient getRestHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.144.128", 9200, "http")));
        return client;
    }

    /**
     * 用来处理搜索结果，转换成链表
     * @param builder
     * @return
     */
    public static List<String> listSearchResult(SearchSourceBuilder builder) {
        // 提交查询
        SearchRequest searchRequest = new SearchRequest("es");
        searchRequest.source(builder);
        RestHighLevelClient client = getRestHighLevelClient();

        // 获得response
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 从response中获得结果
        List<String> list = new LinkedList<>();
        SearchHits hits = searchResponse.getHits();
        Iterator<SearchHit> iterator = hits.iterator();
        while(iterator.hasNext()) {
            SearchHit next = iterator.next();
            list.add(next.getSourceAsString());
        }
        return list;
    }

}
