package ElasticsearchClient;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/29.
 * @Description :
 */

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ElasticSearchController {

    @Autowired
    private TransportClient client;

    //搜索自动联想
    @GetMapping("/auto")
    @ResponseBody
    public List<ElasticSearchResult> findIndexRecordByName(@RequestParam(name = "key") String key) {
        // 构造查询请求
        QueryBuilder bq = QueryBuilders.matchQuery("name.pinyin", key);
        SearchRequestBuilder searchRequest = client.prepareSearch("medcl").setTypes("folks");

        // 设置查询条件和分页参数
        int start = 0;
        int size = 5;
        searchRequest.setQuery(bq).setFrom(start).setSize(size);

        // 获取返回值，并进行处理
        SearchResponse response = searchRequest.execute().actionGet();
        SearchHits shs = response.getHits();
        List<ElasticSearchResult> esResultList = new ArrayList<>();
        for (SearchHit hit : shs) {
            ElasticSearchResult esResult = new ElasticSearchResult();
            double score = hit.getScore();
            BigDecimal b = new BigDecimal(score);
            score = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            String name = (String) hit.getSourceAsMap().get("name");
            System.out.println("score:" + score + "name:" + name);
            esResult.setScore(score);
            esResult.setName(name);
            esResultList.add(esResult);
        }
        return esResultList;
    }

}
