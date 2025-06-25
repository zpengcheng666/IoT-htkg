package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.system.entity.BaseGroup;
import com.hss.modules.system.model.OrgRoleModel;

/**
 * @Description: 分组管理
 * @Author: zpc
 * @Date:   2023-05-15
 * @Version: V1.0
 */
public interface IBaseGroupService extends IService<BaseGroup> {

    void saveGroupAndRoles(OrgRoleModel orgRoleModel);
}
