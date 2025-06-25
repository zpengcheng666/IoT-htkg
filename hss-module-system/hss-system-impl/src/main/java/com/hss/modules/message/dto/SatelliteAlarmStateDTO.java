package com.hss.modules.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
* @description: 设置卫星报警状态dto
* @author zpc
* @date 2024/3/21 10:03
* @version 1.0
*/
@Data
@ApiModel(value="设置卫星报警状态dto", description="设置卫星报警状态dto")
public class SatelliteAlarmStateDTO implements java.io.Serializable{

    @ApiModelProperty(value = "卫星Id列表")
    private List<String> ids;

    @ApiModelProperty(value = "是否报警状态0：不报警；1：报警； 默认是0")
    private Integer alarmState = 0;

}
