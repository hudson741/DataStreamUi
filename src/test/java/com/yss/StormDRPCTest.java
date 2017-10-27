package com.yss;

import org.apache.storm.thrift.TException;
import org.apache.storm.utils.DRPCClient;
import org.apache.storm.utils.Utils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/9/15
 */
public class StormDRPCTest {


    public static void main(String[] args) throws TException {
        int a=5;
        int b=32;
        double f1 = new BigDecimal((float)a/b).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println((int)(f1*100));
//
//        Map config = Utils.readDefaultConfig();
//        DRPCClient client = new DRPCClient(config,"zhangc4", 3772);
//        String result = client.execute("echo", "hello");
//        System.out.println(result);


    }
}
