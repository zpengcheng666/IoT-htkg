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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 报警历史数据
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
@Data
@TableName("ALARM_HISTORY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ALARM_HISTORY对象", description="报警历史数据")
public class AlarmHistory {

	public static final String STATUS_HANDLED = "1";

	public static final String STATUS_UNHANDLED = "0";

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**设备ID*/
//	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
	private java.lang.String deviceId;
	/**设备类型*/
//	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
	private java.lang.String deviceType;



	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
	private java.lang.String deviceName;
	/**所属子系统*/
//	@Excel(name = "所属子系统", width = 15)
    @ApiModelProperty(value = "所属子系统")
	private java.lang.String subsystem;
	/**属性名称*/
	@Excel(name = "设备属性", width = 15)
    @ApiModelProperty(value = "设备属性")
	private java.lang.String attrName;
	/**报警值*/
	@Excel(name = "报警值", width = 15)
	@ApiModelProperty(value = "报警值")
	private java.lang.String alarmValue;
	/**标定值范围*/
	@Excel(name = "标定值", width = 15)
	@ApiModelProperty(value = "标定值")
	private java.lang.String range;
	/**报警开始时间*/
	@Excel(name = "开始时间", width = 15,exportFormat="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "开始时间")
	private java.util.Date alarmStartTime;
	/**报警结束时间*/
	@Excel(name = "结束时间", width = 15,exportFormat="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "结束时间")
	private java.util.Date alarmEndTime;
	/**报警类型名称：有效报警、误报警、测试报警、故障报警*/
	@Excel(name = "报警类型", width = 15)
	@ApiModelProperty(value = "报警类型名称：有效报警、误报警、测试报警、故障报警")
	private java.lang.String alarmTypeName;
	/**报警级别*/
	@Excel(name = "报警级别", width = 15,replace = {"一类区_5b04c2e52b494640b7fc67c0fa24e0a0","一般_0D77B36E6D0A467A84064047326DCE0D","严重_AE6CC7BA64994CFA927C55EC7DDB592D"})
	@ApiModelProperty(value = "报警级别")
	private java.lang.String levelId;
	/**确认人*/
	@Excel(name = "确认人", width = 15)
	@ApiModelProperty(value = "确认人")
	private java.lang.String confirmor;
	/**确认时间*/
	@Excel(name = "确认时间", width = 15,exportFormat="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "确认时间")
	private java.util.Date confirmTime;
	/**处理类型：未处理、已处理*/
	@Excel(name = "处理状态", width = 15,replace = {"未处理_0","已处理_1"})
	@ApiModelProperty(value = "处理状态：未处理、已处理")
	private java.lang.String isHandle;
	@ApiModelProperty(value = "处理状态：未处理、已处理")
	@TableField(exist = false)
	private java.lang.String isHandleTxt;
	/**处理人*/
	@Excel(name = "处理人", width = 15)
	@ApiModelProperty(value = "处理人")
	private java.lang.String handler;
	/**处理时间*/
	@Excel(name = "处理时间", width = 15,exportFormat="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "处理时间")
	private java.util.Date handleTime;

	@ApiModelProperty(value = "属性id")
	private java.lang.String attrId;

	@ApiModelProperty(value = "设备类型id")
	private java.lang.String deviceTypeId;


	/**属性英文名称*/
//	@Excel(name = "属性英文名称", width = 15)
    @ApiModelProperty(value = "属性英文名称")
	private java.lang.String attrEnName;
	/**变量ID*/
//	@Excel(name = "变量ID", width = 15)
    @ApiModelProperty(value = "变量ID")
	private java.lang.String variableId;
	/**变量名称*/
//	@Excel(name = "变量名称", width = 15)
    @ApiModelProperty(value = "变量名称")
	private java.lang.String variableName;
	/**报警类型ID*/
//	@Excel(name = "报警类型ID", width = 15)
    @ApiModelProperty(value = "报警类型ID")
	private java.lang.String alarmTypeId;
	/**报警时长*/
//	@Excel(name = "报警时长", width = 15)
    @ApiModelProperty(value = "报警时长")
	private java.lang.String alarmDuration;
	/**报警级别名称*/
//	@Excel(name = "报警级别名称", width = 15)
    @ApiModelProperty(value = "报警级别名称")
	private java.lang.String levelName;
	/**处理方法*/
//	@Excel(name = "处理方法", width = 15)
    @ApiModelProperty(value = "处理方法")
	private java.lang.String handleMethod;
	/**位置ID*/
//	@Excel(name = "位置ID", width = 15)
    @ApiModelProperty(value = "位置ID")
	private java.lang.String locationId;
	/**位置名称*/
//	@Excel(name = "位置名称", width = 15)
    @ApiModelProperty(value = "位置名称")
	private java.lang.String locationName;
	/**alarmDataId*/
//	@Excel(name = "alarmDataId", width = 15)
    @ApiModelProperty(value = "alarmDataId")
	private java.lang.String alarmDataId;
	/**createdTime*/
    @ApiModelProperty(value = "createdTime")
	private java.util.Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime")
	private java.util.Date updatedTime;
	/**deleted*/
//	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted")
	private java.lang.Integer deleted;

	@ApiModelProperty(value = "查询开始时间")
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTime;

	@ApiModelProperty(value = "查询结束时间")
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date endTime;

	@ApiModelProperty("统计周期")
	@TableField(exist = false)
	private String statisticsCycle;

	@ApiModelProperty("统计结果-总量")
	@TableField(exist = false)
	private Long total;

	@ApiModelProperty(value = "别名")
	private java.lang.String otherName;
}
