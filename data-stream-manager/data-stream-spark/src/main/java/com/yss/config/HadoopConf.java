package com.yss.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/19
 */
public class HadoopConf {

    public static final String HADOOP_HOME = "/opt/hadoop-2.7.4";
    public static final String HADOOP_ETC_PATH = HADOOP_HOME + "/etc/hadoop";
    public static Configuration hadoopConf = new Configuration();
    public static YarnConfiguration yarnConfiguration = new YarnConfiguration(hadoopConf);

    static {
        hadoopConf = new Configuration();
        hadoopConf.addResource(new Path(HADOOP_ETC_PATH + "/core-site.xml"));
        yarnConfiguration = new YarnConfiguration(hadoopConf);
        yarnConfiguration.addResource(new Path(HADOOP_ETC_PATH + "/yarn-site.xml"));
    }

}
