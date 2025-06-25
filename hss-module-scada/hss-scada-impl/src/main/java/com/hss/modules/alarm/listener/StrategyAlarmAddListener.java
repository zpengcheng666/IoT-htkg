package com.hss.modules.alarm.listener;

import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.service.IAlarmStrategyService;
import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import com.hss.modules.devicetype.event.DeviceTypeAlarmStrategyAddEvent;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.service.IConSheBeiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @description: 新增报警策略
* @author zpc
* @date 2024/3/20 15:04
* @version 1.0
*/
@Component
public class StrategyAlarmAddListener implements ApplicationListener<DeviceTypeAlarmStrategyAddEvent> {

    @Autowired
    private IAlarmStrategyService alarmStrategyService;
    @Autowired
    private IConSheBeiService conSheBeiService;
    /**
     * 当设备类型报警策略添加事件发生时的处理逻辑。
     * @param event 设备类型报警策略添加事件
     */
    @Override
    public void onApplicationEvent(DeviceTypeAlarmStrategyAddEvent event) {
        DeviceTypeAlarmStrategy source = (DeviceTypeAlarmStrategy) event.getSource();
        // 1. 根据设备类型查询相关设备
        List<ConSheBei> deviceList = conSheBeiService.listByDeviceTypeId(source.getTypeId());
        // 如果查询到的设备列表为空，则直接返回，不进行后续操作
        if (deviceList.isEmpty()){
            return;
        }
        // 遍历设备列表，为每台设备根据报警策略生成具体的报警策略实例
        List<AlarmStrategy> list = deviceList.stream().map(conSheBei -> {
            // 根据设备ID查询设备属性
            List<ConDeviceAttribute> attributeList = conSheBeiService.listDeviceAttrByDeviceId(conSheBei.getId());

            // 将设备属性的属性ID映射到属性类型的ID上，便于后续处理
            Map<String, String> attrTypeAttrIdMap = attributeList
                    .stream()
                    .collect(Collectors.toMap(ConDeviceAttribute::getAttrId, ConDeviceAttribute::getId));

            // 根据报警策略模板和设备属性ID映射，生成具体的报警策略实例
            return conSheBeiService.alarmStrategyType2strategy(source, attrTypeAttrIdMap, conSheBei.getId());
        }).collect(Collectors.toList());
        // 批量保存生成的报警策略实例
        alarmStrategyService.saveBatch(list);
    }

}
