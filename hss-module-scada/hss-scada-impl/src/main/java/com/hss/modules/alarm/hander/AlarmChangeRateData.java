package com.hss.modules.alarm.hander;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 变化率需要存储的数据
 * @author hd
 */
@Data
public class AlarmChangeRateData {

    /**
     * 最大值
     */
    private BigDecimal minValue;

    /**
     * 存储最大值的时间
     */
    private Long minMs;

    /**
     * 最小值
     */
    private BigDecimal maxValue;

    /**
     * 存储最小值的时间
     */
    private Long maxMs;
}
