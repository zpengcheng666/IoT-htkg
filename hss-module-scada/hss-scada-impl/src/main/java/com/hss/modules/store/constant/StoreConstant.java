package com.hss.modules.store.constant;

/**
 * 常量类
 * @author hd
 */
public interface StoreConstant {

    /**
     * redis key 实时数据
     */
    String REDIS_KEY_CURRENT_VALUE = "value";

    String IS_ENABLE = "1";

    /** 日报 */
    String REPORT_TYPE_DAY = "1";

    /** 周报 */
    String REPORT_TYPE_WEEK = "2";

    /** 月报 */
    String REPORT_TYPE_MONTH = "3";
}
