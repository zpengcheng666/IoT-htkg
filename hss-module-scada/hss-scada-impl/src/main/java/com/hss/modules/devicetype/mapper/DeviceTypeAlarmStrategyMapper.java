package com.hss.modules.devicetype.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import com.hss.modules.scada.model.DeviceTypeStrategyList;

import java.util.List;

/**
 * @Description: 设备类型报警策略
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface DeviceTypeAlarmStrategyMapper extends BaseMapper<DeviceTypeAlarmStrategy> {

    /**
     * 根据类型id查询
     * @param typeId
     * @return
     */
    List<DeviceTypeStrategyList> listCommonByTypeId(String typeId);

    /**
     * 根据类型id删除
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
     * 查询有报警策略的设备类型属性
     * @param typeId
     * @return
     */
    int countStrategyByTypeId(String typeId);

}
