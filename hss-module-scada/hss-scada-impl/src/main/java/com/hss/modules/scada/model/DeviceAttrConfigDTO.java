package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 保存点位关联
 * @author hd
 */
@Data
@ApiModel("保存设备属性的配置信息")
public class DeviceAttrConfigDTO {
    @ApiModelProperty("设备id")
    private String deviceId;

    @ApiModelProperty("属性列表")
    private List<DeviceAttrConfigDTOItem> attrList;
}
