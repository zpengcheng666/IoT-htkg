package com.hss.modules.scada.model;

import lombok.Data;

/**
 * 环境表格配置属性
 * @author hd
 */
@Data
public class EnvDeviceConfigJsonAttr {

    /**
     * 属性名称
     */
    private String name;

    /**
     * 属性英文名称
     */
    private String enName;

    /**
     * 属性报警英文名称
     */
    private String alarmEnName;


    /**
     * 属性高高报警英文名称
     */
    private String hhEnName;

    /**
     * 属性弟弟报警英文名称
     */
    private String llEnName;


}
