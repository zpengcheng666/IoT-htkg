package com.hss.modules.door.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author hd
 */
@Data
@ApiModel("远程开门记录")
public class RemoteOpenDoorVO {
    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "门ID")
    private java.lang.String doorId;

    @ApiModelProperty(value = "门编号")
    private java.lang.String doorCode;


    @Excel(name = "门名称", width = 20)
    @ApiModelProperty(value = "门名称")
    private java.lang.String doorName;


    @Excel(name = "门类型", width = 30)
    @ApiModelProperty(value = "门类型")
    private java.lang.String doorType;

    @Excel(name = "操作人员", width = 30)
    @ApiModelProperty(value = "操作人员")
    private java.lang.String perName;

    @Excel(name = "操作时间",width = 30,exportFormat="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date swipeTime;

    @Excel(name = "开门类型",width = 30)
    @ApiModelProperty(value = "开门类型")
    private java.lang.String openType;
}
