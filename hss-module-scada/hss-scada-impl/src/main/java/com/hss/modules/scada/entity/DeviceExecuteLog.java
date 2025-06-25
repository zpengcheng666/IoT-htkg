package com.hss.modules.scada.entity;

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
 * @Description: 下发设备命令日志
 * @Author: zpc
 * @Date:   2023-03-08
 * @Version: V1.0
 */
@Data
@TableName("CON_DEVICE_EXECUTE_LOG")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CON_DEVICE_EXECUTE_LOG对象", description="下发设备命令日志")
public class DeviceExecuteLog {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;

	/**设备id*/
    @ApiModelProperty(value = "设备id")
	private java.lang.String deviceId;

	@ApiModelProperty(value = "设备名称")
	@TableField(exist = false)
	@Excel(name = "设备名称", width = 20)
	private java.lang.String deviceName;

	/**属性id*/
    @ApiModelProperty(value = "属性id")
	private java.lang.String attrId;

	@Excel(name = "设备属性", width = 20)
	@TableField(exist = false)
	@ApiModelProperty(value = "属性名称")
	private java.lang.String attrName;

	/**属性值*/
    @ApiModelProperty(value = "属性值")
	private java.lang.String value;

	/**执行时间*/
    @ApiModelProperty(value = "执行时间")
	@Excel(name = "执行时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date executeTime;

	/**描述*/
    @ApiModelProperty(value = "描述")
	private java.lang.String remark;

	/**执行结果0失败1成功*/
	@Excel(name = "执行结果", width = 15,replace = {"失败_0","成功_1"})
    @ApiModelProperty(value = "执行结果0失败1成功")
	private java.lang.Integer executeResult;
}
