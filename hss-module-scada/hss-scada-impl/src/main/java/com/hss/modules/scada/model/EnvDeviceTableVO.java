package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author hd
 */
@Data
@ApiModel("环境实时数据表格数据")
public class EnvDeviceTableVO {


    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("分组数量")
    private Integer group;

    @ApiModelProperty("属性列表")
    private List<EnvDeviceTableVOAttr> attrList;



    @ApiModelProperty("设备列表")
    private List<EnvDeviceTableVODevice> deviceList;

}
