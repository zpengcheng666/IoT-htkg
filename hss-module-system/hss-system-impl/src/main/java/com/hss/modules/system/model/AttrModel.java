package com.hss.modules.system.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
* @description: 设备属性
* @author zpc
* @date 2024/3/21 13:30
* @version 1.0
*/
@Data
public class AttrModel {
    @ApiModelProperty("classId 设备类别Id列表")
    private String classId;

    @ApiModelProperty("设备属性id")
    private String attrId;
}
