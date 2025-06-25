package com.hss.modules.store.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 13604
 */
@Data
@ApiModel(value="线性统计Vo", description="线性统计Vo")
public class LineStateVO {

    /**
     * 每组x轴
     */
    @ApiModelProperty("x轴")
    private List<String> xAxis;

    /**
     * 分组
     */
    @ApiModelProperty("标题")
    private List<String > legend;

    /**
     * 第一个list是分组
     * 第二个list是每组的数据
     */
    @ApiModelProperty("数据")
    private List<List<BigDecimal>> series;
}
