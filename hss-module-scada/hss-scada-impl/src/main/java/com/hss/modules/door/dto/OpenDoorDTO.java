package com.hss.modules.door.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @author hd
 * 远程开门记录查询条件
 */
@Data
@ApiModel("远程开门记录查询条件")
public class OpenDoorDTO {

    @ApiModelProperty("门id列表")
    private List<String> doorIds;

    @ApiModelProperty("人员名称")
    private String perName;

    @ApiModelProperty(value = "卡号")
    private String cardCode;

    @ApiModelProperty(value = "进出类型")
    private String accessType;

    @ApiModelProperty(value = "记录类型")
    private Integer recordType;

    @ApiModelProperty("查询开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date startTime;


    @ApiModelProperty("查询结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date endTime;

    @ApiModelProperty("页码")
    private Integer pageNo;

    @ApiModelProperty("每页条数")
    private Integer pageSize;
}
