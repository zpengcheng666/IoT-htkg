package com.hss.modules.store.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel(value="DataHistoryStatSearchModel", description="历史数据统计信息查询Model")
public class DataHistoryStatSearchModel {

    @ApiModelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty("设备Id")
    private String deviceId;

    @ApiModelProperty("设备属性")
    private String attrName;

    @ApiModelProperty("统计开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("统计结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "所属子系统")
    private java.lang.String subsystem;
}
