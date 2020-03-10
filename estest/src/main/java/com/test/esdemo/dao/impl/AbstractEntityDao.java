package com.test.esdemo.dao.impl;

import com.test.esdemo.annotion.EntityInfo;
import com.test.esdemo.client.ElasticSearchClient;
import com.test.esdemo.dao.entity.BaseEntity;
import com.test.esdemo.utils.ObjectToObjectUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public  abstract class AbstractEntityDao<T extends BaseEntity> {
    protected Class<T> clazz = this.getClazz(this.getClass().getGenericSuperclass());
    private ElasticSearchClient elasticSearchClient;

    protected  AbstractEntityDao(){
        if(this.clazz==null){
            this.clazz=this.getClazz(this.clazz.getSuperclass().getGenericSuperclass());
        }
        if(this.clazz==null){
            throw new RuntimeException(this.getClass()+"get T class fail");
        }
        if(elasticSearchClient==null){
            try {
                elasticSearchClient=new ElasticSearchClient();
            } catch (Exception e) {
                throw new RuntimeException("init client fail");
            }
        }
    }

    private Class<T> getClazz(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] array = ((ParameterizedType)type).getActualTypeArguments();
            return (Class)array[0];
        } else {
            return null;
        }
    }
    public  void  bulkInsert(List<? extends Object> list) throws IllegalAccessException {
        TransportClient client = null;
        try {
            client = elasticSearchClient.getResource();
            EntityInfo entityInfo=this.clazz.getAnnotation(EntityInfo.class);
            String index=entityInfo.index();
            String type=entityInfo.type();
            bulkInsert(client,index,type,list);
        } finally {
            elasticSearchClient.returnResource(client);
        }
    }
    private void bulkInsert(TransportClient client, String index, String type, List<? extends Object> list) throws IllegalAccessException {
        BulkRequest bulkRequest = new BulkRequest();
        for(int i=0;i<list.size();i++) {
            Map<String, Object> esMap = ObjectToObjectUtil.objectToMap(list.get(i));
            bulkRequest.add(new IndexRequest(index, type, (String) esMap.get("id")).source(esMap));
        }
        client.bulk(bulkRequest).actionGet();
    }





}
