package com.hss.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.vo.LoginUser;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.entity.BaseOrgan;
import com.hss.modules.system.entity.BaseUser;
import com.hss.modules.system.mapper.BaseUserMapper;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.system.service.IBaseOrganService;
import com.hss.modules.system.service.IBaseUserService;
import org.jeecg.common.constant.CacheConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 用户表
 * @Author: zpc
 * @Date: 2022-11-03
 * @Version: V1.0
 */
@Service
public class BaseUserServiceImpl extends ServiceImpl<BaseUserMapper, BaseUser> implements IBaseUserService {

    @Autowired
    private BaseUserMapper baseUserMapper;

    @Autowired
    private IBaseDictDataService baseDictDataService;

    @Autowired
    private IBaseOrganService baseOrganService;

    @Override
    public LoginUser getEncodeUserInfo(String username) {
        LoginUser loginUser = new LoginUser();
        LambdaQueryWrapper<BaseUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseUser::getUsername, username);
        BaseUser user = this.getOne(queryWrapper);
        loginUser.setUsername(user.getUsername());
        loginUser.setRealname(user.getUsername());
        loginUser.setId(user.getId());
        loginUser.setEmail(user.getEmail());
        loginUser.setAvatar(user.getHeadImg());
        loginUser.setSex(user.getSex());
        loginUser.setPhone(user.getPhone());
        loginUser.setStatus(user.getLockStatus());
        loginUser.setPassword(user.getPassword());
        return loginUser;
    }

    @Override
    public Result<JSONObject> checkUserIsEffective(BaseUser user) {
        Result<JSONObject> result = new Result<JSONObject>();
        if (user == null) {
            result.error500("用户名或密码错误");
            return result;
        }

        // 判断用户状态
        if (user.getLockStatus() != 1) {
            result.error500("账号已被锁定,请联系管理员!");
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> changePassword(BaseUser baseUser) {
        baseUserMapper.changePwd(baseUser);
        return Result.ok("密码修改成功!");
    }

    @Override
    public BaseUser getDetailById(String id) {
        BaseUser users = this.getById(id);
        if (ObjectUtil.isEmpty(users)) {
            return null;
        }
        //组织机构
        BaseOrgan type = baseOrganService.getById(users.getOrganizationId());
        users.setOrganizationIdDisp(type == null ? "" : type.getName());
        //证件类别
        BaseDictData papers = baseDictDataService.getById(users.getPapersId());
        users.setPapersIdDisp(papers == null ? "" : papers.getName());

        return users;
    }

    @Override
    public IPage<BaseUser> pageUser(BaseUser baseUser, Integer pageNo, Integer pageSize) {
        LambdaQueryWrapper<BaseUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(BaseUser::getUsername, "admin");
        queryWrapper.ne(BaseUser::getUsername, "administrator");
        if (OConvertUtils.isNotEmpty(baseUser.getUsername())) {
            queryWrapper.like(BaseUser::getUsername, baseUser.getUsername());
        }

        Page<BaseUser> pages = new Page<>(pageNo, pageSize);
        IPage<BaseUser> pageList = page(pages, queryWrapper);
        pageList.getRecords().forEach(user -> {
            extracted(user);
        });

        return pageList;
    }

    /**
    * 组织机构和证件类型查询
    */
    private void extracted(BaseUser user) {
        //组织机构
        String organizationId = user.getOrganizationId();
        String organName = this.baseMapper.organName(organizationId);
        user.setOrganizationIdDisp(organName);
        //证件类别
        String papersId = user.getPapersId();
        String papersName = this.baseMapper.papersName(papersId);
        user.setPapersIdDisp(papersName);
    }
}
