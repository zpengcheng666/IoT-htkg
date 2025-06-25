package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ConSheBeiOptions implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "英文名称")
    private String enName;

    @ApiModelProperty(value = "门位置")
    private String doorLocation;
}
