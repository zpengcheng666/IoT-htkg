package com.hss.modules.spare.constant;

/**
 * @ClassDescription: 入库类型
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/25 16:39
 */
public enum ShipmentType {
    USE(21,"使用"),
    BORROW(22,"借用");

    private final Integer type;
    private final String label;

    ShipmentType(Integer type, String label) {
        this.type = type;
        this.label = label;
    }

    public Integer getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }
}
