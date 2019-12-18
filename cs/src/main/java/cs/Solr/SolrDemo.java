package cs.Solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/12/4.
 * @Description :
 */
public class SolrDemo {

    public static void main(String[] args) throws IOException, SolrServerException {

        //创建solr客户端
        String urlString = "http://192.168.144.128:8888/solr/cs";
        HttpSolrClient solrClient = new HttpSolrClient.Builder(urlString).build();
//**************************************************增**************************************************
//        //创建索引文档对象
//        SolrInputDocument doc = new SolrInputDocument();
//        //第一个参数：域的名称，域的名称必须是在schema.xml中定义的
//        //第二个参数：域的值,注意：id的域不能少
//        doc.addField("id","1");
//        doc.addField("name","红豆");
//        doc.addField("price","1.2");
//        //将文档写入索引库中
//        solrClient.add(doc);
//        solrClient.commit();
//
//        //批量创建索引文档对象
//        SolrInputDocument doc1 = new SolrInputDocument();
//        doc1.addField("id", "2");
//        doc1.addField("name", "绿豆");
//        doc1.addField("price", 1.8);
//        SolrInputDocument doc2 = new SolrInputDocument();
//        doc2.addField("id", "3");
//        doc2.addField("name", "黑豆");
//        doc2.addField("price", 2.6);
//        Collection<SolrInputDocument> docs = new ArrayList<>();
//        docs.add(doc1);
//        docs.add(doc2);
//        //将文档写入索引库中
//        solrClient.add(docs);
//        solrClient.commit();
//        solrClient.close();
//**************************************************查**************************************************
//        // 创建搜索对象
//        SolrQuery query = new SolrQuery();
//        // 设置搜索条件
//        query.set("q","*:*");
//        //设置排序
//        query.setSort("id", SolrQuery.ORDER.asc);
//        //设置每页显示多少条
//        query.setRows(2);
//        //发起搜索请求
//        QueryResponse response = solrClient.query(query);
//        // 查询结果
//        SolrDocumentList docs = response.getResults();
//        // 查询结果总数
//        long cnt = docs.getNumFound();
//        System.out.println("总条数为"+cnt+"条");
//        for (SolrDocument doc : docs) {
//            System.out.println("id:"+ doc.get("id") + ",name:"+ doc.get("name") + ",price:"+ doc.get("price"));
//        }
//        solrClient.close();

        //条件过滤查询
        //封装查询参数
        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("q", "name:黑* AND id:3");
        //添加到SolrParams对象,SolrParams 有一个 SolrQuery 子类，它提供了一些方法极大地简化了查询操作
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);
        //执行查询返回QueryResponse
        QueryResponse response = solrClient.query(queryParams);
        //获取doc文档
        SolrDocumentList docs = response.getResults();
        //查询结果总数
        long cnt = docs.getNumFound();
        System.out.println("总条数为" + cnt + "条");
        //[6]内容遍历
        for (SolrDocument doc : docs) {
            System.out.println("id:" + doc.get("id") + ",name:" + doc.get("name") + ",price:" + doc.get("price"));
        }
        solrClient.close();
//**************************************************改**************************************************
//        //创建索引文档对象
//        SolrInputDocument doc = new SolrInputDocument();
//        //把红豆价格修改为1.5
//        doc.addField("id","1");
//        doc.addField("name","红豆");
//        doc.addField("price","1.5");
//        //将文档写入索引库中
//        solrClient.add(doc);
//        //提交
//        solrClient.commit();
//        solrClient.close();
//**************************************************删**************************************************
//        //全删
//        solrClient.deleteByQuery("*:*");
//        //模糊匹配删除（带有分词效果的删除）
//        solrClient.deleteByQuery("name:红");
//        //指定id删除
//        solrClient.deleteById("1");
//        solrClient.commit();
//        solrClient.close();
//
//        //通过id批量删除
//        ArrayList<String> ids = new ArrayList<>();
//        ids.add("2");
//        ids.add("3");
//        solrClient.deleteById(ids);
//        //提交
//        solrClient.commit();
//        //关闭资源
//        solrClient.close();

    }

}
