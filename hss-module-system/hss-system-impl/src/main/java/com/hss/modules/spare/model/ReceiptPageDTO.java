package com.hss.modules.spare.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @ClassDescription: 入库单分页查询参数
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/26 10:32
 */
@Data
@ApiModel("入库单分页查询参数")
public class ReceiptPageDTO {

    @ApiModelProperty(value = "入库类型")
    private Integer orderType;

    @ApiModelProperty(value = "单号")
    private String orderNo;

    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "入库日期开始")
    private LocalDate receiptDateStart;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "入库日期结束")
    private LocalDate receiptDateEnd;

    private Integer pageNo;
    private Integer pageSize;


}
