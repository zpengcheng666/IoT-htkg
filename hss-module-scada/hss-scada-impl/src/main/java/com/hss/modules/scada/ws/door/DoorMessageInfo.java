package com.hss.modules.scada.ws.door;

import lombok.Data;

import java.util.Collection;

/**
 * @author 门禁实时消息
 */
@Data
public class DoorMessageInfo {


    /**
     * id
     */
    private String id;

    /**
     * 名字
     */
    private String name;

    /**
     * 别名
     */
    private String otherName;

    /**
     * 门类型
     */
    private String type;

    /**
     * 位置
     */
    private String locationName;

    /**
     * 编号
     */
    private String code;

    /**
     * 属性信息
     */
    private Collection<DoorMessageAttr> attrs;


    /**
     * 动作信息
     */
    private Collection<DoorMessageService> services;

}
