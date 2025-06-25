package com.hss.modules.alarm.listener;

import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.service.IAlarmStrategyService;
import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import com.hss.modules.devicetype.event.DeviceTypeAlarmStrategyDeleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @description: 删除报警策略
* @author zpc
* @date 2024/3/20 15:05
* @version 1.0
*/
@Component
public class StrategyAlarmDeleteListener implements ApplicationListener<DeviceTypeAlarmStrategyDeleteEvent> {

    @Autowired
    private IAlarmStrategyService alarmStrategyService;

    @Override
    public void onApplicationEvent(DeviceTypeAlarmStrategyDeleteEvent event) {
        DeviceTypeAlarmStrategy source = (DeviceTypeAlarmStrategy) event.getSource();
        List<AlarmStrategy> list = alarmStrategyService.listByTypeStrategyId(source.getId());
        if (list.isEmpty()){
            return;
        }
        for (AlarmStrategy alarmStrategy : list) {
            alarmStrategyService.removeById(alarmStrategy.getId());
        }
    }
}
