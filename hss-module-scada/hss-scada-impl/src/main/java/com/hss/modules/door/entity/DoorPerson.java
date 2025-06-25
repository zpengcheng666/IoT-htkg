package com.hss.modules.door.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DoorPerson implements java.io.Serializable{

    private Integer totalNumCount;

    @ApiModelProperty(value = "人员姓名")
    private String perName;

    @ApiModelProperty(value = "人员职务")
    private String perCareer;

    @ApiModelProperty(value = "人员姓名证件类型")
    private String perCardType;

    @ApiModelProperty(value = "人员姓名证件号码")
    private String perCardId;

    @ApiModelProperty(value = "人员单位")
    private String perDepart;

    @ApiModelProperty(value = "人员单位类型")
    private String perDepartType;

    @ApiModelProperty(value = "人员性别")
    private String perSex;

    @ApiModelProperty(value = "序号")
    private Integer num;

}
