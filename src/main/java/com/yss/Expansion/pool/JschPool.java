package com.yss.Expansion.pool;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import org.assertj.core.util.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Session;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/10/11
 */
public class JschPool {
    private static Logger                                   logger            =
        LoggerFactory.getLogger(JschProxy.class);
    private static ConcurrentHashMap<String, List<Session>> concurrentHashMap = new ConcurrentHashMap<>();
    private static GenericKeyedObjectPool<String, Session>  pool;

    static {
        GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();

        poolConfig.setMaxTotal(1000);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMaxTotalPerKey(25);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        poolConfig.setMaxIdlePerKey(20);
        poolConfig.setMinEvictableIdleTimeMillis(1000 * 50);
        poolConfig.setTimeBetweenEvictionRunsMillis(1000 * 5);

        // 使用非公平锁，提高并发效率
        poolConfig.setFairness(false);
        pool = new GenericKeyedObjectPool(new JschSessionObjectFactory(), poolConfig);

        // 循环监控，移除失效的session
        new Thread(new Runnable() {
                       @Override
                       public void run() {
                           while (true) {
                               try {
                                   Thread.currentThread().sleep(2000);
                               } catch (InterruptedException e) {
                                   logger.error("error ", e);
                               }

                               for (String key : concurrentHashMap.keySet()) {
//                                   logger.info("with " + key + " size " + concurrentHashMap.get(key).size());
//                                   logger.info("pool now active key :" + pool.getNumActive(key) + "  idle:"
//                                               + pool.getNumIdle(key) + " getDestroyedCount: "
//                                               + pool.getDestroyedCount() + " getDestroyedByEvictorCount "
//                                               + pool.getDestroyedByEvictorCount() + " getReturnedCount"
//                                               + pool.getReturnedCount());

                                   List<Session> list = concurrentHashMap.get(key);

                                   if (CollectionUtils.isNotEmpty(list)) {
                                       Iterator<Session> iterator = list.iterator();

                                       while (iterator.hasNext()) {
                                           Session session = iterator.next();

                                           if (!session.isConnected()) {
                                               try {
                                                   pool.invalidateObject(key, session);
                                               } catch (Exception e) {
                                                   logger.error("error", e);
                                               }

                                               iterator.remove();
                                           }
                                       }
                                   }
                               }
                           }
                       }
                   }).start();
    }

    public static void clear(String key) {
        List<Session> list = concurrentHashMap.get(key);

        if (CollectionUtils.isNotEmpty(list)) {
            for (Session session : list) {
                if ((session != null) && session.isConnected()) {
                    session.disconnect();
                }
            }
        }
    }

    public static void invalidateObject(String key, Session session) {
        if (StringUtils.isEmpty(key) || (session == null)) {
            return;
        }

        if (session.isConnected()) {
            session.disconnect();
        }

        try {
            pool.invalidateObject(key, session);
        } catch (Exception e) {
            logger.error("error ", e);
        }
    }

    public static void putToMap(String key, Session session) {
        if (StringUtils.isEmpty(key) || (null == session)) {
            return;
        }

        synchronized (key.intern()) {
            List<Session> list = concurrentHashMap.get(key);

            if (CollectionUtils.isEmpty(list)) {
                list = Lists.newArrayList();
                list.add(session);
                concurrentHashMap.put(key, list);
            } else {
                list.add(session);
            }
        }
    }

    public static void returnSession(String jschPoolKey, Session session) {
        if (StringUtils.isEmpty(jschPoolKey) || (null == session)) {
            return;
        }

        if (session.isConnected()) {
            pool.returnObject(jschPoolKey, session);
        } else {
            try {
                pool.invalidateObject(jschPoolKey, session);
            } catch (Exception e) {
                logger.error("error ", e);
            }
        }
    }

    public static Session getSession(JschPoolKey jschPoolKey) throws Exception {
        Session session = pool.borrowObject(jschPoolKey.Obj2Key(), 1500);

        logger.info("pool now active key :" + pool.getNumActive(jschPoolKey.Obj2Key()) + "  idle:"
                    + pool.getNumIdle(jschPoolKey.Obj2Key()) + " getDestroyedCount: " + pool.getDestroyedCount()
                    + " getDestroyedByEvictorCount " + pool.getDestroyedByEvictorCount() + " getReturnedCount"
                    + pool.getReturnedCount());

        return session;
    }
}
