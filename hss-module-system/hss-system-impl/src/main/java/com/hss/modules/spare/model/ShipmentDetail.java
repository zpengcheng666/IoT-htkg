package com.hss.modules.spare.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * @ClassDescription: 入库订单详情
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/26 10:57
 */
@ApiModel("出库单详情")
@Data
public class ShipmentDetail {
    private String id;

    @ApiModelProperty(value = "单号")
    private String orderNo;

    @ApiModelProperty(value = "类型")
    private String orderType;


    @ApiModelProperty(value = "负责人")
    private String useName;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出库日期")
    private LocalDate shipmentDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "物料信息")
    private List<ReceiptDetailItem> items;
}
