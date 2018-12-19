package com.test.esdemo;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.test.esdemo.client.ElasticSearchClient;
import com.test.esdemo.dto.LogAuditDto;
import com.test.esdemo.dto.SubOpType;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class AddApiMain {
  /*  public static void main(String[] args) throws IOException, URISyntaxException {
        ElasticSearchClient elasticSearchClient = new ElasticSearchClient();
        RestHighLevelClient restHighLevelClient = elasticSearchClient.getResource();
     //   restHighLevelClient.
        BulkRequest bulkRequest = new BulkRequest();


        for(int i=1;i<4;i++) {
            bulkRequest.add(new IndexRequest("logaudit", "doc", "916"+i).source(buildData(i)));
        }
        //异步方法不会阻塞并立即返回。
        ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {

            @Override
            public void onResponse(BulkResponse bulkItemResponses) {
                System.out.println("bulkItemResponses = [" + bulkItemResponses + "]");
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        restHighLevelClient.bulkAsync(bulkRequest,listener);
    }

    private static Object buildIndexData(int i) {
        LogAuditDto logAuditDto = new LogAuditDto();
        logAuditDto.setContextPath("/user/listusere/");
        logAuditDto.setCreateTime("20180911141312");
        logAuditDto.setId(String.valueOf(i));
        logAuditDto.setOperatorTargetCode("add_user");
        logAuditDto.setOperatorTargetName("添加用户");
        SubOpType subOpType = new SubOpType();
        subOpType.setCode("add_user");
        subOpType.setName("添加用户");
        logAuditDto.setOpTypee(subOpType);
        Map<String,Object> pmap= new HashMap<>();
        pmap.put("userId","23431");
        pmap.put("name","nihao");
        pmap.put("age",34);
        pmap.put("data","2018-09-11 13:42:57");
        pmap.put("sex","f");
        logAuditDto.setParamsMap(pmap);
        logAuditDto.setProjectId("20334"+i);
        logAuditDto.setProjectName("testproject"+i);
        logAuditDto.setReferer("/test/userpage");
        logAuditDto.setTargetIp("192.168.66.3");
        Map<String,Object> typemap= new HashMap<>();
        typemap.put("userid","3045");
        logAuditDto.setTargetType(typemap);
        logAuditDto.setUserId(34054L);
        logAuditDto.setUserName("nihao");
        return logAuditDto;

    }

    private static Map<String,Object> buildData(int i) {
        Map<String,Object> map = new HashMap<>();

        map.put("contentPath","/user/listusere/"+i);
        map.put("createTime","20180911141312");
        map.put("id",i);
        map.put("optarget","add_user");
        map.put("opname","添加用户");
        Map<String,Object> pmap= new HashMap<>();
        pmap.put("userId","23431");
        pmap.put("name","nihao");
        pmap.put("age",34);
        pmap.put("data","2018-09-11 13:42:57:585");
        pmap.put("sex","f");
        map.put("pmap",pmap);
        map.put("userId","123"+i);
        map.put("userName","nihao"+i);

        return map;

    }*/
}
