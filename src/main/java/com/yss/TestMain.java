package com.yss;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.storm.utils.DRPCClient;
import org.apache.storm.utils.Utils;
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
        Map config = Utils.readDefaultConfig();
        DRPCClient client = new DRPCClient(config,"zhangc3", 3772);
        String result = client.execute("echo", "hello");
        System.out.println(result);
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
