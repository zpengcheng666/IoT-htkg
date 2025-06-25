package com.hss.modules.system.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @description: 终端表单
* @author zpc
* @date 2024/3/21 13:38
* @version 1.0
*/
@Data
public class  TerminalModel implements java.io.Serializable{
    @ApiModelProperty("终端id")
    private String terminalId;

    @ApiModelProperty("终端名称")
    private String name;

    @ApiModelProperty("编号")
    private String code;

    @ApiModelProperty("")
    private String locationId;

    @ApiModelProperty(value = "背景颜色")
    private String backgroundColor;

    @ApiModelProperty(value = "背景图片")
    private String backgroundImg;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "终端信息")
    private TerminalInfoModel[] infoList;

    @ApiModelProperty(value = "安检门,0不显示，1显示")
    private Integer ajm;

    @ApiModelProperty(value = "报警状态,0不报警，1报警")
    private Integer alarmStatus;

    @ApiModelProperty(value = "应急处置：0不启动，1启动")
    private Integer yjcz;

    @ApiModelProperty(value = "门禁：0不选择，1选择")
    private Integer mj;

    @ApiModelProperty(value = "门Id")
    private String doorId;

    @ApiModelProperty(value = "安检门id")
    private String checkDoorId;

    @ApiModelProperty(value = "报警级别")
    private String alarmLevel;

}
