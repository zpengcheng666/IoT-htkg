package com.hss.modules.spare.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* @ClassDescription: 入库单详情
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 15:55
*/
@Data
@TableName("BP_RECEIPT_ORDER_DETAIL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="入库单详情", description="入库单详情")
public class ReceiptOrderDetailEntity {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;

    @ApiModelProperty(value = "入库单id")
	private java.lang.String receiptOrderId;

    @ApiModelProperty(value = "物料id")
	private java.lang.String itemId;

	@ApiModelProperty(value = "仓库id")
	private String warehouseId;


	@ApiModelProperty(value = "库区id")
	private String areaId;

    @ApiModelProperty(value = "数量")
	private java.math.BigDecimal quantity;

    @ApiModelProperty(value = "delFlag")
	private java.lang.Integer delFlag;

    @ApiModelProperty(value = "createBy")
	private java.lang.String createBy;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTime")
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

    @ApiModelProperty(value = "updateBy")
	private java.lang.String updateBy;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "updateTime")
	@TableField(fill = FieldFill.UPDATE)
	private Date updateTime;
}
