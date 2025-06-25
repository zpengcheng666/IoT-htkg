package com.hss.modules.store.entity;

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

import javax.persistence.Transient;

/**
 * @Description: 设备运行时历史数据
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
@Data
@TableName("STORE_HISTORY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="STORE_HISTORY对象", description="设备运行时历史数据")
public class StoreHistory {

	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "id")
	private java.lang.String id;
	/**设备ID*/
//	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
	private java.lang.String deviceId;
	/**设备类型*/
//	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
	private java.lang.String deviceType;

	@TableField(exist = false)
	@Excel(name = "设备类型")
	private java.lang.String deviceType_disp;
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
	/**记录值*/
	@Excel(name = "记录值", width = 20)
	@ApiModelProperty(value = "记录值")
	private java.lang.String recordValue;
	/**单位*/
	@Excel(name = "单位", width = 15)
	@ApiModelProperty(value = "单位")
	private java.lang.String unit;
	/**处理方法记录时间*/
	@Excel(name = "记录时间", width = 15,exportFormat="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "记录时间")
	private java.util.Date recordTime;
	/**位置名称*/
	@Excel(name = "所在位置", width = 15)
	@ApiModelProperty(value = "所在位置")
	private java.lang.String locationName;


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
	/**标定值范围*/
//	@Excel(name = "标定值范围", width = 15)
    @ApiModelProperty(value = "标定值范围")
	private java.lang.String range;
	/**位置ID*/
//	@Excel(name = "位置ID", width = 15)
    @ApiModelProperty(value = "位置ID")
	private java.lang.String locationId;
	@ApiModelProperty(value = "查询开始时间")
	@Transient
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTime;

	@ApiModelProperty(value = "查询结束时间")
	@Transient
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date endTime;

	@ApiModelProperty(value = "属性id")
	private String attrId;
}
