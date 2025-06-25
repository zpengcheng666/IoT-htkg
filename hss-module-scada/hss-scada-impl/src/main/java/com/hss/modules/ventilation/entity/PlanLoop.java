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
 * @Description: 全自动方案
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("T_VENTILATION_PLAN_LOOP")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_VENTILATION_PLAN_LOOP对象", description="全自动方案")
public class PlanLoop {
    
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
	/**控制系统id*/
	@Excel(name = "控制系统id", width = 15)
    @ApiModelProperty(value = "控制系统id")
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
