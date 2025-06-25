package com.hss.modules.linkage.service;
import com.hss.modules.linkage.entity.LinkageStrategy;

import java.util.Set;

/**
 * 将关联的变量注册进redis
 * @author hd
 */
public interface LinkageStrategyRegisterService {

    /**
     * 注册
     * @param strategy 联动策略
     */
    void register(LinkageStrategy strategy);


    /**
     * 取消注册
     * @param attrIds 属性列表
     * @param id 联动id
     */
    void registerCancel(Set<String> attrIds, String id);

    /**
     * 取消注册
     * @param strategy
     */
    void registerCancel(LinkageStrategy strategy);

    /**
     * 更新注册
     * @param oldStrategy
     * @param newStrategy
     */
    void registerUpdate(LinkageStrategy oldStrategy, LinkageStrategy newStrategy);


    /**
     * 查询注册的策略id
     * @param attrId
     * @return
     */
    Set<String> getStrategyByAttrId(String attrId);



}
