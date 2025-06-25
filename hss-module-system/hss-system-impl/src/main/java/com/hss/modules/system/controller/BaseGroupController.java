package com.hss.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.modules.system.entity.BaseGroup;
import com.hss.modules.system.entity.BaseGroupAuth;
import com.hss.modules.system.model.OrgRoleModel;
import com.hss.modules.system.service.IBaseGroupAuthService;
import com.hss.modules.system.service.IBaseGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 分组管理
 * @Author: zpc
 * @Date: 2023-05-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "分组管理")
@RestController
@RequestMapping("/system/baseGroup")
public class BaseGroupController extends HssController<BaseGroup, IBaseGroupService> {
    @Autowired
    private IBaseGroupService baseGroupService;
    @Autowired
    private IBaseGroupAuthService baseGroupAuthService;

    /**
     * 添加组织机构和角色、菜单关系
     *
     * @param orgRoleModel
     * @return
     */
    @ApiOperation(value = "添加组织机构和角色、菜单关系", notes = "添加组织机构和角色、菜单关系")
    @PostMapping(value = "/addGroupAndRoles")
    @RequiresRoles(value = {"admin", "administrator"}, logical = Logical.OR)
    public Result<?> addGroupAndRoles(@RequestBody OrgRoleModel orgRoleModel) {
        baseGroupService.saveGroupAndRoles(orgRoleModel);
        return Result.OK("添加成功！");
    }

    /**
     * 获取组织机构菜单权限
     *
     * @param orgId
     * @return
     */
    @ApiOperation(value = "获取组织机构菜单权限", notes = "获取组织机构菜单权限")
    @GetMapping(value = "/getGroupRoles")
    @RequiresRoles(value = {"admin", "administrator"}, logical = Logical.OR)
    public Result<?> getGroupRoles(String orgId) {
        LambdaQueryWrapper<BaseGroupAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseGroupAuth::getOrgId, orgId);
        List<BaseGroupAuth> groupRoleList = baseGroupAuthService.list(queryWrapper);
        if (groupRoleList == null) {
            return Result.OK(new ArrayList<>());
        }
        List<String> authList = groupRoleList.stream().map(BaseGroupAuth::getAuthId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(authList)) {
            return Result.OK(new ArrayList<>());
        }

        return Result.OK(authList);
    }
}
