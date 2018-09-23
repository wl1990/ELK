package com.test.esdemo.pool;

public class HostAndPort {
    private String nodeName;
    private String host;
    private int port;
    private String schema;

    public HostAndPort(String nodeName,String host,int port,String scheme){
        this.nodeName = nodeName;
        this.host = host;
        this.port = port;
        this.schema = scheme;
    }
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
