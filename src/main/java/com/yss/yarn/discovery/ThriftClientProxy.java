package com.yss.yarn.discovery;

import java.lang.reflect.Method;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.Proxy;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/7/7
 */
public class ThriftClientProxy implements InitializingBean {
    private Logger                            logger = LoggerFactory.getLogger(YarnThriftClient.class);
    private GenericObjectPool<TServiceClient> pool;
    private Object                            proxyClient;
    TServiceClientFactory                     factory;
    @Autowired
    private ServerAddressDiscovery            serverAddressDiscovery;

    @Override
    public void afterPropertiesSet() throws Exception {

        // 加载Iface接口
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

        poolConfig.setMaxTotal(30);
        poolConfig.setTimeBetweenEvictionRunsMillis(1000*60);
        poolConfig.setTestWhileIdle(true);
        //使用非公平锁，提高并发效率
        poolConfig.setFairness(false);

        pool = new GenericObjectPool(new ThriftClientObjectFactory(factory, serverAddressDiscovery), poolConfig);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        proxyClient = Proxy.newProxyInstance(classLoader,
                                             proxyClient.getClass().getInterfaces(),
                                             new InvocationHandler() {
                                                 @Override
                                                 public Object invoke(Object o, Method method, Object[] objects)
                                                         throws Throwable {
                                                     TServiceClient client = pool.borrowObject(500);

                                                     logger.info("pool now active :" + pool.getNumActive() + "  idle:"
                                                                 + pool.getNumIdle() + " wait: "
                                                                 + pool.getNumWaiters());

                                                     if (client == null) {
                                                         return null;
                                                     }

                                                     try {
                                                         Object result = method.invoke(client, objects);

                                                         pool.returnObject(client);

                                                         return result;
                                                     } catch (Exception e) {
                                                         pool.invalidateObject(client);

                                                         throw e;
                                                     }
                                                 }
                                             });
        serverAddressDiscovery.register(new ThriftCallBack());
    }

    public void clear() {
        logger.info("收到回调信息，清理本地无用对象");
        pool.clear();
    }

    public static void main(String[] args) throws Exception {



    }

    public TServiceClientFactory getFactory() {
        return factory;
    }

    public void setFactory(TServiceClientFactory factory) {
        this.factory = factory;
    }

    public Object getProxyClient() {
        return proxyClient;
    }

    public void setProxyClient(Object proxyClient) {
        this.proxyClient = proxyClient;
    }

    class ThriftCallBack implements YarnMasterServerDiscovery.CallBack {
        @Override
        public String call() {
            clear();

            return null;
        }
    }
}
