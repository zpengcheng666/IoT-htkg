package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassDescription: 入库单详情物料信息
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/26 11:00
 */
@ApiModel("入库单详情物料信息")
@Data
public class ReceiptDetailItem {

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "物料编号")
    private String itemNo;

    @ApiModelProperty(value = "物料类型")
    private String itemTypeName;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "库房")
    private String area;


}
