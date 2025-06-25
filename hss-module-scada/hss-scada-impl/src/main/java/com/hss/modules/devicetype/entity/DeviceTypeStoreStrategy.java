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
 * @Description: 设备类型存储策略
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Data
@TableName("T_DEVICE_TYPE_STORE_STRATEGY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_DEVICE_TYPE_STORE_STRATEGY对象", description="设备类型存储策略")
public class DeviceTypeStoreStrategy {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**存储策略类型*/
	@Excel(name = "存储策略类型", width = 15)
    @ApiModelProperty(value = "存储策略类型")
	private java.lang.String type;
	/**存储单位，如小时、分钟*/
	@Excel(name = "存储单位，如小时、分钟", width = 15)
    @ApiModelProperty(value = "存储单位，如小时、分钟")
	private java.lang.String unit;
	/**周期，具体隔间*/
	@Excel(name = "周期，具体隔间", width = 15)
    @ApiModelProperty(value = "周期，具体隔间")
	private java.lang.String period;
	/**时间表达式，可以是某时刻，也可以是周期时间*/
	@Excel(name = "时间表达式，可以是某时刻，也可以是周期时间", width = 15)
    @ApiModelProperty(value = "时间表达式，可以是某时刻，也可以是周期时间")
	private java.lang.String expression;
	/**变量ID*/
	@Excel(name = "变量ID", width = 15)
    @ApiModelProperty(value = "变量ID")
	private java.lang.String variableId;
	/**策略组*/
	@Excel(name = "策略组", width = 15)
    @ApiModelProperty(value = "策略组")
	private java.lang.String groupId;
	/**是否启用*/
	@Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
	private java.lang.String isEnable;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private java.lang.String description;
	/**存储策略模式*/
	@Excel(name = "存储策略模式", width = 15)
    @ApiModelProperty(value = "存储策略模式")
	private java.lang.Integer storeMode;
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
	/**设备类型id*/
	@Excel(name = "设备类型id", width = 15)
    @ApiModelProperty(value = "设备类型id")
	private java.lang.String typeId;
}
