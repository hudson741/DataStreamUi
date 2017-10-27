package com.yss.Expansion.pool;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/8/23
 */
public class JschPoolKey {

    private String user;

    private String password;

    private String host;

    public JschPoolKey(){}

    public JschPoolKey(String user,String password,String host){
        this.user = user;
        this.password = password;
        this.host = host;
    }

    public String Obj2Key(){

        return JSONObject.toJSONString(this);
    }

    public static JschPoolKey key2Obj(String key){
        return JSONObject.parseObject(key,JschPoolKey.class);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
