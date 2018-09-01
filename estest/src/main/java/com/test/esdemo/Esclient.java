package com.test.esdemo;

import org.apache.http.HttpHost;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class Esclient {
    private static final String HOST = "127.0.0.1";
    private static final Integer PORT=9300;

    private static RestHighLevelClient highLevelClient=null;



    public static RestHighLevelClient highLevelClient(){
        if(highLevelClient == null) {
            highLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("localhost", 9200, "http")));
        }
        return highLevelClient;
    }

    public static void highLevelClientClose(){
        if(highLevelClient !=null){
            try {
                highLevelClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            highLevelClient = null;
        }
    }
    public static void main(String[] args) {
        RestHighLevelClient highLevelClient = highLevelClient();
        System.out.println("args = [" + highLevelClient + "]");
        IndicesClient indicesClient = highLevelClient.indices();
        System.out.println("indicesClient = [" + indicesClient + "]");
    }

}
