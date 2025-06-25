package com.hss.modules.devicetype.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import com.hss.modules.scada.model.DeviceTypeStrategyList;

import java.util.List;

/**
 * @Description: 设备类型报警策略
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface IDeviceTypeAlarmStrategyService extends IService<DeviceTypeAlarmStrategy> {

    /**
     * 根据类型id查询策略
     * @param typeId
     * @return
     */
    List<DeviceTypeStrategyList> listCommonByTypeId(String typeId);

    /**
     * 根据typeId删除
     * @param typeId
     */
    void deleteByTypeId(String typeId);

    /**
     * 根据typeId查询
     * @param typeId
     * @return
     */
    List<DeviceTypeAlarmStrategy> listByTypeId(String typeId);


    /**
     * 查询设备类型是否有报警策略
     * @param id
     * @return
     */
    boolean isStrategyByTypeId(String id);

    /**
     * 新增报警策略
     * @param deviceTypeAlarmStrategy
     */
    void add(DeviceTypeAlarmStrategy deviceTypeAlarmStrategy);

    /**
     * 编辑报警策略
     * @param deviceTypeAlarmStrategy
     */
    void edit(DeviceTypeAlarmStrategy deviceTypeAlarmStrategy);

    /**
     * 删除报警策略
     * @param id
     */
    void delete(String id);
}
