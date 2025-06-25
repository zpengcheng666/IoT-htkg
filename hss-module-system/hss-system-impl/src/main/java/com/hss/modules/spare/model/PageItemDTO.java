package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassDescription: 查询物料信息
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/26 13:54
 */
@Data
@ApiModel("查询物料信息")
public class PageItemDTO {
    @ApiModelProperty("物料名称")
    private String itemName;

    @ApiModelProperty("物料类型id")
    private String itemTypeId;

    @ApiModelProperty("仓库id")
    private String warehouseId;

    @ApiModelProperty("库区id")
    private String areaId;

    private Integer pageNo;
    private Integer pageSize;




}
