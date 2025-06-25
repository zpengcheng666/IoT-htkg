package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.BaseGroup;
import com.hss.modules.system.entity.BaseGroupAuth;
import com.hss.modules.system.entity.BaseGroupRole;
import com.hss.modules.system.mapper.BaseGroupMapper;
import com.hss.modules.system.model.OrgRoleModel;
import com.hss.modules.system.service.IBaseGroupAuthService;
import com.hss.modules.system.service.IBaseGroupRoleService;
import com.hss.modules.system.service.IBaseGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 分组管理
 * @Author: zpc
 * @Date:   2023-05-15
 * @Version: V1.0
 */
@Service
public class BaseGroupServiceImpl extends ServiceImpl<BaseGroupMapper, BaseGroup> implements IBaseGroupService {

    @Autowired
    private IBaseGroupRoleService baseGroupRoleService;
    @Autowired
    private IBaseGroupAuthService baseGroupAuthService;

    @Override
    public void saveGroupAndRoles(OrgRoleModel orgRoleModel) {
        LambdaQueryWrapper<BaseGroupRole> delete = new LambdaQueryWrapper<>();
        delete.eq(BaseGroupRole::getGroupId, orgRoleModel.getOrgId());
        this.baseGroupRoleService.remove(delete);

        if (StringUtils.isEmpty(orgRoleModel.getResIds())){
            return ;
        }

        String[] menuIds = StringUtils.split(orgRoleModel.getResIds(), ",");

        for (String menu: menuIds) {
            BaseGroupRole entity = new BaseGroupRole();
            entity.setMenuId(menu);
            entity.setGroupId(orgRoleModel.getOrgId());
            this.baseGroupRoleService.save(entity);
        }

        LambdaQueryWrapper<BaseGroupAuth> deleteQuery = new LambdaQueryWrapper<>();
        deleteQuery.eq(BaseGroupAuth::getOrgId, orgRoleModel.getOrgId());
        this.baseGroupAuthService.remove(deleteQuery);

        if (StringUtils.isEmpty(orgRoleModel.getChildrenIds())){
            return ;
        }

        String[] authIds = StringUtils.split(orgRoleModel.getChildrenIds(), ",");

        for (String auth: authIds) {
            BaseGroupAuth entity = new BaseGroupAuth();
            entity.setOrgId(orgRoleModel.getOrgId());
            entity.setAuthId(auth);
            this.baseGroupAuthService.save(entity);
        }
    }
}
