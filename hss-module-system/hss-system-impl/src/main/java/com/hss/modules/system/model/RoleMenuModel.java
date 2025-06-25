package com.hss.modules.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @description: 角色菜单权限
* @author zpc
* @date 2024/3/21 13:35
* @version 1.0
*/
@Data
@ApiModel(value="RoleResourceModel", description="更新角色对应的资源列表")
public class RoleMenuModel {

    @ApiModelProperty("roleId 角色Id")
    private String roleId;

    @ApiModelProperty("角色对应的资源id列表")
    private String resIds;

    @ApiModelProperty("权限列表")
    private  String childrenIds;
}
