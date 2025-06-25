package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassDescription: 盘库单详情物料信息
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/26 11:00
 */
@ApiModel("盘库单详情物料信息")
@Data
public class CheckDetailItem {

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "物料编号")
    private String itemNo;

    @ApiModelProperty(value = "物料类型")
    private String itemTypeName;

    @ApiModelProperty(value = "账面数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "盘库数量")
    private BigDecimal checkQuantity;
    @ApiModelProperty(value = "变化量")
    private BigDecimal changeQuantity;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "库房")
    private String area;




}
