package com.hss.modules.message.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PrePlanWorkModel implements java.io.Serializable{
    @ApiModelProperty(value = "工作项名称")
    private java.lang.String name;

    @ApiModelProperty(value = "工作内容")
    private java.lang.String content;

    @ApiModelProperty(value = "序号")
    private java.lang.Integer index1;

    @ApiModelProperty(value = "阶段ID")
    private java.lang.String stageId;

    @ApiModelProperty(value = "createdTime")
    private java.util.Date createdTime;

    @ApiModelProperty(value = "updatedTime")
    private java.util.Date updatedTime;

    @ApiModelProperty(value = "提交时间")
    private Date sumbitTime;

    @ApiModelProperty(value = "提交人")
    private String sumbitPerson;

    @ApiModelProperty(value = "状态")
    private Integer status;
}
