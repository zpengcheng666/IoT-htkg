package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassDescription: 库存看板参数
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/26 13:54
 */
@Data
@ApiModel("库存看板参数")
public class InventoryShowDTO {
    @ApiModelProperty("物料id")
    private String itemId;

    @ApiModelProperty("仓库id")
    private String warehouseId;

    @ApiModelProperty("库区id")
    private String areaId;

    private Integer pageNo;
    private Integer pageSize;




}
