package com.hss.modules.system.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
* @description: 分布区间统计
* @author zpc
* @date 2024/3/21 13:36
* @version 1.0
*/
@Data
public class StatisticsSectionModel implements java.io.Serializable{

    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @ApiModelProperty(value = "属性英文名称")
    private String attrEnName;

    @ApiModelProperty(value = "设备类别id")
    private String devClass;

    @ApiModelProperty(value = "设备属性")
    private String attrId;

    @ApiModelProperty(value = "personList值展示表达式")
    private List<String> intervalValueList;

    @ApiModelProperty(value = "idList")
    private List<String> idList;
}
