package com.hss.modules.scada.model;

import lombok.Data;

import java.util.List;

/**
 * 环境表格配置信息
 * @author hd
 */
@Data
public class EnvDeviceConfigJson {

    /**
     * 标题
     */
    private String title;

    /**
     * 分组
     */
    private Integer group = 1;


    /**
     * 设备类型
     */
    private List<String> types;


    /**
     * 属性信息
     */
    private List<EnvDeviceConfigJsonAttr> attrList;



}
