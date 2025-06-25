package com.hss.modules.system.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @description: 用户关联角色
* @author zpc
* @date 2024/3/21 13:38
* @version 1.0
*/
@Data
public class UserRoleModel {
    @ApiModelProperty("roleId 角色Id列表")
    private String roleId;

    @ApiModelProperty("用户id")
    private String userId;
}
