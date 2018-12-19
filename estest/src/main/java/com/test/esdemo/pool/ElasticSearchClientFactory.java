package com.test.esdemo.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ElasticSearchClientFactory implements PooledObjectFactory<TransportClient> {
    private AtomicReference<Set<HostAndPort>> nodesReference = new AtomicReference<Set<HostAndPort>>();
    private String clusterName;

    public ElasticSearchClientFactory(String clusterName, Set<HostAndPort> clusterNodes){
        this.clusterName = clusterName;
        this.nodesReference.set(clusterNodes);
    }


    @Override
    public PooledObject<TransportClient> makeObject() throws Exception {
        Settings settings = Settings.builder()
                //嗅探其他节点
                .put("client.transport.sniff", true)
                //设置集群的名称
                .put("cluster.name", clusterName).build();
        TransportClient restHighLevelClient = new PreBuiltTransportClient(settings);
        for(HostAndPort each:nodesReference.get()){
            restHighLevelClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(each.getHost()), each.getPort()));
        }
        List<DiscoveryNode> discoveryNodeList = restHighLevelClient.connectedNodes();
        System.out.println("---"+discoveryNodeList.size());
        PooledObject<TransportClient> pooledObject = new DefaultPooledObject(restHighLevelClient);
        return pooledObject;
    }

    @Override
    public void destroyObject(PooledObject<TransportClient> pooledObject) throws Exception {
      /*  TransportClient restHighLevelClient = pooledObject.getObject();
        if(restHighLevelClient != null && restHighLevelClient.ping()){
            try{
                restHighLevelClient.close();
            }catch (Exception e){
                throw new ElasticsearchException("could not destroy the client.",e);
            }
        }*/
    }

    @Override
    public boolean validateObject(PooledObject<TransportClient> pooledObject) {
     /*   RestHighLevelClient client = pooledObject.getObject();
        try {
            return client.ping();
        }catch(Exception e){
            return false;
        }*/
        TransportClient restHighLevelClient = pooledObject.getObject();
        List<DiscoveryNode> discoveryNodeList = restHighLevelClient.connectedNodes();
        if(discoveryNodeList !=null && discoveryNodeList.size()>0){
            return true;
        }
        return false;

    }

    @Override
    public void activateObject(PooledObject<TransportClient> pooledObject) throws Exception {
     /*   RestHighLevelClient client = pooledObject.getObject();
        boolean response = client.ping();*/
    }

    @Override
    public void passivateObject(PooledObject<TransportClient> pooledObject) throws Exception {

    }
}
