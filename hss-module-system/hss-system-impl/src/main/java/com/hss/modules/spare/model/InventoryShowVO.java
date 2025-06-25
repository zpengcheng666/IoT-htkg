package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassDescription: 库存看板
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/26 14:01
 */
@Data
@ApiModel("库存看板")
public class InventoryShowVO {

    @ApiModelProperty("仓库")
    private String warehouseName;
    private String warehouseId;
    private Integer warehouseCount;
    @ApiModelProperty("库区")
    private String areaName;
    private String areaId;
    private Integer areaCount;
    @ApiModelProperty("物料类型")
    private String itemType;

    @ApiModelProperty("物料编码")
    private String itemNo;
    @ApiModelProperty("物料名称")
    private String itemName;
    private String itemId;
    private Integer itemCount;
    @ApiModelProperty("数量")
    private BigDecimal quantity;

}
