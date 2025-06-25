package com.hss.modules.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
* @description: 发布消息
* @author zpc
* @date 2024/3/21 10:02
* @version 1.0
*/
@Data
@ApiModel(value="发布消息", description="发布消息")
public class PublishDutyShiftMseeateDTO {
    @ApiModelProperty(value = "终端ID列表")
    private List<String> terminalIds;
}
