package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 供应商option
 * @author wuyihan
 * @date 2024/4/26 13:16
 * @version 1.0
 */
@Data
public class CarrierModel implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "编号")
    private String carrierNo;
    @ApiModelProperty(value = "名称")
    private String carrierName;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "座机号")
    private String tel;
    @ApiModelProperty(value = "联系人")
    private String contact;
    @ApiModelProperty(value = "职位")
    private String level;
    @ApiModelProperty(value = "电子邮箱")
    private String email;
    @ApiModelProperty(value = "备注")
    private String remark;

}
