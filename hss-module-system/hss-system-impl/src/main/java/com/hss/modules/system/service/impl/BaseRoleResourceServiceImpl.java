package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.BaseRoleAuth;
import com.hss.modules.system.entity.BaseRoleMenu;
import com.hss.modules.system.mapper.BaseRoleResourceMapper;
import com.hss.modules.system.model.RoleMenuModel;
import com.hss.modules.system.service.IBaseRoleAuthService;
import com.hss.modules.system.service.IBaseRoleResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 角色、资源关系
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Service
public class BaseRoleResourceServiceImpl extends ServiceImpl<BaseRoleResourceMapper, BaseRoleMenu> implements IBaseRoleResourceService {

    @Autowired
    private IBaseRoleAuthService baseRoleAuthService;

    @Override
    public void editRoleMenu(RoleMenuModel model) {
        // 1. 根据roleId删除掉所有关联的resourceIds
        LambdaQueryWrapper<BaseRoleMenu> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(BaseRoleMenu::getRoleId, model.getRoleId());
        this.remove(deleteWrapper);

        // 2. 重新插入所有的resourceIds
        if (StringUtils.isEmpty(model.getResIds())) {
            return;
        }
        String[] resids = StringUtils.split(model.getResIds(), ",");
        for (String resId : resids) {
            BaseRoleMenu entity = new BaseRoleMenu();
            entity.setRoleId(model.getRoleId());
            entity.setResId(resId);

            this.save(entity);
        }

        LambdaQueryWrapper<BaseRoleAuth> delete = new LambdaQueryWrapper<>();
        delete.eq(BaseRoleAuth::getRoleId, model.getRoleId());
        this.baseRoleAuthService.remove(delete);
        if (StringUtils.isEmpty(model.getChildrenIds())) {
            return;
        }
        String[] childrenId = model.getChildrenIds().split(",");
        for (String child : childrenId) {
            BaseRoleAuth entity = new BaseRoleAuth();
            entity.setRoleId(model.getRoleId());
            entity.setChildrenIds(child);

            this.baseRoleAuthService.save(entity);
        }
    }
}
