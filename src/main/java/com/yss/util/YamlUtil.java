package com.yss.util;

import org.assertj.core.util.Lists;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/8/7
 */
public class YamlUtil {


    public static void main(String[] args) throws IOException {

        Node node = new Node();
        node.setHost("zhangcTest1");
        node.setIp("192.168.10.3");
        node.setUser("root");
        node.setPassword("Tudou=123");

        List<Node> list = Lists.newArrayList();
        list.add(node);
        list.add(new Node("192.168.10.4","zhangc2","root","Tudou=123"));


        Yaml yaml = new Yaml();
        yaml.dump(list,new FileWriter("/Users/zhangchi/work/Node.yaml"));

        List<Node> node1 = yaml.loadAs(new FileInputStream("/Users/zhangchi/work/Node.yaml"),List.class);
        System.out.println(node1.get(0).getHost());
    }


    public static class Node{
        private String ip;
        private String host;
        private String user;
        private String password;

        public Node(){}

        public Node(String ip,String host,String user,String password){
            this.ip = ip;
            this.host = host;
            this.user = user;
            this.password = password;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
