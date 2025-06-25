package com.hss.modules.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.FTSafe.Read;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.constant.CommonConstant;
import com.hss.core.common.system.util.JwtUtil;
import com.hss.core.common.system.vo.LoginUser;
import com.hss.core.common.util.Md5Util;
import com.hss.core.common.util.PasswordUtil;
import com.hss.core.config.JeecgBaseConfig;
import com.hss.modules.system.entity.BaseUser;
import com.hss.modules.system.model.SysLoginModel;
import com.hss.modules.system.service.IBaseUserService;
import com.hss.modules.system.util.RandImageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
* @description: 登录
* @author zpc、wuyihan
* @date 2024/3/21 11:35
* @version 1.0
*/
@RestController
@RequestMapping("/sys")
@Api(tags="用户登录")
@Slf4j
public class LoginController {
	@Autowired
	private IBaseUserService userService;
	@Autowired
    private RedisUtil redisUtil;
	@Autowired
	private JeecgBaseConfig jeecgBaseConfig;

	@ApiOperation("登录接口")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel){
		/**
		 * 检测是否启动授权检测
		 */
		if (jeecgBaseConfig.getAuthMode()){
			try {
				String dogKey = Read.getDogKey();
				if (StringUtils.isBlank(dogKey)){
					Result<JSONObject> result = new Result<JSONObject>();
					result.error500("软件未授权，请联系系统管理员！");
					return result;
				}
//				if (!StringUtils.equalsIgnoreCase(jeecgBaseConfig.getAuthModeKey(), dogKey)){
//					Result<JSONObject> result = new Result<JSONObject>();
//					result.error500("软件未授权，请联系系统管理员！");
//					return result;
//				}
			} catch (Throwable e) {
				log.error("获取加密狗失败", e);
				Result<JSONObject> result = new Result<JSONObject>();
				result.error500("软件未授权，请联系系统管理员！");
				return result;
			}
		}
		Result<JSONObject> result = new Result<JSONObject>();
		String username = sysLoginModel.getUsername();
		String password = sysLoginModel.getPassword();

		//1. 校验用户是否有效
		LambdaQueryWrapper<BaseUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(BaseUser::getUsername,username);
		BaseUser user = userService.getOne(queryWrapper);
		// 验证用户是否有效
		result = userService.checkUserIsEffective(user);
		if(!result.isSuccess()) {
			return result;
		}
		//2. 校验用户名或密码是否正确
		String userpassword = PasswordUtil.encrypt(username, password, user.getSalt());
		String syspassword = user.getPassword();
		if (!syspassword.equals(userpassword)) {
			result.error500("用户名或密码错误");
			return result;
		}
		//用户登录信息
		userInfo(user, result);
		return result;
	}

	/**
	 * 退出登录
	 * @param request
	 * @return
	 */
	@ApiOperation("退出登录")
	@PostMapping(value = "/logout")
	public Result<Object> logout(HttpServletRequest request) {
		// 1. 获取登录用户信息
		LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
	    if(loginUser!=null) {
			// 2. 添加用户logout日志
	    	log.info(" 用户名:  "+loginUser.getRealname()+",退出成功！ ");
			// 3. 清空用户登录Token缓存
			String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
	    	redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
			// 4. 清空用户登录Shiro权限缓存
			redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + loginUser.getId());
			// 5. 清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
			redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, loginUser.getUsername()));
			// 6. 调用shiro的logout
			SecurityUtils.getSubject().logout();
	    	return Result.ok("退出登录成功！");
	    }else {
	    	return Result.error("Token无效!");
	    }
	}

	/**
	 * 用户信息
	 *
	 * @param user
	 * @param result
	 * @return
	 */
	private Result<JSONObject> userInfo(BaseUser user, Result<JSONObject> result) {
		String username = user.getUsername();
		String syspassword = user.getPassword();
		// 获取用户部门信息
		JSONObject obj = new JSONObject(new LinkedHashMap<>());
		// 生成token
		String token = JwtUtil.sign(username, syspassword);
		// 设置token缓存有效时间
		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
		obj.put("token", token);
		// 取消password和salt字段
		user.setSalt(null);
		user.setPassword(null);
		obj.put("userInfo", user);
		result.setResult(obj);
		result.success("登录成功");
		return result;
	}
}