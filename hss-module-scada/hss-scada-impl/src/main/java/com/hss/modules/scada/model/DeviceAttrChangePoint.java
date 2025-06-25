package com.hss.modules.scada.model;

import lombok.Data;

/**
 * 属性绑定变更
 * @author hd
 */
@Data
public class DeviceAttrChangePoint {

    /**
     * 属性id
     */
    private String attrId;

    /**
     * 老的点位id
     */
    private String oldPointId;


    /**
     * 新的点位id
     */
    private String newPointId;

    /**
     * 老的表达式
     */
    private String oldExpression;


    /**
     * 新的表达式
     */
    private String newExpression;
}
