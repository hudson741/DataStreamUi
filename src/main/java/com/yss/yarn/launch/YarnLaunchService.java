package com.yss.yarn.launch;

import java.io.IOException;

import java.util.List;
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
     *
     * @throws Exception
     */
    void launchApp(String jarPath, String jarName, String hdfsUrl, String yarnUrl, String lanchMainClass,Map<String,String> env)
            throws Exception;

    /**
     * 发布storm
     */
    void launchStormDockerComponent(String appName,String containerName, String dockerIp, String process, Map<String, String> host);
}
