package com.hss.modules.system.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
* @description: 人员option
* @author zpc
* @date 2024/3/21 13:34
* @version 1.0
*/
@Data
public class OptionModel implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "code")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "英文名称")
    private String enName;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "工作单位")
    private String workUnit;
    @ApiModelProperty(value = "职位")
    private String positions;

}
