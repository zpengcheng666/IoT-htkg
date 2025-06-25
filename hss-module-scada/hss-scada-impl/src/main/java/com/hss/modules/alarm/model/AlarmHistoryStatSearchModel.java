package com.hss.modules.alarm.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
* @description: 历史数据统计信息查询Model
* @author zpc
* @date 2024/3/20 15:07
* @version 1.0
*/
@Data
@ApiModel(value="AlarmHistoryStatSearchModel", description="历史数据统计信息查询Model")
public class AlarmHistoryStatSearchModel {

    @ApiModelProperty(value = "设备类型")
    private List<String> deviceType;

    @ApiModelProperty(value = "设备Id")
    private List<String> deviceId;

    @ApiModelProperty(value = "设备属性")
    private List<String> attrId;

    @ApiModelProperty(value = "显示结果, total：次数； duration：时长")
    private String statisticsResult;

    @ApiModelProperty(value = "统计周期，day：天；month：月；year：年；quarter：季度")
    private String statisticsMethod;

    @ApiModelProperty(value = "统计方式， alarmLevel：报警登记； alarmType：报警类型")
    private String statisticsWay;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date startTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endTime;

    @ApiModelProperty(value = "所属子系统")
    private java.lang.String subsystem;
}
