package com.test.esdemo.pool;

import org.elasticsearch.client.RestHighLevelClient;

import java.util.Set;

public class ElasticSearchPool extends Pool<RestHighLevelClient> {
    private String clusterName;
    private Set<HostAndPort> clusterNodes;

    public ElasticSearchPool(ElasticSearchPoolConfig config){
        super(config, new ElasticSearchClientFactory(config.getClusterName(), config.getNodes()));
        this.clusterName = clusterName;
        this.clusterNodes = clusterNodes;
    }

    public String getClustername() {
        return clusterName;
    }

    public void setClustername(String clustername) {
        this.clusterName = clustername;
    }

    public Set<HostAndPort> getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(Set<HostAndPort> clusterNodes) {
        this.clusterNodes = clusterNodes;
    }
}
