package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hd
 */
@Data
@ApiModel("设备排序列表")
public class DeviceSortVO {

    @ApiModelProperty("中间表id")
    private String id;

    @ApiModelProperty("设备名称")
    private String name;
    @ApiModelProperty("序号")
    private Integer sortNumber;

    @ApiModelProperty(value = "设备id", hidden = true)
    private String deviceId;
}
