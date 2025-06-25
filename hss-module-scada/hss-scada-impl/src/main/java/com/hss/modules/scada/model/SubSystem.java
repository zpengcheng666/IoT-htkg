package com.hss.modules.scada.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "子系统")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubSystem {

    @ApiModelProperty("子系统Id")
    private String id;

    @ApiModelProperty("子系统Name")
    private String name;

    @ApiModelProperty("子系统包含的子模块s")
    private List<SubModule> subModules;
}
