package com.hss.modules.scada.model;

import lombok.Data;

/**
 * 属性绑定变更
 * @author hd
 */
@Data
public class DeviceAttrChangeVar {

    /**
     * 属性id
     */
    private String attrId;

    /**
     * 旧的的表达式
     */
    private String oldExpression;


    /**
     * 新的表达式
     */
    private String newExpression;
}
