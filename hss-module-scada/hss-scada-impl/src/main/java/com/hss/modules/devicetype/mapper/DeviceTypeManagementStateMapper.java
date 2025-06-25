package com.hss.modules.devicetype.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.devicetype.entity.DeviceTypeManagementState;

import java.util.List;

/**
 * @Description: 设备类型管理状态管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface DeviceTypeManagementStateMapper extends BaseMapper<DeviceTypeManagementState> {

    /**
     * 根据类型id查询
     * @param typeId
     * @return
     */
    List<DeviceTypeManagementState> listByTypeId(String typeId);

    /**
     * 根据typeId取消默认状态
     * @param typeId
     */
    void setNotDefaultByTypeId(String typeId);

    /**
     * 根据类型id删除
     * @param typeId
     */
    void deleteByTypeId(String typeId);
}
