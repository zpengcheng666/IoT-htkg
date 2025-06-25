package com.hss.modules.scada.model;

import com.hss.modules.devicetype.model.DeviceTypeAttrConfigOptionItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Data
@ApiModel("设备属性模型")
public class DeviceAttrModel {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("英文名称")
    private String enName;

    @ApiModelProperty("数据类型")
    private String dataType;

    @ApiModelProperty(value = "展示区域:popover 悬浮气泡, dataTable 数据表格, dataList 数据列表")
    private String displayAreas;

    @ApiModelProperty(value = "是否可控")
    private Integer isAct;

    @ApiModelProperty(value = "是否配置")
    private Integer isConfigurable;

    @ApiModelProperty(value = "控制项")
    private List<DeviceTypeAttrConfigOptionItem> actList;


    @ApiModelProperty(value = "值映射项")
    private Map<String, String> valueMap;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("记录时间")
    private Date recordTime;

    private String inputType;

    @ApiModelProperty(value = "单位")
    private String unit;



}