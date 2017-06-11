package com.yss.storm;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.yss.storm.submit.StormRemoteJarSubmiter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import com.yss.storm.node.NimbusNode;
import com.yss.storm.node.UiNode;

/**
 * Created by zhangchi on 2017/5/17.
 */

public class StormNodesService implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(StormNodesService.class);
    public final static String                    STORM_NIMBUS_PATH = "/docker/storm/nimbus";
    public final static String                    STORM_UI_PATH     = "/docker/storm/ui";

    private ConcurrentHashMap<String, NimbusNode> nimbusMap         = new ConcurrentHashMap<String, NimbusNode>();
    private ConcurrentHashMap<String, UiNode>     uiMap             = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String,String>   nimbusHostsMap = new ConcurrentHashMap<>();

    @Autowired
    private CuratorFramework                      curatorFramework;

    private PathChildrenCache                     nimbusChildrenCache;
    private PathChildrenCache                     uiChildrenCache ;
    private PathChildrenCache                     nimbusHostsChildCache;

    @Override
    public void afterPropertiesSet() throws Exception {
        startNimbusChildrenCache();
        startUChildrenCache();

    }

    private void startNimbusChildrenCache() throws Exception {
        nimbusChildrenCache = new PathChildrenCache(curatorFramework, STORM_NIMBUS_PATH, true);
        nimbusChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        nimbusChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                                          public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                                                  throws Exception {
                                              if (event.getType() == PathChildrenCacheEvent.Type.INITIALIZED) {
                                                  reloadNimbus(client);
                                              } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                                                  reloadNimbus(client);
                                              } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                                                  reloadNimbus(client);
                                              } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
                                                  reloadNimbus(client);
                                              }
                                          }
                                      });
    }

    private void startUChildrenCache() throws Exception {
        uiChildrenCache = new PathChildrenCache(curatorFramework, STORM_UI_PATH, true);
        uiChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        uiChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                    throws Exception {
                if (event.getType() == PathChildrenCacheEvent.Type.INITIALIZED) {
                    reloadUI(client);
                } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                    reloadUI(client);
                } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                    reloadUI(client);
                } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
                    reloadUI(client);
                }
            }
        });
    }

    private void reloadNimbus(CuratorFramework client) throws Exception {
        nimbusMap.clear();

        List<String> stormNimbusChildren = client.getChildren().forPath(STORM_NIMBUS_PATH);

        for (String nimbus : stormNimbusChildren) {
            String[] nimbusNode = nimbus.split(":");
            byte[]   port       = client.getData().forPath(STORM_NIMBUS_PATH + "/" + nimbus);
            logger.info("reloadUI "+nimbusNode[0] +" : "+port);
            nimbusMap.put(nimbusNode[0], new NimbusNode(nimbusNode[0], nimbusNode[1],Integer.parseInt(new String(port))));
        }
    }

    private void reloadUI(CuratorFramework client) throws Exception {
        uiMap.clear();

        List<String> stormUiChildren = client.getChildren().forPath(STORM_UI_PATH);

        for (String ui : stormUiChildren) {
            String[] uiNode = ui.split(":");
            byte[]   port       = client.getData().forPath(STORM_UI_PATH + "/" + ui);
            logger.info("reloadUI "+uiNode[0] +" : "+port);
            uiMap.put(uiNode[0], new UiNode(uiNode[0], Integer.parseInt(new String(port))));
        }
    }


    public List<NimbusNode> getNimbusNodeList() {
        List<NimbusNode> list = Lists.newArrayList();

        for (NimbusNode nimbusNode : nimbusMap.values()) {
            list.add(nimbusNode);
        }

        return list;
    }

    public List<UiNode> getUiNodeList() {
        List<UiNode> list = Lists.newArrayList();

        for (UiNode uiNode : uiMap.values()) {
            list.add(uiNode);
        }

        return list;
    }

}
