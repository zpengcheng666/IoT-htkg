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
* @ClassDescription: 库存记录
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 15:49
*/
@Data
@TableName("BP_INVENTORY_HISTORY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="库存记录", description="库存记录")
public class InventoryHistoryEntity {
    

	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private String id;

    @ApiModelProperty(value = "单据id")
	private String formId;

    @ApiModelProperty(value = "单据类型")
	private Integer formType;

    @ApiModelProperty(value = "描述信息")
	private String remark;


    @ApiModelProperty(value = "仓库id")
	private String warehouseId;


    @ApiModelProperty(value = "库区id")
	private String areaId;

    @ApiModelProperty(value = "物料id")
	private String itemId;

	@ApiModelProperty(value = "操作数量")
	private BigDecimal quantity;

    @ApiModelProperty(value = "delFlag")
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

}
