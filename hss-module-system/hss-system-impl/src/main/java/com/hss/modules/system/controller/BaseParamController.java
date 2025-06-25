package com.hss.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.system.entity.BaseParam;
import com.hss.modules.system.service.IBaseParamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

 /**
 * @Description: 参数管理
 * @Author: zpc
 * @Date:   2023-05-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags="参数管理")
@RestController
@RequestMapping("/system/baseParam")
public class BaseParamController extends HssController<BaseParam, IBaseParamService> {
	@Autowired
	private IBaseParamService baseParamService;
	
	@ApiOperation(value="参数管理-分页列表查询", notes="参数管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BaseParam baseParam,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		if (StringUtils.isNotEmpty(baseParam.getName())){
			baseParam.setName("*" + baseParam.getName() + "*");
		}
		if (StringUtils.isNotEmpty(baseParam.getCode())){
			baseParam.setCode(baseParam.getCode() + "*");
		}
		QueryWrapper<BaseParam> queryWrapper = QueryGenerator.initQueryWrapper(baseParam, req.getParameterMap());
		queryWrapper.orderByAsc("CODE");
		Page<BaseParam> page = new Page<>(pageNo, pageSize);
		IPage<BaseParam> pageList = baseParamService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	@AutoLog(value = "添加系统参数")
	@ApiOperation(value="参数管理-添加", notes="参数管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BaseParam baseParam) {
		baseParamService.save(baseParam);
		LogUtil.setOperate(baseParam.getName());
		return Result.OK("添加成功！");
	}

	@AutoLog(value = "编辑系统参数")
	@ApiOperation(value="参数管理-编辑", notes="参数管理-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody BaseParam baseParam) {
		baseParamService.updateById(baseParam);
		LogUtil.setOperate(baseParam.getName());
		return Result.OK("编辑成功!");
	}

	@AutoLog(value = "删除系统参数")
	@ApiOperation(value="参数管理-通过id删除", notes="参数管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		BaseParam byId = baseParamService.getById(id);
		if (byId == null){
			return Result.OK("删除成功!");
		}
		baseParamService.removeById(id);
		LogUtil.setOperate(byId.getName());
		return Result.OK("删除成功!");
	}

	@ApiOperation(value="参数管理-通过id查询", notes="参数管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BaseParam baseParam = baseParamService.getById(id);
		return Result.OK(baseParam);
	}
}
