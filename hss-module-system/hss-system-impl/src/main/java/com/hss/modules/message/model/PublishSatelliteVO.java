package com.hss.modules.message.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class PublishSatelliteVO {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;

    @Excel(name = "卫星名称", width = 15)
    @ApiModelProperty(value = "卫星名称")
    private String name;

    @Excel(name = "卫星编号", width = 15)
    @ApiModelProperty(value = "卫星编号")
    private String code;

    @Excel(name = "卫星国别", width = 15)
    @ApiModelProperty(value = "卫星国别")
    private String nationality;

    @Excel(name = "卫星类别", width = 15)
    @ApiModelProperty(value = "卫星类别")
    private String satelliteType;


    @Excel(name = "进入时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "进入时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enterTime;

    @Excel(name = "离开时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "离开时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime leaveTime;

    /**publishTime*/
    @ApiModelProperty(value = "发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "消息状态(0：未发布，1：已发布，2：发布中，3：已过期)")
    private Integer state;

    @ApiModelProperty(value = "过境时长")
    @Excel(name = "过境时长(分钟)", width = 15)
    private String period;

    private List<String> terminalIds;
}
