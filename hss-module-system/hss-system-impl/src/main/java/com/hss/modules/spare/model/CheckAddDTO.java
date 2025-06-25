package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassDescription: 入库参数
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/25 16:46
 */
@Data
@ApiModel("盘库参数")
public class CheckAddDTO {

    @ApiModelProperty(value = "单号")
    private String orderNo;

    @ApiModelProperty(value = "负责人")
    private String useName;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "物料明细信息")
    private List<CheckAddItem> items;
}
