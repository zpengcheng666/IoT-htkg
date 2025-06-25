package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备属性和点位关联信息
 * @author hd
 *
 */
@Data
@ApiModel("设备点位绑定")
public class DeviceAttrPointRelationItem {

    @ApiModelProperty("属性id")
    private String attrId;

    @ApiModelProperty("点位id")
    private String pointId;

    @ApiModelProperty("表达式")
    private String expression;

}
