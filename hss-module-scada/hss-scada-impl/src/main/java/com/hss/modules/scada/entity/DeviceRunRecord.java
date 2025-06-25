package com.hss.modules.scada.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 设备运行记录表
 * @Author: zpc
 * @Date:   2023-08-01
 * @Version: V1.0
 */
@Data
@TableName("DEVICE_RUN_RECORD")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DEVICE_RUN_RECORD对象", description="设备运行记录表")
public class DeviceRunRecord {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
	private java.lang.String deviceId;
	/**运行状态:1运行,0停止*/
	@Excel(name = "运行状态:1运行,0停止", width = 15)
    @ApiModelProperty(value = "运行状态:1运行,0停止")
	private java.lang.Integer deviceState;
	/**记录时间*/
	@Excel(name = "记录时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "记录时间")
	private java.util.Date recordTime;
	/**持续时间：秒*/
	@Excel(name = "持续时间：秒", width = 15)
    @ApiModelProperty(value = "持续时间：秒")
	private java.lang.Integer sumTime;
}
