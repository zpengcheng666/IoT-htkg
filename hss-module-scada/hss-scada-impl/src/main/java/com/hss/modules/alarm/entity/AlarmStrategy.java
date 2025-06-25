package com.hss.modules.alarm.entity;

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
 * @Description: 报警策略
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
@Data
@TableName("ALARM_STRATEGY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ALARM_STRATEGY对象", description="报警策略")
public class AlarmStrategy {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
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

	/**
	 * 报警状态的变量
	 */
	@Excel(name = "状态变量ID", width = 15)
    @ApiModelProperty(value = "状态变量ID")
	private java.lang.String statusVarId;

	/**
	 * 触发报警表达式
	 */
	@Excel(name = "条件表达式", width = 15)
    @ApiModelProperty(value = "条件表达式")
	private java.lang.String expression;

	/**
	 * 结束报警表达式
	 */
	@Excel(name = "clearExpression", width = 15)
    @ApiModelProperty(value = "clearExpression")
	private java.lang.String clearExpression;

	/**
	 * 正常值范围
	 */
	@Excel(name = "标定值范围", width = 15)
    @ApiModelProperty(value = "标定值范围")
	private java.lang.String range;

	/**
	 * 触发报警的变量
	 */
	@Excel(name = "原始变量ID", width = 15)
    @ApiModelProperty(value = "原始变量ID")
	private java.lang.String originVarId;


	/**
	 * 存储报警的条件
	 */
	@Excel(name = "存储条件", width = 15)
    @ApiModelProperty(value = "存储条件")
	private java.lang.String storeCondition;

	/**
	 * 推送报警的条件
	 */
	@Excel(name = "报警推送条件", width = 15)
    @ApiModelProperty(value = "报警推送条件")
	private java.lang.String alarmPushCondition;

	/**
	 * 使能状态
	 */
	@Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
	private java.lang.String isEnable;


	/**
	 * 报警状态变量的表达式
	 */
	@Excel(name = "值表达式", width = 15)
    @ApiModelProperty(value = "值表达式")
	private java.lang.String valueExpression;


	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
	private java.lang.String deviceId;
	/**
	 * 报警描述
	 */
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private java.lang.String description;
	/**突发事件类型ID*/
	@Excel(name = "突发事件类型ID", width = 15)
    @ApiModelProperty(value = "突发事件类型ID")
	private java.lang.String contingencyClass;
	/**延时开始*/
	@Excel(name = "延时开始", width = 15)
    @ApiModelProperty(value = "延时开始")
	private java.lang.Integer delayBegin;
	/**延时解除*/
	@Excel(name = "延时解除", width = 15)
    @ApiModelProperty(value = "延时解除")
	private java.lang.Integer delayRemove;
	/**报警弹窗*/
	@Excel(name = "报警弹窗", width = 15)
    @ApiModelProperty(value = "报警弹窗")
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

	@ApiModelProperty(value = "策略id")
	private String strategyId;

}
