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
@ApiModel("点位关联")
public class DeviceAttrPointRelationVO {

    @ApiModelProperty("属性id")
    private String attrId;

    @ApiModelProperty("属性名称")
    private String attrName;

    @ApiModelProperty("网关id")
    private String gatewayId;

    @ApiModelProperty("设备id")
    private String deviceId;


    @ApiModelProperty("点位id")
    private String pointId;


    @ApiModelProperty("表达式")
    private String expression;

    @ApiModelProperty("表达式字符串")
    private String expressionStr;









}
