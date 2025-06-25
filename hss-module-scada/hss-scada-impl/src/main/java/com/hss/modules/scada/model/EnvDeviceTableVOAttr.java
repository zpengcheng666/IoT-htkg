package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hd
 */
@Data
@ApiModel("环境实时数据表格数据")
public class EnvDeviceTableVOAttr {

    @ApiModelProperty("属性名称")
    private String name;

    @ApiModelProperty("英文名称")
    private String enName;

    @ApiModelProperty("报警英文名称")
    private String alarmEnName;

    @ApiModelProperty("高高报警英文名称")
    private String hhEnName;
    @ApiModelProperty("低低报警英文名称")
    private String llEnName;




}
