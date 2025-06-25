package com.hss.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 气象信息
 * @Author: zpc
 * @Date:   2022-12-06
 * @Version: V1.0
 */
@Data
@TableName("T_PUBLISH_WEATHER")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_PUBLISH_WEATHER对象", description="气象信息")
public class PublishWeather {
    /**ID*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private java.lang.String id;

    /**weatherTime*/
    @ApiModelProperty(value = "weatherTime")
    @Excel(name = "气象时间", width = 20, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date weatherTime;
    /**内容*/
    @Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private java.lang.String content;
    /**publishTime*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "publishTime")
    private java.util.Date publishTime;
    /**消息状态(0：未发布，1：已发布，2：发布中，3：已过期)*/
    @Excel(name = "消息状态(0：未发布，1：已发布，2：发布中，3：已过期)", width = 15)
    @ApiModelProperty(value = "消息状态(0：未发布，1：已发布，2：发布中，3：已过期)")
    private java.lang.Integer state;
    /**messageType*/
    @Excel(name = "messageType", width = 15)
    @ApiModelProperty(value = "messageType")
    private java.lang.String messageType;

    @ExcelIgnore
    @ApiModelProperty(value = "终端ID列表")
    @TableField(exist = false)
    private List<String> terminalIds;

    /**最高气温*/
    @Excel(name = "最高气温", width = 15)
    @ApiModelProperty(value = "最高气温")
    private java.lang.String highTmp;

    /**最低气温*/
    @Excel(name = "最低气温", width = 15)
    @ApiModelProperty(value = "最低气温")
    private java.lang.String lowTmp;


    @TableField(exist = false)
    private String weatherTimeStr;

}
