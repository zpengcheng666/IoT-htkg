package com.hss.modules.facility.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/2/27 9:02
 */
@Data
@ApiModel("设备运行日志查询")
public class DeviceRunLogDTO {

    @ApiModelProperty("设备类型id")
    private String type;
    @ApiModelProperty("设备名称")
    private String name;
    @ApiModelProperty("设备id")
    private String devId;

    private Integer pageNo;

    private Integer pageSize;
}
