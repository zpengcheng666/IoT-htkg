package com.hss.modules.alarm.hander;

import com.hss.modules.alarm.constant.AlarmConstant;
import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.model.AlarmProcessData;
import com.hss.modules.message.dto.AlarmWebSocketMessage;
import com.hss.modules.scada.ws.AlarmWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * 报警消息推送
 *
 * @author hd
 */
@Service
@Order(AlarmProcess.PUBLISH)
@Slf4j
public class AlarmProcessPublish implements AlarmProcess {

    /**
    * 该函数用于根据传入的AlarmProcessData数据判断是否需要推送报警信息，并进行推送。
    * 首先从AlarmProcessData中获取报警策略AlarmStrategy，再从策略中获取报警推送条件alarPushCondition。
    * 根据不同的推送条件和报警状态，判断是否需要推送报警信息，将isPublish设置为true表示需要推送。
    * 当isPublish为true时，构建报警信息AlarmWebSocketMessage，并根据报警状态设置相应的消息内容。
    * 最后通过AlarmWebSocket.sentMessage()方法发送构建的报警信息。
    * 函数始终返回true，表示处理成功。
     *
    * @param data 包含报警处理所需数据的AlarmProcessData对象。
    * @return 始终返回true，表示报警处理成功。
    * @author zpc
    * @date 2024/3/26 10:08
    * @version 1.0
    */
    @Override
    public boolean process(AlarmProcessData data) {
        AlarmStrategy strategy = data.getStrategy();
        String alarPushCondition = strategy.getAlarmPushCondition();
        boolean isPublish = false;
        // 全部推送， 不是老的报警就推送
        if (AlarmConstant.ALARM_SAVE_OR_PUBLISH_CONDITION_ALL.equals(alarPushCondition)
                && !AlarmConstant.ALARM_STATUS_OLD.equals(data.getAlarmStatus())) {
            isPublish = true;
            // 进入报警的时候推送
        } else if (AlarmConstant.ALARM_SAVE_OR_PUBLISH_CONDITION_TRUE.equals(alarPushCondition)
                && AlarmConstant.ALARM_STATUS_NEW.equals(data.getAlarmStatus())) {
            isPublish = true;
            // 出报警的时候推送
        } else if (AlarmConstant.ALARM_SAVE_OR_PUBLISH_CONDITION_FALSE.equals(alarPushCondition)
                && AlarmConstant.ALARM_STATUS_FALSE.equals(data.getAlarmStatus())) {
            isPublish = true;
        }
        if (isPublish) {
            AlarmWebSocketMessage msg = AlarmWebSocketMessage.buildAlarmMessage(
                    data.getAlarmData().getDeviceId(),
                    data.getAlarmData().getDeviceName(),
                    data.getAlarmData().getRecordTime(),
                    //2023-11-04修改
                    data.getAlarmData().getOtherName(),
                    // On 2024-06-23 By chushubin
                    data.getAlarmData().getSubSystem(),
                    data.getAlarmData().getSceneId(),
                    data.getAlarmData().getAlarmLevel());
            if (AlarmConstant.ALARM_STATUS_NEW.equals(data.getAlarmStatus())) {
                msg.setMessage("发生警告，请及时处理");
            }
            AlarmWebSocket.sentMessage(msg);
        }
        return true;
    }
}
