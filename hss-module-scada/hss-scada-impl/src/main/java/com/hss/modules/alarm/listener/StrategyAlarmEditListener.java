package com.hss.modules.alarm.listener;

import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.service.IAlarmStrategyService;
import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import com.hss.modules.devicetype.event.DeviceTypeAlarmStrategyEditEvent;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.service.IConSheBeiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @description: 编辑报警策略
* @author zpc
* @date 2024/3/20 15:06
* @version 1.0
*/
@Component
public class StrategyAlarmEditListener implements ApplicationListener<DeviceTypeAlarmStrategyEditEvent> {

    @Autowired
    private IAlarmStrategyService alarmStrategyService;
    @Autowired
    private IConSheBeiService conSheBeiService;

    /**
     * 当设备类型报警策略编辑事件发生时的处理逻辑。
     *
     * @param event 设备类型报警策略编辑事件，携带了编辑后的报警策略信息。
     */
    @Override
    public void onApplicationEvent(DeviceTypeAlarmStrategyEditEvent event) {
        // 获取事件源，即被编辑的设备类型报警策略。
        DeviceTypeAlarmStrategy source = (DeviceTypeAlarmStrategy) event.getSource();

        // 根据设备类型报警策略ID，查询已有的报警策略。
        List<AlarmStrategy> list = alarmStrategyService.listByTypeStrategyId(source.getId());
        // 如果查询结果为空，则直接返回，无需处理。
        if (list.isEmpty()){
            return;
        }
        // 遍历查询到的报警策略，针对每个报警策略进行更新处理。
        for (AlarmStrategy alarmStrategy : list) {
            // 根据报警策略关联的设备ID，查询该设备的所有属性。
            List<ConDeviceAttribute> attributeList = conSheBeiService.listDeviceAttrByDeviceId(alarmStrategy.getDeviceId());

            // 将设备属性的属性ID映射到属性类型的ID上，便于后续更新报警策略时使用。
            Map<String, String> attrTypeAttrIdMap = attributeList
                    .stream()
                    .collect(Collectors.toMap(ConDeviceAttribute::getAttrId, ConDeviceAttribute::getId));
            // 根据新的设备类型报警策略和属性ID映射，转换并生成新的报警策略对象。
            AlarmStrategy update = conSheBeiService.alarmStrategyType2strategy(source, attrTypeAttrIdMap, alarmStrategy.getDeviceId());
            update.setId(alarmStrategy.getId());
            // 根据新的报警策略对象更新数据库中的报警策略信息。
            alarmStrategyService.updateById(update);
        }
    }
}
