package com.hss.modules.system.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
/**
* @description: 终端详情
* @author zpc
* @date 2024/3/21 13:37
* @version 1.0
*/
@Data
public class TerminalInfoModel implements java.io.Serializable{
    /**信息类型*/
    @Excel(name = "信息类型", width = 15)
    @ApiModelProperty(value = "信息类型")
    private java.lang.String infoType;

    @ApiModelProperty(value = "")
    private boolean moved;

    @ApiModelProperty(value = "终端名称")
    private java.lang.String name;

    @ApiModelProperty("x轴")
    private Integer x;

    @ApiModelProperty("y轴")
    private Integer y;

    @ApiModelProperty("宽")
    private Integer w;

    @ApiModelProperty("高")
    private Integer h;

    @ApiModelProperty("id")
    private Integer i;
}
