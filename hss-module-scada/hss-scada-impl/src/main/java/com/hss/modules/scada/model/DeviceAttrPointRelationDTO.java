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
@ApiModel("保存点位关联")
public class DeviceAttrPointRelationDTO {
    @ApiModelProperty("设备id")
    private String deviceId;

    @ApiModelProperty("关联列表")
    private List<DeviceAttrPointRelationItem> attrList;
}
