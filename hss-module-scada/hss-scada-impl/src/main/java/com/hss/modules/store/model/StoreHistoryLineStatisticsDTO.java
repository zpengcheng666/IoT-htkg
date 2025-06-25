package com.hss.modules.store.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author hd
 */
@Data
@ApiModel("历史曲线请求参数")
public class StoreHistoryLineStatisticsDTO {

    @ApiModelProperty(value = "属性ids", required = true)
    private List<String> attrIds;

    @ApiModelProperty(value = "统计开始时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty(value = "统计结束时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
