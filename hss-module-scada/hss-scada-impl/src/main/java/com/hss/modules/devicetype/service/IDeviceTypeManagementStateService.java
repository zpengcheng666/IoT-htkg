package com.hss.modules.devicetype.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.devicetype.entity.DeviceTypeManagementState;

import java.util.List;

/**
 * @Description: 设备类型管理状态管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface IDeviceTypeManagementStateService extends IService<DeviceTypeManagementState> {


    /**
     * 根据类型id查询
     * @param typeId
     * @return
     */
    List<DeviceTypeManagementState> listByTypeId(String typeId);

    /**
     * 设置默认
     * @param id
     */
    void setDefault(String id);

    /**
     * 根据typeId删除
     * @param typeId
     */
    void deleteByTypeId(String typeId);
}
