package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据表格返回参数
 * @author hd
 */
@Data
@ApiModel("数据表格返回数据设备属性信息")
public class DataTableVODeviceAttr {


    @ApiModelProperty("属性名称")
    private String name;

    @ApiModelProperty("属性英文名称")
    private String enName;
}
