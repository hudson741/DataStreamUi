package com.yss.controller;

import com.jcraft.jsch.ChannelShell;
import com.yss.entity.SystemEntity;
import com.yss.model.SchSession;
import com.yss.model.UserSchSessions;
import com.yss.utils.R;
import com.yss.utils.SortedSet;
import com.yss.ws.SSHService;
import com.yss.service.TerminalService;
import com.yss.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/8/28
 */
@Controller
public class SecureShellController extends AbstractController {

    @Autowired
    public TerminalService terminalService;

    @Autowired
    private SSHService sshService;

    private static final String SESSION_ID = "sessionId";
    private static final AtomicLong sessionId = new AtomicLong();
    public static Map<Long, UserSchSessions> userSchSessionMap = new ConcurrentHashMap<>();


    @RequestMapping("/admin/selectSystemsForCompositeTerms")
    public ModelAndView selectSystemsForCompositeTerms(@RequestParam List<Long> systemSelectId) {
        ModelAndView mv = new ModelAndView("secure_shell.jsp");
        Long userId = ShiroUtils.getUserId();
        if (systemSelectId != null && !systemSelectId.isEmpty()) {
            terminalService.setInitialSystemStatus(systemSelectId, userId.intValue());
            SystemEntity pendingSystemStatus = terminalService.getNextPendingSystem(userId.intValue());
            if (pendingSystemStatus != null) {
                mv.addObject("pendingSystemStatus", pendingSystemStatus);
                getSession().setAttribute(SESSION_ID, sessionId.getAndIncrement());
            }
        }
        return mv;

    }

    @RequestMapping("/admin/createTerms")
    public ModelAndView createTerms(@RequestParam Map<String, String> params) {
        ModelAndView mv = new ModelAndView("secure_shell.jsp");
        Long userId = getUserId();
        Long sessionId = Long.parseLong(getSession().getAttribute(SESSION_ID).toString());
        String pendingSystemId = params.get("pendingSystemStatus.id");
        if (pendingSystemId != null) {
            SystemEntity pendingSystemStatus = new SystemEntity();
            pendingSystemStatus.setId(Integer.parseInt(pendingSystemId));

            SystemEntity currentSystemStatus = terminalService.getSystemStatus(userId.intValue(), Integer.parseInt(pendingSystemId));
            if (currentSystemStatus != null && (SystemEntity.INITIAL_STATUS.equals(currentSystemStatus.getStatusCd())
                    || SystemEntity.AUTH_FAIL_STATUS.equals(currentSystemStatus.getStatusCd())
                    || SystemEntity.PUBLIC_KEY_FAIL_STATUS.equals(currentSystemStatus.getStatusCd()))
                    ) {

                currentSystemStatus = sshService.openSSHTermOnSystem(null, null, userId, sessionId, currentSystemStatus, userSchSessionMap);

            }
            if (currentSystemStatus != null && (SystemEntity.AUTH_FAIL_STATUS.equals(currentSystemStatus.getStatusCd())
                    || SystemEntity.PUBLIC_KEY_FAIL_STATUS.equals(currentSystemStatus.getStatusCd()))) {
                pendingSystemStatus = currentSystemStatus;
            } else {
                pendingSystemStatus = terminalService.getNextPendingSystem(userId.intValue());
                while (pendingSystemStatus != null && currentSystemStatus != null && SystemEntity.SUCCESS_STATUS.equals(currentSystemStatus.getStatusCd())) {
                    currentSystemStatus = sshService.openSSHTermOnSystem(null, null, userId, sessionId, pendingSystemStatus, userSchSessionMap);
                    pendingSystemStatus = terminalService.getNextPendingSystem(userId.intValue());
                }
            }
//            mv.addObject("pendingSystemStatus", pendingSystemStatus);
            mv.addObject("currentSystemStatus", currentSystemStatus);
        }

        if (terminalService.getNextPendingSystem(userId.intValue()) == null) {
            List<SystemEntity> systemEntityList = setSystemList(userId, sessionId);

            List<SystemEntity> allocatedSystemList = terminalService.getUserSystemStatus(userId.intValue());
            mv.addObject("allocatedSystemList", allocatedSystemList);
            mv.addObject("systemList", systemEntityList);
        }

        return mv;

    }


    @RequestMapping("/admin/setPtyType")
    @ResponseBody
    public R setPtyType(Integer id, Integer ptyWidth, Integer ptyHeight) {

//        Integer id = Integer.parseInt(parmas.get("id"));
//        Integer ptyWidth = Integer.parseInt(parmas.get("userSettings.ptyWidth"));
//        Integer ptyHeight = Integer.parseInt("userSettings.ptyHeight");

        Long sessionId = Long.parseLong(getSession().getAttribute(SESSION_ID).toString());
        if (userSchSessionMap != null) {
            UserSchSessions userSchSessions = userSchSessionMap.get(sessionId);
            if (userSchSessions != null && userSchSessions.getSchSessionMap() != null) {
                SchSession schSession = userSchSessions.getSchSessionMap().get(id);
                ChannelShell channel = (ChannelShell) schSession.getChannel();
                channel.setPtySize((int) Math.floor(ptyWidth / 7.2981), (int) Math.floor(ptyHeight / 14.4166), ptyWidth, ptyHeight);
                schSession.setChannel(channel);
            }
        }
        return new R().ok();
    }


    private List<SystemEntity> setSystemList(Long userId, Long sessionId) {
        //check user map
        if (userSchSessionMap != null && !userSchSessionMap.isEmpty() && userSchSessionMap.get(sessionId) != null) {

            //get user sessions
            Map<Integer, SchSession> schSessionMap = userSchSessionMap.get(sessionId).getSchSessionMap();
            List<SystemEntity> systemList = new ArrayList<>();


            for (SchSession schSession : schSessionMap.values()) {
                //add to host system list
                systemList.add(schSession.getHostSystem());
                //run script it exists
            }
            Collections.reverse(systemList);
            return systemList;
        }
        return null;

    }

}
