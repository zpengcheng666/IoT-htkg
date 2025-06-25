package com.hss.modules.door.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelIgnore;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * @author hd
 */
@Data
@ApiModel("远程开门记录")
public class OpenDoorVO {
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


    @Excel(name = "开门时间", width =  30, exportFormat="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开门时间")
    private java.util.Date openTime;

    @Excel(name = "进出人数", width = 30)
    @ApiModelProperty(value = "进出人数")
    private java.lang.Integer perNumRecord;

    @Excel(name = "刷卡人员1", width = 30)
    @ApiModelProperty(value = "刷卡人员1")
    private java.lang.String perName1;

    @Excel(name = "刷卡人单位1", width = 30)
    @ApiModelProperty(value = "刷卡人单位1")
    private java.lang.String perDept1;

    @Excel(name = "刷卡人职务1", width = 30)
    @ApiModelProperty(value = "刷卡人职务1")
    private java.lang.String perTitle1;

    @Excel(name = "卡号1", width = 30)
    @ApiModelProperty(value = "卡号1")
    private java.lang.String cardCode1;
    /**刷卡时间1*/
    @Excel(name = "刷卡时间1", width = 30, exportFormat="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "刷卡时间1")
    private java.util.Date swipeTime1;

    @Excel(name = "进出类型", width = 30)
    @ApiModelProperty(value = "进出类型")
    private java.lang.String accessType;

    @Excel(name = "开门类型", width = 30)
    @ApiModelProperty(value = "开门类型")
    private java.lang.String openType;

    @ExcelIgnore
    @ApiModelProperty(value = "记录类型", hidden = true)
    private java.lang.Integer recordType;

    @Excel(name = "记录类型", width = 30)
    @ApiModelProperty(value = "记录类型")
    private java.lang.String recordTypeStr;

}
