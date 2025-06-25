package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("场景发布Model")
@Data
public class ScenePublishModel implements java.io.Serializable{

    @ApiModelProperty("场景ID")
    private String id;

    @ApiModelProperty("发布状态， 1：发布；0：取消发布")
    private String status;
}
