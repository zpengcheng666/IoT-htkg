package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hd
 */
@ApiModel("站点状态切换参数")
@Data
public class SiteStateUpdateDTO {

    @ApiModelProperty("站点id")
    private String siteId;

    @ApiModelProperty("站点状态")
    private Integer siteState;
}
