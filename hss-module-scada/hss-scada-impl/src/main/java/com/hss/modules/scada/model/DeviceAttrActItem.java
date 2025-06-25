package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hd
 */
@Data
@ApiModel("动作项")
public class DeviceAttrActItem {

    @ApiModelProperty("动作名称")
    private String name;
    @ApiModelProperty("动作值")
    private String order;
}
