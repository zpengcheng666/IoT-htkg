package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ConSheBeiDoorOptions implements Serializable {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "门名称")
    private String name;

    @ApiModelProperty(value = "英文名称")
    private String enName;

    @ApiModelProperty(value = "门类型")
    private String doorType;

    @ApiModelProperty(value = "位置信息")
    private String doorLocation;
}
