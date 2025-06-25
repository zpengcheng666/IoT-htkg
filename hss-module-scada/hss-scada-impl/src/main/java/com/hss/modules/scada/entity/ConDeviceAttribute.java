package com.hss.modules.scada.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @Description: 设备属性、变量绑定表
 * @Author: zpc
 * @Date:   2022-12-13
 * @Version: V1.0
 */
@Data
@TableName("CON_SHEBEI_SHUXING")
@EqualsAndHashCode(exclude = {"createdTime", "updatedTime", "deleted"})
@Accessors(chain = true)
@ApiModel(value="CON_SHEBEI_SHUXING对象", description="设备属性、变量绑定表")
public class ConDeviceAttribute {
    

	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private String id;

	@Deprecated
	@ApiModelProperty(value = "图标id")
	private String iconId;

	@Excel(name = "属性名称", width = 15)
    @ApiModelProperty(value = "属性名称")
	private String name;

	@Excel(name = "属性英文名称", width = 15)
    @ApiModelProperty(value = "属性英文名称")
	private String enName;

	@Excel(name = "变量ID", width = 15)
	@ApiModelProperty(value = "变量ID")
	private String variableId;


	@Deprecated
	@Excel(name = "网关id", width = 15)
	@ApiModelProperty(value = "网关id")
	private String wgId;

	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
	private String deviceId;

	@Excel(name = "设备名称", width = 15)
	@ApiModelProperty(value = "设备名称")
	private String deviceName;


	@Excel(name = "设备类型id", width = 15)
	@ApiModelProperty(value = "设备类型id")
	private String deviceTypeId;

	@Excel(name = "设备类型属性id", width = 15)
	@ApiModelProperty(value = "设备类型属性id")
	private String attrId;


	@Excel(name = "初始值", width = 15)
	@ApiModelProperty(value = "初始值")
	private String initValue;

	@Excel(name = "最小值", width = 15)
	@ApiModelProperty(value = "最小值")
	private String minValue;

	@Excel(name = "最大值", width = 15)
	@ApiModelProperty(value = "最大值")
	private String maxValue;

	@Excel(name = "是否存储", width = 15)
	@ApiModelProperty(value = "是否存储")
	private Integer isSave;

	@Excel(name = "是否关联", width = 15)
	@ApiModelProperty(value = "是否关联")
	private Integer isAssociate;

	@Excel(name = "是否关联变量", width = 15)
	@ApiModelProperty(value = "是否关联变量")
	private Integer isAssociateVar;

	@Excel(name = "是否配置", width = 15)
	@ApiModelProperty(value = "是否配置")
	private Integer isConfigurable;

	@Excel(name = "变量表达式", width = 15)
	@ApiModelProperty(value = "变量表达式")
	private String expression;

	@Excel(name = "变量关联表达式", width = 15)
	@ApiModelProperty(value = "变量关联表达式")
	private String varExpression;

	@Excel(name = "排序", width = 15)
	@ApiModelProperty(value = "排序")
	private Integer sortNumber;


    @ApiModelProperty(value = "createdTime")
	private Date createdTime;

    @ApiModelProperty(value = "updatedTime")
	private Date updatedTime;

	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted")
	private Integer deleted;

	@Excel(name = "valueMap", width = 15)
	@ApiModelProperty(value = "valueMap")
	private String valueMap;

	@Excel(name = "数据类型", width = 15)
	@ApiModelProperty(value = "数据类型")
	private String dataType;

	@Excel(name = "单位", width = 15)
	@ApiModelProperty(value = "单位")
	private String unit;



}
