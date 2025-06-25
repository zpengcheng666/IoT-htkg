package com.hss.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Description: 卫星临空信息
 * @Author: zpc
 * @Date:   2022-12-07
 * @Version: V1.0
 */
@Data
@TableName("T_PUBLISH_SATELLITE_NEW")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="卫星临空信息")
public class PublishSatellite {


    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;

    @Excel(name = "卫星名称", width = 15)
    @ApiModelProperty(value = "卫星名称")
    private String name;

    @Excel(name = "卫星编号", width = 15)
    @ApiModelProperty(value = "卫星编号")
    private String code;

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

    @ApiModelProperty(value = "离开时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime leaveTime;

    /**publishTime*/
    @ApiModelProperty(value = "发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "消息状态(0：未发布，1：已发布，2：发布中，3：已过期)")
    private Integer state;

    @ApiModelProperty(value = "过境时长")
    @Excel(name = "过境时长", width = 15)
    private String period;

}
