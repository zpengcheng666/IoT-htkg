package com.hss.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.LoginUserUtils;
import com.hss.modules.system.entity.BaseMenu;
import com.hss.modules.system.model.MenuTreeListModel;
import com.hss.modules.system.model.UpdateStatusModel;
import com.hss.modules.system.service.IBaseMenuService;
import dm.jdbc.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 系统菜单
 * @Author: zpc
 * @Date: 2022-11-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "系统菜单")
@RestController
@RequestMapping("/system/baseMenu")
public class BaseMenuController extends HssController<BaseMenu, IBaseMenuService> {
    @Autowired
    private IBaseMenuService baseMenuService;

    /**
     * 系统菜单-树状查询
     *
     * @return
     */
    @ApiOperation(value = "系统菜单-结果树查询", notes = "系统菜单-结果树查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<?> queryMenuTreeList(String title,Integer status) {
        List<MenuTreeListModel> treeList = baseMenuService.queryMenuTreeList(title,status);
        return Result.OK(treeList);
    }

    /**
     * 用户菜单权限
     * admin查询所有菜单，其他用户按照权限查询
     * @return
     */
    @ApiOperation(value = "用户菜单权限", notes = "用户菜单权限")
    @RequestMapping(value = "/queryMenuTreeListByUser", method = RequestMethod.GET)
    public Result<?> queryMenuTreeListByUser(String title) {
        String userId = LoginUserUtils.getUser().getId();
        List<MenuTreeListModel> menuList = baseMenuService.queryMenuTreeListByUser(userId, title);
        return Result.OK(menuList);
    }

    /**
     * 获取当前用户权限标识符
     * @return
     */
    @ApiOperation(value = "获取当前用户权限标识符", notes = "获取当前用户权限标识符")
    @RequestMapping(value = "/getAuths", method = RequestMethod.GET)
    public Result<?> getAuths() {
        String userId = LoginUserUtils.getUser().getId();
        List<String> authList = baseMenuService.getAuths(userId);
        return Result.OK(authList);
    }

    /**
     * 添加
     *
     * @param baseMenu
     * @return
     */
    @AutoLog(value = "系统菜单-添加")
    @ApiOperation(value = "系统菜单-添加", notes = "系统菜单-添加")
    @PostMapping(value = "/add")
    @RequiresRoles(value = {"admin","administrator"},logical = Logical.OR)
    public Result<?> add(@RequestBody BaseMenu baseMenu) {
        if(StringUtil.isNotEmpty(baseMenu.getTitle())){
            int countTitle = baseMenuService.countTitle(baseMenu.getTitle());
            if (countTitle > 0) {
                return Result.error("组件名称已存在，请重新输入！");
            }
        }

        if(StringUtil.isNotEmpty(baseMenu.getAuth())){
            int countAuth = baseMenuService.countAuth(baseMenu.getAuth());
            if (countAuth > 0) {
                return Result.error("权限标识已存在，请重新输入！");
            }
        }

        if(StringUtils.isNotEmpty(baseMenu.getAuth())){
            String str = baseMenu.getAuth().replaceAll("\\s*", "");
            baseMenu.setAuth(str);
        }
        if(StringUtils.isNotEmpty(baseMenu.getTitle())){
            String str = baseMenu.getTitle().replaceAll("\\s*", "");
            baseMenu.setTitle(str);
        }
        if(StringUtils.isNotEmpty(baseMenu.getName())){
            String str = baseMenu.getName().replaceAll("\\s*", "");
            baseMenu.setName(str);
        }
        if(StringUtils.isNotEmpty(baseMenu.getComponent())){
            String str = baseMenu.getComponent().replaceAll("\\s*", "");
            baseMenu.setComponent(str);
        }
        if(StringUtils.isNotEmpty(baseMenu.getPath())){
            String str = baseMenu.getPath().replaceAll("\\s*", "");
            baseMenu.setPath(str);
        }
        baseMenu.setStatus(0);
        baseMenuService.save(baseMenu);
        BaseMenu byId = baseMenuService.getById(baseMenu.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param baseMenu
     * @return
     */
    @AutoLog(value = "系统菜单-编辑")
    @ApiOperation(value = "系统菜单-编辑", notes = "系统菜单-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    @RequiresRoles(value = {"admin","administrator"},logical = Logical.OR)
    public Result<?> editMenu(@RequestBody BaseMenu baseMenu) {
        baseMenuService.updateById(baseMenu);
        BaseMenu byId = baseMenuService.getById(baseMenu.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("编辑成功!");
    }

    /**
     * @description: 修改菜单状态，0显示1不显示
     * @author zpc
     * @date 2023/5/17 17:26
     * @version 1.0
     */
    @ApiOperation(value = "修改菜单状态，0显示1不显示", notes = "修改菜单状态，0显示1不显示")
    @RequestMapping(value = "/editStatus", method = {RequestMethod.POST})
    @RequiresRoles(value = {"admin","administrator"},logical = Logical.OR)
    public Result<?> editStatus(@RequestBody UpdateStatusModel updateStatusModel) {
        LambdaQueryWrapper<BaseMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseMenu::getId, updateStatusModel.getId());
        BaseMenu byId = baseMenuService.getOne(queryWrapper);

        byId.setStatus(updateStatusModel.getStatus());
        baseMenuService.updateById(byId);
        return Result.OK("修改状态成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统菜单-删除")
    @ApiOperation(value = "系统菜单-删除", notes = "系统菜单-删除")
    @DeleteMapping(value = "/delete")
    @RequiresRoles(value = {"admin","administrator"},logical = Logical.OR)
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        BaseMenu byId = baseMenuService.getById(id);
        LogUtil.setOperate(byId.getName());
        baseMenuService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "系统菜单-批量删除", notes = "系统菜单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.baseMenuService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "系统菜单-通过id查询", notes = "系统菜单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BaseMenu baseMenu = baseMenuService.getById(id);
        return Result.OK(baseMenu);
    }
}
