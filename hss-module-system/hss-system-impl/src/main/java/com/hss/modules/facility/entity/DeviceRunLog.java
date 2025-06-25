package com.hss.modules.facility.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 动用使用
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Data
@TableName("DF_DEVICE_RUNLOG")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DF_DEVICE_RUNLOG对象", description="动用使用")
public class DeviceRunLog {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**设备ID*/
	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
	private java.lang.String devId;
	@TableField(exist = false)
	private String devId_disp;
	/**启动时间*/
    @ApiModelProperty(value = "启动时间")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTime;
	/**停止时间*/
    @ApiModelProperty(value = "停止时间")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date stopTime;
	/**本次运行时长*/
	@Excel(name = "本次运行时长", width = 15)
    @ApiModelProperty(value = "本次运行时长单位s")
	private java.lang.Integer duration;
	/**累计运行时长*/
	@Excel(name = "累计运行时长", width = 15)
    @ApiModelProperty(value = "累计运行时长单位s")
	private java.lang.Integer totalDuration;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String remark;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
	private java.lang.String devName;
	/**图元设备ID-暂用，待动用使用与业务系统拆分*/
	@Excel(name = "图元设备ID-暂用，待动用使用与业务系统拆分", width = 15)
    @ApiModelProperty(value = "图元设备ID-暂用，待动用使用与业务系统拆分")
	private java.lang.String biDeviceId;
	@TableField(select = true)
	@TableLogic
	private Integer deleted;
}
