package com.hss.modules.scada.model;

import lombok.Data;

import java.util.Date;

/**
 * 属性数据信息
 * @author hd
 */
@Data
public class DeviceAttrData {


    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 中文名称
     */
    private String name;

    /**
     * 英文名称
     */
    private String enName;

    /**
     * 属性值
     */
    private String value;


    /**
     * 更新时间
     */
    private Date updateTime;

    private String variableId;

}
