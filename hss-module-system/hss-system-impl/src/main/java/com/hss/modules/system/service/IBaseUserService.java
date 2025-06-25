package com.hss.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.vo.LoginUser;
import com.hss.modules.system.entity.BaseUser;

/**
 * @Description: 用户表
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface IBaseUserService extends IService<BaseUser> {

    LoginUser getEncodeUserInfo(String username);

    Result<JSONObject> checkUserIsEffective(BaseUser user);

    Result<?> changePassword(BaseUser baseUser);

    BaseUser getDetailById(String id);

    IPage<BaseUser> pageUser(BaseUser baseUser, Integer pageNo, Integer pageSize);
}
