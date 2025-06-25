package com.hss.modules.alarm.model;

import lombok.Data;

/**
 * @author hd
 * 最后一个月报警属灵统计查询
 */
@Data
public class StateCountLastMonthBO {

    /**
     * 属性
     */
    Integer count;

    /**
     * 日期
     */
    String day;
}
