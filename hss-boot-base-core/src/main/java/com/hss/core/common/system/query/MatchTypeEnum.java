package com.hss.core.common.system.query;

import com.hss.core.common.util.OConvertUtils;

/**
 * 查询链接规则
 *
 * @Author Sunjianlei
 */
public enum MatchTypeEnum {

    /**查询链接规则 AND*/
    AND("AND"),
    /**查询链接规则 OR*/
    OR("OR");

    private String value;

    MatchTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MatchTypeEnum getByValue(Object value) {
        if (OConvertUtils.isEmpty(value)) {
            return null;
        }
        return getByValue(value.toString());
    }

    public static MatchTypeEnum getByValue(String value) {
        if (OConvertUtils.isEmpty(value)) {
            return null;
        }
        for (MatchTypeEnum val : values()) {
            if (val.getValue().toLowerCase().equals(value.toLowerCase())) {
                return val;
            }
        }
        return null;
    }
}
