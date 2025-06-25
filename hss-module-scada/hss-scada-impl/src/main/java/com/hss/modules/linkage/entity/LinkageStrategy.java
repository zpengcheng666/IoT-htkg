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

/**
 * @Description: 联动策略
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Data
@TableName("T_LINKAGE_STRATEGY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_LINKAGE_STRATEGY对象", description="联动策略")
public class LinkageStrategy {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**条件表达式*/
	@Excel(name = "条件表达式", width = 15)
    @ApiModelProperty(value = "条件表达式")
	private java.lang.String expression;
	/**事件ID*/
	@Excel(name = "事件ID", width = 15)
    @ApiModelProperty(value = "事件ID")
	private java.lang.String eventId;
	/**是否启用*/
	@Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
	private java.lang.String isEnable;
	/**策略组*/
	@Excel(name = "策略组", width = 15)
    @ApiModelProperty(value = "策略组")
	private java.lang.String groupId;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private java.lang.String description;
	/**类型*/
	@Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
	private java.lang.String type;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
	private java.lang.String unit;
	/**周期*/
	@Excel(name = "周期", width = 15)
    @ApiModelProperty(value = "周期")
	private java.lang.String period;
	/**启动时间*/
    @ApiModelProperty(value = "启动时间")
	private java.util.Date startTime;
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

	@Excel(name = "条件表达式解析后", width = 15)
	@ApiModelProperty(value = "条件表达式解析后")
	private java.lang.String expressionStr;

	@Excel(name = "报警策略id", width = 15)
	@ApiModelProperty(value = "报警策略id")
	private String alarmStrategyId;

	@Excel(name = "报警设备id", width = 15)
	@ApiModelProperty(value = "报警设备id")
	private String alarmDeviceId;
}
