package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.BaseRoleUser;
import com.hss.modules.system.mapper.BaseRoleUserMapper;
import com.hss.modules.system.model.UserRoleModel;
import com.hss.modules.system.service.IBaseRoleUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description: 角色、用户关系
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Service
public class BaseRoleUserServiceImpl extends ServiceImpl<BaseRoleUserMapper, BaseRoleUser> implements IBaseRoleUserService {

    @Override
    public void saveRoleUser(UserRoleModel userRoleModel) {
        // 1. 根据userId删除掉所有关联的roleIds
        LambdaQueryWrapper<BaseRoleUser> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(BaseRoleUser::getUserId, userRoleModel.getUserId());
        this.remove(deleteWrapper);

        // 2. 重新插入所有的roleIds
        if (StringUtils.isEmpty(userRoleModel.getRoleId())) {
            return ;
        }
        String[] roleIds = StringUtils.split(userRoleModel.getRoleId(), ",");

        for (String roleId: roleIds) {
            BaseRoleUser entity = new BaseRoleUser();
            entity.setRoleId(roleId);
            entity.setUserId(userRoleModel.getUserId());
            entity.setDeleted(0);
            entity.setUpdatedTime(new Date());
            this.save(entity);
        }
    }
}
