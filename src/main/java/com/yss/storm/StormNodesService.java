package com.yss.storm;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.yss.storm.node.DrpcNode;
import com.yss.yarn.discovery.YarnThriftClient;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryUntilElapsed;

import org.apache.thrift.TException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import com.yss.config.Conf;
import com.yss.storm.node.NimbusNode;
import com.yss.storm.node.UiNode;

/**
 * Created by zhangchi on 2017/5/17.
 */
public class StormNodesService {
    public final static String                    STORM_INIT_PATH = "/storm";
    public final static String                    STORM_NIMBUS_HOSTS = "/storm/nimbuses";
    private Logger                                logger             = LoggerFactory.getLogger(StormNodesService.class);
    private List<String>                          nimbusHosts        = Lists.newArrayList();
    private CuratorFramework                      curatorFramework   = null;
    private PathChildrenCache                     nimbusHostsChildCache;

    @Autowired
    private YarnThriftClient yarnThriftClient;

    private void reloadNimbusHosts(CuratorFramework client) throws Exception {
        nimbusHosts.clear();

        List<String> stormNimbusChildren = client.getChildren().forPath(STORM_NIMBUS_HOSTS);

        for (String nimbus : stormNimbusChildren) {
            String[] nimbusNode = nimbus.split(":");

            logger.info("found nimbus : " + nimbus);
            nimbusHosts.add(nimbusNode[0]);
        }
    }

    private void startNimbusHostsChildrenCache() throws Exception {
        nimbusHostsChildCache = new PathChildrenCache(curatorFramework, STORM_NIMBUS_HOSTS, true);
        nimbusHostsChildCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        nimbusHostsChildCache.getListenable().addListener(new PathChildrenCacheListener() {
                                              public void childEvent(CuratorFramework client,
                                                                     PathChildrenCacheEvent event)
                                                      throws Exception {
                                                  reloadNimbusHosts(client);
                                              }
                                          });
    }

    public void refresh() throws Exception {
        try {
            if (curatorFramework != null) {
                curatorFramework.close();
            }

            if (StringUtils.isNotEmpty(Conf.getSTORM_ZK())) {
                RetryUntilElapsed retryUntilElapsed = new RetryUntilElapsed(6000, 1000);

                curatorFramework = CuratorFrameworkFactory.newClient(Conf.getSTORM_ZK() + ":" + "2181",
                                                                     retryUntilElapsed);
                curatorFramework.start();
                startNimbusHostsChildrenCache();
            }
        } catch (Exception e) {
            logger.error("error ", e);
        }
    }

    public static void main(String[] args){

        CuratorFramework curatorFramework;
        try {
                RetryUntilElapsed retryUntilElapsed = new RetryUntilElapsed(20000, 100);

                curatorFramework = CuratorFrameworkFactory.newClient("123.207.8.134" + ":" + "2181",
                        retryUntilElapsed);
                curatorFramework.start();
                Stat stat = curatorFramework.checkExists().forPath(STORM_INIT_PATH);
                if(stat!=null) {
                    curatorFramework.delete().deletingChildrenIfNeeded().forPath(STORM_INIT_PATH);
                }

//            }
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("error ", e);
        }
    }

    public void clearStorm() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(STORM_INIT_PATH);
        if(stat!=null) {
            curatorFramework.delete().deletingChildrenIfNeeded().forPath(STORM_INIT_PATH);
        }
    }

    public List<String> getNimbusHosts() {
        return nimbusHosts;
    }

    public void setNimbusHosts(List<String> nimbusHosts) {
        this.nimbusHosts = nimbusHosts;
    }

    public List<DrpcNode> getDrpcNodeList(){
        List<DrpcNode> list = Lists.newArrayList();
        try {
            String json = yarnThriftClient.getStormDrpc();
            if(StringUtils.isEmpty(json)){
                return Lists.newArrayList();
            }
            List<String> array = JSONObject.parseArray(json,String.class);
            for(String dprcNode:array){
                String[] drpc = dprcNode.split(":");
                DrpcNode drpcNode = new DrpcNode(drpc[0],drpc[1],drpc[2],Integer.parseInt(drpc[3]));
                list.add(drpcNode);
            }

        } catch (TException e) {
            logger.error("error ",e);
        }
        return list;
    }

    public List<NimbusNode> getNimbusNodeList() {
        List<NimbusNode> list = Lists.newArrayList();
        try {
            String json = yarnThriftClient.getStormNimbus();
            if(StringUtils.isEmpty(json)){
                return Lists.newArrayList();
            }
            List<String> array = JSONObject.parseArray(json,String.class);
            for(String nimbusNode:array){
                String[] nimbus = nimbusNode.split(":");
                NimbusNode nimbusNode1 = new NimbusNode(nimbus[0],nimbus[1],nimbus[2],Integer.parseInt(nimbus[3]));
                list.add(nimbusNode1);
            }

        } catch (Throwable e) {
          logger.error("error ",e);
        }
        return list;
    }
}
