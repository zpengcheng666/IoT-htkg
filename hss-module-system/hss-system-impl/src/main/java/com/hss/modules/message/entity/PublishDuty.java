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
 * @Description: 值班安排表
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Data
@TableName("T_PUBLISH_DUTY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_PUBLISH_DUTY对象", description="值班安排表")
public class PublishDuty {
    /**ID*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private java.lang.String id;
    /**值班安排*/
    @Excel(name = "值班安排", width = 15)
    @ApiModelProperty(value = "值班安排")
    private java.lang.String name;
    /**publishTime*/
    @ApiModelProperty(value = "publishTime")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date publishTime;
    /**消息状态(0：未发布，1：已发布，2：发布中，3：已过期)*/
    @Excel(name = "消息状态(0：未发布，1：已发布，2：发布中，3：已过期)", width = 15)
    @ApiModelProperty(value = "消息状态(0：未发布，1：已发布，2：发布中，3：已过期)")
    private java.lang.Integer state;
    /**messageType*/
    @Excel(name = "messageType", width = 15)
    @ApiModelProperty(value = "messageType")
    private java.lang.String messageType;

    /**状态*/
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    /**值班区域*/
    @Excel(name = "值班区域", width = 15)
    @ApiModelProperty(value = "值班区域")
    private java.lang.String dutyLocation;
    /**值班岗位*/
    @Excel(name = "值班岗位", width = 15)
    @ApiModelProperty(value = "值班岗位")
    private java.lang.String dutyPost;
    /**日期*/
    @ApiModelProperty(value = "日期")
    private java.util.Date dutyDate;
    /**小组*/
    @ApiModelProperty(value = "小组")
    private java.util.Date dutyGroup;
    /**周期*/
    @ApiModelProperty(value = "周期")
    private java.util.Date dutyPeriod;
    /**编号*/
    @Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
    private java.lang.String code;

    @ApiModelProperty(value = "岗位列表")
    @ExcelIgnore
    @TableField(exist = false)
    private List<PublishDutyType> types;

    @ExcelIgnore
    @ApiModelProperty(value = "终端ID列表")
    @TableField(exist = false)
    private List<String> terminalIds;
}
