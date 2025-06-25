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
import java.time.LocalDate;
import java.util.Date;

/**
* @ClassDescription:  盘库单
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 15:40
*/
@Data
@TableName("BP_INVENTORY_CHECK")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="盘库单", description="盘库单")
public class InventoryCheckEntity {
    

	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private String id;

    @ApiModelProperty(value = "单号")
	private String checkNo;

    @ApiModelProperty(value = "盈亏数")
	private BigDecimal checkTotal;

    @ApiModelProperty(value = "盘点人")
	private String useName;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "盘点日期")
	private LocalDate checkDate;

    @ApiModelProperty(value = "备注")
	private String remark;

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

	@TableField(fill = FieldFill.UPDATE)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "updateTime")
	private Date updateTime;
}
