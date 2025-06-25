package com.hss.modules.devicetype.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 设备类型报警策略
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Data
@TableName("T_DEVICE_TYPE_ALARM_STRATEGY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_DEVICE_TYPE_ALARM_STRATEGY对象", description="设备类型报警策略")
public class DeviceTypeAlarmStrategy {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**报警类型*/
	@Excel(name = "报警类型", width = 15)
    @ApiModelProperty(value = "报警类型")
	private java.lang.String type;
	/**报警级别*/
	@Excel(name = "报警级别", width = 15)
    @ApiModelProperty(value = "报警级别")
	private java.lang.String levelId;
	/**策略组*/
	@Excel(name = "策略组", width = 15)
    @ApiModelProperty(value = "策略组")
	private java.lang.String groupId;
	/**变化周期，应用于变化率报警*/
	@Excel(name = "变化周期，应用于变化率报警", width = 15)
    @ApiModelProperty(value = "变化周期，应用于变化率报警")
	private java.lang.String period;
	/**频率*/
	@Excel(name = "频率", width = 15)
    @ApiModelProperty(value = "频率")
	private java.lang.String frequency;
	/**状态变量id*/
	@Excel(name = "状态变量id", width = 15)
    @ApiModelProperty(value = "状态变量id")
	private java.lang.String statusVarId;
	/**表达式*/
	@Excel(name = "表达式", width = 15)
    @ApiModelProperty(value = "表达式")
	private java.lang.String expression;
	/**clearExpression*/
	@Excel(name = "clearExpression", width = 15)
    @ApiModelProperty(value = "clearExpression")
	private java.lang.String clearExpression;
	/**标定值范围*/
	@Excel(name = "标定值范围", width = 15)
    @ApiModelProperty(value = "标定值范围")
	private java.lang.String range;
	/**原始变量id*/
	@Excel(name = "原始变量id", width = 15)
    @ApiModelProperty(value = "原始变量id")
	private java.lang.String originVarId;
	/**存储条件*/
	@Excel(name = "存储条件", width = 15)
    @ApiModelProperty(value = "存储条件")
	private java.lang.String storeCondition;
	/**报警推送条件*/
	@Excel(name = "报警推送条件", width = 15)
    @ApiModelProperty(value = "报警推送条件")
	private java.lang.String alarmPushCondition;
	/**是否启用*/
	@Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
	private java.lang.String isEnable;
	/**值表达式*/
	@Excel(name = "值表达式", width = 15)
    @ApiModelProperty(value = "值表达式")
	private java.lang.String valueExpression;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private java.lang.String description;
	/**突发类型id*/
	@Excel(name = "突发类型id", width = 15)
    @ApiModelProperty(value = "突发类型id")
	private java.lang.String contingencyClass;
	/**延时开始*/
	@Excel(name = "延时开始", width = 15)
    @ApiModelProperty(value = "延时开始")
	private java.lang.Integer delayBegin;
	/**延时解除*/
	@Excel(name = "延时解除", width = 15)
    @ApiModelProperty(value = "延时解除")
	private java.lang.Integer delayRemove;
	/**弹窗*/
	@Excel(name = "弹窗", width = 15)
    @ApiModelProperty(value = "弹窗")
	private java.lang.Integer alarmAction;
	/**报警策略模式*/
	@Excel(name = "报警策略模式", width = 15)
    @ApiModelProperty(value = "报警策略模式")
	private java.lang.Integer alarmMode;
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
	/**类型ID*/
	@Excel(name = "类型ID", width = 15)
    @ApiModelProperty(value = "类型ID")
	private java.lang.String typeId;
}
