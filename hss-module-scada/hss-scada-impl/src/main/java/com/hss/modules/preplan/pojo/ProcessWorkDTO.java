package com.hss.modules.preplan.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 预案处理
 * @author hd
 */
@Data
@ApiModel("预案处理请求参数")
public class ProcessWorkDTO {

    @ApiModelProperty("工作id")
    private String workId;
}
