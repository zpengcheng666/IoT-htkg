package com.hss.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.model.StrategyEnable;
import com.hss.modules.store.entity.StoreStrategy;

import java.util.List;

/**
 * @Description: 设备运行时数据存储策略
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
public interface IStoreStrategyService extends IService<StoreStrategy> {

    /**
     * 分页查询
     * @param page
     * @param name
     * @param deviceId
     * @return
     */
    IPage<StoreStrategy> getPage(Page<StoreStrategy> page, String name, String deviceId);

    /**
     * 使能
     * @param strategyEnable
     */
    void enable(StrategyEnable strategyEnable);

    /**
     * 添加
     * @param storeStrategy
     */
    void add(StoreStrategy storeStrategy);

    /**
     * 编辑
     * @param storeStrategy
     */
    void edit(StoreStrategy storeStrategy);

    /**
     * 删除
     * @param id
     */
    void delete(String id);




    /**
     * 执行存储策略
     * @param strategy
     */
    void runStory(StoreStrategy strategy);


    /**
     * 根据设备id删除
     * @param deviceId
     */
    void deleteByDeviceId(String deviceId);

    /**
     * 根据策略类型查询
     * @param typeId
     * @return
     */
    List<StoreStrategy> listByTypeStrategyId(String typeId);


    /**
     * 根据任务类型查询使能的任务
     * @param type
     * @return
     */
    List<StoreStrategy> listEnableByType(String type);

    void syncByDevice(ConSheBei conSheBei);
}
