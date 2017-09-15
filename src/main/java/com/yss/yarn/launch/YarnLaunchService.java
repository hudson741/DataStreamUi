package com.yss.yarn.launch;

import java.util.Map;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/20
 */
public interface YarnLaunchService {

    /**
     * 内部直接调用
     * @param appName
     */
    void launchApp(String appName,Map<String,String> env) throws Exception;

    /**
     * 内部直接调用
     * @param appName
     */
    void launchApp(String jarPath,String appName,Map<String,String> env) throws Exception;

    /**
     * 发布storm
     */
    void launchStormDockerComponent(String containerName, String dockerIp, String process, String node,String cm,String appId,Map<String, String> host);

    /**
     * 更新
     */
    void refresh();
}
