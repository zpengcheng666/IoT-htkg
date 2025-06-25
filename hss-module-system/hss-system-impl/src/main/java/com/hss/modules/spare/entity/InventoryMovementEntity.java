package com.hss.modules.spare.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

/**
* @ClassDescription: 移库单
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 15:53
*/
@Data
@TableName("BP_INVENTORY_MOVEMENT")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="移库单", description="移库单")
public class InventoryMovementEntity {
    

	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;

    @ApiModelProperty(value = "单号")
	private java.lang.String orderNo;

    @ApiModelProperty(value = "负责人")
	private java.lang.String useName;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期")
	private LocalDate movementDate;

    @ApiModelProperty(value = "备注")
	private java.lang.String remark;

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
