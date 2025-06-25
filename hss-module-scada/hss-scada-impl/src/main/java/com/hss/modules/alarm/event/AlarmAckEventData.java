package com.hss.modules.alarm.event;

import lombok.Data;

import java.util.Date;

/**
 * @author hd
 * 确认报警事件数据
 */
@Data
public class AlarmAckEventData {

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 属性id
     */
    private String attrId;

    /**
     * 报警类型
     */
    private String ackType;

    /**
     * 报警时间
     */
    private Date alarmTime;


    /**
     * 报警策略id
     */
    private String alarmStrategyId;

}
