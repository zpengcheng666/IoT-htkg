package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassDescription: 查询库存记录参数
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/28 14:00
 */
@Data
@ApiModel("查询库存记录参数")
public class ItemOptDTO {


    @ApiModelProperty("操作类型")
    private Integer optType;

    @ApiModelProperty("物料id")
    private String itemId;

    @ApiModelProperty("仓库id")
    private String warehouseId;

    @ApiModelProperty("库区id")
    private String areaId;


    private Integer pageNo;
    private Integer pageSize;

}
