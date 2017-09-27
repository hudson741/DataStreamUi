package com.yss.service.impl;

import com.yss.dao.StatusDao;
import com.yss.dao.SystemDao;
import com.yss.entity.StatusEntity;
import com.yss.entity.SystemEntity;
import com.yss.service.TerminalService;
import com.yss.utils.QueryMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/4
 */
@Service
public class TerminalServiceImp implements TerminalService {

    @Autowired
    private StatusDao statusDao;
    @Autowired
    private SystemDao systemDao;


    @Override
    public void setInitialSystemStatus(List<Long> systemSelectIds, int userId) {

        deleteAllSystemStatus(userId);
        for(Long systemSelectId : systemSelectIds){
            StatusEntity entity = new StatusEntity();
            entity.setId(systemSelectId.intValue());
            entity.setUserId(userId);
            entity.setStatusCd(SystemEntity.INITIAL_STATUS);
            statusDao.save(entity);
        }

    }

    @Override
    public SystemEntity getNextPendingSystem(int userId) {
        QueryMap queryMap = new QueryMap();
        queryMap.put("INITIAL_STATUS", SystemEntity.INITIAL_STATUS);
        queryMap.put("AUTH_FAIL_STATUS", SystemEntity.AUTH_FAIL_STATUS);
        queryMap.put("KEYAUTHFAIL", SystemEntity.PUBLIC_KEY_FAIL_STATUS);
        queryMap.put("userId", userId);
        List<StatusEntity> status = statusDao.queryList(queryMap);

        if(!status.isEmpty()){
            StatusEntity entity = status.get(0);

            SystemEntity hostSystem  = systemDao.queryObject(entity.getId());
            hostSystem.setStatusCd(entity.getStatusCd());

            return hostSystem;
        }
        return null;
    }

    @Override
    public SystemEntity getSystemStatus(int userId, int systemId) {
        StatusEntity statusEntity = statusDao.queryObject(systemId);
        SystemEntity systemEntity = systemDao.queryObject(systemId);
        systemEntity.setStatusCd(statusEntity.getStatusCd());
        return systemEntity;
    }

    @Override
    public List<SystemEntity> getUserSystemStatus(int userId) {

        return  systemDao.queryList(new QueryMap().put("userId", userId));
    }


    private void deleteAllSystemStatus(int userId){

        statusDao.delete(new QueryMap().put("user_id", userId));

    }



}






















