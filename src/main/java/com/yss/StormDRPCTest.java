package com.yss;

import org.apache.storm.thrift.TException;
import org.apache.storm.utils.DRPCClient;
import org.apache.storm.utils.Utils;

import java.util.Map;
import java.util.Random;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/9/15
 */
public class StormDRPCTest {


    public static void main(String[] args) throws TException {
        Map config = Utils.readDefaultConfig();
        DRPCClient client = new DRPCClient(config,"zhangc4", 3772);
        String result = client.execute("echo", "hello");
        System.out.println(result);


    }
}
