package com.hss.modules.spare.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;


/**
* @ClassDescription: 盘库单详情
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 15:36
*/
@Data
@TableName("BP_INVENTORY_CHECK_DETAIL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="盘库单详情", description="盘库单详情")
public class InventoryCheckDetailEntity {

	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private String id;

    @ApiModelProperty(value = "盘点单id")
	private String checkId;

    @ApiModelProperty(value = "物料id")
	private String itemId;

	@ApiModelProperty(value = "库房id")
	private String warehouseId;

	@ApiModelProperty(value = "库区id")
	private String areaId;




    @ApiModelProperty(value = "库存数量")
	private BigDecimal quantity;

    @ApiModelProperty(value = "盘点数量")
	private BigDecimal checkQuantity;


    @ApiModelProperty(value = "delFlag", hidden = true)
	private Integer delFlag;

    @ApiModelProperty(value = "createBy")
	private String createBy;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTime")
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;


    @ApiModelProperty(value = "updateBy")
	private String updateBy;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "updateTime")
	@TableField(fill = FieldFill.UPDATE)
	private Date updateTime;

    @ApiModelProperty(value = "备注")
	private String remark;
}
