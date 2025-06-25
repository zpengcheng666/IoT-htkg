package com.hss.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.system.entity.BaseRole;
import com.hss.modules.system.entity.BaseRoleAuth;
import com.hss.modules.system.model.RoleMenuModel;
import com.hss.modules.system.service.IBaseRoleAuthService;
import com.hss.modules.system.service.IBaseRoleResourceService;
import com.hss.modules.system.service.IBaseRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 角色表
 * @Author: zpc
 * @Date: 2022-11-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "角色表")
@RestController
@RequestMapping("/system/baseRole")
public class BaseRoleController extends HssController<BaseRole, IBaseRoleService> {
    @Autowired
    private IBaseRoleService baseRoleService;

    @Autowired
    private IBaseRoleResourceService baseRoleResourceService;

    @Autowired
    private IBaseRoleAuthService baseRoleAuthService;

    /**
     * 列表查询
     *
     * @param baseRole
     * @return
     */
    @ApiOperation(value = "角色查询list", notes = "角色查询list")
    @GetMapping(value = "/listRole")
    public Result<?> queryPageList(BaseRole baseRole,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<BaseRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(BaseRole::getName, "admin");
        queryWrapper.ne(BaseRole::getName, "administrator");
        if (OConvertUtils.isNotEmpty(baseRole.getName())) {
            //按照角色名称模糊查询
            queryWrapper.like(BaseRole::getName, baseRole.getName());
        }

        Page<BaseRole> page = new Page<>(pageNo, pageSize);
        IPage<BaseRole> pageList = baseRoleService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    @ApiOperation(value = "查询角色拥有的菜单权限", notes = "查询角色拥有的菜单权限")
    @GetMapping(value = "/listRolePermissions")
    @RequiresRoles(value = {"admin", "administrator"}, logical = Logical.OR)
    public Result<?> listRolePermissions(String roleId) {
        LambdaQueryWrapper<BaseRoleAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseRoleAuth::getRoleId, roleId);
        List<BaseRoleAuth> roleAuthListt = baseRoleAuthService.list(queryWrapper);
        if (roleAuthListt == null) {
            return Result.OK();
        }
        List<String> collect = roleAuthListt.stream().map(BaseRoleAuth::getChildrenIds).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(collect)) {
            return Result.OK(new ArrayList<>());
        }
        return Result.OK(collect);
    }

    /**
     * 添加
     *
     * @param baseRole
     * @return
     */
    @AutoLog(value = "角色表-添加")
    @ApiOperation(value = "角色表-添加", notes = "角色表-添加")
    @PostMapping(value = "/addRole")
    @RequiresRoles(value = {"admin", "administrator"}, logical = Logical.OR)
    public Result<?> addRole(@RequestBody BaseRole baseRole) {
        LambdaQueryWrapper<BaseRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseRole::getName, baseRole.getName());
        long countName = baseRoleService.count(queryWrapper);
        if (countName > 0) {
            return Result.error("角名称重复，请重新输入");
        }
        baseRole.setCreatedTime(new Date());
        baseRoleService.save(baseRole);
        BaseRole byId = baseRoleService.getById(baseRole.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param baseRole
     * @return
     */
    @AutoLog(value = "角色表-编辑")
    @ApiOperation(value = "角色表-编辑", notes = "角色表-编辑")
    @RequestMapping(value = "/editRole", method = {RequestMethod.POST})
    @RequiresRoles(value = {"admin", "administrator"}, logical = Logical.OR)
    public Result<?> editRole(@RequestBody BaseRole baseRole) {
        BaseRole baseRole1 = baseRoleService.getById(baseRole.getId());
        baseRole1.setDescribe(baseRole.getDescribe());
        //状态（0：无效，1：有效）
        baseRole1.setState(baseRole.getState());
        //是否可删除（0：不可删除，1：可删除）
        baseRole1.setIsEditable(baseRole.getIsEditable());
        baseRole1.setName(baseRole.getName());
        baseRole1.setUpdatedTime(new Date());
        baseRoleService.saveOrUpdate(baseRole1);
        BaseRole byId = baseRoleService.getById(baseRole.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("编辑成功");
    }

    /**
     * 角色有效 & 无效
     *
     * @param baseRole
     * @return
     */
    @ApiOperation(value = "角色有效 & 无效", notes = "角色有效 & 无效")
    @RequestMapping(value = "/frozenState", method = RequestMethod.POST)
    @RequiresRoles(value = {"admin", "administrator"}, logical = Logical.OR)
    public Result<BaseRole> frozenState(@RequestBody BaseRole baseRole) {
        Result<BaseRole> result = new Result<BaseRole>();
        try {
            String ids = baseRole.getId();
            Integer status = baseRole.getState();
            String[] arr = ids.split(",");
            for (String id : arr) {
                if (OConvertUtils.isNotEmpty(id)) {
                    this.baseRoleService.update(new BaseRole().setState(status),
                            new UpdateWrapper<BaseRole>().lambda().eq(BaseRole::getId, id));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败" + e.getMessage());
        }
        result.success("操作成功!");
        return result;

    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "角色表-删除")
    @ApiOperation(value = "角色表-删除", notes = "角色表-删除")
    @DeleteMapping(value = "/delete")
    @RequiresRoles(value = {"admin", "administrator"}, logical = Logical.OR)
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        BaseRole byId = baseRoleService.getById(id);
        LogUtil.setOperate(byId.getName());
        baseRoleService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "角色表-批量删除", notes = "角色表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.baseRoleService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "角色表-通过id查询", notes = "角色表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        baseRoleService.getById(id);
        return Result.OK();
    }

    @ApiOperation(value = "角色关联菜单权限", notes = "角色关联菜单权限")
    @RequestMapping(value = "/editRoleMenu", method = {RequestMethod.POST})
    @RequiresRoles(value = {"admin", "administrator"}, logical = Logical.OR)
    public Result<?> editRoleMenu(@RequestBody RoleMenuModel model) {
        baseRoleResourceService.editRoleMenu(model);
        return Result.OK("编辑成功!");
    }
}
