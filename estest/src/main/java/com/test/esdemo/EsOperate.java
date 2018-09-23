package com.test.esdemo;


import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;
import java.util.Map;

public class EsOperate {

    public  void createIndex() throws IOException {
        RestHighLevelClient restHighLevelClient = Esclient.highLevelClient();
        CreateIndexRequest request = new CreateIndexRequest("twitter_two");
        //创建的每个索引都可以有与之关联的特定设置。
        request.settings(Settings.builder()
                .put("index.number_of_shards", 2)
                .put("index.number_of_replicas", 1)
        );
        //创建索引时创建文档类型映射
        request.mapping("tweet",
                "  {\n" +
                        "    \"tweet\": {\n" +
                        "      \"properties\": {\n" +
                        "        \"message\": {\n" +
                        "          \"type\": \"text\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }",//类型映射，需要的是一个JSON字符串
                XContentType.JSON);

        //为索引设置一个别名
        request.alias(
                new Alias("twitter_alias")
        );
        requestSet(request);
        request.waitForActiveShards(1);
        //同步执行
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request);

        //异步执行
        //异步执行创建索引请求需要将CreateIndexRequest实例和ActionListener实例传递给异步方法：
        //CreateIndexResponse的典型监听器如下所示：
        //异步方法不会阻塞并立即返回。
        ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
                //如果执行成功，则调用onResponse方法;
            }
            @Override
            public void onFailure(Exception e) {
                //如果失败，则调用onFailure方法。
            }
        };
        restHighLevelClient.indices().createAsync(request, listener);

        //返回的CreateIndexResponse允许检索有关执行的操作的信息，如下所示：
        boolean acknowledged = createIndexResponse.isAcknowledged();
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();

    }

    public void deleteIndex(String indexName) throws IOException {
        RestHighLevelClient restHighLevelClient = Esclient.highLevelClient();
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        requestSet(deleteIndexRequest);
        deleteIndexRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
        DeleteIndexResponse deleteIndexResponse = restHighLevelClient.indices().delete(deleteIndexRequest);
        boolean ack = deleteIndexResponse.isAcknowledged();
    }

    public boolean openIndex(String indexName) throws IOException {
        RestHighLevelClient restHighLevelClient = Esclient.highLevelClient();
        OpenIndexRequest openIndexRequest = new OpenIndexRequest("indexName");
        requestSet(openIndexRequest);
        openIndexRequest.waitForActiveShards(1);
        //设置IndicesOptions控制如何解决不可用的索引以及如何扩展通配符表达式
        openIndexRequest.indicesOptions(IndicesOptions.strictExpandOpen());
        OpenIndexResponse openIndexResponse = restHighLevelClient.indices().open(openIndexRequest);
        boolean acknowledged = openIndexResponse.isAcknowledged();
        boolean shardsAcknowledged = openIndexResponse.isShardsAcknowledged();
        return acknowledged && shardsAcknowledged;
    }

    public boolean closeIndex(String indexName) throws IOException {
        RestHighLevelClient restHighLevelClient = Esclient.highLevelClient();
        CloseIndexRequest closeIndexRequest = new CloseIndexRequest(indexName);
        requestSet(closeIndexRequest);
        closeIndexRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
        CloseIndexResponse closeIndexResponse = restHighLevelClient.indices().close(closeIndexRequest);
        return closeIndexResponse.isAcknowledged();
    }

    public Object getIndexData(String indexName,String typeName,String docName) throws IOException {
        RestHighLevelClient restHighLevelClient = Esclient.highLevelClient();
        GetRequest getRequest = new GetRequest(indexName,typeName,docName);
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        //为特定字段配置_source_include
        String[] includes = new String[]{"message", "*Date"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(fetchSourceContext);
       /*
       //配置指定stored_fields的检索（要求字段在映射中单独存储）
        getRequest.storedFields("message");
        GetResponse getResponse = client.get(getRequest);
        //检索message 存储字段（要求将字段分开存储在映射中）
        String message = getResponse.getField("message").getValue();
        */
        getRequest.routing("routing");//设置routing值
        getRequest.parent("parent");//设置parent值
        getRequest.preference("preference");//设置preference值
        getRequest.realtime(false);//设置realtime为false，默认是true
        getRequest.refresh(true);//在检索文档之前执行刷新（默认为false）
     //   getRequest.version(2);//设置版本
        getRequest.versionType(VersionType.EXTERNAL);//设置版本类型
        GetResponse getResponse = restHighLevelClient.get(getRequest);
        if(getResponse.isExists()){
            String index = getResponse.getIndex();
            String type = getResponse.getType();
            String id = getResponse.getId();
            long version = getResponse.getVersion();
            String sourceAsString = getResponse.getSourceAsString();//检索文档(String形式)
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
            return sourceAsMap;
        }
        return null;
    }

    private  void  requestSet(AcknowledgedRequest<?>  request){
        request.timeout(TimeValue.timeValueMinutes(2));
        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
    }



    public static void main(String[] args) throws IOException {
       // add();
        Long l=2L<<30;
        System.out.println("args = [" + l + "]");
    }
}
