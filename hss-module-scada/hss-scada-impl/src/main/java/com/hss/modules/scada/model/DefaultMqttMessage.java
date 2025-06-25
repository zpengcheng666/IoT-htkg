package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Map;

/**
* @description: mqtt 默认消息格式
* @author zpc
* @date 2024/3/26 10:23
* @version 1.0
*/
@Data
@ApiModel("mqtt 默认消息格式")
public class DefaultMqttMessage implements java.io.Serializable{

    @ApiModelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty("设备Id")
    private String deviceId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("变量Id")
    private String variableId;

    @ApiModelProperty("变量名称")
    private String variableName;

    @ApiModelProperty("变量值")
    private String value;

    @ApiModelProperty("原始数据")
    private String OriValue;

    @ApiModelProperty("变量值的数据类型")
    private String dataType;

    @ApiModelProperty("记录时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date recordTime;

    @ApiModelProperty("扩展字段")
    private Map<String, Object> appendixes;
}
