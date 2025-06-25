package com.hss.modules.alarm.hander;

import com.hss.modules.alarm.constant.AlarmConstant;
import com.hss.modules.alarm.entity.AlarmData;
import com.hss.modules.alarm.entity.AlarmHistory;
import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.model.AlarmProcessData;
import com.hss.modules.alarm.service.IAlarmDataService;
import com.hss.modules.alarm.service.IAlarmHistoryService;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Date;
/**
 * 报警存储
 * @author hd
 */
@Service
@Order(AlarmProcess.SAVE)
public class AlarmProcessSave implements AlarmProcess {

    @Autowired
    private IAlarmHistoryService alarmHistoryService;
    @Autowired
    private IAlarmDataService alarmDataService;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public boolean process(AlarmProcessData data) {
        if (AlarmConstant.ALARM_STATUS_NEW.equals(data.getAlarmStatus())){
            AlarmStrategy strategy = data.getStrategy();
            ConDeviceAttribute deviceAttribute = data.getOriginAttr();
            AlarmData alarmData = data.getAlarmData();

            ConSheBei device = data.getDevice();
            AlarmHistory alarmHistory = new AlarmHistory();

            alarmHistory.setDeviceId(alarmData.getDeviceId());
            alarmHistory.setDeviceType(alarmData.getDeviceType());
            alarmHistory.setDeviceName(alarmData.getDeviceName());
            alarmHistory.setSubsystem(device.getSubsystem());
            alarmHistory.setAttrName(deviceAttribute.getName());
            alarmHistory.setAttrEnName(deviceAttribute.getEnName());
            alarmHistory.setVariableId(deviceAttribute.getId());
            alarmHistory.setVariableName(deviceAttribute.getName());
            alarmHistory.setLocationId(device.getLocationId());
            //增加别名2023-11-04
            alarmHistory.setOtherName(alarmData.getOtherName());
            alarmHistory.setAlarmValue(alarmData.getRecordValue());
            alarmHistory.setRange(alarmData.getRange());
            alarmHistory.setLevelId(strategy.getLevelId());
            alarmHistory.setAlarmDataId(data.getAlarmData().getId());
            alarmHistory.setAlarmStartTime(new Date());
            alarmHistory.setAttrId(deviceAttribute.getId());
            alarmHistory.setDeviceTypeId(device.getDeviceTypeId());
            alarmHistory.setCreatedTime(new Date());
            alarmHistoryService.save(alarmHistory);
            alarmDataService.save(data.getAlarmData());
        } else if ((AlarmConstant.ALARM_STATUS_FALSE.equals(data.getAlarmStatus()))){
            alarmHistoryService.falseAlarm(data.getAlarmData().getId());
            data.getAlarmData().setStatus(AlarmStatus.STOP);
            alarmDataService.updateById(data.getAlarmData());
        }
        return true;
    }
}
