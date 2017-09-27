package com.yss.service;

import com.yss.entity.SystemEntity;

import java.util.List;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/4
 */
public interface TerminalService {


    void setInitialSystemStatus(List<Long> systemSelectIds, int userId);

    SystemEntity getNextPendingSystem(int userId);

    SystemEntity getSystemStatus(int userId, int systemId);

    List<SystemEntity> getUserSystemStatus(int userId);


}
