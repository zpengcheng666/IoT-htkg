package com.hss.modules.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
* @description: 发布消息dto
* @author zpc
* @date 2024/3/21 10:03
* @version 1.0
*/
@Data
@ApiModel(value="发布消息dto", description="发布消息dto")
public class PublishMessageDTO {
    @ApiModelProperty(value = "消息id")
    private String messageId;

    @ApiModelProperty(value = "终端ID列表")
    private List<String> terminalIds;

}
