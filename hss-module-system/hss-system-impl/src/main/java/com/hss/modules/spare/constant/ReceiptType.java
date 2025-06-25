package com.hss.modules.spare.constant;

/**
 * @ClassDescription: 入库类型
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/25 16:39
 */
public enum ReceiptType {
    PURCHASE(11,"采购"),
    BORROW(12,"借出归还"),
    USE(13,"使用归还");

    private final Integer type;
    private final String label;

    ReceiptType(Integer type, String label) {
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
