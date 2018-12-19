package com.test.esdemo.pool;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ElasticSearchPoolTest {

    public static void main(String[] args) throws IOException {
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("127.0.0.1:9300","127.0.0.1",9300,"tcp"));
        ElasticSearchPoolConfig config = new ElasticSearchPoolConfig();
        config.setConnectTimeMillis(8000);
        config.setMaxTotal(100);
        config.setClusterName("elasticsearch");
        config.setNodes(nodes);
        ElasticSearchPool pool = new ElasticSearchPool(config);
        long start = System.currentTimeMillis();
        TransportClient client = pool.getResource();
        List<DiscoveryNode> discoveryNodeList = client.connectedNodes();
        for(DiscoveryNode discoveryNode : discoveryNodeList){
            System.out.println("args = [" + discoveryNode.getHostName() + "]");
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时(ms)："+(end-start));
    }
}
