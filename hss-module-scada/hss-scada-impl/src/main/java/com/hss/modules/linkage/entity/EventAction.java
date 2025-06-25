package com.hss.modules.linkage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelIgnore;

/**
 * @Description: 事件动作
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Data
@TableName("T_EVENT_ACTION")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_EVENT_ACTION对象", description="事件动作")
public class EventAction {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**动作名称*/
	@Excel(name = "动作名称", width = 15)
    @ApiModelProperty(value = "动作名称")
	private java.lang.String name;
	/**动作类型，如变量赋值、摄像机预置位调用*/
	@Excel(name = "动作类型，如变量赋值、摄像机预置位调用", width = 15)
    @ApiModelProperty(value = "动作类型，如变量赋值、摄像机预置位调用")
	private java.lang.String type;
	/**操作对象，如变量ID、设备ID*/
	@Excel(name = "操作对象，如变量ID、设备ID", width = 15)
    @ApiModelProperty(value = "操作对象，如变量ID、设备ID")
	private java.lang.String operationId;

	@ExcelIgnore
	@ApiModelProperty(value = "操作对象名称")
	private java.lang.String operationName;
	/**操作值*/
	@Excel(name = "操作值", width = 15)
    @ApiModelProperty(value = "操作值")
	private java.lang.String operationValue;
	/**动作延时*/
	@Excel(name = "动作延时", width = 15)
    @ApiModelProperty(value = "动作延时")
	private java.lang.String delayTime;
	/**事件ID*/
	@Excel(name = "事件ID", width = 15)
    @ApiModelProperty(value = "事件ID")
	private java.lang.String eventId;
	/**设备ID*/
	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
	private java.lang.String deviceId;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private java.lang.String description;
	/**命令类型*/
	@Excel(name = "命令类型", width = 15)
    @ApiModelProperty(value = "命令类型")
	private java.lang.String command;
	/**channelId*/
	@Excel(name = "channelId", width = 15)
    @ApiModelProperty(value = "channelId")
	private java.lang.String channelId;
	/**终端ID*/
	@Excel(name = "终端ID", width = 15)
    @ApiModelProperty(value = "终端ID")
	private java.lang.String terminals;
	/**createdTime*/
    @ApiModelProperty(value = "createdTime")
	private java.util.Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime")
	private java.util.Date updatedTime;
	/**deleted*/
	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted")
	private java.lang.Integer deleted;
}
