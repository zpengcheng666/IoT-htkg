package com.hss.modules.system.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @description: 修改密码
* @author zpc
* @date 2024/3/21 13:30
* @version 1.0
*/
@Data
public class ChangePwdModel implements java.io.Serializable{

    private String id;

    @ApiModelProperty(value = "旧密码")
    private String oldPwd;

    @ApiModelProperty(value = "新密码")
    private String newPwd1;

    @ApiModelProperty(value = "再次输入新密码")
    private String newPwd2;
}
