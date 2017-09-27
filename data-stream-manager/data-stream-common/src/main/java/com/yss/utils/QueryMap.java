package com.yss.utils;

import java.util.HashMap;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/4
 */
public class QueryMap extends HashMap<String, Object> {

    public QueryMap() {
        super();
    }

    public QueryMap put(String K, Object V){
        super.put(K, V);
        return this;
    }
}
