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
@ApiModel("数据表格返回数据设备信息")
public class DataTableVODevice {

    @ApiModelProperty("设备id")
    private String id;

    @ApiModelProperty("设备名称")
    private String name;


    @ApiModelProperty("属性列表")
    private List<DataTableVODeviceAttr> attrList;
}
