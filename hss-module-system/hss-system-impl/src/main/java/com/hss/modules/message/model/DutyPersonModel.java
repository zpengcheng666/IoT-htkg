package com.hss.modules.message.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class DutyPersonModel {
    /**名称*/
    @Excel(name = "名称", width = 20)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;

    /**岗位*/
    @ApiModelProperty(value = "岗位")
    @Excel(name = "岗位", width = 20)
    private java.lang.String dutyPost;

    /**时间段*/
    @Excel(name = "时间段", width = 20)
    @ApiModelProperty(value = "时间段")
    private java.lang.String dutySjd;

    /**职位*/
    @Excel(name = "职位", width = 20)
    @ApiModelProperty(value = "职位")
    private java.lang.String dutyPostion;
}
