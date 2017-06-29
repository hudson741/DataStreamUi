package com.yss.yarn.launch;

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.net.URLDecoder;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.yss.config.Conf;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.floodCtr.generate.FloodContrThriftService;

import com.yss.util.FileUtil;
import com.yss.yarn.Exception.JarNotExsitsException;
import com.yss.yarn.controller.YarnLaunchController;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/20
 */
public class YarnLaunch implements YarnLaunchService {
    private Logger              logger        = LoggerFactory.getLogger(YarnLaunchService.class);
    private int                 appMasterPort = 9000;
    private String              appName;
    private String              defaultQueue;
    private int                 amSize;
    private String              classPath;
//    private Map<String, String> yarnConf;
    private String              remoteAPPMasterHost;
    private int nimbusPort = 9005;
    private int uiPort = 9002;

    private Map<String,String> getYarnConf(){
        Map<String,String> map = new HashMap<>();
        map.put("fs.defaultFS", Conf.getFS_DEFAULT_FS());
        map.put("yarn.resourcemanager.address",Conf.getYARN_RESOURCEMANAGER_ADDREES());
        map.put("yarn.resourcemanager.scheduler.address",Conf.getYARN_RESOURCEMANAGER_SCHEDULER_ADDRESS());
        return map;
    }

    @Override
    public void launchApp(String appName,Map<String,String> env) throws Exception {
        URL    jarPathUrl = com.google.common.io.Resources.getResource("FloodContrYu-1.0-release.jar");
        String jarPath    = jarPathUrl.getPath();

        if (StringUtils.isEmpty(jarPath)) {
            throw new JarNotExsitsException("需要指定运行的jar不存在");
        }

        String targetPath = getJarPath(YarnLaunchController.class) + "/dockerpubjar";

        targetPath = targetPath.replace("file:", "");

        String target = FileUtil.copy(jarPathUrl.openStream(), targetPath, "FloodContrYu-1.0-release.jar");

        APPLaunch.launchApplication(appName,
                                    defaultQueue,
                                    amSize,
                                    target,
                                    getYarnConf(),
                                    classPath,
                                    "com.floodCtr.storm.MasterServer",
                                    env);
    }

    @Override
    public void launchApp(String path, String appName, Map<String, String> env) throws Exception {
        URL    jarPathUrl = new File(path).toURL();
        String jarPath    = jarPathUrl.getPath();

        if (StringUtils.isEmpty(jarPath)) {
            throw new JarNotExsitsException("需要指定运行的jar不存在");
        }

        String targetPath = getJarPath(YarnLaunchController.class) + "/dockerpubjar";

        targetPath = targetPath.replace("file:", "");

        String target = FileUtil.copy(jarPathUrl.openStream(), targetPath, "FloodContrYu-1.0-release.jar");

        APPLaunch.launchApplication(appName,
                defaultQueue,
                amSize,
                target,
                getYarnConf(),
                classPath,
                "com.floodCtr.storm.MasterServer",
                env);
    }

    public void launchApp(String path, String jarName, String hdfsUrl, String yarnUrl, String lanchMainClass,Map<String,String> env)
            throws Exception {
        URL    jarPathUrl = new File(path).toURL();
        String jarPath    = jarPathUrl.getPath();

        if (StringUtils.isEmpty(jarPath)) {
            throw new JarNotExsitsException("需要指定运行的jar不存在");
        }

        String targetPath = getJarPath(YarnLaunchController.class) + "/dockerpubjar";

        targetPath = targetPath.replace("file:", "");

        String              target   = FileUtil.copy(jarPathUrl.openStream(), targetPath, jarName);
        Map<String, String> yarnConf = new HashMap<>();

        yarnConf.put("fs.defaultFS", hdfsUrl);
        yarnConf.put("yarn.resourcemanager.address", yarnUrl);
        APPLaunch.launchApplication(appName, defaultQueue, amSize, target, yarnConf, classPath, lanchMainClass,env);
    }

    @Override
    public void launchStormDockerComponent(String appName,String containerName, String dockerIp, String process,
                                           Map<String, String> host) {
        TTransport transport = new TSocket(appName, appMasterPort);

        try {
            transport.open();

            // 协议层
            TProtocol protocol = new TBinaryProtocol(transport);

            // 创建RPC客户端
            FloodContrThriftService.Client client = new FloodContrThriftService.Client(protocol);
            // 调用服务

            String dockerArgs = "storm " + process + " -c nimbus.thrift.port="+nimbusPort+" -c ui.port="+uiPort;
            Map<String, String> port = new HashMap<>();

            port.put(uiPort + "", uiPort + "");

            if (process.equals("nimbus")) {
                port.put(nimbusPort + "", nimbusPort + "");
            } else if (process.equals("ui")) {
                port.put(uiPort + "", uiPort + "");
            }

            client.addDockerComponent("storm", containerName, dockerIp, dockerArgs, "192.168.10.8", host, port);

            // 关闭通道
            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public int getAmSize() {
        return amSize;
    }

    public void setAmSize(int amSize) {
        this.amSize = amSize;
    }

    public int getAppMasterPort() {
        return appMasterPort;
    }

    public void setAppMasterPort(int appMasterPort) {
        this.appMasterPort = appMasterPort;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getDefaultQueue() {
        return defaultQueue;
    }

    public void setDefaultQueue(String defaultQueue) {
        this.defaultQueue = defaultQueue;
    }

    private String getJarPath(Class<?> my_class) throws IOException {
        ClassLoader loader     = my_class.getClassLoader();
        String      class_file = my_class.getName().replaceAll("\\.", "/") + ".class";

        for (Enumeration<URL> itr = loader.getResources(class_file); itr.hasMoreElements(); ) {
            URL url = itr.nextElement();

            if ("jar".equals(url.getProtocol())) {
                String toReturn = url.getPath();

                toReturn = toReturn.replaceAll("\\+", "%2B");
                toReturn = URLDecoder.decode(toReturn, "UTF-8");

                String returnP = toReturn.replaceAll("!.*$", "");

                return new File(returnP).getParent();
            }
        }

        File   file = new File(loader.getResource(class_file).getFile());
        String pro  = System.getProperty("user.dir");

        while (true) {
            if (file.exists() && file.getAbsolutePath().endsWith(pro)) {
                return file.getAbsolutePath();
            } else {
                File file1 = file;

                file = new File(file1.getParent());
                logger.info("hudson " + file.getAbsolutePath());

                if (!file.exists()) {
                    return null;
                }
            }
        }
    }

    public String getRemoteAPPMasterHost() {
        return remoteAPPMasterHost;
    }

    public void setRemoteAPPMasterHost(String remoteAPPMasterHost) {
        this.remoteAPPMasterHost = remoteAPPMasterHost;
    }

//    public Map<String, String> getYarnConf() {
//        return yarnConf;
//    }
//
//    public void setYarnConf(Map<String, String> yarnConf) {
//        this.yarnConf = yarnConf;
//    }


    public int getNimbusPort() {
        return nimbusPort;
    }

    public void setNimbusPort(int nimbusPort) {
        this.nimbusPort = nimbusPort;
    }

    public int getUiPort() {
        return uiPort;
    }

    public void setUiPort(int uiPort) {
        this.uiPort = uiPort;
    }
}
