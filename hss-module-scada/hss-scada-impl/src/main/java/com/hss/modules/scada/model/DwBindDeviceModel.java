package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DwBindDeviceModel implements java.io.Serializable{

    @ApiModelProperty("网关Id")
    private String wgId;



    @ApiModelProperty("设备Id")
    private String deviceId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("图标id")
    private String iconid;

    @ApiModelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty("位置")
    private SceneDeviceLocation location;

    @ApiModelProperty("设备属性列表")
    private List<AttrModel> attrs;

    @Data
    @ApiModel("设备属性")
    public static final class AttrModel implements java.io.Serializable{

        @ApiModelProperty("变量Id")
        private String variableId;

        @ApiModelProperty("变量名称")
        private String variableName;

        @ApiModelProperty("属性名称")
        private String attrName;

        @ApiModelProperty("属性名称英文")
        private String attrEnName;

        @ApiModelProperty("属性值")
        private String initValue;

    }
}
