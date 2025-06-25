package com.hss.modules.alarm.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("报警批量处理参数")
public class AlarmHistoryHandlerDTO {
    @ApiModelProperty("ids")
    private List<String> ids;

    @ApiModelProperty("处理人")
    private String handler;

    @ApiModelProperty("处理意见")
    private String handleMethod;

}
