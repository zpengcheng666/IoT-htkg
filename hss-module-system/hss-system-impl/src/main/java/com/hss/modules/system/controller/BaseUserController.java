package com.hss.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.constant.CommonConstant;
import com.hss.core.common.exception.HssBootException;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.*;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.entity.BaseOrgan;
import com.hss.modules.system.entity.BaseRoleUser;
import com.hss.modules.system.entity.BaseUser;
import com.hss.modules.system.model.ChangePwdModel;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.system.service.IBaseOrganService;
import com.hss.modules.system.service.IBaseRoleUserService;
import com.hss.modules.system.service.IBaseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 用户管理
 * @Author: zpc
 * @Date: 2022-11-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "用户管理")
@RestController
@RequestMapping("/system/baseUser")
public class BaseUserController extends HssController<BaseUser, IBaseUserService> {
    @Autowired
    private IBaseUserService baseUserService;
    @Autowired
    private IBaseRoleUserService baseRoleUserService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * @description: 用户管理首页查询列表
     * @author zpc
     * @date 2022/11/25 16:10
     * @version 1.0
     */
    @ApiOperation(value = "用户管理-分页列表查询", notes = "用户管理-分页列表查询")
    @GetMapping(value = "/listUser")

    public Result<?> queryPageList(BaseUser baseUser,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage<BaseUser> pageList = baseUserService.pageUser( baseUser,pageNo,pageSize);
        return Result.OK(pageList);
    }

    @ApiOperation(value = "根据用户id查询查询用户拥有的角色", notes = "根据用户id查询查询用户拥有的角色")
    @RequestMapping(value = "/queryUserRole", method = RequestMethod.GET)
    @RequiresRoles(value = {"admin", "administrator"}, logical = Logical.OR)
    public Result<?> queryUserRole(String userId) {
        LambdaQueryWrapper<BaseRoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseRoleUser::getUserId, userId);
        List<BaseRoleUser> list = baseRoleUserService.list(queryWrapper);
        if (CollectionUtil.isEmpty(list)) {
            return Result.OK();
        }

        List<Map<String, String>> mapList = list.stream().map(e -> {
            Map<String, String> map = new HashMap<>(16);
            map.put("roleId", e.getRoleId());
            return map;
        }).collect(Collectors.toList());

        return Result.OK(mapList);
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @AutoLog(value = "用户管理-添加")
    @ApiOperation(value = "用户管理-添加", notes = "用户管理-添加")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public Result<BaseUser> addUser(@RequestBody BaseUser baseUser) {
        Result<BaseUser> result = new Result<>();
        LambdaQueryWrapper<BaseUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseUser::getUsername, baseUser.getUsername());
        queryWrapper.eq(BaseUser::getDeleted, CommonConstant.DEL_FLAG_0);
        List<BaseUser> list = this.baseUserService.list(queryWrapper);
        if (!list.isEmpty()) {
            throw new HssBootException("用户名存在，请重新输入！");
        }
        baseUser.setCreatetime(DateUtils.getDate());
        String salt = OConvertUtils.randomGen(8);
        baseUser.setSalt(salt);
        String pwd = "111111";
        String passwordEncode = PasswordUtil.encrypt(baseUser.getUsername(), pwd, salt);
        baseUser.setPassword(passwordEncode);
        baseUser.setLockStatus(1);
        baseUser.setDeleted(CommonConstant.DEL_FLAG_0);
        baseUserService.save(baseUser);
        result.success("添加成功！");

        BaseUser byId = baseUserService.getById(baseUser.getId());
        LogUtil.setOperate(byId.getUsername());

        return Result.OK("添加用户成功！");
    }

    /**
     *
     */
    @AutoLog(value = "用户管理-个人设置")
    @ApiOperation(value = "用户管理-个人设置", notes = "用户管理-个人设置")
    @RequestMapping(value = "/personalsetting", method = RequestMethod.POST)
    public Result<?> personalsetting(@RequestBody BaseUser baseUser) {
        Result<BaseUser> result = new Result<>();
        BaseUser user = baseUserService.getById(LoginUserUtils.getUser().getId());
        user.setMilitaryId(baseUser.getMilitaryId());
        user.setOrganizationId(baseUser.getOrganizationId());
        user.setDepartment(baseUser.getDepartment());
        user.setName(baseUser.getName());
        user.setUsername(baseUser.getUsername());
        user.setPositionId(baseUser.getPositionId());
        user.setPhone(baseUser.getPhone());
        user.setUpdatedTime(new Date());
        this.baseUserService.saveOrUpdate(user);
        BaseUser byId = baseUserService.getById(baseUser.getId());
        LogUtil.setOperate(byId.getUsername());

        return result.success("设置成功");
    }


    /**
     * 编辑
     *
     * @param baseUser
     * @return
     */
    @ApiOperation(value = "用户管理-编辑", notes = "用户管理-编辑")
    @RequestMapping(value = "/editUser", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> editUser(@RequestBody BaseUser baseUser) {
        Result<BaseUser> result = new Result<>();
        BaseUser sysdict = baseUserService.getById(baseUser.getId());
        if (sysdict == null) {
            result.error500("未找到对应实体id");
        } else {
            sysdict.setUpdatedTime(new Date());
            boolean ok = baseUserService.saveOrUpdate(baseUser);
            BaseUser byId = baseUserService.getById(baseUser.getId());
            LogUtil.setOperate(byId.getUsername());
            if (ok) {
                result.success("编辑成功!");
            }
        }

        return result;
    }

    /**
     * 用户修改密码
     *
     * @param changePwdModel
     * @return
     */
    @AutoLog(value = "用户管理-修改密码")
    @ApiOperation(value = "用户管理-修改密码", notes = "1.oldPwd旧密码2.newPwd1新密码3.newPwd2再次输入新密码")
    @RequestMapping(value = "/changePwd", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> changePwd(@RequestBody ChangePwdModel changePwdModel, HttpServletRequest request, HttpServletResponse response) {
        // 1. 判断用户是否存在
        BaseUser u = this.baseUserService.getById(LoginUserUtils.getUser().getId());
        if (ObjectUtil.isEmpty(u)) {
            throw new HssBootException("用户不存在！");
//            return Result.error("用户不存在！");
        }

        // 2. 判断老密码是否正确
        String oldPwd1 = PasswordUtil.encrypt(LoginUserUtils.getUser().getUsername(), changePwdModel.getOldPwd(), u.getSalt());
        String oldPwd2 = u.getPassword();
        if (!StringUtils.equals(oldPwd1, oldPwd2)) {
            throw new HssBootException("密码验证失败！");
//            return Result.error("密码验证失败！");
        }

        // 3. 判断新密码和确认密码是否相同
        if (!StringUtils.equals(changePwdModel.getNewPwd1(), changePwdModel.getNewPwd2())) {
            throw new HssBootException("新密码和确认密码不相等！");
//            return Result.error("新密码和确认密码不相等！");
        }

        // 4. 生成新密码，更新新密码
        String salt = OConvertUtils.randomGen(8);
        u.setSalt(salt);
        u.setPassword(PasswordUtil.encrypt(LoginUserUtils.getUser().getUsername(), changePwdModel.getNewPwd1(), salt));
        baseUserService.changePassword(u);

        //清空用户登录Token缓存
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);

        //清空用户登录Shiro权限缓存
        redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + LoginUserUtils.getUser().getId());

        //调用shiro的logout
        SecurityUtils.getSubject().logout();

        // 退出登录
        request.getSession().invalidate();
        return Result.OK("修改密码成功！");
    }

    /**
     * 首页用户重置密码
     */
    @AutoLog(value = "用户管理-重置密码")
    @ApiOperation(value = "用户管理-重置密码", notes = "重置密码为111111")
    @GetMapping(value = "/resetPwd")
    public Result<?> resetPwd(@RequestParam(name = "id", required = true) String id) {
        BaseUser baseUser = this.baseUserService.getById(id);
        String username = baseUser.getUsername();
        String salt = OConvertUtils.randomGen(8);
        baseUser.setSalt(salt);
        String pwd = PasswordUtil.encrypt(username, "111111", salt);
        baseUser.setPassword(pwd);
        baseUserService.changePassword(baseUser);
        return Result.OK("重置密码完成！");

    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户管理-删除")
    @ApiOperation(value = "用户管理-删除", notes = "用户管理-删除")
    @DeleteMapping(value = "/deleteUser")
    public Result<?> deleteUser(@RequestParam(name = "id") String id) {
        BaseUser byId = baseUserService.getById(id);
        LogUtil.setOperate(byId.getUsername());
        baseUserService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "用户管理-批量删除", notes = "用户管理-批量删除")
    @DeleteMapping(value = "/deleteBatchUser")
    public Result<?> deleteBatchUser(@RequestParam(name = "ids") String ids) {
        this.baseUserService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 获取登录用户的个人信息
     *
     * @param
     * @return
     */
    @ApiOperation(value = "用户管理-获取登录用户的个人信息", notes = "用户管理-获取登录用户的个人信息")
    @GetMapping(value = "/queryPersonMes")
    public Result<?> queryPersonMes() {
        //查询个人信息，包括需要转码的字段信息
        BaseUser baseUser = baseUserService.getDetailById(LoginUserUtils.getUser().getId());
        return Result.OK(baseUser);
    }

    /**
     * 冻结&解冻用户
     *
     * @param baseUser
     * @return
     */
    @AutoLog(value = "用户冻结&解冻")
    @ApiOperation(value = "用户冻结&解冻", notes = "用户冻结&解冻")
    @RequestMapping(value = "/frozenBatch", method = RequestMethod.POST)
    public Result<BaseUser> frozenBatch(@RequestBody BaseUser baseUser) {
        Result<BaseUser> result = new Result<BaseUser>();
        try {
            String ids = baseUser.getId();
            Integer status = baseUser.getLockStatus();
            String[] arr = ids.split(",");
            for (String id : arr) {
                if (OConvertUtils.isNotEmpty(id)) {
                    this.baseUserService.update(new BaseUser().setLockStatus(Integer.parseInt(String.valueOf(status))),
                            new UpdateWrapper<BaseUser>().lambda().eq(BaseUser::getId, id));
                }
            }
            BaseUser byId = baseUserService.getById(baseUser.getId());
            LogUtil.setOperate(byId.getName());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败" + e.getMessage());
        }
        result.success("操作成功!");
        return result;

    }

    /**
     * 导出excel
     *
     * @param request
     * @param baseUser
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BaseUser baseUser) {
        return super.exportXls(request, baseUser, BaseUser.class, "用户管理");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BaseUser.class);
    }

}
