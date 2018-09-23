package com.test.esdemo.pool;

import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ElasticSearchPoolTest {

    public static void main(String[] args) throws IOException {
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("127.0.0.1:9200","127.0.0.1",9200,"http"));
        ElasticSearchPoolConfig config = new ElasticSearchPoolConfig();
        config.setConnectTimeMillis(8000);
        config.setMaxTotal(100);
        config.setClusterName("my-application");
        config.setNodes(nodes);
        ElasticSearchPool pool = new ElasticSearchPool(config);
        long start = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            RestHighLevelClient client = pool.getResource();
            boolean response = client.ping();
            pool.returnResource(client);
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时(ms)："+(end-start));
    }
}
