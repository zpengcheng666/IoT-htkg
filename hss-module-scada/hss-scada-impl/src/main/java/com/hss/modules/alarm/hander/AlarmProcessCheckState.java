package com.hss.modules.alarm.hander;

import com.hss.modules.alarm.constant.AlarmConstant;
import com.hss.modules.alarm.model.AlarmProcessData;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;


/**
 * 校验报警状态是否改变
 * @author hd
 */
@Service
@Order(AlarmProcess.CHECK_STATE)
public class AlarmProcessCheckState implements AlarmProcess {

    @Override
    public boolean process(AlarmProcessData data) {
        // 没有缓存 && 并报警 = 新的报警
        if (!data.getCached() && data.getAlarm()){
            data.setAlarmStatus(AlarmConstant.ALARM_STATUS_NEW);
            return true;
        }
        // 有缓存 && 报警 = 老的报警
        if (data.getCached() && data.getAlarm()){
            data.setAlarmStatus(AlarmConstant.ALARM_STATUS_OLD);
            return true;
        }
        // 有缓存 && 解除报警 = 解除报警
        if (data.getCached() && !data.getAlarm()){
            data.setAlarmStatus(AlarmConstant.ALARM_STATUS_FALSE);
            return true;
        }
        // 没有缓存 && 解除报警 = 不报警
        if (!data.getCached() && !data.getAlarm()){
            data.setAlarmStatus(AlarmConstant.ALARM_STATUS_OLD);
            return true;
        }
        return true;
    }
}
