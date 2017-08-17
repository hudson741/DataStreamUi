package com.yss.test;

import java.io.File;
import java.net.InetAddress;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.floodCtr.generate.FloodJob;
import com.floodCtr.generate.FloodJobRunningState;
import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.zookeeper.KeeperException;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/9
 */
public class TestMain {
    private static final String PATH   = "/docker/storm";
    static CuratorFramework     client = CuratorFrameworkFactory.newClient("123.207.8.134:2181",
                                                                           new ExponentialBackoffRetry(1000, 3));
    static PathChildrenCache pathChildrenCache = new PathChildrenCache(client, PATH, true);

    public static void list(CuratorFramework client, String pathN) throws Exception {
        String       name = pathN;
        String       path = PATH + "/" + name;
        List<String> list = client.getChildren().forPath(path);

        for (String u : list) {
            System.out.println(u + "  " + new String(client.getData().forPath(path + "/" + u)));
        }
    }

    public static void main(String[] args) throws Exception {
        String a = "[{\"businessType\":\"storm\",\"floodJob\":{\"businessTag\":\"ui\",\"cpu\":1,\"dockerCMD\":{\"containerName\":\"ui-1\",\"dockerArgs\":\"storm ui -c ui.port=9002 -c storm.zookeeper.servers=[\\\\\\\"10.186.58.13\\\\\\\"] -c nimbus.seeds=[\\\\\\\"192.168.10.3,192.168.10.4\\\\\\\"]\",\"host\":{\"ui-1\":\"192.168.10.5\"},\"hostName\":\"ui-1\",\"imageName\":\"storm\",\"ip\":\"192.168.10.5\",\"port\":{\"9092\":\"9092\"}},\"jobId\":\"c5029853-80f8-4cc7-b144-cf6239237f10\",\"memory\":1024,\"netUrl\":\"overlay\"},\"runIp\":\"zhangc3\",\"state\":0},{\"businessType\":\"storm\",\"floodJob\":{\"businessTag\":\"nimbus\",\"cpu\":1,\"dockerCMD\":{\"containerName\":\"nimbus-1498655934406\",\"dockerArgs\":\"storm  nimbus  -c nimbus.thrift.port=9005 -c storm.zookeeper.servers=[\\\\\\\"10.186.58.13\\\\\\\"] -c nimbus.seeds=[\\\\\\\"192.168.10.3,192.168.10.4\\\\\\\"]\",\"host\":{\"nimbus-1498655934406\":\"192.168.10.3\"},\"hostName\":\"nimbus-1498655934406\",\"imageName\":\"storm\",\"ip\":\"192.168.10.3\",\"port\":{\"9005\":\"9005\"}},\"jobId\":\"140c4711-4e7a-492f-a817-d9bdf976847f\",\"memory\":1024,\"netUrl\":\"overlay\",\"nodeBind\":\"zhangc4\"},\"runIp\":\"zhangc4\",\"state\":0}]\n";

        List<FloodJobRunningState> list = JSONObject.parseArray(a,FloodJobRunningState.class);

        for(FloodJobRunningState floodJobRunningState:list){
            System.out.println(floodJobRunningState.getContainerId());
        }
    }

    public static void remove(CuratorFramework client, String pathN) throws Exception {
        String name = pathN;
        String path = PATH + "/" + name;

        try {
            List<String> list = client.getChildren().forPath(path);

            if (CollectionUtils.isNotEmpty(list)) {
                for (String u : list) {
                    client.delete().forPath(path + "/" + u);
                }
            }

            client.delete().forPath(path);
            client.sync();
        } catch (KeeperException.NoNodeException e) {}
    }

    public static void start() {
        try {
            client.start();
            pathChildrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setValue(CuratorFramework client, String pathN, String child, String value) throws Exception {
        String name  = pathN;
        String path  = PATH + "/" + name + "/" + child;
        byte[] bytes = value.getBytes();

        try {
            if (client.checkExists().forPath(path) != null) {
                client.setData().forPath(path, bytes);
            } else {
                client.create().creatingParentsIfNeeded().forPath(path, bytes);
            }
        } catch (KeeperException.NoNodeException e) {

            // client.create().creatingParentsIfNeeded().forPath(path, bytes);
        }
    }
}
