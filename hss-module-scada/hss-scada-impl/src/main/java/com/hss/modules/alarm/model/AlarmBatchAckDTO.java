package com.hss.modules.alarm.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 确认报警请求参数
 * @author hd
 */
@Data
@ApiModel("批量确认报警请求参数")
public class AlarmBatchAckDTO {

    @ApiModelProperty("id列表")
    private List<String> ids;
    @ApiModelProperty("报警类型")
    private String type;
    @ApiModelProperty("操作人")
    private String actOperateName;
    @ApiModelProperty("立即处理")
    private Integer isHandle;
    @ApiModelProperty("处理人")
    private String handler;
    @ApiModelProperty("处理内容提")
    private String handlerContents;

    @ApiModelProperty("触发联动列表")
    private List<AlarmAckLinkage> linkageStrategyList;
}
