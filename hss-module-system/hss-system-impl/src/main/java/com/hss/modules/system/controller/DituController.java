package com.hss.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.system.entity.Ditu;
import com.hss.modules.system.service.IDituService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 场景底图
 * @Author: zpc
 * @Date:   2024-02-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags="场景底图")
@RestController
@RequestMapping("/system/ditu")
public class DituController extends HssController<Ditu, IDituService> {
	@Autowired
	private IDituService dituService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ditu
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "场景底图-分页列表查询")
	@ApiOperation(value="场景底图-分页列表查询", notes="场景底图-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Ditu ditu,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Ditu> queryWrapper = QueryGenerator.initQueryWrapper(ditu, req.getParameterMap());
		Page<Ditu> page = new Page<Ditu>(pageNo, pageSize);
		IPage<Ditu> pageList = dituService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param ditu
	 * @return
	 */
	@AutoLog(value = "场景底图-添加")
	@ApiOperation(value="场景底图-添加", notes="场景底图-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Ditu ditu) {
		dituService.save(ditu);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param ditu
	 * @return
	 */
	@AutoLog(value = "场景底图-编辑")
	@ApiOperation(value="场景底图-编辑", notes="场景底图-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody Ditu ditu) {
		dituService.updateById(ditu);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "场景底图-通过id删除")
	@ApiOperation(value="场景底图-通过id删除", notes="场景底图-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		dituService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "场景底图-批量删除")
	@ApiOperation(value="场景底图-批量删除", notes="场景底图-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dituService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "场景底图-通过id查询")
	@ApiOperation(value="场景底图-通过id查询", notes="场景底图-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Ditu ditu = dituService.getById(id);
		return Result.OK(ditu);
	}
}
