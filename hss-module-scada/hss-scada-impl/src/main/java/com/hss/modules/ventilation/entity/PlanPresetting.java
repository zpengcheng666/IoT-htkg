package com.hss.modules.ventilation.entity;

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
import org.jeecgframework.poi.excel.annotation.ExcelIgnore;

/**
 * @Description: 预设方案
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("T_VENTILATION_PLAN_PRESETTING")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_VENTILATION_PLAN_PRESETTING对象", description="预设方案")
public class PlanPresetting {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**编码*/
	@Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
	private java.lang.String code;
	/**DDC_ID*/
	@Excel(name = "DDC_ID", width = 15)
    @ApiModelProperty(value = "DDC_ID")
	private java.lang.String ddcId;
	/**控制系统name*/
	@ExcelIgnore
	@TableField(exist = false)
	private java.lang.String controlSystemName;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private java.lang.String description;
}
