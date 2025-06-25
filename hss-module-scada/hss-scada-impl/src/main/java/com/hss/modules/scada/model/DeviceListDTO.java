package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备列表请求参数
 * @author hd
 */
@ApiModel("设备列表请求参数")
@Data
public class DeviceListDTO {
    @ApiModelProperty("设备类型id")
    private String deviceTypeId;

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty(value = "设备编码")
    private String code;

    @ApiModelProperty(value = "场景id")
    private String sceneId;

    @ApiModelProperty(value = "pageNo")
    private Integer pageNo;
    @ApiModelProperty(value = "pageSize")
    private Integer pageSize;
}
