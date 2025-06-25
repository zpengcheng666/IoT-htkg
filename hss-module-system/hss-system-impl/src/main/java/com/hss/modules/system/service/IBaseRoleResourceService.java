package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.system.entity.BaseRoleMenu;
import com.hss.modules.system.model.RoleMenuModel;

/**
 * @Description: 角色、资源关系
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface IBaseRoleResourceService extends IService<BaseRoleMenu> {

    void editRoleMenu(RoleMenuModel model);
}
