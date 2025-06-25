package com.hss.modules.alarm.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 确认报警触发联动
 * @author hd
 */
@Data
@ApiModel("确认报警触发联动")
public class AlarmAckLinkage {

    @ApiModelProperty("联动id")
    private String id;
    @ApiModelProperty("是否联动")
    private Boolean act;
}
