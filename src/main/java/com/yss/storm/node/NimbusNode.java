package com.yss.storm.node;

/**
 * Created by zhangchi on 2017/5/17.
 */
public class NimbusNode {

    private String host;

    private String dockerHost;

    private int port;

    public NimbusNode(String host,String dockerHost,int port){
        this.host = host;
        this.port = port;
        this.dockerHost = dockerHost;
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

    public String getDockerHost() {
        return dockerHost;
    }

    public void setDockerHost(String dockerHosts) {
        this.dockerHost = dockerHosts;
    }

    public String toString(){
        return "host : "+host+" port : "+port;
    }

}
