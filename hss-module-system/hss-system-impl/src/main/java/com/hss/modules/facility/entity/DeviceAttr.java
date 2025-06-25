package com.hss.modules.facility.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 属性字典
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Data
@TableName("DF_BD_DEVICE_ATTR")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DF_BD_DEVICE_ATTR对象", description="属性字典")
public class DeviceAttr {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;

	/**设备ID*/
	@Excel(name = "设备ID", width = 50)
	@ApiModelProperty(value = "设备ID")
	private java.lang.String deviceId;

	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**代码*/
	@Excel(name = "代码", width = 15)
    @ApiModelProperty(value = "代码")
	private java.lang.String code;
	/**排序号*/
	@Excel(name = "排序号", width = 15)
    @ApiModelProperty(value = "排序号")
	private java.lang.Integer index1;
	/**是否可删除*/
	@Excel(name = "是否可删除", width = 15)
    @ApiModelProperty(value = "是否可删除")
	private java.lang.Integer canDelete;
	/**数据表名\字典编码*/
	@Excel(name = "数据表名/字典编码", width = 15)
    @ApiModelProperty(value = "数据表名/字典编码")
	private java.lang.String dataUrl;
	/**属性类型*/
	@Excel(name = "属性类型", width = 15)
    @ApiModelProperty(value = "属性类型")
	private java.lang.String attrType;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
	private java.lang.String unit;
	/**是否配置(0:不是配置,1:配置)*/
	@Excel(name = "是否配置(0:不是配置,1:配置)", width = 15)
    @ApiModelProperty(value = "是否配置(0:不是配置,1:配置)")
	private java.lang.Integer isSelect;

	@TableField(select = true)
	@ApiModelProperty(value = "删除标志",hidden = true)
	private Integer deleted;
	@TableField(exist = false)
	private Integer checked;

	@TableField(exist = false)
	private String value;
}
