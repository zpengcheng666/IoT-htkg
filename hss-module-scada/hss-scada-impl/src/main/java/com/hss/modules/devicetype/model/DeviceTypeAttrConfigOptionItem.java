package com.hss.modules.devicetype.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hd
 */
@Data
@ApiModel("设配属性配置项")
public class DeviceTypeAttrConfigOptionItem {

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("值")
    private String value;
}
