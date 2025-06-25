package com.hss.modules.alarm.hander;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hss.modules.alarm.constant.AlarmConstant;
import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.model.AlarmProcessData;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataAttrValueUpdateEvent;
import com.hss.modules.scada.model.DeviceAttrData;
import com.hss.modules.scada.service.DeviceDataService;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * 状态变量更新
 * @author hd
 */
@Service
@Order(AlarmProcess.PUBLISH_STATUS_UPDATE)
public class AlarmProcessPublishStatusUpdate implements AlarmProcess {

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private DeviceDataService deviceDataService;
    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;
    @Override
    public boolean process(AlarmProcessData data) {
        Integer alarmStatus = data.getAlarmStatus();
        // 为新报警 或者报警解除的时候 发布关联变量变更消息
        AlarmStrategy strategy = data.getStrategy();
        if (AlarmConstant.ALARM_STATUS_NEW.equals(alarmStatus) || AlarmConstant.ALARM_STATUS_FALSE.equals(alarmStatus)){
            String statusVarId = strategy.getStatusVarId();
            if (StringUtils.isEmpty(statusVarId)){
                return true;
            }
            // todo 报警值应该设置中获取
            String nowStatus = data.getAlarm() ? "1" : "0";
            DeviceAttrData data1 = deviceDataService.getAttrValueByAttrId(statusVarId);
            if (data1 == null || ObjectUtil.equal(nowStatus, data1.getValue())){
                return true;
            }
            ConDeviceAttribute statusAttr = conDeviceAttributeService.getById(statusVarId);
            if (statusAttr == null) {
                return true;
            }
            data.setStatusAttr(statusAttr);
            statusAttr.setInitValue(nowStatus);
            publisher.publishEvent(new ScadaDataAttrValueUpdateEvent(statusAttr));
        }
        return true;
    }
}
