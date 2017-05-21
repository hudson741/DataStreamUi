package com.yss.storm.nimbus;

/**
 * Created by zhangchi on 2017/5/17.
 */
public class NimbusNode {

    private String host;

    private int port;

    public NimbusNode(String host,int port){
        this.host = host;
        this.port = port;
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

    public String toString(){
        return "host : "+host+" port : "+port;
    }

}
