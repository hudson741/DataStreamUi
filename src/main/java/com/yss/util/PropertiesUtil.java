package com.yss.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/8/18
 */
public class PropertiesUtil {

    private static Properties properties = new Properties();

    static {
        try {
            File file = new File(FileUtil.getJarPath(PropertiesUtil.class)+"/dataStream.properties");
            if(!file.exists()){
                file.createNewFile();
            }
            properties.load(new FileInputStream(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getProperty(String key){

        String v = properties.getProperty(key,"");
        System.out.println(v);
        return v;

    }

    public static void main(String[] args){
        PropertiesUtil.getProperty("b");
    }


}
