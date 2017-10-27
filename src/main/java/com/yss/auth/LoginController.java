package com.yss.auth;

import com.yss.util.R;
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

import static com.yss.auth.AuthConfig.getUser;


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


    @RequestMapping(
        value = "/logind",
        method = RequestMethod.POST
    )
    public R login( HttpServletResponse response,HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password ) throws IOException {

        AuthConfig.Auth auth = AuthConfig.login(username,password);

        Map<String,Object> map = new HashMap<>();
        map.put("sessionId",auth.getCode());
        map.put("timeStamp",System.currentTimeMillis()+"");
        String token = JwtUtil.sign(map);
        response.addCookie(new Cookie("sessionId",token));

        if(auth == AuthConfig.Auth.NULL){
            return R.error(200, "用户名或密码错误");
        }
        return R.ok();

    }

    @RequestMapping(value = "/index")
    public ModelAndView index(HttpServletResponse response,HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        AuthConfig.Auth auth = getUser(request);
        if(auth == AuthConfig.Auth.SUPER){
            modelAndView.addObject("auth",1);
        }else if(auth == AuthConfig.Auth.STORM){
            modelAndView.addObject("auth",0);
        }
        return modelAndView;
    }


    @RequestMapping(
            value = "/logout",
            method = RequestMethod.GET
    )
    public ModelAndView logout( HttpServletResponse response ) throws IOException {
        AuthConfig.Auth auth = AuthConfig.Auth.NULL;
        Map<String,Object> map = new HashMap<>();
        map.put("sessionId",auth.getCode());
        map.put("timeStamp",System.currentTimeMillis()+"");
        String token = JwtUtil.sign(map);
        response.addCookie(new Cookie("sessionId",token));
        return new ModelAndView("login");
    }

}
