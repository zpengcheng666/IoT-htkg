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
public class DeviceAttrRelation {

    /**
     * 点位id
     */
    @ApiModelProperty("点位id")
    private String variableId;

    /**
     * 设备上的属性名称
     */
    @ApiModelProperty("网关属性名称")
    private String gatewayAttrName;

    /**
     * 场景中的属性属性名称
     */
    @ApiModelProperty("属性名称")
    private String sceneAttrName;

    /**
     * 场景中属性英文名称
     */
    @ApiModelProperty("场景属性英文名称")
    private String sceneAttrEnName;

    @ApiModelProperty("属性id")
    private String relationId;

    @ApiModelProperty("表达式")
    private String varExpression;

    @ApiModelProperty("表达式描述")
    private String varExpressionCh;

    @ApiModelProperty("关联类型：point：点位关联,variable:变量关联")
    private String relationType;






}
