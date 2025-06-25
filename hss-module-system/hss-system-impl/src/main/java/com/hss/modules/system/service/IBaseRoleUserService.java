package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.system.entity.BaseRoleUser;
import com.hss.modules.system.model.UserRoleModel;

/**
 * @Description: 角色、用户关系
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface IBaseRoleUserService extends IService<BaseRoleUser> {

    void saveRoleUser(UserRoleModel userRoleModel);
}
