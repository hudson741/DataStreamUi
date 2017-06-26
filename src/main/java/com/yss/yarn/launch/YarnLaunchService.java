package com.yss.yarn.launch;


/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/20
 */
public interface YarnLaunchService {

    /**
     *
     * @throws Exception
     */
    void launchApp(String jarPath,String jarName,String hdfsUrl,String yarnUrl,String lanchMainClass) throws Exception;


}
