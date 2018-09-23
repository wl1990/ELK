package com.test.esdemo;

import com.test.esdemo.client.ElasticSearchClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.net.URISyntaxException;

public class SearchApiMain {
    public static void main(String[] args) throws IOException, URISyntaxException {
        ElasticSearchClient elasticSearchClient = new ElasticSearchClient();
        RestHighLevelClient restHighLevelClient = elasticSearchClient.getResource();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("pmap.sex","f");

        BoolQueryBuilder matchQueryBuilder = QueryBuilders.boolQuery();
        matchQueryBuilder.must(QueryBuilders.termQuery("pmap.sex","f"));
        matchQueryBuilder.mustNot(QueryBuilders.termQuery("id",1));
        matchQueryBuilder.mustNot(QueryBuilders.termQuery("id",2));
      //  matchQueryBuilder.should(QueryBuilders.rangeQuery("utm").from(0,true).to(100,true));
    //    matchQueryBuilder.minimumShouldMatch(1);
//            matchQueryBuilder.filter();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(matchQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(3);

        SearchRequest searchRequest = new SearchRequest("logaudit");//限定index
        searchRequest.types("doc");//限定type
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        System.out.println(searchResponse);
    }
}
