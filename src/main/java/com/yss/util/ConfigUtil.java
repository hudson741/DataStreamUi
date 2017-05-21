package com.yss.util;

import java.io.IOException;
import java.util.Properties;

/**
 * User: mzang
 * Date: 2014-10-14
 * Time: 16:59
 */
public class ConfigUtil {

    static Properties props = new Properties();

    static {
        try {
            props.load(ConfigUtil.class.getResourceAsStream("/conf.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProp(String key) {
        return props.get(key).toString();
    }

}
