package com.hss.modules.system.model;

import lombok.Data;

/**
* @description: 机构权限
* @author zpc
* @date 2024/3/21 13:34
* @version 1.0
*/
@Data
public class OrgRoleModel {
    private String resIds;

    private String orgId;

    private String childrenIds;
}
