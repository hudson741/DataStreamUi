package com.yss.yarn.launch;


/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/20
 */
public interface YarnLaunchService {

    /**
     * 提交stormDocker 至yarn调度平台
     * @throws Exception
     */
    void launchStorm(String jarPath,String jarName,String hdfsUrl,String yarnUrl,String lanchMainClass) throws Exception;


}
