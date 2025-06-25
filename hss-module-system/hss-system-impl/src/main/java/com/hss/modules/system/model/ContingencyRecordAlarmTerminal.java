package com.hss.modules.system.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @description: 应急处置
* @author zpc
* @date 2024/3/21 13:31
* @version 1.0
*/
@Data
public class ContingencyRecordAlarmTerminal implements java.io.Serializable{

    @ApiModelProperty("终端id")
    private String termianlIds;

    @ApiModelProperty("预案id")
    private String contingencyId;

    @ApiModelProperty("报警id")
    private String alarmId;

    @ApiModelProperty("发起人")
    private String sponsor;

}
