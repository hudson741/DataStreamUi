package com.yss.config;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/28
 */
public class Conf {

    private static volatile String STORM_ZK ;

    private static volatile String FS_DEFAULT_FS ;

    private static volatile String YARN_RESOURCEMANAGER_ADDREES ;

    private static volatile String YARN_RESOURCEMANAGER_SCHEDULER_ADDRESS ;

    private static volatile String YARN_RESOURCE_UI_ADDRESS ;

    private static volatile String YARN_JAVA_HOME;

    private static volatile String YARN_HADOOP_HOME;

    public static  String getSTORM_ZK() {
        return STORM_ZK;
    }

    public static void setSTORM_ZK(String STORM_ZK) {
        Conf.STORM_ZK = STORM_ZK;
    }

    public static String getFS_DEFAULT_FS() {
        return FS_DEFAULT_FS;
    }

    public static void setFS_DEFAULT_FS(String FS_DEFAULT_FS) {
        Conf.FS_DEFAULT_FS = FS_DEFAULT_FS;
    }

    public static String getYARN_RESOURCEMANAGER_ADDREES() {
        return YARN_RESOURCEMANAGER_ADDREES;
    }

    public static void setYARN_RESOURCEMANAGER_ADDREES(String YARN_RESOURCEMANAGER_ADDREES) {
        Conf.YARN_RESOURCEMANAGER_ADDREES = YARN_RESOURCEMANAGER_ADDREES;
    }

    public static String getYARN_RESOURCEMANAGER_SCHEDULER_ADDRESS() {
        return YARN_RESOURCEMANAGER_SCHEDULER_ADDRESS;
    }

    public static  void setYARN_RESOURCEMANAGER_SCHEDULER_ADDRESS(String YARN_RESOURCEMANAGER_SCHEDULER_ADDRESS) {
        Conf.YARN_RESOURCEMANAGER_SCHEDULER_ADDRESS = YARN_RESOURCEMANAGER_SCHEDULER_ADDRESS;
    }

    public static String getYarnResourceUiAddress() {
        return YARN_RESOURCE_UI_ADDRESS;
    }

    public static void setYarnResourceUiAddress(String yarnResourceUiAddress) {
        YARN_RESOURCE_UI_ADDRESS = yarnResourceUiAddress;
    }

    public static String getYarnJavaHome() {
        return YARN_JAVA_HOME;
    }

    public static void setYarnJavaHome(String yarnJavaHome) {
        YARN_JAVA_HOME = yarnJavaHome;
    }

    public static String getYarnHadoopHome() {
        return YARN_HADOOP_HOME;
    }

    public static void setYarnHadoopHome(String yarnHadoopHome) {
        YARN_HADOOP_HOME = yarnHadoopHome;
    }

}
