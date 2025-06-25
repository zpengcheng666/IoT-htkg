package com.hss.modules.facility.entity;

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
 * @Description: 交接管理
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Data
@TableName("DF_DEVICE_HANDOVER")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DF_DEVICE_HANDOVER对象", description="交接管理")
public class DeviceHandover {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**设备ID*/
	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
	private java.lang.String devId;
	@TableField(exist = false)
	private String devId_disp;

	/**交接时间*/
    @ApiModelProperty(value = "交接时间")
//	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date handoverTime;

	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd")
	private java.util.Date begin_handoverTime;
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd")
	private java.util.Date end_handoverTime;

	/**交付单位*/
	@Excel(name = "交付单位", width = 15)
    @ApiModelProperty(value = "交付单位")
	private java.lang.String deliverer;
	/**接收单位*/
	@Excel(name = "接收单位", width = 15)
    @ApiModelProperty(value = "接收单位")
	private java.lang.String receiver;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String note;
	/**接收人*/
	@Excel(name = "接收人", width = 15)
    @ApiModelProperty(value = "接收人")
	private java.lang.String receiveMan;
	/**交付人*/
	@Excel(name = "交付人", width = 15)
    @ApiModelProperty(value = "交付人")
	private java.lang.String deliverMan;
	/**监交人*/
	@Excel(name = "监交人", width = 15)
    @ApiModelProperty(value = "监交人")
	private java.lang.String monitor;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
	private java.lang.String devName;

	/**文件路径*/
	@Excel(name = "文件路径", width = 15)
	@ApiModelProperty(value = "文件路径")
	private java.lang.String imgUrls;
}
