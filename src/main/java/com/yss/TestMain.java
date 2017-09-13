package com.yss;

import java.io.File;
import java.net.InetAddress;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import org.assertj.core.util.Lists;

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
//        String json ="[{\"businessType\":\"storm-oo\",\"containerIdStr\":\"container_1505285451101_0015_01_000003\",\"floodJob\":{\"businessTag\":\"nimbus\",\"cpu\":1,\"dockerCMD\":{\"containerName\":\"nimbus-1505303033425\",\"dockerArgs\":\"storm  nimbus  -c nimbus.thrift.port=9005 -c storm.zookeeper.servers=[\\\\\\\"10.186.58.13\\\\\\\"] -c nimbus.seeds=[\\\\\\\"192.168.10.3\\\\\\\"]\",\"host\":{\"nimbus-1505303033425\":\"192.168.10.3\"},\"hostName\":\"nimbus-1505303033425\",\"imageName\":\"1187655234/storm-1.1.0-1c2g:1.0\",\"ip\":\"192.168.10.3\",\"port\":{\"9005\":\"9005\"},\"volume\":{\"/home/hadoop/stormlog\":\"/opt/storm/logs\"}},\"jobId\":\"6241a985-7462-46de-b5c4-10a2fe4842d0\",\"launch_type\":\"NEW\",\"memory\":1024,\"netUrl\":\"overlay\",\"nodeBind\":\"zhangc4\",\"priority\":\"HIGH\"},\"jobId\":\"6241a985-7462-46de-b5c4-10a2fe4842d0\",\"nodeHOST\":\"zhangc4\",\"nodePort\":39856,\"runIp\":\"zhangc4\",\"runningState\":\"STOP\"},{\"businessType\":\"storm-oo\",\"containerIdStr\":\"container_1505285451101_0015_01_000004\",\"floodJob\":{\"businessTag\":\"supervisor\",\"cpu\":1,\"dockerCMD\":{\"containerName\":\"supervisor-1505303379507\",\"dockerArgs\":\"storm supervisor -c nimbus.thrift.port=9005 -c ui.port=9092 -c storm.zookeeper.servers=[\\\\\\\"10.186.58.13\\\\\\\"] -c nimbus.seeds=[\\\\\\\"192.168.10.3\\\\\\\"]\",\"host\":{\"supervisor-1505303379507\":\"192.168.10.3\"},\"hostName\":\"supervisor-1505303379507\",\"imageName\":\"1187655234/storm-1.1.0-1c2g:1.0\",\"ip\":\"192.168.10.3\",\"port\":{},\"volume\":{\"/home/hadoop/stormlog\":\"/opt/storm/logs\"}},\"jobId\":\"8a7cb201-6941-4f13-8512-c1eacfae7206\",\"launch_type\":\"NEW\",\"memory\":1024,\"netUrl\":\"overlay\",\"priority\":\"HIGH\"},\"jobId\":\"8a7cb201-6941-4f13-8512-c1eacfae7206\",\"nodeHOST\":\"zhangc3\",\"nodePort\":38551,\"runIp\":\"zhangc3\",\"runningState\":\"STOP\"},{\"businessType\":\"storm-oo\",\"containerIdStr\":\"container_1505285451101_0015_01_000005\",\"floodJob\":{\"businessTag\":\"nimbus\",\"cpu\":1,\"dockerCMD\":{\"containerName\":\"nimbus-1505303500319\",\"dockerArgs\":\"storm nimbus -c nimbus.thrift.port=9005 -c ui.port=9092 -c storm.zookeeper.servers=[\\\\\\\"10.186.58.13\\\\\\\"] -c nimbus.seeds=[\\\\\\\"192.168.10.3\\\\\\\"]\",\"host\":{\"nimbus-1505303500319\":\"192.168.10.3\"},\"hostName\":\"nimbus-1505303500319\",\"imageName\":\"1187655234/storm-1.1.0-1c2g:1.0\",\"ip\":\"192.168.10.3\",\"port\":{\"9005\":\"9005\"},\"volume\":{\"/home/hadoop/stormlog\":\"/opt/storm/logs\"}},\"jobId\":\"5d03ace9-6c34-4b0e-a2e4-9b96fe616b07\",\"launch_type\":\"NEW\",\"memory\":1024,\"netUrl\":\"overlay\",\"priority\":\"HIGH\"},\"jobId\":\"5d03ace9-6c34-4b0e-a2e4-9b96fe616b07\",\"nodeHOST\":\"zhangc3\",\"nodePort\":38551,\"runIp\":\"zhangc3\",\"runningState\":\"RUNNING\"},{\"businessType\":\"storm-oo\",\"containerIdStr\":\"container_1505285451101_0015_01_000002\",\"floodJob\":{\"businessTag\":\"ui\",\"cpu\":1,\"dockerCMD\":{\"containerName\":\"ui-1505303033440\",\"dockerArgs\":\"storm ui -c ui.port=9092 -c nimbus.thrift.port=9005 -c storm.zookeeper.servers=[\\\\\\\"10.186.58.13\\\\\\\"] -c nimbus.seeds=[\\\\\\\"192.168.10.3\\\\\\\"]\",\"host\":{\"ui-1505303033440\":\"192.168.10.5\"},\"hostName\":\"ui-1505303033440\",\"imageName\":\"1187655234/storm-1.1.0-1c2g:1.0\",\"ip\":\"192.168.10.5\",\"port\":{\"9092\":\"9092\"},\"volume\":{\"/home/hadoop/stormlog\":\"/opt/storm/logs\"}},\"jobId\":\"7e702d53-a05d-4034-aeba-9cd1f251692a\",\"launch_type\":\"NEW\",\"memory\":1024,\"netUrl\":\"overlay\",\"priority\":\"LOW\"},\"jobId\":\"7e702d53-a05d-4034-aeba-9cd1f251692a\",\"nodeHOST\":\"zhangc5\",\"nodePort\":42785,\"runIp\":\"zhangc5\",\"runningState\":\"RUNNING\"}]";

        List<FloodJobRunningState> list = Lists.newArrayList();
        for(FloodJobRunningState f:list){
            if (f.getFloodJob().getBusinessTag().equals("ui") && f.getRunningState() == FloodJobRunningState.RUNNING_STATE.RUNNING) {
                System.out.println( f.getRunIp() + ":9092");
            }
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
