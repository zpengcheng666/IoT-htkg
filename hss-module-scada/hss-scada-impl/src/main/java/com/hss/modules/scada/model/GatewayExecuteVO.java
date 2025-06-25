package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 请求网关返回参数
 * @author hd
 */
@Data
@ApiModel("请求网关返回参数")
public class GatewayExecuteVO {

    @ApiModelProperty("功能码")
    private String code;
    @ApiModelProperty("msg")
    private String msg;
}
