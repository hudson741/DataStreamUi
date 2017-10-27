package com.yss.auth;

import com.yss.util.PropertiesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

    private static String stormuser = PropertiesUtil.getProperty("stormuser");

    private static String stormuPd = PropertiesUtil.getProperty("stormuPd");

    public static List<String> superUrlAuth = Lists.newArrayList();

    public static List<String> stormUrlAuth = Lists.newArrayList();

    public static List<String> sparkUrlAuth = Lists.newArrayList();


    //由于用户较少，这里配置为不能访问的url
    private static Map<String,List<String>> userAuthFilterUrlMap = new HashMap<>();

    public static boolean isAuthUrl(String url){
        if(superUrlAuth.contains(url) || stormUrlAuth.contains(url) || sparkUrlAuth.contains(url)){
            return true;
        }
        return false;
    }

    static{
        superUrlAuth.add("/nodeM2");
        superUrlAuth.add("/hrc");
        superUrlAuth.add("/yarnKillApp");
        superUrlAuth.add("/dockerPub");
        superUrlAuth.add("/stormdockerPub");
        superUrlAuth.add("/settingsSet");
        superUrlAuth.add("/yarnStopDockerJob");
        superUrlAuth.add("/removeDockerJob");
        superUrlAuth.add("/yarnRestart");
        superUrlAuth.add("/dockerindexftl");
        superUrlAuth.add("/hcmd");
        superUrlAuth.add("/registerRoot");
        superUrlAuth.add("/expand");
        superUrlAuth.add("/delPNodes");
        superUrlAuth.add("/confSyn");
        superUrlAuth.add("/checkConnect");
        superUrlAuth.add("/index");

        stormUrlAuth.add("/activeTopo");

        sparkUrlAuth.add("/spark");

        List<String> stormAuthFilter = Lists.newArrayList();
        stormAuthFilter.addAll(superUrlAuth);
        stormAuthFilter.addAll(sparkUrlAuth);

        userAuthFilterUrlMap.put(Auth.STORM.getCode(),stormAuthFilter);


    }

    public static Auth getUser(HttpServletRequest request){

        String sessionId = null;
        String token = null;

        Cookie[] cookies = request.getCookies();
        if(ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")) {
                    token = cookie.getValue();
                }
            }
        }
        if(StringUtils.isEmpty(token)){
            return null;
        }

        Map<String,Object> map = JwtUtil.verify(token);
        sessionId = (String)map.get("sessionId");
        return Auth.getUser(sessionId);

    }

    public static boolean hasAuth(HttpServletRequest request,String url){
        Auth auth = getUser(request);

        if(auth == null){
            return false;
        }
        if(auth == Auth.SUPER){
            return true;
        }
        List<String> authUrl = userAuthFilterUrlMap.get(auth.getCode());
        if(CollectionUtils.isEmpty(authUrl)){
            return false;
        }
        if(authUrl.contains(url)){
            return false;
        }
        return true;
    }

    public static Auth login(String user,String password){
        String token = JwtUtil.signPassWord(password);
        if(user.equals(sAdmin) && token.equals(sAdminPd)){
            return Auth.SUPER;
        }else if(user.equals(stormuser) && token.equals(stormuPd)) {
            return Auth.STORM;
        }else{
            return Auth.NULL;
        }
    }



    public enum Auth{

        SUPER("SUPER"),STORM("STORM"),NULL("NULL");

        private Auth(String code){
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public static Auth getUser(String sessionId) {
            if(sessionId.equals(SUPER.getCode())){
                return SUPER;
            }else if(sessionId.equals(STORM.getCode())){
                return STORM;
            }else{
                return NULL;
            }
        }

        private String code;
    }

}
