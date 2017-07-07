package com.yss.yarn.discovery;

import java.net.InetSocketAddress;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.RetryUntilElapsed;

import org.assertj.core.util.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import com.yss.config.Conf;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/29
 */
public class YarnMasterServerDiscovery implements ServerAddressDiscovery {
    private final static String MASTER_SERVER_PATH = "/floodContrYu/masterServer";
    private Logger              logger             = LoggerFactory.getLogger(YarnMasterServerDiscovery.class);
    private CuratorFramework    curatorFramework   = null;
    private List<CallBack>      callBackList       = Lists.newArrayList();
    private NodeCache           masterServerCache;
    InetSocketAddress           masterAddress;
    @Autowired
    private YarnThriftClient    thriftClient;

    public static void main(String[] args) throws Exception {}

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
                startFloodJobRunningStateCache();
            }
        } catch (Exception e) {
            logger.error("error ", e);
        }
    }

    public void register(CallBack callBack) {
        this.callBackList.add(callBack);
    }

    private void reload() {
        String data = null;

        try {
            data = new String(curatorFramework.getData().forPath(MASTER_SERVER_PATH));
        } catch (Exception e) {
            logger.error("error ", e);

            return;
        }

        String[] server       = data.split(":");
        String   masterServer = server[0];
        Integer  port         = Integer.parseInt(server[1]);

        if (masterServer.equals("10.135.1.181")) {
            masterServer = "zhangc1";
        } else if (masterServer.equals("10.186.58.13")) {
            masterServer = "zhangc2";
        } else if (masterServer.equals("10.135.96.95")) {
            masterServer = "zhangc3";
        } else if (masterServer.equals("10.104.108.213")) {
            masterServer = "zhangc4";
        } else if (masterServer.equals("10.104.254.122")) {
            masterServer = "zhangc5";
        }

        logger.info("found appMaster " + masterServer + "  " + port);
        masterAddress = new InetSocketAddress(masterServer, port);
    }

    public void remove(CallBack callBack) {
        this.callBackList.remove(callBack);
    }

    private void startFloodJobRunningStateCache() throws Exception {
        masterServerCache = new NodeCache(curatorFramework, MASTER_SERVER_PATH);
        masterServerCache.getListenable().addListener(new NodeCacheListener() {
                                          @Override
                                          public void nodeChanged() {
                                              logger.info("node Changed......");
                                              reload();

                                              if (callBackList.size() > 0) {
                                                  for (CallBack c : callBackList) {
                                                      c.call();
                                                  }
                                              }
                                          }
                                      });
        masterServerCache.start();
    }

    @Override
    public InetSocketAddress getServiceAdress() {
        return masterAddress;
    }

    public YarnThriftClient getThriftClient() {
        return thriftClient;
    }

    public void setThriftClient(YarnThriftClient thriftClient) {
        this.thriftClient = thriftClient;
    }

    interface CallBack {

        /**
         * 回调方法
         * @return
         */
        String call();
    }
}
