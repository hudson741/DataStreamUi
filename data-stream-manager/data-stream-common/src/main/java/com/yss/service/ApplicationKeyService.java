package com.yss.service;

import com.yss.entity.ApplicationKeyEntity;

import java.util.List;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/4
 */
public interface ApplicationKeyService {

     List<ApplicationKeyEntity> getAllApplications();

     void addNewApplicationKey(ApplicationKeyEntity entity);
}
