package com.test.esdemo.client;

import com.test.esdemo.form.PageForm;
import com.test.esdemo.utils.BDateUtil;
import com.test.esdemo.utils.ObjectToObjectUtil;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Component
public class EsHelper {
    @Autowired
    private ElasticSearchClient elasticSearchClient;

    public static <T> List<T> esHitsToList(SearchHit[] searchHits,Class<T> objectClass) throws IllegalAccessException, InstantiationException {
        List<T> resultForms = new ArrayList<>();
        if (searchHits == null || searchHits.length <= 0) {
            return resultForms;
        }
        for (SearchHit searchHit : searchHits) {
            resultForms.add(esHitToObject(searchHit,objectClass));
        }
        return resultForms;
    }

    public SearchHit[] getHitById(String id,String index,String type){
        TransportClient client = null;
        try {
            client = elasticSearchClient.getResource();
            BoolQueryBuilder matchQueryBuilder = QueryBuilders.boolQuery();
            matchQueryBuilder.must(QueryBuilders.termQuery("_id", id));
            SearchRequest searchRequest = new SearchRequest(index);//限定index
            searchRequest.types(type);//限定type
            searchRequest.searchType(SearchType.DEFAULT);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchQueryBuilder);
            searchSourceBuilder.size(1);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest).actionGet();
            return searchResponse.getHits().getHits();
        } finally {
            elasticSearchClient.returnResource(client);
        }
    }

    public static <T> T esHitToObject(SearchHit searchHit,Class<T> c) throws IllegalAccessException, InstantiationException {
        if(searchHit==null){
            return null;
        }
        T obj=c.newInstance();
        Field[] fields=c.getDeclaredFields();
        Map<String, Object> map = searchHit.getSourceAsMap();
        for(Field f:fields){
            f.setAccessible(true);
            if(f.getName().equals("id")){
                f.set(obj,searchHit.getId());
            }
            if(map.get(f.getName())==null){
                continue;
            }
             if(f.getType()==Long.class){
                if(map.get(f.getName()).getClass()==Integer.class){
                    f.set(obj,Long.parseLong(String.valueOf(map.get(f.getName()))));
                }else{
                    f.set(obj,(Long)map.get(f.getName()));
                }
            }
             if(f.getType()==String.class && !f.getName().equals("id")){
                f.set(obj,String.valueOf(map.get(f.getName())));
            }
             if(f.getType()==Double.class){
                f.set(obj,(Double)map.get(f.getName()));
            }
             if(f.getType()==Integer.class){
                f.set(obj,(Integer)map.get(f.getName()));
            }
        }
        return obj;
    }

    public void esUpdate(String index,String type,Object updateObject) throws IOException, IllegalAccessException {
        TransportClient client = null;
        try {
            client = elasticSearchClient.getResource();
            Field[] fields=updateObject.getClass().getDeclaredFields();
            XContentBuilder xBuild = XContentFactory.jsonBuilder().startObject();
            String id=null;
            for(Field field:fields){
                field.setAccessible(true);
                if(field.getName().equals("id")){
                    id=(String)field.get(updateObject);
                }else if(field.get(updateObject)!=null){
                    if(field.getType()==String.class){
                        xBuild.field(field.getName(),(String)field.get(updateObject));
                    }
                    if(field.getType()==Long.class){
                        xBuild.field(field.getName(),(Long)field.get(updateObject));
                    }
                    if(field.getType()==Double.class){
                        xBuild.field(field.getName(),(Double)field.get(updateObject));
                    }
                    if(field.getType()==Float.class){
                        xBuild.field(field.getName(),(Float)field.get(updateObject));
                    }
                    if(field.getType()==Integer.class){
                        xBuild.field(field.getName(),(Integer)field.get(updateObject));
                    }
                }
            }
            xBuild.endObject();
            UpdateRequest updateRequest = new UpdateRequest(index, type, id);
            updateRequest.doc(xBuild);
            client.update(updateRequest).actionGet();
        } finally {
            elasticSearchClient.returnResource(client);
        }
    }

    public <T> T getObjectById(String id, String index, String type, Class<T> objectClass) throws InstantiationException, IllegalAccessException {
        SearchHit[] searchHits = getHitById(id,index, type);
        if (searchHits.length < 1) {
            return null;
        }
        return esHitToObject(searchHits[0],objectClass);
    }

    public void delete(String id, String index, String type) {
        TransportClient client = null;
        try {
            client = elasticSearchClient.getResource();
            DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
            client.delete(deleteRequest).actionGet();
        } finally {
            elasticSearchClient.returnResource(client);
        }
    }

    /**
     * 分页查询es数据
     * @param matchQueryBuilder
     * @param index
     * @param type
     * @param limit
     * @param offset
     * @param objectClass
     * @return
     */
    public <T> PageForm<T> getSearchHitsPages(BoolQueryBuilder matchQueryBuilder, String index, String type, int limit, int offset, Class<T> objectClass) throws IllegalAccessException, InstantiationException {
        return getSearchHitsPages(matchQueryBuilder, index, type, limit, offset,null ,null,objectClass);
    }

    /**
     * 分页查询
     * @param matchQueryBuilder
     * @param index
     * @param type
     * @param limit
     * @param offset
     * @param includes
     * @param excludes
     * @param objectClass
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> PageForm<T> getSearchHitsPages(BoolQueryBuilder matchQueryBuilder, String index, String type, int limit, int offset, String[] includes, String[] excludes,Class<T> objectClass) throws InstantiationException, IllegalAccessException {
        return getSearchHitsPages(matchQueryBuilder, index, type, limit, offset,includes ,excludes,objectClass,null);
    }


    /**
     * 分页查询es数据
     * @param matchQueryBuilder
     * @param index
     * @param type
     * @param limit
     * @param offset
     * @param includes include column ,if empty include all columns
     * @param excludes exinclude column
     * @param routes route value
     * @return
     */
    public <T> PageForm<T> getSearchHitsPages(BoolQueryBuilder matchQueryBuilder, String index, String type, int limit, int offset, String[] includes, String[] excludes, Class<T> objectClass, String[] routes) throws InstantiationException, IllegalAccessException {
        TransportClient client = null;
        try {
            client = elasticSearchClient.getResource();
            SearchRequest searchRequest = countTotalSearchRequest(matchQueryBuilder,index,type);
            if(routes!=null && routes.length>0){
                searchRequest.routing(routes);
            }
            SearchResponse searchResponse = client.search(searchRequest).actionGet();
            int totalrow = (int) searchResponse.getHits().getTotalHits();
            PageForm<T> pageForm = new PageForm<>();
            pageForm.setTotalCount(totalrow);
            pageForm.setCrrentPage(offset / limit);
            if (totalrow <= 0) {
                pageForm.setList(new ArrayList<T>());
                return pageForm;
            }
            setPageAndColumn(searchRequest, limit, offset,includes,excludes);
            searchResponse = client.search(searchRequest).actionGet();
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            List<T> apiScenceFormList = esHitsToList(searchHits,objectClass);
            pageForm.setList(apiScenceFormList);
            return pageForm;
        } finally {
            elasticSearchClient.returnResource(client);
        }
    }

    public static void setPageAndColumn(SearchRequest searchRequest, int limit, int offset,String[] includes,String[] excludes) {
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        searchRequest.source().fetchSource(fetchSourceContext);
        searchRequest.source().size(limit);
        searchRequest.source().from(offset);
    }

    public static SearchRequest countTotalSearchRequest(BoolQueryBuilder matchQueryBuilder,String index,String type) {
        SearchRequest searchRequest = new SearchRequest(index);//限定index
        searchRequest.types(type);//限定type
        searchRequest.searchType(SearchType.DEFAULT);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(matchQueryBuilder);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }


    public void uniqueCheckAndInsert(BoolQueryBuilder matchQueryBuilder, String index, String type, String id, Object objectDto) throws Exception, IllegalAccessException {
        TransportClient client = null;
        try {
            client = elasticSearchClient.getResource();
            SearchRequest searchRequest = countTotalSearchRequest(matchQueryBuilder,index,type);
            SearchResponse searchResponse = client.search(searchRequest).actionGet();
            int totalrow = (int) searchResponse.getHits().getTotalHits();
            if(totalrow>0){
                throw new Exception("场景已添加，不能重复添加");
            }
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.add(new IndexRequest(index, type,id).source(ObjectToObjectUtil.objectToMap(objectDto)));
            client.bulk(bulkRequest);
        } finally {
            elasticSearchClient.returnResource(client);
        }
    }

    public static String getRoute(String yyyyMMdd){
        int disindex=distributeMonth(yyyyMMdd);
        return yyyyMMdd.substring(0,6)+disindex;
    }

    /**
     *  获取时间范围的route
     * @param startTime yyyyMMdd
     * @param endTime  yyyyMMdd
     * @return
     */
    public static String[] getRoutes(String startTime,String endTime){
        int start=Integer.parseInt(startTime);
        int end=Integer.parseInt(endTime);
        Set<String> routeSet=new HashSet<String>();
        routeSet.add(getRoute(startTime));
        while(start<end){
            startTime=BDateUtil.getDateStrOffDay(startTime,"yyyyMMdd","yyyyMMdd",1);
            if(start>end){
                break;
            }
            routeSet.add(getRoute(startTime));
            start=Integer.parseInt(startTime);
        }
        String[] routes=new String[routeSet.size()];
        return routeSet.toArray(routes);
    }

    private static int distributeMonth(String yyyyMMdd){
        String dd=yyyyMMdd.substring(6,8);
        if(dd.startsWith("0")){
            dd=dd.substring(1,2);
        }
        if(Integer.parseInt(dd)<8){
            return 1;
        }else if(Integer.parseInt(dd)<15){
            return 2;
        }else if(Integer.parseInt(dd)<22){
            return 3;
        }else if(Integer.parseInt(dd)<29){
            return 4;
        }else{
            return 5;
        }
    }

    /**
     * 获取集群节点信息
     */
    public List<DiscoveryNode> getNodes() {
        TransportClient client = null;
        try {
            client = elasticSearchClient.getResource();
            return client.connectedNodes();
        } finally {
            elasticSearchClient.returnResource(client);
        }
    }

    public Map<String, IndexStats> getIndexs() {
        TransportClient client = null;
        try {
            client = elasticSearchClient.getResource();
            ActionFuture<IndicesStatsResponse> isr = client.admin().indices().stats(new IndicesStatsRequest().all());
            return isr.actionGet().getIndices();
        } finally {
            elasticSearchClient.returnResource(client);
        }
    }

    public IndexStats getIndexDetail(String index){
        Map<String, IndexStats> indexStatsMap = getIndexs();
        if(indexStatsMap==null || indexStatsMap.size()<=0){
            return null;
        }
        return indexStatsMap.get(index);
    }

    public void bulkInsert(TransportClient client,String index, String type,List<? extends Object> list) throws IllegalAccessException {
        BulkRequest bulkRequest = new BulkRequest();
        for(int i=0;i<list.size();i++) {
            Map<String, Object> esMap = ObjectToObjectUtil.objectToMap(list.get(i));
            bulkRequest.add(new IndexRequest(index, type, (String) esMap.get("id")).source(esMap));
        }
        client.bulk(bulkRequest).actionGet();
    }

    /**
     * 分组统计top个
     * @param index
     * @param type
     * @param matchQueryBuilder
     * @param countname
     * @param field
     * @return
     */
    public SearchRequest groupByCountRequest(String index,String type,BoolQueryBuilder matchQueryBuilder,String countname,String field,int topSize) {
        ValueCountAggregationBuilder aggregationBuilder = AggregationBuilders.count("count_"+countname).field(field);
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("by_"+countname).field(field);
        termsAggregationBuilder.subAggregation(aggregationBuilder);
        termsAggregationBuilder.size(topSize);
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        searchRequest.searchType(SearchType.DEFAULT);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(matchQueryBuilder);
        searchSourceBuilder.aggregation(termsAggregationBuilder);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }
    public SearchResponse prepareScrollPages(TransportClient client, BoolQueryBuilder matchQueryBuilder, String index, String type, Map<String,SortOrder> orderCol, int pageSize) {
        SearchRequestBuilder requestBuilder=client.prepareSearch(index).setTypes(type);
        if(orderCol!=null){
            for(Map.Entry<String,SortOrder> entry:orderCol.entrySet()){
                requestBuilder.addSort(entry.getKey(),entry.getValue());
            }
        }
        requestBuilder.setQuery(matchQueryBuilder);
        SearchResponse response =requestBuilder.setSize(pageSize).setScroll(new TimeValue(2000)).execute().actionGet();
        return response;
    }

    public SearchResponse getScrollPages(TransportClient client, String scrollId) {
        SearchScrollRequestBuilder searchScrollRequestBuilder = client.prepareSearchScroll(scrollId);
        // 重新设定滚动时间
        searchScrollRequestBuilder.setScroll(new TimeValue(4000));
        // 请求
        return searchScrollRequestBuilder.get();
    }

    public static void deleteIndex(TransportClient client,String indexName){
        if(indexIsExists(client,indexName)){
            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName)
                    .execute().actionGet();
        }
    }

    public  static boolean indexIsExists(TransportClient client,String index){
        IndicesExistsRequest request = new IndicesExistsRequest(index);
        IndicesExistsResponse response = client.admin().indices().exists(request).actionGet();
        if(response.isExists()){
            return true;
        }
        return false;
    }
}
