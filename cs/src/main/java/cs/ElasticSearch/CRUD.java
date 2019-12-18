package cs.ElasticSearch;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cs.ElasticSearch.ESDemo.getRestHighLevelClient;
import static cs.ElasticSearch.ESDemo.listSearchResult;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/12.
 * @Description :
 */
public class CRUD {

    /**
     * 根据单个属性查询
     * @param key
     * @param value
     * @param length
     * @return
     */
    public static List<String> simpleSearch(String key,Object value,int length) {
        // 使用上面已经编写好的方法
        Map<String,Object> map = new HashMap<>();
        map.put(key,value);
        return multiSearch(map,length);
    }

    /**
     * 多条件查询
     * @param mustMap
     * @param length
     * @return
     */
    public static List<String> multiSearch(Map<String,Object> mustMap, int length) {
        // 根据多个条件 生成 boolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 循环添加多个条件
        for (Map.Entry<String, Object> entry : mustMap.entrySet()) {
            boolQueryBuilder.must(QueryBuilders
                    .matchQuery(entry.getKey(), entry.getValue()));
        }

        // boolQueryBuilder生效
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.size(length);

        // 其中listSearchResult是自己编写的方法，以供多中查询方式使用。
        return listSearchResult(searchSourceBuilder);
    }

    /**
     * 根据时间段去查询
     * @param length
     * @return
     */
    public static List<String> searchByDate(Date from, Date to, int length) {
        // 生成builder
        RangeQueryBuilder rangeQueryBuilder =
                QueryBuilders.rangeQuery("date").from(from).to(to);

        /// boolQueryBuilder生效
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(rangeQueryBuilder);
        searchSourceBuilder.size(length);

        return listSearchResult(searchSourceBuilder);
    }

    /**
     * 删除es的整个数据库
     * @param
     * @return
     * @throws IOException
     */
    public static boolean delete() throws IOException {
        RestHighLevelClient client = getRestHighLevelClient();
        DeleteIndexRequest request =
                new DeleteIndexRequest("es");
        client.indices().delete(request, RequestOptions.DEFAULT);
        return true;
    }

    /**
     * 后文段模糊查找方法，可以理解为 like value
     * @param key
     * @param prefix
     * @param size
     * @return
     */
    public static List<String> fuzzy(String key, String prefix,int size) {
        PrefixQueryBuilder builder = QueryBuilders.prefixQuery(key, prefix);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(size);
        searchSourceBuilder.query(builder);
        return listSearchResult(searchSourceBuilder);
    }

    public static void main(String[] args){

    }

}
