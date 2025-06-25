package com.hss.modules.alarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.alarm.entity.AlarmStrategy;

import java.util.List;

/**
* @description: 报警策略
* @author zpc
* @date 2024/3/20 15:07
* @version 1.0
*/
public interface AlarmStrategyMapper extends BaseMapper<AlarmStrategy> {

    /**
     * 根据设备id删除
     * @param deviceId
     */
    void deleteByDeviceId(String deviceId);

    /**
     * 根据设备id查询
     * @param deviceId
     * @return
     */
    List<AlarmStrategy> listByDeviceId(String deviceId);

    /**
     * 根据策略id查询
     * @param typeId
     * @return
     */
    List<AlarmStrategy> listByTypeStrategyId(String typeId);

    /**
     * 根据attrId查询输入的属性id
     * @param attrId
     * @return
     */
    List<String> listOutAttrIdByAttrId(String attrId);
}
