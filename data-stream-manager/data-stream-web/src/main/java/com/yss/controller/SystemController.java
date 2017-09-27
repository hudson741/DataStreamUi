package com.yss.controller;

import com.google.gson.Gson;
import com.yss.dao.SystemDao;
import com.yss.entity.SystemEntity;
import com.yss.exception.RRException;
import com.yss.utils.QueryMap;
import com.yss.ws.SSHService;
import com.yss.utils.R;
import com.yss.utils.SortedSet;
import com.yss.validator.ValidatorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/1
 */
@Controller
public class SystemController extends AbstractController {

    @Autowired
    private SystemDao systemDao;

    @Autowired
    private SSHService sshService;


    @RequestMapping("/view/system")
    public ModelAndView viewallSystems(){
        ModelAndView mv = new ModelAndView("view_systems.jsp");
        SortedSet sortedSet = new SortedSet();
        List<SystemEntity> aa = systemDao.queryList(new HashMap<String, Object>());
        sortedSet.setItemList(aa);
        mv.addObject(sortedSet);
        return mv;
    }

    @RequestMapping("/admin/saveSystem")
    @ResponseBody
    public R saveSystem(SystemEntity systemEntity, String password){

        ValidatorUtils.validateEntity(systemEntity);
        if(systemEntity.getId()!=null){
            SystemEntity hostSystem = sshService.authAndAddPubKey(systemEntity, null, password);
            hostSystem.setAddUserId(getUserId().intValue());
            systemDao.update(hostSystem);
        }else {
            if(StringUtils.isEmpty(password)){
                throw new RRException("密码不能为空");
            }
            SystemEntity hostSystem = sshService.authAndAddPubKey(systemEntity, null, password);
            hostSystem.setAddUserId(getUserId().intValue());
            systemDao.save(hostSystem);
        }
        return new R().ok();
    }

    @RequestMapping("/admin/delete/{systemId}")
    public ModelAndView deleteSystem(@PathVariable Long systemId) {
        systemDao.delete(systemId);
        ModelAndView mv = new ModelAndView("view_systems.jsp");
        SortedSet sortedSet = new SortedSet();
        List<SystemEntity> aa = systemDao.queryList(new HashMap<String, Object>());
        sortedSet.setItemList(aa);
        mv.addObject(sortedSet);
        return mv;
    }


}
