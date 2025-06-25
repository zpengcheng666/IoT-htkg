package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 保存点位关联
 * @author hd
 */
@Data
public class DeviceAttrRelationSave {
    @ApiModelProperty("设备id")
    private String deviceId;
    @ApiModelProperty("关联类型：point：点位关联,variable:变量关联")
    private String relationType;

    @ApiModelProperty("关联列表")
    private List<DeviceAttrRelation> deviceAttrRelationList;
}
