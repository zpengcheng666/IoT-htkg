package com.hss.modules.alarm.model;

import lombok.Data;

/**
* @description: 报警数据统计
* @author zpc
* @date 2024/3/20 15:08
* @version 1.0
*/
@Data
public class AlarmStatisticsBO {

    /**
     * x轴名称
     */
    private String valueName;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 值
     */
    private Long value;
}
