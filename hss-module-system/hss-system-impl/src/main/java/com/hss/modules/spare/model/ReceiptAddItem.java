package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassDescription: 入库参数
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/25 16:46
 */
@Data
@ApiModel("出入库参数物料信息")
public class ReceiptAddItem {

    @ApiModelProperty(value = "物料id")
    private String itemId;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "库房id")
    private String warehouseId;

    @ApiModelProperty(value = "库区id")
    private String areaId;
}
