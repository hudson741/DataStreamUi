package com.yss.storm.nimbus;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

/**
 * Created by zhangchi on 2017/5/17.
 */
@Service
public class NimbusNodesService implements InitializingBean {
    public final static String                    STORM_PATH = "/storm/nimbuses";
    private ConcurrentHashMap<String, NimbusNode> nimbusMap  = new ConcurrentHashMap<String, NimbusNode>();
    @Autowired
    private CuratorFramework                      curatorFramework;
    private PathChildrenCache                     childrenCache;

    public void afterPropertiesSet() throws Exception {
        childrenCache = new PathChildrenCache(curatorFramework, STORM_PATH, true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                                      public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {

                                          if (event.getType() == PathChildrenCacheEvent.Type.INITIALIZED) {
                                              reload(client);
                                          } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                                              reload(client);
                                          } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                                              reload(client);
                                          } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
                                              reload(client);
                                          }
                                      }
                                  });
    }

    private void reload(CuratorFramework client) throws Exception {
        nimbusMap.clear();

        List<String> stormNimbusChildren = client.getChildren().forPath(STORM_PATH);

        for (String nimbus : stormNimbusChildren) {
            String[] nimbusNode = nimbus.split(":");

            nimbusMap.put(nimbusNode[0], new NimbusNode(nimbusNode[0], Integer.parseInt(nimbusNode[1])));
        }
    }

    public List<NimbusNode> getNimbusNodeList() {
        List<NimbusNode> list = Lists.newArrayList();

        for (NimbusNode nimbusNode : nimbusMap.values()) {
            list.add(nimbusNode);
        }

        return list;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
