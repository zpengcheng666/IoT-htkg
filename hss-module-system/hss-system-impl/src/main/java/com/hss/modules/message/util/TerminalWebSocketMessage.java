package com.hss.modules.message.util;

import lombok.Data;

/**
* @description: 发布消息实体
* @author zpc
* @date 2024/3/21 9:52
* @version 1.0
*/
@Data
public class TerminalWebSocketMessage {

    /**
     * 心跳
     */
    public static final String TYPE_HEARTBEAT = "0";
    /**
     * 值班
     */
    public static final String TYPE_DUTY = "1";
    public static final String TYPE_DO_WORK = "12";
    /**
     * 通知
     */
    public static final String TYPE_NOTICE = "2";
    /**
     * 卫星临空
     */
    public static final String TYPE_IN_SATELLITE = "31";
    /**
     * 卫星将要临空
     */
    public static final String TYPE_NOT_SATELLITE = "32";
    /**
     * 天气
     */
    public static final String TYPE_WEATHER = "4";

    /**
     * 进出信息
     */
    public static final String TYPE_IN_OUP = "5";

    /**
     * 进出信息
     */
    public static final String TYPE_DOOR = "51";

    /**
     * 报警消息
     */
    public static final String TYPE_IN_ALARM = "6";

    /**
     * 安检门信息
     */
    public static final String TYPE_CHECK_DOOR = "7";

    /**
     * 应急处置信息
     */
    public static final String TYPE_YJCZ = "8";

    /**
     * 终端信息变更
     */
    public static final String TYPE_INFO = "100";

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息列表
     */
    private Object message;
}
