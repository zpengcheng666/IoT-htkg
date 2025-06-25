package com.hss.modules.alarm.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
* @description: 报警数据统计
* @author zpc
* @date 2024/3/20 15:09
* @version 1.0
*/
@Data
@ApiModel(value="线性统计Vo", description="线性统计Vo")
public class LineStateVO {

    @ApiModelProperty("x轴")
    private List<String> xAxis;

    @ApiModelProperty("标题")
    private List<String > legend;

    @ApiModelProperty("数据")
    private List<List<BigDecimal>> series;
}
