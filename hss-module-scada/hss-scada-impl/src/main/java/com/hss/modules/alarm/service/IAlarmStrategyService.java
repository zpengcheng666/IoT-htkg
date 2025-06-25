package com.hss.modules.alarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.model.StrategyEnable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
* @description: 报警策略，使能报警，校验并执行策略，根据属性id查询输入的变量集合，根据策略id查询，同步设备，根据属性列表查询输出变量
* @author zpc
* @date 2024/3/20 11:46
* @version 1.0
*/
public interface IAlarmStrategyService extends IService<AlarmStrategy> {

    /**
     * 使能
     * @param strategyEnable
     */
    void enable(StrategyEnable strategyEnable);

    /**
     * 添加
     * @param alarmStrategy
     */
    void add(AlarmStrategy alarmStrategy);

    /**
     * 删除策略
     * @param deviceId
     */
    void deleteByDeviceId(String deviceId);

    /**
     * 校验并执行策略
     * @param attrId 变化的属性id列表
     * @return 变化的变量属性id
     */
    void checkAndRunStrategy(String attrId);

    /**
     * 根据属性id查询输入的变量集合
     * @param attrId
     * @return
     */
    Set<String> getOutputByAttrId(String attrId);

    /**
     * 根据策略id查询
     * @param typeId
     * @return
     */
    List<AlarmStrategy> listByTypeStrategyId(String typeId);

    /**
     * 同步设备
     * @param conSheBei
     * @return
     */
    void syncByDevice(ConSheBei conSheBei);

    /**
     * 根据属性列表查询输出变量
     * @param attrIds
     * @return
     */
    Set<String> listAttrIdsByAttrIds(Collection<String> attrIds);
}
