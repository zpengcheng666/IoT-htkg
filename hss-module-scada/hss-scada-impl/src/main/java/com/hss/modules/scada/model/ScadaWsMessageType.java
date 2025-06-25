package com.hss.modules.scada.model;

/**
* @description: WS 组态的消息类型
* @author zpc
* @date 2024/3/19 15:27
* @version 1.0
*/
public class ScadaWsMessageType {

    /**业务数据*/
    public static final String BIZ = "01";

    /**提醒数据*/
    public static final String WARNING = "02";

    /**确认消息*/
    public static final String ACK = "04";

    /**心跳*/
    public static final String HEART = "05";

}
