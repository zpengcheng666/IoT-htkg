package com.hss.modules.facility.entity;

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
 * @Description: 设备基础信息表--设施设备-扩展属性值
 * @Author: chushubin
 * @Date:   2024-04-15
 * @Version: V1.0
 */
@Data
@TableName("DF_BI_DEVICE_ATTR")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DF_BI_DEVICE_ATTR对象", description="设备基础信息表--设施设备-扩展属性值")
public class DeviceBIAttrEntity {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private String id;
	/**设备ID，外键关联DF_BI_DEVICE.ID*/
	@Excel(name = "设备ID，外键关联DF_BI_DEVICE.ID", width = 15)
    @ApiModelProperty(value = "设备ID，外键关联DF_BI_DEVICE.ID")
	private String deviceId;

	/**扩展属性ID，DF_BD_DEVICE_ATTR.ID*/
	@Excel(name = "扩展属性ID，DF_BD_DEVICE_ATTR.ID", width = 15)
    @ApiModelProperty(value = "扩展属性ID，DF_BD_DEVICE_ATTR.ID")
	private String attrId;

	/**扩展属性值*/
	@Excel(name = "扩展属性值", width = 15)
    @ApiModelProperty(value = "扩展属性值")
	private String attrVal;
}
