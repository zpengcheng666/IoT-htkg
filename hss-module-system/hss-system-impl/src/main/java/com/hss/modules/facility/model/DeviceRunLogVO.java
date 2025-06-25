package com.hss.modules.facility.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/2/27 9:07
 */
@Data
@ApiModel("设备运行记录分页查询")
public class DeviceRunLogVO {

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("设备id")
    private String devId;
    @ApiModelProperty("设备名称")
    @Excel(name = "设备名称", width = 20)
    private String devId_disp;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("运行开始时间")
    @Excel(name = "最后启动时间", width = 20, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("运行结束时间")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后停止时间", width = 20, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date stopTime;

    @ApiModelProperty("本次运行时间")
    @Excel(name = "最后运行时长", width = 30)
    private String duration;

    @ApiModelProperty("总运行时间")
    @Excel(name = "累计运行时长", width = 30)
    private String totalDuration;

    @ApiModelProperty("描述信息")
    @Excel(name = "备注", width = 20)
    private String remark;
}
