package com.hss.modules.devicetype.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.devicetype.entity.DeviceTypeStoreStrategy;
import com.hss.modules.scada.model.DeviceTypeStrategyList;

import java.util.List;

/**
 * @Description: 设备类型存储策略
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface IDeviceTypeStoreStrategyService extends IService<DeviceTypeStoreStrategy> {

    /**
     * 根据类型id查询
     * @param typeId
     * @return
     */
    List<DeviceTypeStrategyList> listCommonByTypeId(String typeId);

    /**
     * 根据类型删除
     * @param typeId
     */
    void deleteByTypeId(String typeId);

    /**
     * 根据typeid查询
     * @param typeId
     * @return
     */
    List<DeviceTypeStoreStrategy> listByTypeId(String typeId);

    /**
     * 查询设备类型是否有存储策略
     * @param type
     * @return
     */
    boolean isStrategyByTypeId(String type);

    /**
     * 添加
     * @param deviceTypeStoreStrategy
     */
    void add(DeviceTypeStoreStrategy deviceTypeStoreStrategy);

    /**
     * 编辑
     * @param deviceTypeStoreStrategy
     */
    void edit(DeviceTypeStoreStrategy deviceTypeStoreStrategy);

    /**
     * 删除
     * @param id
     */
    void delete(String id);
}
