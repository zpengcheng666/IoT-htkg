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
public class CardDoorVO {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "门ID")
    private String doorId;

    @ApiModelProperty(value = "门编号")
    private String doorCode;


    @Excel(name = "门名称", width = 20)
    @ApiModelProperty(value = "门名称")
    private String doorName;


    @Excel(name = "门类型", width = 30)
    @ApiModelProperty(value = "门类型")
    private String doorType;

    @Excel(name = "人员名称", width = 30)
    @ApiModelProperty(value = "人员名称")
    private String perName;

    @Excel(name = "单位", width = 30)
    @ApiModelProperty(value = "单位")
    private String perDept;

    @Excel(name = "职务", width = 30)
    @ApiModelProperty(value = "职务")
    private String perTitle;


    @Excel(name = "卡号", width = 30)
    @ApiModelProperty(value = "卡号")
    private String cardCode;

    @Excel(name = "刷卡时间",width = 30,exportFormat="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "刷卡时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date swipeTime;

    @Excel(name = "进出类型",width = 30)
    @ApiModelProperty(value = "进出类型")
    private java.lang.String accessType;

    @Excel(name = "开门类型",width = 30)
    @ApiModelProperty(value = "开门类型")
    private String openType;

    @Excel(name = "刷卡结果",width = 30)
    @ApiModelProperty(value = "刷卡结果")
    private java.lang.String cardResult;
}
