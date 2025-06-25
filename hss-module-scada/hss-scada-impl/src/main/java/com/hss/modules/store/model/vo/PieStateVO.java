package com.hss.modules.store.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 13604
 */
@Data
@ApiModel(value="饼图统计Vo", description="饼图统计Vo")
public class PieStateVO {

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("值")
    private BigDecimal value;
    @ApiModelProperty("百分比")
    private BigDecimal ratio;
}
