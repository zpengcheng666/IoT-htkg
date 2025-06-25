package com.hss.modules.system.controller;

import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.modules.system.entity.BaseRoleUser;
import com.hss.modules.system.model.UserRoleModel;
import com.hss.modules.system.service.IBaseRoleUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 角色、用户关系
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags="角色、用户关系")
@RestController
@RequestMapping("/system/baseRoleUser")
public class BaseRoleUserController extends HssController<BaseRoleUser, IBaseRoleUserService> {
	@Autowired
	private IBaseRoleUserService baseRoleUserService;

	/**
	 * 角色、用户关系-添加
	 *
	 * @param userRoleModel
	 * @return
	 */
	@ApiOperation(value="角色、用户关系-添加", notes="角色、用户关系-添加")
	@RequestMapping(value = "/addRoleUser", method = {RequestMethod.POST})
	@RequiresRoles(value = {"admin","administrator"},logical = Logical.OR)
	public Result<?> addRoleUser(@RequestBody UserRoleModel userRoleModel) {
		baseRoleUserService.saveRoleUser(userRoleModel);
		return Result.OK("添加成功！");
	}
}
