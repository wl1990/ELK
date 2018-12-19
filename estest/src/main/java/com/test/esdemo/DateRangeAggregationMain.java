package com.test.esdemo;

import com.test.esdemo.client.ElasticSearchClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author:admin
 * @date:2018/7/13
 * @description
 */
public class DateRangeAggregationMain {
   /* public static void main(String[] args) throws IOException, URISyntaxException {
        ElasticSearchClient elasticSearchClient = new ElasticSearchClient();
        RestHighLevelClient client = elasticSearchClient.getResource();
        try{
          *//*  DateRangeAggregationBuilder dateRangeAggregationBuilder = AggregationBuilders.dateRange("date_range");
            dateRangeAggregationBuilder.field("createTime");
            dateRangeAggregationBuilder.format("yyyy-MM-dd HH:mm:ss.sssZ");
            dateRangeAggregationBuilder.addRange("2018-09-25 12:00:00.000Z","2018-09-25 12:40:00.000Z");
            dateRangeAggregationBuilder.timeZone(DateTimeZone.forOffsetHours(0));*//*

            RangeAggregationBuilder rangeAggregationBuilder = AggregationBuilders.range("createTime");
             rangeAggregationBuilder.field("createTime").addRange(20180925164434d,20180925164435d);
            BoolQueryBuilder matchQueryBuilder = QueryBuilders.boolQuery();
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("userId","346");
            matchQueryBuilder.must(termQueryBuilder);
            matchQueryBuilder.must(QueryBuilders.rangeQuery("projectId").from(21385,true).to(21388,true));
            SearchRequest searchRequest = new SearchRequest("logaudit");//限定index
            searchRequest.types("doc");//限定type

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchQueryBuilder);
         //   searchSourceBuilder.aggregation(rangeAggregationBuilder);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest);
            System.out.println(searchResponse);

        }finally{

        }
    }*/
}
