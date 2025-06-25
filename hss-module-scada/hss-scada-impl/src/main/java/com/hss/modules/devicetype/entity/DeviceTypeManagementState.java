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
 * @Description: 设备类型管理状态管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Data
@TableName("T_DEVICE_TYPE_MANAGEMENT_STATE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_DEVICE_TYPE_MANAGEMENT_STATE对象", description="设备类型管理状态管理")
public class DeviceTypeManagementState {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**设备类型id*/
	@Excel(name = "设备类型id", width = 15)
    @ApiModelProperty(value = "设备类型id")
	private java.lang.String typeId;
	/**状态名称*/
	@Excel(name = "状态名称", width = 15)
    @ApiModelProperty(value = "状态名称")
	private java.lang.String stateName;
	/**状态英文名称*/
	@Excel(name = "状态英文名称", width = 15)
    @ApiModelProperty(value = "状态英文名称")
	private java.lang.String stateEnName;

	@Excel(name = "默认状态", width = 15)
	@ApiModelProperty(value = "默认状态")
	private Integer defaultState;
}
