package com.hss.modules.alarm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 报警数据
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
@Data
@TableName("ALARM_DATA")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ALARM_DATA对象", description="报警数据")
public class AlarmData {
    
	/**ID*/
	@TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**设备ID*/
	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
	private java.lang.String deviceId;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
	private java.lang.String deviceType;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
	private java.lang.String deviceName;
	/**状态变量ID*/
	@Excel(name = "状态变量ID", width = 15)
    @ApiModelProperty(value = "状态变量ID")
	private java.lang.String statusVarId;
	/**原始变量ID*/
	@Excel(name = "原始变量ID", width = 15)
    @ApiModelProperty(value = "原始变量ID")
	private java.lang.String originVarId;
	/**原始变量名称*/
	@Excel(name = "原始变量名称", width = 15)
    @ApiModelProperty(value = "原始变量名称")
	private java.lang.String originVarName;
	/**记录值*/
	@Excel(name = "记录值", width = 15)
    @ApiModelProperty(value = "记录值")
	private java.lang.String recordValue;
	/**记录时间*/
    @ApiModelProperty(value = "记录时间")
	private java.util.Date recordTime;
	/**标定值范围*/
	@Excel(name = "标定值范围", width = 15)
    @ApiModelProperty(value = "标定值范围")
	private java.lang.String range;
	/**报警类型：变化率报警等*/
	@Excel(name = "报警类型：变化率报警等", width = 15)
    @ApiModelProperty(value = "报警类型：变化率报警等")
	private java.lang.String alarmType;
	/**报警级别：一般、严重*/
	@Excel(name = "报警级别：一般、严重", width = 15)
    @ApiModelProperty(value = "报警级别：一般、严重")
	private java.lang.String alarmLevel;
	@TableField(exist = false)
	private java.lang.String alarmLevel_disp;
	/**报警级别名称*/
	@Excel(name = "报警级别名称", width = 15)
    @ApiModelProperty(value = "报警级别名称")
	private java.lang.String alarmLevelName;
	/**记录状态：开始报警、解除报警*/
	@Excel(name = "记录状态：开始报警、解除报警", width = 15)
    @ApiModelProperty(value = "记录状态：开始报警、解除报警")
	private java.lang.String status;
	/**是否确认*/
	@Excel(name = "是否确认", width = 15)
    @ApiModelProperty(value = "是否确认")
	private java.lang.Integer isConfirm;
	/**策略ID*/
	@Excel(name = "策略ID", width = 15)
    @ApiModelProperty(value = "策略ID")
	private java.lang.String strategyId;
	/**contingencyName*/
	@Excel(name = "contingencyName", width = 15)
    @ApiModelProperty(value = "contingencyName")
	private java.lang.String contingencyName;
	/**contingencyClass*/
	@Excel(name = "contingencyClass", width = 15)
    @ApiModelProperty(value = "contingencyClass")
	private java.lang.String contingencyClass;
	/**confirmor*/
	@Excel(name = "confirmor", width = 15)
    @ApiModelProperty(value = "confirmor")
	private java.lang.String confirmor;
	/**confirmTime*/
    @ApiModelProperty(value = "confirmTime")
	private java.util.Date confirmTime;
	/**alarmLevelNumber*/
	@Excel(name = "alarmLevelNumber", width = 15)
    @ApiModelProperty(value = "alarmLevelNumber")
	private java.lang.Integer alarmLevelNumber;
	/**linkedCamera*/
	@Excel(name = "linkedCamera", width = 15)
    @ApiModelProperty(value = "linkedCamera")
	private java.lang.String linkedCamera;
	/**开始确认时间*/
    @ApiModelProperty(value = "开始确认时间")
	private java.util.Date beginConfirmTime;
	/**是否视频弹窗*/
	@Excel(name = "是否视频弹窗", width = 15)
    @ApiModelProperty(value = "是否视频弹窗")
	private java.lang.Integer alarmAction;

	@ApiModelProperty(value = "别名")
	private java.lang.String otherName;

	@ApiModelProperty(value = "子系统")
	private java.lang.String subSystem;

	@ApiModelProperty(value = "场景Id")
	private java.lang.String sceneId;
}
