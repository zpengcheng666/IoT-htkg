package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("策略容器，包含了存储策略、联动策略、报警策略")
public class EventArrModel implements java.io.Serializable{

    @ApiModelProperty("存储策略")
    private String storageStrategy;

    @ApiModelProperty("联动策略")
    private String linkageStrategy;

    @ApiModelProperty("报警策略")
    private String alarmStrategy;
}
