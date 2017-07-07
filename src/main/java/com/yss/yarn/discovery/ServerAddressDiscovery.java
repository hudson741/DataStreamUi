package com.yss.yarn.discovery;

import java.net.InetSocketAddress;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/7/7
 */
public interface ServerAddressDiscovery {

    /**
     * 获取远程服务在zk上的地址
     * @return
     */
    InetSocketAddress getServiceAdress();

    /**
     * 刷新配置
     * @throws Exception
     */
    void refresh() throws Exception;

    /**
     * 注册回调，针对监听地址变化时
     * @param callBack
     */
    void register(YarnMasterServerDiscovery.CallBack callBack);

    /**
     * 移除回调
     * @param callBack
     */
    void remove(YarnMasterServerDiscovery.CallBack callBack);

}
