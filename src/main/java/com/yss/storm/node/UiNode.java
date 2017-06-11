package com.yss.storm.node;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/10
 */
public class UiNode {

    private String host;

    private int port;

    public UiNode(String host,int port){
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
