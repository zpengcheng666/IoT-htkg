package com.hss.modules.message.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PrePlanStageModel implements java.io.Serializable{
    @ApiModelProperty(value = "阶段名称")
    private java.lang.String name;

    @ApiModelProperty(value = "createdTime")
    private java.util.Date createdTime;

    @ApiModelProperty(value = "工作项列表")
    private List<PrePlanWorkModel> workitemList;

    @ApiModelProperty(value = "完成状态")
    private Integer status;
}
