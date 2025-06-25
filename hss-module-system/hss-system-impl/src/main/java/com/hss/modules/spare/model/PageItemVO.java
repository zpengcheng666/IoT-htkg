package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassDescription: 物料列表分页查询数据
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/26 14:01
 */
@Data
@ApiModel("物料列表分页查询数据")
public class PageItemVO {

    @ApiModelProperty("物料id")
    private String itemId;
    @ApiModelProperty("物料名称")
    private String itemName;
    @ApiModelProperty("物料编码")
    private String itemNo;
    @ApiModelProperty("物料类型")
    private String itemTypeName;
    @ApiModelProperty("仓库")
    private String warehouseName;

    @ApiModelProperty("仓库Id")
    private String warehouseId;

    @ApiModelProperty("库区Id")
    private String areaId;



    @ApiModelProperty("数量")
    private BigDecimal quantity;

}
