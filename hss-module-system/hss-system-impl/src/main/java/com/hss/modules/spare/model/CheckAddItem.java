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
@ApiModel("盘库参数物料信息")
public class CheckAddItem extends ReceiptAddItem {

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "盘点数量")
    private BigDecimal checkQuantity;

}
