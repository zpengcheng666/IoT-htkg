package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author hd
 */
@Data
@ApiModel("动作")
public class DeviceAttrAct {

    @ApiModelProperty("动作名称")
    private String name;
    @ApiModelProperty("英文名称")
    private String enName;
    @ApiModelProperty("动作列表")
    private List<DeviceAttrActItem> orderList;
}
