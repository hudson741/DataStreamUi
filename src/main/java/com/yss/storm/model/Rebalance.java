package com.yss.storm.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/5/24
 */
public class Rebalance {
    private RebalanceOptions rebalanceOptions;
    private String           callback;

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();

        map.put("spout", 2);
        map.put("count", 3);
        System.out.println(Rebalance.getRebalanceInstanceJSONStr(null, map, null));

    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public static String getRebalanceInstanceJSONStr(Integer numWorkers, Map<String, Integer> components,
                                                     String callback) {
        Rebalance        rebalance        = new Rebalance();
        RebalanceOptions rebalanceOptions = new RebalanceOptions();

        if ((numWorkers != null) && (numWorkers > 0)) {
            rebalanceOptions.setNumWorkers(numWorkers);
        }

        JSONObject jsonObject = new JSONObject();

        if ((components != null) &&!components.isEmpty()) {
            for (String key : components.keySet()) {
                jsonObject.put(key, components.get(key));
            }

            rebalanceOptions.setExecutors(jsonObject);
        }

        rebalance.setRebalanceOptions(rebalanceOptions);

        if (!StringUtils.isEmpty(callback)) {
            rebalance.setCallback(callback);
        }

        return JSONObject.toJSONString(rebalance);
    }

    public RebalanceOptions getRebalanceOptions() {
        return rebalanceOptions;
    }

    public void setRebalanceOptions(RebalanceOptions rebalanceOptions) {
        this.rebalanceOptions = rebalanceOptions;
    }

    private static class RebalanceOptions {
        private Integer    numWorkers;
        private JSONObject executors;

        public JSONObject getExecutors() {
            return executors;
        }

        public void setExecutors(JSONObject executors) {
            this.executors = executors;
        }

        public Integer getNumWorkers() {
            return numWorkers;
        }

        public void setNumWorkers(Integer numWorkers) {
            this.numWorkers = numWorkers;
        }
    }
}
