package com.test.esdemo.pool;

import org.elasticsearch.client.transport.TransportClient;


public class ElasticSearchPool extends Pool<TransportClient> {
    public ElasticSearchPool(ElasticSearchPoolConfig config){
        super(config, new ElasticSearchClientFactory(config.getClusterName(), config.getNodes()));

    }
}
