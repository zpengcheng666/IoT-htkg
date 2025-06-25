package com.hss.modules.door.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 最后一次通行记录
 * @author hd
 */
@Data
@ApiModel("最后一次通行记录")
public class GetLastVOInOut {

    @ApiModelProperty(value = "记录id")
    private String id;

    @ApiModelProperty(value = "门ID")
    private String doorId;

    @ApiModelProperty(value = "门编号")
    private String doorCode;

    @ApiModelProperty(value = "门名称")
    private String doorName;

    @ApiModelProperty(value = "门类型")
    private String doorType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开门时间")
    private java.util.Date openTime;

    @ApiModelProperty(value = "人员")
    private String perName;

    @ApiModelProperty(value = "人单位")
    private String perDept;

    @ApiModelProperty(value = "人职务")
    private String perTitle;

    @ApiModelProperty(value = "卡号")
    private String cardCode;

    @ApiModelProperty(value = "头像")
    private String photo;

    @ApiModelProperty(value = "刷卡时间")
    private java.util.Date swipeTime;

    @ApiModelProperty(value = "开门方式")
    private String openType;






}
