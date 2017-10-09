package com.yss.auth;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/9/27
 */

@RestController
public class MenuController {

    /**
     * 面板
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/dashboard",
            method = RequestMethod.GET
    )
    public ModelAndView dashboard(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/dashboard");
    }

    /**
     * 面板2
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/dashboard2",
            method = RequestMethod.GET
    )
    public ModelAndView dashboard2(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/dashboard2");
    }

    /**
     * 界面布局
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/layout",
            method = RequestMethod.GET
    )
    public ModelAndView layout(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/layout");
    }

    /**
     * 界面布局2
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/layout2",
            method = RequestMethod.GET
    )
    public ModelAndView layout2(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/widgets");
    }

    /**
     * 图表
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/chart1",
            method = RequestMethod.GET
    )
    public ModelAndView chart1(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/charts/chartjs");
    }

    /**
     * 图表2
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/chart2",
            method = RequestMethod.GET
    )
    public ModelAndView chart2(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/charts/morris");
    }

    /**
     * 图表3
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/chart3",
            method = RequestMethod.GET
    )
    public ModelAndView chart3(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/charts/flot");
    }

    /**
     * 图表4
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/chart4",
            method = RequestMethod.GET
    )
    public ModelAndView chart4(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/charts/inline");
    }

    /**
     * UI1
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/ui1",
            method = RequestMethod.GET
    )
    public ModelAndView ui1(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/UI/general");
    }

    /**
     * UI2
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/ui2",
            method = RequestMethod.GET
    )
    public ModelAndView ui2(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/UI/icons");
    }

    /**
     * UI3
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/ui3",
            method = RequestMethod.GET
    )
    public ModelAndView ui3(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/UI/buttons");
    }

    /**
     * UI4
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/ui4",
            method = RequestMethod.GET
    )
    public ModelAndView ui4(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/UI/sliders");
    }

    /**
     * UI5
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/ui5",
            method = RequestMethod.GET
    )
    public ModelAndView ui5(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/UI/timeline");
    }

    /**
     * UI6
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/ui6",
            method = RequestMethod.GET
    )
    public ModelAndView ui6(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/UI/modals");
    }


    /**
     * 元素1
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/forms1",
            method = RequestMethod.GET
    )
    public ModelAndView forms1(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/forms/general");
    }

    /**
     * 元素2
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/forms2",
            method = RequestMethod.GET
    )
    public ModelAndView forms2(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/forms/advanced");
    }

    /**
     * 元素3
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/forms3",
            method = RequestMethod.GET
    )
    public ModelAndView forms3(ModelMap model, HttpServletRequest httpServletRequest) {
        return new ModelAndView("/include/forms/editors");
    }



    /**
     * 简单表格
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/menutable1",
            method = RequestMethod.GET
    )
    public ModelAndView menutable(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("/include/tables/simple");
    }

    /**
     * 数据表格
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/menutable2",
            method = RequestMethod.GET
    )
    public ModelAndView menutable2(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("/include/tables/data");
    }

    /**
     * diy表格
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/menutable3",
            method = RequestMethod.GET
    )
    public ModelAndView menutable3(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("/include/tables/diytable");
    }

    /**
     * 日历
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/calendar",
            method = RequestMethod.GET
    )
    public ModelAndView calendar(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("/include/calendar");
    }

    /**
     * 日历
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/mail",
            method = RequestMethod.GET
    )
    public ModelAndView mail(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("/include/mailbox/mailbox");
    }

    /**
     * lizi
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/lizi1",
            method = RequestMethod.GET
    )
    public ModelAndView lizi1(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("/include/examples/invoice");
    }

    /**
     * lizi2
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/lizi2",
            method = RequestMethod.GET
    )
    public ModelAndView lizi2(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("/include/examples/profile");
    }

    /**
     * 远程部署
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/hrc",
            method = RequestMethod.GET
    )
    public ModelAndView hrc(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("/expand/hrc");
    }



    /**
     * topology pub
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/topoPubV",
            method = RequestMethod.GET
    )
    public ModelAndView topoPubV(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("/storm/topoPub");
    }

    /**
     * storm 集群初始化
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/storminit",
            method = RequestMethod.GET
    )
    public ModelAndView storminit(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("/storm/storminit");
    }

    /**
     * storm 单容器
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/stormsingle",
            method = RequestMethod.GET
    )
    public ModelAndView stormsingle(ModelMap model, HttpServletRequest httpServletRequest) {
        //index.html全局首页
        return new ModelAndView("/storm/stormsingle");
    }

}
