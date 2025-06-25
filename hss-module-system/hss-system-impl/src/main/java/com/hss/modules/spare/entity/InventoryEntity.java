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
* @ClassDescription: 库存
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 15:43
*/
@Data
@TableName("BP_INVENTORY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="库存", description="库存")
public class InventoryEntity {
    

	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private String id;

    @ApiModelProperty(value = "物料id")
	private String itemId;

	@ApiModelProperty(value = "数量")
	private BigDecimal quantity;

    @ApiModelProperty(value = "描述")
	private String remark;


    @ApiModelProperty(value = "仓库id")
	private String warehouseId;

    @ApiModelProperty(value = "库区id")
	private String areaId;

    @ApiModelProperty(value = "删除标记")
	private Integer delFlag;

    @ApiModelProperty(value = "创建人")
	private String createBy;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

    @ApiModelProperty(value = "更新人")
	private String updateBy;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
	@TableField(fill = FieldFill.UPDATE)
	private Date updateTime;


    @ApiModelProperty(value = "版本号")
	private Integer version;



}
