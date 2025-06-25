package com.hss.modules.devicetype.model;

import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * by组态查询设备属性列表
 * 返回参数
 * @author hd
 */
@Data
@ApiModel("by组态查询设备属性")
public class DeviceTypeAttributeSceneListByType {

    @ApiModelProperty("点位属性")
    private List<DeviceTypeAttribute> pointList;

    @ApiModelProperty("配置属性")
    private List<DeviceTypeAttribute> configList;

}
