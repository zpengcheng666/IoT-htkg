package com.hss.modules.ventilation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @description: 方案列表
* @author zpc
* @date 2024/3/20 10:29
* @version 1.0
*/
@Data
@ApiModel("方案列表")
public class PlanListVO {

    /**
     * 方案id
     */
    @ApiModelProperty("方案id")
    private String id;

    /**
     * 方案名称
     */
    @ApiModelProperty("方案名称")
    private String name;

    /**
     * 方案编号
     */
    @ApiModelProperty("方案编号")
    private String code;
}
