package com.yss.auth;

import com.alibaba.fastjson.JSON;
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

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView)
            throws Exception {}

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // 获取请求的URL
        String url = request.getRequestURI();

        //如果不是权限需要的url，那么就直接鉴权成功
        if(!AuthConfig.isAuthUrl(url)){
            return true;
        }

        if(AuthConfig.hasAuth(request,url)){
            for (Cookie cookie : request.getCookies()){
                request.setAttribute("cookies."+cookie.getName() , cookie.getValue());
            }
            return true;
        }
        // 不符合条件的，跳转到登录界面
        request.getRequestDispatcher("/").forward(request, response);

        return false;
    }
}
