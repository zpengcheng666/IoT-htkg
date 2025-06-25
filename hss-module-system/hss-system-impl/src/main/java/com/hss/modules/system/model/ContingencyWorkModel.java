package com.hss.modules.system.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @description: 工作项阶段
* @author zpc
* @date 2024/3/21 13:31
* @version 1.0
*/
@Data
public class ContingencyWorkModel {
    @ApiModelProperty("阶段id")
    private String stageId;

    @ApiModelProperty("工作项名称")
    private String name;

    @ApiModelProperty("内容")
    private String content;
}
