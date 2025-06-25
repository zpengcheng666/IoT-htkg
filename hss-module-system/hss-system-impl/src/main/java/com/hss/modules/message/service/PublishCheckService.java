package com.hss.modules.message.service;


import com.hss.modules.message.model.MessageStateBO;

/**
 * @ClassDescription: 进入和离开校验
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/2 14:50
 */
public interface PublishCheckService {


    /**
     * 根据id查询
     * @param id id
     * @return bo
     */
    MessageStateBO getBoById(String id);










}
