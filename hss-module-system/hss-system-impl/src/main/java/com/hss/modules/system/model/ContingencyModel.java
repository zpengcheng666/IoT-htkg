package com.hss.modules.system.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @description: 预案
* @author zpc
* @date 2024/3/21 13:30
* @version 1.0
*/
@Data
public class ContingencyModel {
    @ApiModelProperty("预案id")
    private String planId;

    @ApiModelProperty("阶段名称")
    private String name;
}
