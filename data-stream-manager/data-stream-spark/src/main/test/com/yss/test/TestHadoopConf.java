package com.yss.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.server.balancer.Dispatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/15
 */

public class TestHadoopConf {

    @Test
    public void testReadConf() {
//        System.setProperty("hadoop.home.dir","E:\\work\\hadoop-2.7.4");
        Configuration hadoopConf = new Configuration();
        hadoopConf.addResource(new Path("E:\\work\\hadoop-2.7.4\\etc\\hadoop\\core-site.xml"));
        System.out.println(hadoopConf);
    }

    @Test
    public void testDNS() throws UnknownHostException {
        String host = "http://yarn1:4040".replace("http://", "").split(":")[0];
        String ip = InetAddress.getByName("192.168.102.154").getHostAddress();
        System.out.println(ip);
//                InetAddress inetAddr= InetAddress.getByAddress(ipBytes); //创建InetAddress对象
//        String canonical=inetAddr.getCanonicalHostName();       //获取域名
//        String host=inetAddr.getHostName();
    }

    @Test
    public void testtime() {
        long time = 61;

        StringBuilder builder = new StringBuilder();
        //计算秒
        long second = time % 60;
        System.out.println("花费：" + second);
        long minute = time / 60;
        System.out.println("分：" + minute);
        long hour = time / 60 / 60;
        System.out.println("时：" + hour);


    }


}
