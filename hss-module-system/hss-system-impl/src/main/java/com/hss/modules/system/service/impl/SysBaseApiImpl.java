package com.hss.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hss.common.system.api.ISysBaseAPI;
import com.hss.core.common.constant.SymbolConstant;
import com.hss.core.common.desensitization.annotation.SensitiveDecode;
import com.hss.core.common.desensitization.util.SensitiveInfoUtil;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.system.vo.*;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.system.entity.BaseMenu;
import com.hss.modules.system.entity.BaseRole;
import com.hss.modules.system.entity.BaseRoleUser;
import com.hss.modules.system.entity.BaseUser;
import com.hss.modules.system.mapper.BaseMenuMapper;
import com.hss.modules.system.mapper.BaseRoleMapper;
import com.hss.modules.system.mapper.BaseRoleUserMapper;
import com.hss.modules.system.mapper.BaseUserMapper;
import com.hss.modules.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.CacheConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 底层共通业务API，提供其他独立模块调用
 * @Author: zpc
 * @Date:2019-4-20
 * @Version:V1.0
 */
@Slf4j
@Service
public class SysBaseApiImpl implements ISysBaseAPI {
    /**
     * 当前系统数据库类型
     */
    private static String DB_TYPE = "";

    @Autowired
    private IBaseDictDataService dictDataService;
    @Autowired
    private IBaseUserService userService;
    @Autowired
    private IBaseLogService logService;
    @Autowired
    private IBaseRoleResourceService baseRoleResourceService;
    @Autowired
    private IBaseRoleUserService baseRoleUserService;
    @Autowired
    private IBaseUserService baseUserService;
    @Autowired
    private BaseMenuMapper baseMenuMapper;
    @Autowired
    private IBaseRoleService baseRoleService;

    @Override
    @SensitiveDecode
    public LoginUser getUserByName(String username) {
        if (OConvertUtils.isEmpty(username)) {
            return null;
        }
        LoginUser user = this.userService.getEncodeUserInfo(username);

        //相同类中方法间调用时脱敏解密 Aop会失效，获取用户信息太重要，此处采用原生解密方法，不采用@SensitiveDecodeAble注解方式
        try {
            SensitiveInfoUtil.handlerObject(user, false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public String translateDictFromTable(String table, String text, String code, String key) {
        return null;
    }

    @Override
    public String translateDict(String code, String key) {
        return null;
    }

    @Override
    public List<SysPermissionDataRuleModel> queryPermissionDataRule(String component, String requestPath, String username) {
        return null;
    }

    /**
     * 匹配前端传过来的地址 匹配成功返回正则地址
     * AntPathMatcher匹配地址
     * ()* 匹配0个或多个字符
     * ()**匹配0个或多个目录
     */
    private String getRegexpUrl(String url) {
        return null;
    }

    @Override
    public SysUserCacheInfo getCacheUser(String username) {
        SysUserCacheInfo info = new SysUserCacheInfo();
        info.setOneDepart(true);
        LoginUser user = this.getUserByName(username);

        try {
            SensitiveInfoUtil.handlerObject(user, false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (user != null) {
            info.setSysUserCode(user.getUsername());
            info.setSysUserName(user.getRealname());
            info.setSysOrgCode(user.getOrgCode());
        } else {
            return null;
        }
        //多部门支持in查询
        List<String> sysMultiOrgCode = new ArrayList<String>();
        info.setSysMultiOrgCode(sysMultiOrgCode);
        return info;
    }

    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE, key = "#code", unless = "#result == null ")
    public List<DictModel> queryDictItemsByCode(String code) {
        return null;
    }

    @Override
    public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
        if (table.indexOf(SymbolConstant.SYS_VAR_PREFIX) >= 0) {
            table = QueryGenerator.getSqlRuleValue(table);
        }
        return null;
    }

    /**
     * 获取数据库类型
     *
     * @param dataSource
     * @return
     * @throws SQLException
     */
    private String getDatabaseTypeByDataSource(DataSource dataSource) throws SQLException {
        return DB_TYPE;

    }

    @Override
    public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray) {
        return null;
    }

    @Override
    public DynamicDataSourceModel getDynamicDbSourceByCode(String dbSourceCode) {
        return null;
    }

    /**
     * 查询用户拥有的角色集合
     *
     * @param username
     * @return
     */
    @Override
    public Set<String> getUserRoleSet(String username) {
        Set<String> set = new HashSet<>();
        if (username.equals("admin") || username.equals("administrator")) {
            set.add("admin");
            set.add("administrator");
            return set;
        }
        //1.根据用户名获取用户id
        LambdaQueryWrapper<BaseUser> queryUser = new LambdaQueryWrapper<>();
        queryUser.eq(BaseUser::getUsername, username);
        String userId = baseUserService.getOne(queryUser).getId();

        //2.根据用户id查询拥有角色
        LambdaQueryWrapper<BaseRoleUser> queryRoleUser = new LambdaQueryWrapper<>();
        queryRoleUser.eq(BaseRoleUser::getUserId, userId);
        List<BaseRoleUser> list = baseRoleUserService.list(queryRoleUser);
        if (CollectionUtil.isEmpty(list)) {
            return new HashSet<>();
        }

        //3.获取角色id列表
        List<String> rolesId = list.stream().map(BaseRoleUser::getRoleId).collect(Collectors.toList());

        //4.获取角色name列表，并返回
        LambdaQueryWrapper<BaseRole> queryRole = new LambdaQueryWrapper<>();
        queryRole.in(BaseRole::getId, rolesId);
        Set<String> setRoleNmaes = baseRoleService.list(queryRole).stream().map(BaseRole::getName).collect(Collectors.toSet());

        return setRoleNmaes;
    }

    /**
     * 查询用户拥有的权限集合
     *
     * @param username
     * @return
     */
    @Override
    public Set<String> getUserPermissionSet(String userId) {
        Set<String> permissionSet = new HashSet<>();
        List<BaseMenu> permissionList = baseMenuMapper.queryByUser(userId);
        for (BaseMenu po : permissionList) {
            if (StringUtils.isNotEmpty(po.getAuth())) {
                permissionSet.add(po.getAuth());
            }
        }
        log.info("-------通过数据库读取用户拥有的权限auth------username： " + userId + ",Perms size: " + (permissionSet == null ? 0 : permissionSet.size()));
        return permissionSet;
    }

    /**
     * 查询用户拥有的角色集合 common api 里面的接口实现
     *
     * @param username
     * @return
     */
    @Override
    public Set<String> queryUserRoles(String username) {
        return getUserRoleSet(username);
    }

    /**
     * 查询用户拥有的权限集合 common api 里面的接口实现
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> queryUserAuths(String userId) {
        return getUserPermissionSet(userId);
    }

    @Override
    public Map<String, List<DictModel>> translateManyDict(String dictCodes, String keys) {
        return null;
    }

    @Override
    public List<DictModel> translateDictFromTableByKeys(String table, String text, String code, String keys) {
        return null;
    }
}