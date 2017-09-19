package com.yss.yarn.launch;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.*;

import com.floodCtr.generate.FloodJob;
import com.yss.ftp.FtpConnectionFactory;
import com.yss.storm.controller.StormDockerController;
import com.yss.storm.node.DrpcNode;
import com.yss.util.PropertiesUtil;
import com.yss.util.YarnUtil;
import com.yss.yarn.exception.NoNimbusException;
import com.yss.yarn.exception.ZKConfException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.service.Service;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.util.Apps;
import org.apache.hadoop.yarn.util.Records;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


import com.google.common.collect.Lists;

import com.yss.config.Conf;
import com.yss.ftp.FtpService;
import com.yss.storm.StormNodesService;
import com.yss.storm.node.NimbusNode;
import com.yss.util.FileUtil;
import com.yss.Expansion.Exception.JarNotExsitsException;
import com.yss.yarn.discovery.YarnThriftClient;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/20
 */
public class YarnLaunch implements YarnLaunchService, InitializingBean {
    private Logger            logger        = LoggerFactory.getLogger(YarnLaunch.class);
    private int               appMasterPort ;
    private int               nimbusPort   ;
    private int               uiPort        ;
    private YarnClient        yarnClient;
    private YarnConfiguration yarnConf;
    private String            defaultQueue;
    private String            classPath;
    @Autowired
    private YarnThriftClient  yarnThriftClient;
    @Autowired
    private StormNodesService stormNodesService;
    @Autowired
    private FtpService        ftpService;

    @Override
    public void afterPropertiesSet() throws Exception {
        appMasterPort = Integer.parseInt(PropertiesUtil.getProperty("appMasterPort"));
        nimbusPort = Integer.parseInt(PropertiesUtil.getProperty("nimbusPort"));
        uiPort = Integer.parseInt(PropertiesUtil.getProperty("uiPort"));
        defaultQueue = PropertiesUtil.getProperty("defaultQueue");
        classPath = PropertiesUtil.getProperty("appMasterClassPath");

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(30);
        poolConfig.setTestWhileIdle(true);
        //使用非公平锁，提高并发效率
        poolConfig.setFairness(false);



    }

    private String buildDRPCHostsArrays() {

        List<DrpcNode> list = stormNodesService.getDrpcNodeList();

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        List<String> drpcNodesList = Lists.newArrayList();

        for (DrpcNode drpcNode : list) {
            drpcNodesList.add(drpcNode.getDockerIp());
        }

        String dprcArray = "[";

        for (Object drpc : drpcNodesList) {
            dprcArray = dprcArray + "\\\"" + drpc + "\\\"" + ",";
        }

        dprcArray = dprcArray.substring(0, dprcArray.length() - 1) + "]";

        return dprcArray;

    }

    private String buildNimbusHostsArrays() {
        List<NimbusNode> list = stormNodesService.getNimbusNodeList();

        if (CollectionUtils.isEmpty(list)) {
            return "";
        }

        List<String> nimbusSeedsList = Lists.newArrayList();

        for (NimbusNode nimbusNode : list) {
            nimbusSeedsList.add(nimbusNode.getDockerIp());
        }

        String nimbusSeedsArray = "[";

        for (Object nimbus : nimbusSeedsList) {
            nimbusSeedsArray = nimbusSeedsArray + "\\\"" + nimbus + "\\\"" + ",";
        }

        nimbusSeedsArray = nimbusSeedsArray.substring(0, nimbusSeedsArray.length() - 1) + "]";

        return nimbusSeedsArray;
    }

    private String buildZkHostsArrays(String zk) {

        // 调用服务
        String       zkHostsArrays = "[";
        List<String> zkList        = Lists.newArrayList();
        String[]     zkArray       = zk.split(",");

        for (String zkHost : zkArray) {
            zkList.add(zkHost);
        }

        for (Object zkHost : zkList) {
            zkHostsArrays = zkHostsArrays + "\\\"" + zkHost + "\\\" ";
        }

        zkHostsArrays = zkHostsArrays.substring(0, zkHostsArrays.length() - 1) + "]";

        return zkHostsArrays;
    }

    /**
     * 暂不使用
     * 将文件写入yarn依赖的底层FTP存储系统，再封装成localResource给yarn使用
     * @param appHome
     * @param file
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    private LocalResource writeReturnFTPLocalResources(String appHome,File file) throws IOException, URISyntaxException {
//        long size = file.length();
//
//        ftpService.upload(appHome, file);
//
//        long          timeStamp     = ftpService.getFtpFileTimeStamp(Path.SEPARATOR +appHome + Path.SEPARATOR  + file.getName());
//        LocalResource localResource = LocalResource.newInstance(
//                org.apache.hadoop.yarn.api.records.URL.fromURI(
//                        new URI(FtpConnectionFactory.getRemoteFtpServerAddress()+Path.SEPARATOR +appHome+Path.SEPARATOR +file.getName())),
//                LocalResourceType.FILE,
//                LocalResourceVisibility.APPLICATION,
//                size,
//        timeStamp);
//        return localResource;

        return null;
    }


    /**
     * 将文件写入yarn依赖的底层HDFS存储系统，再封装成localResource给yarn使用
     * @param appHome
     * @param file
     * @return
     * @throws IOException
     */
    private LocalResource writeReturnHDFSLocalResources(String appHome,File file) throws IOException {
        FileSystem fs      = FileSystem.get(yarnConf);
        Path src     = new Path(file.getPath());
        Path       dst     = new Path(fs.getHomeDirectory(), appHome + Path.SEPARATOR + file.getName());
        fs.copyFromLocalFile(false, true, src, dst);
        return  YarnUtil.newYarnAppResource(fs, dst);

    }

    private void launch(String appName, String appMasterJar, String lanchMainClass,
                        Map<String, String> runenv)
            throws Exception {
        String hadoopHome = Conf.getYarnHadoopHome();
        classPath = classPath.replaceAll("HADOOP_HOME", hadoopHome);
        YarnClientApplication        client_app = yarnClient.createApplication();
        GetNewApplicationResponse    app        = client_app.getNewApplicationResponse();
        ApplicationId                appId      = app.getApplicationId();
        ApplicationSubmissionContext appContext = Records.newRecord(ApplicationSubmissionContext.class);

        appContext.setApplicationId(app.getApplicationId());
        appContext.setApplicationName(appName);
        appContext.setQueue(defaultQueue);

        ContainerLaunchContext     amContainer    = Records.newRecord(ContainerLaunchContext.class);
        Map<String, LocalResource> localResources = new HashMap<String, LocalResource>();

        logger.info("Copy App Master jar from local filesystem and add to local environment");
        logger.info("load  " + appMasterJar);

        File file = new File(appMasterJar);
        String     appHome = YarnUtil.getApplicationHomeForId(appId.toString());
        LocalResource localResource = null;

        String FS_SYSTEM = Conf.getFS_DEFAULT_FS();
        String fs = null;
        Map<String, String> env = new HashMap<String, String>();

        if(FS_SYSTEM.startsWith("hdfs")){
             localResource = writeReturnHDFSLocalResources(appHome,file);
             fs = "hdfs";
        }else if(FS_SYSTEM.startsWith("ftp")){
             localResource = writeReturnFTPLocalResources(appHome,file);
             fs="ftp";
             env.put("ftpAddr",FtpConnectionFactory.getAddr());
             env.put("ftpPort",FtpConnectionFactory.getPort());
             env.put("ftpUserName",FtpConnectionFactory.getUserName());
             env.put("ftpPassword",FtpConnectionFactory.getPassword());
        }

        localResources.put(file.getName(), localResource);
        amContainer.setLocalResources(localResources);
        logger.info("Set the environment for the application master");



        List<String> yarn_classpath_cmd = java.util.Arrays.asList("yarn", "classpath");

        logger.info("YARN CLASSPATH COMMAND = [" + yarn_classpath_cmd + "]");

        String yarn_class_path = classPath;
        Apps.addToEnvironment(env, ApplicationConstants.Environment.CLASSPATH.name(), "./"+file.getName());
        Apps.addToEnvironment(env, ApplicationConstants.Environment.CLASSPATH.name(), yarn_class_path);
        logger.info("YARN CLASSPATH = [" + yarn_class_path + "]");
        env.put("appName", appName);
        env.put("fs",fs);
        env.put("appId", appId.toString());
        env.putAll(runenv);

        amContainer.setEnvironment(env);

        Vector<String> vargs = new Vector<String>();

        vargs.add(Conf.getYarnJavaHome() + "/bin/java");
        vargs.add("-Dlogfile.name=" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/master.log");
        vargs.add(lanchMainClass);
        vargs.add("1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout");
        vargs.add("2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr");
        logger.info("Setting up app master command:" + vargs);
        amContainer.setCommands(vargs);

        Resource capability = Resource.newInstance(1000,1);
        appContext.setResource(capability);
        appContext.setAMContainerSpec(amContainer);
        appContext.setApplicationName(appName);
        yarnClient.submitApplication(appContext);
    }

    @Override
    public void launchApp(String appName, Map<String, String> env)
            throws Exception {
        URL    jarPathUrl = com.google.common.io.Resources.getResource("FloodContrYu-1.0-release.jar");
        String jarPath    = jarPathUrl.getPath();

        if (StringUtils.isEmpty(jarPath)) {
            throw new JarNotExsitsException("需要指定运行的jar不存在");
        }

        String targetPath = FileUtil.getJarPath(StormDockerController.class) + "/dockerpubjar";

        targetPath = targetPath.replace("file:", "");

        String targetFilePath = FileUtil.copy(jarPathUrl.openStream(), targetPath, "FloodContrYu-1.0-release.jar");

        launch(appName, targetFilePath, "com.floodCtr.storm.MasterServer", env);
    }

    @Override
    public void launchApp(String jarSourcePath, String appName,
                          Map<String, String> env)
            throws Exception {
        logger.info("hadoop class path is " + this.classPath);
        File   sourceJar = new File(jarSourcePath);
        String jarPath   = sourceJar.getPath();

        if (StringUtils.isEmpty(jarPath)) {
            throw new JarNotExsitsException("需要指定运行的jar不存在");
        }

        String targetPath = FileUtil.getJarPath(StormDockerController.class) + "/dockerpubjar";


        String target = FileUtil.copy(new FileInputStream(sourceJar), targetPath, sourceJar.getName());

        launch(appName, target, "com.floodCtr.storm.MasterServer", env);
    }

    @Override
    public void launchStormDockerComponent(String containerName, String dockerIp, String process,String node,
                                           String cm, String appId,Map<String, String> host) throws NoNimbusException, ZKConfException {
        try {

            // 调用服务
            String              dockerArgs = "storm " + process + " -c nimbus.thrift.port=" + nimbusPort
                                             + " -c ui.port=" + uiPort;
            Map<String, String> port       = new HashMap<>();

//          port.put(uiPort + "", uiPort + "");
            if (process.equals("nimbus")) {
                port.put(nimbusPort + "", nimbusPort + "");
            } else if (process.equals("ui")) {
                port.put(uiPort + "", uiPort + "");
            }

            if (StringUtils.isEmpty(Conf.getSTORM_ZK())) {
                throw new ZKConfException("请确保zk配置正确");
            }

            String zkHostsArrays = buildZkHostsArrays(Conf.getSTORM_ZK());

            dockerArgs = dockerArgs + " -c storm.zookeeper.servers=" + zkHostsArrays;

            String nimbusSeedsArray = buildNimbusHostsArrays();
            if(StringUtils.isEmpty(nimbusSeedsArray) && !process.equals("nimbus")){
                throw new NoNimbusException("请先配置nimbus");

            }

            dockerArgs = dockerArgs + " -c nimbus.seeds=" + (StringUtils.isEmpty(nimbusSeedsArray)?"["+"\\\""+dockerIp+"\\\""+"]":nimbusSeedsArray);

            String drpcArray = buildDRPCHostsArrays();
            if(StringUtils.isNotEmpty(drpcArray)) {
                dockerArgs = dockerArgs + " -c drpc.servers=" + drpcArray;
            }

            String priority = StringUtils.isEmpty(node)?FloodJob.PRIORITY.LOW.getCode()+"":FloodJob.PRIORITY.HIGH.getCode()+"";

            String imageName = "";
            if(cm.equals(3)){
                imageName = PropertiesUtil.getProperty("H2DockerImage");
            }else if(cm.equals("2")){
                imageName = PropertiesUtil.getProperty("H1DockerImage");
            }else{
                imageName = PropertiesUtil.getProperty("NOMALDockerImage");
            }

            yarnThriftClient.addDockerComponent(imageName,
                                                containerName,
                                                StringUtils.isEmpty(node)?null:node,
                                                dockerIp,
                                                process,
                                                priority,
                                                dockerArgs,
                                                null,
                                                cm,
                                                appId,
                                                host,
                                                port);
        } catch (TTransportException e) {
            logger.error("error ", e);
        } catch (TException e) {
            logger.error("error ", e);
        }
    }

    public void refresh() {
        if ((yarnClient != null) && yarnClient.isInState(Service.STATE.STARTED)) {
            yarnClient.stop();
        }
        Configuration conf =  new Configuration();
        conf.set("fs.defaultFS", Conf.getFS_DEFAULT_FS());
        conf.set("yarn.resourcemanager.address", Conf.getYARN_RESOURCEMANAGER_ADDREES());
        conf.set("yarn.resourcemanager.scheduler.address", Conf.getYARN_RESOURCEMANAGER_SCHEDULER_ADDRESS());

        yarnConf   = new YarnConfiguration(conf);

        yarnClient = YarnClient.createYarnClient();
        yarnClient.init(yarnConf);
        yarnClient.start();
    }

    public int getAppMasterPort() {
        return appMasterPort;
    }

    public void setAppMasterPort(int appMasterPort) {
        this.appMasterPort = appMasterPort;
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
