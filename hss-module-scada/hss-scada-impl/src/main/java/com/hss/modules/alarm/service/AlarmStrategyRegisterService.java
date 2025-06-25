package com.hss.modules.alarm.service;

import com.hss.modules.alarm.entity.AlarmStrategy;

import java.util.Set;

/**
* @description: 将报警关联的变量注册进redis
* @author zpc
* @date 2024/3/20 11:45
* @version 1.0
*/
public interface AlarmStrategyRegisterService {

    /**
     * 注册
     * @param strategy 报警策略
     */
    void register(AlarmStrategy strategy);


    /**
     * 取消注册
     * @param attrIds 属性列表
     * @param strategyId 策略id
     */
    void registerCancel(Set<String> attrIds, String strategyId);

    /**
     * 取消注册
     * @param strategy
     */
    void registerCancel(AlarmStrategy strategy);

    /**
     * 更新注册
     * @param oldStrategy
     * @param newStrategy
     */
    void registerUpdate(AlarmStrategy oldStrategy, AlarmStrategy newStrategy);


    /**
     * 查询注册的策略id
     * @param attrId
     * @return
     */
    Set<String> getStrategyByAttrId(String attrId);




}
