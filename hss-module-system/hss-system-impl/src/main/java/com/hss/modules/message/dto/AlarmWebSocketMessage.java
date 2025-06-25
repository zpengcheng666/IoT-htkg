package com.hss.modules.message.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
* @description: 消息类型
* @author zpc
* @date 2024/3/21 10:00
* @version 1.0
*/
@Data
public class AlarmWebSocketMessage{

    // 报警
    public static final Integer TYPE_ALARM = 1;

    // 通知
    public static final Integer TYPE_NOTIFY = 2;

    // 提醒
    public static final Integer TYPE_REMIND = 3;

    @ApiModelProperty(value = "消息类型")
    private Integer type;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "记录时间")
    private Date eventTime;

    @ApiModelProperty(value = "消息列表")
    private Object message;

    //2023-11-04修改，新增别名
    @ApiModelProperty(value = "别名")
    private String otherName;

    @ApiModelProperty(value = "子系统")
    private String subSystem;

    @ApiModelProperty(value = "场景")
    private String sceneId;

    @ApiModelProperty(value = "报警级别Id")
    private String levelId;

    public static AlarmWebSocketMessage buildAlarmMessage(String deviceId, String deviceName, Date eventTime,String otherName){
        AlarmWebSocketMessage m = new AlarmWebSocketMessage();
        m.setType(TYPE_ALARM);
        m.setDeviceId(deviceId);
        m.setDeviceName(deviceName);
        m.setEventTime(eventTime);
        //2023-11-04修改
        m.setOtherName(otherName);
        return m;
    }

    /**
     * On 2024-06-23 By chushubin
     * @param deviceId
     * @param deviceName
     * @param eventTime
     * @param otherName
     * @param subSystem
     * @param sceneId
     * @param levelId
     * @return
     */
    public static AlarmWebSocketMessage buildAlarmMessage(String deviceId, String deviceName, Date eventTime,
                                                          String otherName, String subSystem, String sceneId,
                                                          String levelId){
        AlarmWebSocketMessage m = new AlarmWebSocketMessage();
        m.setType(TYPE_ALARM);
        m.setDeviceId(deviceId);
        m.setDeviceName(deviceName);
        m.setEventTime(eventTime);
        m.setOtherName(otherName);
        m.setSubSystem(subSystem);
        m.setSceneId(sceneId);
        m.setLevelId(levelId);
        return m;
    }
}
