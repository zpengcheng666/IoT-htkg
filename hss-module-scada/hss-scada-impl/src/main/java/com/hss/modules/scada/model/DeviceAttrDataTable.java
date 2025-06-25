package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author hd
 */
@Data
@ApiModel("数据表格属性")
public class DeviceAttrDataTable {

    @ApiModelProperty("属性名称")
    private String name;
    @ApiModelProperty("英文名称")
    private String enName;
    @ApiModelProperty("值映射")
    private Map<String,String> valueMap;
}
