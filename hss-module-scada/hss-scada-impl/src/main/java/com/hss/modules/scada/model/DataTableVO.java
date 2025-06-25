package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 数据表格返回参数
 * @author hd
 */
@Data
@ApiModel("数据表格返回数据")
public class DataTableVO {

    @ApiModelProperty("设备类型id")
    private String deviceTypeId;

    @ApiModelProperty("设备类型名称")
    private String deviceTypeName;

    @ApiModelProperty("设备列表")
    private List<DataTableVODevice> deviceList;
}
