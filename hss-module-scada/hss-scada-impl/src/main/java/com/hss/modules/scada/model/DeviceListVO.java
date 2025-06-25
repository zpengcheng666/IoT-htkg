package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

/**
 * 谁列表返回参数
 * @author hd
 */
@ApiModel("设备列表返回参数")
@Data
public class DeviceListVO {
    @ApiModelProperty("设备id")
    private String id;

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty(value = "设备编码")
    private String code;

    @ApiModelProperty(value = "设备类型")
    private String type;

    @ApiModelProperty(value = "所属子系统")
    private Collection<String> subsystemList;

    @ApiModelProperty(value = "位置")
    private String locationId;

    @ApiModelProperty(value = "场景列表")
    private Collection<String> sceneList;

    //2023-11-04修改，新增别名
    @ApiModelProperty("别名")
    private String otherName;
}
