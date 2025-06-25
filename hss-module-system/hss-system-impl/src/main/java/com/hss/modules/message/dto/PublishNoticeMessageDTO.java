package com.hss.modules.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
* @description: 通知
* @author zpc
* @date 2024/3/21 10:01
* @version 1.0
*/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value="发布通知公告消息dto", description="发布通知公告消息dto")
public class PublishNoticeMessageDTO extends PublishMessageDTO{
    @ApiModelProperty(value = "预计发布时间，根据发布类型选择")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date effectiveTime;
    /**overdueTime*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "过期时间")
    private java.util.Date overdueTime;
    @ApiModelProperty(value = "发布人")
    private java.lang.String publisher;
    @ApiModelProperty(value = "发布类型(reserve:预约发布，immediate:立即发布)")
    private java.lang.String publishType;


}
