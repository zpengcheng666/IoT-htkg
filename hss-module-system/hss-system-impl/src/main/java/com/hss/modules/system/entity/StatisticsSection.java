package com.hss.modules.system.entity;

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

import java.util.List;

/**
 * @Description: 分布区间统计设置
 * @Author: zpc
 * @Date:   2022-12-05
 * @Version: V1.0
 */
@Data
@TableName("STATISTICS_SECTION")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="STATISTICS_SECTION对象", description="分布区间统计设置")
public class StatisticsSection {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**属性英文名称*/
	@Excel(name = "属性英文名称", width = 15)
    @ApiModelProperty(value = "属性英文名称")
	private java.lang.String attrEnName;
	/**属性名称*/
	@Excel(name = "属性名称", width = 15)
    @ApiModelProperty(value = "属性名称")
	private java.lang.String attrName;
	/**分布区间计算表达式*/
	@Excel(name = "分布区间计算表达式", width = 15)
    @ApiModelProperty(value = "分布区间计算表达式")
	private java.lang.String sectionCalculation;
	/**分布区间展示表达式*/
	@Excel(name = "分布区间展示表达式", width = 15)
    @ApiModelProperty(value = "分布区间展示表达式")
	private java.lang.String sectionDisplay;
	/**排序号*/
	@Excel(name = "排序号", width = 15)
    @ApiModelProperty(value = "排序号")
	private java.lang.Integer orderNum;

	/**设备类型id*/
	@Excel(name = "设备类型id", width = 15)
	@ApiModelProperty(value = "设备类型id")
	private java.lang.String devClassid;

	/**设备属性id*/
	@Excel(name = "设备属性id", width = 15)
	@ApiModelProperty(value = "设备属性id")
	private java.lang.String devAttrid;

	@TableField(exist = false)
	private List<String> intervalValueList;
}
