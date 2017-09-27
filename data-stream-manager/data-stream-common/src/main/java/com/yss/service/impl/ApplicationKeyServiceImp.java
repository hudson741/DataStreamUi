package com.yss.service.impl;

import com.yss.dao.ApplicationKeyDao;
import com.yss.entity.ApplicationKeyEntity;
import com.yss.service.ApplicationKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/4
 */
@Service
public class ApplicationKeyServiceImp implements ApplicationKeyService {

    @Autowired
    private ApplicationKeyDao dao;

    @Override
    public List<ApplicationKeyEntity> getAllApplications() {
        return dao.queryList(new HashMap<String, Object>());
    }

    @Override
    public void addNewApplicationKey(ApplicationKeyEntity entity) {

        dao.save(entity);

    }
}
