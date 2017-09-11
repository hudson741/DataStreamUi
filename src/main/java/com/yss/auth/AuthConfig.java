package com.yss.auth;

import com.yss.util.PropertiesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/9/7
 */
public class AuthConfig {


    private static String sAdmin = PropertiesUtil.getProperty("aAdmin");

    private static String sAdminPd = PropertiesUtil.getProperty("aAdminPd");

    private static String user = PropertiesUtil.getProperty("user");

    private static String uPd = PropertiesUtil.getProperty("uPd");

    private static Map<String,List<String>> userUrlMap = new HashMap<>();

    static{
        List<String> userUrl = Lists.newArrayList() ;
        userUrl.add("/nodeM");
        userUrl.add("/yarnindex");
        userUrl.add("/dockerPub");
        userUrl.add("/stormPub");
        userUrl.add("/sindex");
        userUrlMap.put(Auth.USER.getCode(),userUrl);
    }

    public static boolean hasAuth(String user,String url){
        if(StringUtils.isEmpty(user)){
            return false;
        }
        if(user.equals(Auth.SUPER.getCode())){
            return true;
        }
        List<String> authUrl = userUrlMap.get(user);
        if(CollectionUtils.isEmpty(authUrl)){
            return false;
        }
        if(authUrl.contains(url)){
            return false;
        }
        return true;
    }

    public static Auth login(String user,String password){
        if(user.equals(sAdmin) && password.equals(sAdminPd)){
            return Auth.SUPER;
        }else if(user.equals(AuthConfig.user) && password.equals(uPd)) {
            return Auth.USER;
        }else{
            return Auth.NULL;
        }
    }

    public enum Auth{

        SUPER("SUPER"),USER("USER"),NULL("NULL");

        private Auth(String code){
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        private String code;
    }

}
