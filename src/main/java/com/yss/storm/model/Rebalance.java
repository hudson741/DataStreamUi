package com.yss.storm.model;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/5/24
 */
public class Rebalance {

    private RebalanceOptions rebalanceOptions;

    private String callback = "callback";

    private static class RebalanceOptions {

        private int numWorkers;

        private JSONObject executors;

        public int getNumWorkers() {
            return numWorkers;
        }

        public void setNumWorkers(int numWorkers) {
            this.numWorkers = numWorkers;
        }

        public JSONObject getExecutors() {
            return executors;
        }

        public void setExecutors(JSONObject executors) {
            this.executors = executors;
        }


    }


    public RebalanceOptions getRebalanceOptions() {
        return rebalanceOptions;
    }

    public void setRebalanceOptions(RebalanceOptions rebalanceOptions) {
        this.rebalanceOptions = rebalanceOptions;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public static String getRebalanceInstanceJSONStr(int numWorkers,Map<String,Integer> components){
        Rebalance rebalance = new Rebalance();
        RebalanceOptions rebalanceOptions = new RebalanceOptions();
        rebalanceOptions.setNumWorkers(numWorkers);
        JSONObject jsonObject = new JSONObject();
        if(components!=null && !components.isEmpty()){
            for(String key:components.keySet()){
                jsonObject.put(key,components.get(key));
            }
            rebalanceOptions.setExecutors(jsonObject);
        }
        rebalance.setRebalanceOptions(rebalanceOptions);
        return JSONObject.toJSONString(rebalance);
    }

    public static void main(String[] args){
        Map<String,Integer> map = new HashMap<>();
        map.put("spout",2);
        map.put("count",3);
        System.out.println(Rebalance.getRebalanceInstanceJSONStr(3,map));

    }

}


