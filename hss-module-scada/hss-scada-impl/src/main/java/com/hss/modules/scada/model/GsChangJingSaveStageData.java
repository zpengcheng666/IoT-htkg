package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保存场景数据
 * @author hd
 */
@Data
@ApiModel("场景保存参数")
public class GsChangJingSaveStageData {

    @ApiModelProperty("场景画面JSON")
    private String stageDatajson;
    @ApiModelProperty("该场景绑定的所有设备（数据点）的编号，")
    private String dataKeyArray;
    @ApiModelProperty("画面的图片 base64格式  可作为画面的缩略图")
    private String stageBase64;
    @ApiModelProperty("点位绑定属性")
    private String bindArr;
    @ApiModelProperty("场景ID")
    private String stageId;
    @ApiModelProperty("场景名称")
    private String name;
    @ApiModelProperty("场景描述")
    private String description;

    @ApiModelProperty("子系统ID")
    private String subsystemId;

    @ApiModelProperty("模块id")
    private String moduleId;

}
