package com.hss.modules.scada.ws.terminal;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassDescription: 安全时段数据
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/5/17 13:16
 */
@Data
public class SecureMessage {
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;


    @ApiModelProperty(value = "安全时长")
    private String secure;
}
