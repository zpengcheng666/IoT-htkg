package com.hss.modules.scada.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("子系统下的子模块")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubModule {

    @ApiModelProperty("子模块的ID")
    private String id;

    @ApiModelProperty("子模块的名字")
    private String name;

    @ApiModelProperty("子模块所属子系统")
    private SubSystem subSystem;
}
