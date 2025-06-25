package com.hss.modules.facility.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatQualityConditionModel {
    @ApiModelProperty("id 质量状况id")
    private String id;

    @ApiModelProperty("name 质量状况名称")
    private String name;

    @ApiModelProperty("count 质量状况数量统计")
    private String count;
}
