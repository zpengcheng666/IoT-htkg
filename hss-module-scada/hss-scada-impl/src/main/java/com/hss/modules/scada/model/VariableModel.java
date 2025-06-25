package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("网关设备变量")
public class VariableModel {

    @ApiModelProperty("变量ID，这个值需要从网管中获取，用来关联平台中的设备属性")
    private String id;

    @ApiModelProperty("变量名称")
    private String name;

    @ApiModelProperty("数据类型")
    private String dataType;

    @ApiModelProperty("设备Id")
    private String deviceId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("备注")
    private String notes;
}
