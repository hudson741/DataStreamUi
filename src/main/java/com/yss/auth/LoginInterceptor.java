package com.yss.auth;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/9/7
 */
public class LoginInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    /**
     * Handler执行完成之后调用这个方法
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exc)
            throws Exception {}

    /**
     * Handler执行之后，ModelAndView返回之前调用这个方法
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView)
            throws Exception {}

    /**
     * Handler执行之前调用这个方法
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // 获取请求的URL
        String url = request.getRequestURI();

        // URL:login.jsp是公开的;这个demo是除了login.jsp是可以公开访问的，其它的URL都进行拦截控制
        System.out.println("fuck  " + url);

        if ((url.indexOf("logind") >= 0)
                || url.equals("/")
                || (url.indexOf("css") >= 0)
                || (url.indexOf("js") >= 0)
                || (url.indexOf(".woff") >=0)
                || (url.indexOf(".ico") >=0)
                || (url.indexOf("error") >=0)) {
            return true;
        }

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
        logger.info("fuck  " + url);
        if(StringUtils.isEmpty(token)){
            return false;
        }

        Map<String,Object> map = JwtUtil.verify(token);
        sessionId = (String)map.get("sessionId");

        if(AuthConfig.hasAuth(sessionId,url)){
            return true;
        }
        // 不符合条件的，跳转到登录界面
        request.getRequestDispatcher("/").forward(request, response);

        return false;
    }
}
