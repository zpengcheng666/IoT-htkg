package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author hd
 */
@Data
@ApiModel("环境实时数据表格数据")
public class EnvDeviceTableVODevice {

    @ApiModelProperty("设备id")
    private String id;

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty("别名")
    private String otherName;

    private Map<String, String> data;




}
