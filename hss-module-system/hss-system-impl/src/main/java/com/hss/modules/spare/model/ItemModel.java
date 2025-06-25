package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 物料option
 * @author wuyihan
 * @date 2024/5/13 14:53
 * @version 1.0
 */
@Data
public class ItemModel implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "编号")
    private String itemNo;
    @ApiModelProperty(value = "名称")
    private String itemName;
    @ApiModelProperty(value = "规格")
    private String specification;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "分类id")
    private String itemType;
    @ApiModelProperty(value = "数量单位")
    private String unit;
    @ApiModelProperty(value = "安全库存")
    private BigDecimal quantity;
    @ApiModelProperty(value = "货架id")
    private String rackId;
    @ApiModelProperty(value = "库区id")
    private String areaId;
    @ApiModelProperty(value = "仓库id")
    private String warehouseId;
    @ApiModelProperty(value = "生产日期")
    private Date productionDate;
    @ApiModelProperty(value = "有效期")
    private Date expiryDate;
    @ApiModelProperty(value = "批次")
    private String batch;
    @ApiModelProperty(value = "状态")
    private Integer status;

}
