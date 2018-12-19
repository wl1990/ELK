package com.test.esdemo.client;

import com.test.esdemo.exception.ElasticSearchException;
import com.test.esdemo.pool.ElasticSearchConnectionManager;
import com.test.esdemo.pool.ElasticSearchPool;
import org.apache.http.Header;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author : jingma2
 * @date :2018/7/21
 * @description
 */
@Component
public class ElasticSearchClient {

    private ElasticSearchPool pool;

    public ElasticSearchClient() throws IOException, URISyntaxException {
        System.out.println("-------init es------");
        ElasticSearchConnectionManager.load();
        this.pool = ElasticSearchConnectionManager.sharePool();
        long start = System.currentTimeMillis();
        TransportClient client = pool.getResource();
        List<DiscoveryNode> discoveryNodeList = client.connectedNodes();
        for(DiscoveryNode discoveryNode : discoveryNodeList){
            System.out.println("args = [" + discoveryNode.getHostName() + "]");
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时(ms)："+(end-start));
    }

    private ElasticSearchClient(String clusterName){
        this.pool = ElasticSearchConnectionManager.sharePool(clusterName);
    }

   /* public boolean ping(){
       *//* import org.elasticsearch.client.RestHighLevelClient;
        client = null;
        try{
            client = getResource();
            boolean result = client.ping();
            returnResource(client);
            return result;
        }catch(Exception e){
            returnBrokenResource(client);
            return false;
        }*//*
    }*/

   /* public MainResponse info(){
        RestHighLevelClient client = null;
        try{
            client = getResource();
            MainResponse result = client.info();
            returnResource(client);
            return result;
        }catch(Exception e){
            returnBrokenResource(client);
            throw new ElasticSearchException(e);
        }
    }*/

  /*  public boolean exists(GetRequest getRequest, Header... headers){
        RestHighLevelClient client = null;
        try{
            client = getResource();
            boolean result = client.exists(getRequest,headers);
            returnResource(client);
            return result;
        }catch(Exception e){
            returnBrokenResource(client);
            throw new ElasticSearchException(e);
        }
    }*/

   /* public GetResponse get(GetRequest getRequest, Header... headers){
        RestHighLevelClient client = null;
        try{
            client = getResource();
            GetResponse result = client.get(getRequest,headers);
            returnResource(client);
            return result;
        }catch(Exception e){
            returnBrokenResource(client);
            throw new ElasticSearchException(e);
        }
    }*/

    public TransportClient getResource(){
        TransportClient client = null;
        try{
            client = pool.getResource();
            return client;
        }catch(RuntimeException e){
            if(client!=null) {
            //    returnBrokenResource(client);
            }
            throw new ElasticSearchException(e);
        }
    }

   /* public void returnResource(RestHighLevelClient client){
        try{
            if(client!=null){
                this.pool.returnResource(client);
            }
        }catch(Exception e){
            this.pool.returnBrokenResource(client);
        }
    }*/

    public void returnResource(TransportClient client){
        try{
            if(client!=null){
                this.pool.returnResource(client);
            }
        }catch(Exception e){
            this.pool.returnBrokenResource(client);
        }
    }

    /*public void returnBrokenResource(RestHighLevelClient client){
        pool.returnBrokenResource(client);
    }

    public static void main(String[] args) {
        try {
            ElasticSearchClient elasticSearchClient = new ElasticSearchClient();
            RestHighLevelClient restHighLevelClient = elasticSearchClient.getResource();
            createIndex(restHighLevelClient);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Object getIndexData(RestHighLevelClient restHighLevelClient,String indexName,String typeName,String docName) throws IOException {
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("exe","run exec");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(matchQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(5);

        SearchRequest searchRequest = new SearchRequest(indexName);//限定index
        searchRequest.types(typeName);//限定type
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        System.out.println(searchResponse);
        return null;
    }

    public static void createIndex(RestHighLevelClient restHighLevelClient) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("mylog");
        request.settings(Settings.builder()
                .put("index.number_of_shards",10)
                .put("index.number_of_replicas",1));
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request);
        System.out.println("CreateIndexResponse = [" + createIndexResponse + "]");
    }*/



}
