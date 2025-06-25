package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("设备模型")
public class DeviceModel {

    @ApiModelProperty("设备Id")
    private String deviceId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty("设备编号")
    private String deviceCode;

    @ApiModelProperty("所在位置")
    private String locationName;

    //2023-11-04修改，增加别名
    @ApiModelProperty("设备别名")
    private String otherName;

    @ApiModelProperty("设备属性列表")
    private List<DeviceAttrModel> attrs;

}
