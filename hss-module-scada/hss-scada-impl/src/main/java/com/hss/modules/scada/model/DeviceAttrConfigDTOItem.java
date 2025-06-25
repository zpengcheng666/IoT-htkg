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
public class DeviceAttrConfigDTOItem {
    @ApiModelProperty("属性id")
    private String id;

    @ApiModelProperty("属性英文名称")
    private String enName;

    @ApiModelProperty("值")
    private String value;


}
