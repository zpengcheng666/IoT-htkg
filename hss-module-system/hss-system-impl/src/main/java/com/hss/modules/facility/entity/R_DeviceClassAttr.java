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
 * @Description: 设备类别与设备属性中间表
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Data
@TableName("R_DEVCLASS_CLASSATTR")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="R_DEVCLASS_CLASSATTR对象", description="设备类别与设备属性中间表")
public class R_DeviceClassAttr {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**设备类别ID*/
	@Excel(name = "设备类别ID", width = 15)
    @ApiModelProperty(value = "设备类别ID")
	private java.lang.String classId;
	/**设备类别属性ID*/
	@Excel(name = "设备类别属性ID", width = 15)
    @ApiModelProperty(value = "设备类别属性ID")
	private java.lang.String attrId;
	/**标记是否继承子父级*/
	@Excel(name = "标记是否继承子父级", width = 15)
    @ApiModelProperty(value = "标记是否继承子父级")
	private java.lang.Integer isExtend;
}
