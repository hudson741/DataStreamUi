package com.yss.Expansion;

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

    public static void main(String[] args){
        JschPoolKey jschPoolKey = new JschPoolKey();
        jschPoolKey.setHost("zhangc1");
        jschPoolKey.setUser("root");
        jschPoolKey.setPassword("Tudou=123");

        String json = jschPoolKey.Obj2Key();
        System.out.println("fuck "+json);


        JschPoolKey jschPoolKey1 = new JschPoolKey();
        jschPoolKey1.setHost("zhangc1");
        jschPoolKey1.setUser("root");
        jschPoolKey1.setPassword("Tudou=123");

        String json1 = jschPoolKey.Obj2Key();
        System.out.println("fuck "+json1);

        if(json1.equals(json)){
            System.out.println("fffffff");
        }

        JschPoolKey key = jschPoolKey.key2Obj(json);
        System.out.println(key.getHost()+": "+key.getPassword()+": "+key.getUser());
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
