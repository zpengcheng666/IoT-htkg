package com.hss.modules.store.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author hd
 */
@Data
@ApiModel("环境子系统和规律统计请求参数")
public class EnvGoodRatioDTO {

    @ApiModelProperty(value = "属性id", required = true)
    private String attrId;

    @ApiModelProperty(value = "统计开始时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "统计结束时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
