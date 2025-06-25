package com.hss.modules.message.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassDescription: 卫星临空vo
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/8 16:50
 */
@Data
@ApiModel("卫星临空数据vo")
public class PublishSatelliteSecureVO {
    @ApiModelProperty(value = "ID")
    private String id;

    @Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**publishTime*/
    @ApiModelProperty(value = "发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "消息状态(0：未发布，1：已发布，2：发布中，3：已过期)")
    private Integer state;

    @ApiModelProperty(value = "安全时长")
    @Excel(name = "安全时长", width = 15)
    private String secure;

    private List<String> terminalIds;
}
