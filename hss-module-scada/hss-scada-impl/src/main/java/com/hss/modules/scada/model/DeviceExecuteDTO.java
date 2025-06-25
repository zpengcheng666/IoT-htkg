package com.hss.modules.scada.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备总做请求参数
 * @author hd
 */
@Data
@ApiModel("设备动作请求参数")
public class DeviceExecuteDTO {

    @ApiModelProperty("设备动作")
    private String deviceId;

    @ApiModelProperty("属性英文名称")
    private String attrEnName;

    @ApiModelProperty("值")
    private String value;
}
