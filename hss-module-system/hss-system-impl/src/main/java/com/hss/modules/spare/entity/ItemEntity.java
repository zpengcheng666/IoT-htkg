package com.hss.modules.spare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * @Description: 物料管理
 * @Author: wuyihan
 * @Date:   2024-04-29
 * @Version: V1.0
 */
@Data
@TableName("BP_ITEM")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BP_ITEM对象", description="物料管理")
public class ItemEntity {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
	private java.lang.String itemNo;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String itemName;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
	private java.lang.String specification;
	/**品牌*/
	@Excel(name = "品牌", width = 15)
    @ApiModelProperty(value = "品牌")
	private java.lang.String brand;
	/**分类id*/
	@Excel(name = "分类id", width = 15)
    @ApiModelProperty(value = "分类id")
	private java.lang.String itemType;
	@TableField(exist = false)
	private java.lang.String itemTypeName;
	/**数量单位*/
	@Excel(name = "数量单位", width = 15)
    @ApiModelProperty(value = "数量单位")
	private java.lang.String unit;
	/**安全库存*/
	@Excel(name = "安全库存", width = 15)
    @ApiModelProperty(value = "安全库存")
	private BigDecimal quantity;
	/**货架id*/
	@Excel(name = "货架id", width = 15)
    @ApiModelProperty(value = "货架id")
	private java.lang.String rackId;
	/**库区id*/
	@Excel(name = "库区id", width = 15)
    @ApiModelProperty(value = "库区id")
	private java.lang.String areaId;
	/**仓库id*/
	@Excel(name = "仓库id", width = 15)
    @ApiModelProperty(value = "仓库id")
	private java.lang.String warehouseId;
	/**生产日期*/
	@Excel(name = "生产日期", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "生产日期")
	private java.util.Date productionDate;
	/**有效期*/
	@Excel(name = "有效期", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "有效期")
	private java.util.Date expiryDate;
	/**批次*/
	@Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
	private java.lang.String batch;
	/**状态(0在库,1使用,2借出)*/
	@Excel(name = "状态(0在库,1使用,2借出)", width = 15)
    @ApiModelProperty(value = "状态(0在库,1使用,2借出)")
	private java.lang.Integer status;
}
