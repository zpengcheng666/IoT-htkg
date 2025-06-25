package com.hss.modules.message.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 值班信息
 */
@Data
@ApiModel("终端值班信息")
public class DutyTerminalInfoVO {

    @ApiModelProperty("值班安排")
    private String dutyName;
    @ApiModelProperty("人名")
    private String name;
    @ApiModelProperty("岗位")
    private String dutyPost;
    @ApiModelProperty("时间段")
    private String dutySjd;


}
