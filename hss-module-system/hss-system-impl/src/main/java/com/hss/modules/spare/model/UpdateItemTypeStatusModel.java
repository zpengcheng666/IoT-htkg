package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改物料类型状态 model
 *
 * @author wuyihan
 */
@Data
public class UpdateItemTypeStatusModel {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("状态")
    private Integer status;
}
