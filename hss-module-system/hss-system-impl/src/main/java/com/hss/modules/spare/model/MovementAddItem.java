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
@ApiModel("移库参数物料信息")
public class MovementAddItem {

    @ApiModelProperty(value = "物料id")
    private String itemId;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "原始库房id")
    private String sourceWarehouseId;

    @ApiModelProperty(value = "原始库区id")
    private String sourceAreaId;

    @ApiModelProperty(value = "目标库房id")
    private String targetWarehouseId;

    @ApiModelProperty(value = "目标库区id")
    private String targetAreaId;
}
