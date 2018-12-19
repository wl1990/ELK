package com.test.esdemo.index;


import com.test.esdemo.client.ElasticSearchClient;
import com.test.esdemo.dto.UrlSceneMapDto;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * es 索引初始化
 */
@Component
public class EsIndexInit {
    @Autowired
    private ElasticSearchClient elasticSearchClient;

    @PostConstruct
    public void initEsIndex() {
        try {
            TransportClient client =elasticSearchClient.getResource();

            Set<String> apiAnalyzeFields = new HashSet<>();
            apiAnalyzeFields.add("url");
            apiAnalyzeFields.add("desc");
            // url scene mapping init
            if(!indexIsExists(client,"indexname")){
                createIndexMapping(client,"indexname","type",5,1,UrlSceneMapDto.class.getDeclaredFields(),apiAnalyzeFields);
            }


        }catch(Exception e){

        }
    }


    private static void addIndex(TransportClient client,String indexName,int shards,int replicas){
        Settings settings = Settings.builder()
                .put("number_of_shards", shards)
                .put("number_of_replicas", replicas).build();
        CreateIndexResponse  indexresponse = client.admin().indices()
                .prepareCreate(indexName).setSettings(settings).execute().actionGet();
        System.out.println("CreateIndexResponse = [" + indexresponse + "]");
    }

    public static void createIndexMapping(TransportClient client,String indexName,String indexTypeName,int shards,int replicas,Field[] fields,Set<String> analyzeFields) {
        try {
            List<IndexMappingModel> mappingModelList = createIndexModelList(fields,analyzeFields);
            XContentBuilder mapBuilder = XContentFactory.jsonBuilder();
            mapBuilder.startObject().startObject(indexTypeName).startObject("properties");
            for(IndexMappingModel model:mappingModelList){
                mapBuilder.startObject(model.getField());
                if(StringUtils.isNotEmpty(model.getFieldType())){
                    mapBuilder.field("type",model.getFieldType());
                }
                if(StringUtils.isNotEmpty(model.getIndexAnalyzed())){
                    mapBuilder.field("index",model.getIndexAnalyzed());
                }
                mapBuilder.endObject();
            }
            mapBuilder.endObject().endObject().endObject();
            Settings settings = Settings.builder()
                    .put("number_of_shards", shards)
                    .put("number_of_replicas", replicas).build();
            CreateIndexRequestBuilder createIndexRequestBuilder = client.admin()
                    .indices().prepareCreate(indexName).setSettings(settings).addMapping(indexTypeName,mapBuilder);
            createIndexRequestBuilder.execute().actionGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<IndexMappingModel> createIndexModelList(Field[] fileds,Set<String> analyzeFields) {
        List<IndexMappingModel> mappingModels = new ArrayList<>();
        for(Field filed:fileds){
            IndexMappingModel indexMappingModel = new IndexMappingModel();
            if(analyzeFields.contains(filed.getName())){
                TypeMatchManager.setFieldType(filed,indexMappingModel,true);
            }else{
                TypeMatchManager.setFieldType(filed,indexMappingModel,false);
            }
            mappingModels.add(indexMappingModel);
        }
        return mappingModels;
    }

    public  static boolean indexIsExists(TransportClient client,String index){
        IndicesExistsRequest request = new IndicesExistsRequest(index);
        IndicesExistsResponse response = client.admin().indices().exists(request).actionGet();
        if(response.isExists()){
            return true;
        }
        return false;
    }

    private static void deleteIndex(TransportClient client,String indexName){
        if(indexIsExists(client,indexName)){
            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName)
                    .execute().actionGet();
            System.out.println("dResponse = [" + dResponse + "], indexName = [" + indexName + "]");
        }
    }


    public static void testIndexMapping(TransportClient client,String indexName,String indexTypeName,int shards,int replicas) {
        try {
            XContentBuilder mapBuilder = XContentFactory.jsonBuilder();
            mapBuilder.startObject().startObject(indexTypeName).startObject("properties");
            mapBuilder.startObject("desc").field("type","string");
                mapBuilder.endObject();
            mapBuilder.startObject("param").field("type","string").field("index","not_analyzed");
            mapBuilder.endObject();
            mapBuilder.endObject().endObject().endObject();
            Settings settings = Settings.builder()
                    .put("number_of_shards", shards)
                    .put("number_of_replicas", replicas).build();
            CreateIndexRequestBuilder createIndexRequestBuilder = client.admin()
                    .indices().prepareCreate(indexName).setSettings(settings).addMapping(indexTypeName,mapBuilder);
            createIndexRequestBuilder.execute().actionGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
