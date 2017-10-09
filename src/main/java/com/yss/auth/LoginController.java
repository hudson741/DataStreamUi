package com.yss.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/9/7
 */

@RestController
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * 默认登录页
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/",
            method = RequestMethod.GET
    )
    public ModelAndView stormIndex(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("login");
//        return new ModelAndView("/pages/examples/login");
    }

    /**
     * 默认错误页
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/error",
            method = RequestMethod.GET
    )
    public ModelAndView errorForward(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("login");
    }

    /**
     * 超管首页
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/sindex",
            method = RequestMethod.GET
    )
    public ModelAndView Index(ModelMap model, HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView("index");
        //index.html全局首页
        return modelAndView;
    }


    /**
     * 超管首页
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/sindez",
            method = RequestMethod.GET
    )
    public ModelAndView sindez(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("indexzz");
    }

    /**
     * 普通用户首页
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/indexl",
            method = RequestMethod.GET
    )
    public ModelAndView Indexl(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("indexl");
    }

    @RequestMapping(
        value = "/logind",
        method = RequestMethod.POST
    )
    public ModelAndView login( HttpServletResponse response, @RequestParam("username") String username, @RequestParam("password") String password ) throws IOException {

        AuthConfig.Auth auth = AuthConfig.login(username,password);

        Map<String,Object> map = new HashMap<>();
        map.put("sessionId",auth.getCode());
        map.put("timeStamp",System.currentTimeMillis()+"");
        String token = JwtUtil.sign(map);
        response.addCookie(new Cookie("sessionId",token));

        if(auth == AuthConfig.Auth.NULL){
            response.sendRedirect("/");
            return null;
        }

        if(auth == AuthConfig.Auth.SUPER){
            response.sendRedirect("/sindex");
        }else{
            response.sendRedirect("/indexl");
        }

        return null;
    }


    @RequestMapping(
            value = "/logout",
            method = RequestMethod.GET
    )
    public String logout( HttpServletResponse response ) throws IOException {
        AuthConfig.Auth auth = AuthConfig.Auth.NULL;
        Map<String,Object> map = new HashMap<>();
        map.put("sessionId",auth.getCode());
        map.put("timeStamp",System.currentTimeMillis()+"");
        String token = JwtUtil.sign(map);
        response.addCookie(new Cookie("sessionId",token));
        return "已登出";
    }

}
