package com.hss.modules.preplan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.preplan.entity.ContingencyRecordWorkitem;
import com.hss.modules.preplan.service.IContingencyRecordWorkitemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

 /**
 * @Description: 特情处置工作
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Slf4j
@Api(tags="特情处置工作")
@RestController
@RequestMapping("/system/contingencyRecordWorkitem")
public class ContingencyRecordWorkitemController extends HssController<ContingencyRecordWorkitem, IContingencyRecordWorkitemService> {
	@Autowired
	private IContingencyRecordWorkitemService contingencyRecordWorkitemService;

	@ApiOperation(value="特情处置工作-分页列表查询", notes="特情处置工作-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ContingencyRecordWorkitem contingencyRecordWorkitem,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ContingencyRecordWorkitem> queryWrapper = QueryGenerator.initQueryWrapper(contingencyRecordWorkitem, req.getParameterMap());
		Page<ContingencyRecordWorkitem> page = new Page<ContingencyRecordWorkitem>(pageNo, pageSize);
		IPage<ContingencyRecordWorkitem> pageList = contingencyRecordWorkitemService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	@ApiOperation(value="特情处置工作-添加", notes="特情处置工作-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ContingencyRecordWorkitem contingencyRecordWorkitem) {
		contingencyRecordWorkitemService.save(contingencyRecordWorkitem);
		return Result.OK("添加成功！");
	}

	@ApiOperation(value="特情处置工作-编辑", notes="特情处置工作-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody ContingencyRecordWorkitem contingencyRecordWorkitem) {
		contingencyRecordWorkitemService.updateById(contingencyRecordWorkitem);
		return Result.OK("编辑成功!");
	}

	@ApiOperation(value="特情处置工作-通过id删除", notes="特情处置工作-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		contingencyRecordWorkitemService.removeById(id);
		return Result.OK("删除成功!");
	}

	@ApiOperation(value="特情处置工作-批量删除", notes="特情处置工作-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.contingencyRecordWorkitemService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	@ApiOperation(value="特情处置工作-通过id查询", notes="特情处置工作-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ContingencyRecordWorkitem contingencyRecordWorkitem = contingencyRecordWorkitemService.getById(id);
		return Result.OK(contingencyRecordWorkitem);
	}




}
