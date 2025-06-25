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
 * @Description: 通知公告
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Data
@TableName("T_PUBLISH_NOTICE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_PUBLISH_NOTICE对象", description="通知公告")
public class PublishNotice {
    /**ID*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private java.lang.String id;

    /**标题*/
    @Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String title;

    /**内容*/
    @Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private java.lang.String content;

    /**effectiveTime*/
    @ApiModelProperty(value = "预计发布时间，根据发布类型选择")
    private java.util.Date effectiveTime;

    /**overdueTime*/
    @ApiModelProperty(value = "过期时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date overdueTime;

    /**publishTime*/
    @ApiModelProperty(value = "发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date publishTime;

    /**发布人*/
    @Excel(name = "发布人", width = 15)
    @ApiModelProperty(value = "发布人")
    private java.lang.String publisher;

    /**发布类型(reserve:预约发布，immediate:立即发布)*/
    @Excel(name = "发布类型(reserve:预约发布，immediate:立即发布)", width = 15)
    @ApiModelProperty(value = "发布类型(reserve:预约发布，immediate:立即发布)")
    private java.lang.String publishType;

    /**消息状态(0：未发布，1：已发布，2：发布中，3：已过期)*/
    @Excel(name = "消息状态(0：未发布，1：已发布，2：发布中，3：已过期)", width = 15)
    @ApiModelProperty(value = "消息状态(0：未发布，1：已发布，2：发布中，3：已过期)")
    private java.lang.Integer state;

    /**messageType*/
    @Excel(name = "messageType", width = 15)
    @ApiModelProperty(value = "messageType")
    private java.lang.String messageType;

//    @ExcelIgnore
    @ApiModelProperty(value = "终端ID列表")
    @TableField(exist = false)
    private List<String> terminalIds;

}
