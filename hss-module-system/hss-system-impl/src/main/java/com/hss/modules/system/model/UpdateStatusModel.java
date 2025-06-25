package com.hss.modules.system.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateStatusModel implements Serializable {
    @ApiModelProperty("菜单id")
    private String id;

    @ApiModelProperty("状态")
    private Integer status;
}
