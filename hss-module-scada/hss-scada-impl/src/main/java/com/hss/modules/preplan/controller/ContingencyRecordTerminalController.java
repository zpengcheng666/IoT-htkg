package com.hss.modules.preplan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.preplan.entity.ContingencyRecordTerminal;
import com.hss.modules.preplan.service.IContingencyRecordTerminalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

 /**
 * @Description: 特情处置终端关系
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Slf4j
@Api(tags="特情处置终端关系")
@RestController
@RequestMapping("/system/contingencyRecordTerminal")
public class ContingencyRecordTerminalController extends HssController<ContingencyRecordTerminal, IContingencyRecordTerminalService> {
	@Autowired
	private IContingencyRecordTerminalService contingencyRecordTerminalService;
	
	/**
	 * 分页列表查询
	 *
	 * @param contingencyRecordTerminal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
//	@AutoLog(value = "特情处置终端关系-分页列表查询")
	@ApiOperation(value="特情处置终端关系-分页列表查询", notes="特情处置终端关系-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ContingencyRecordTerminal contingencyRecordTerminal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ContingencyRecordTerminal> queryWrapper = QueryGenerator.initQueryWrapper(contingencyRecordTerminal, req.getParameterMap());
		Page<ContingencyRecordTerminal> page = new Page<ContingencyRecordTerminal>(pageNo, pageSize);
		IPage<ContingencyRecordTerminal> pageList = contingencyRecordTerminalService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param contingencyRecordTerminal
	 * @return
	 */
	@ApiOperation(value="特情处置终端关系-添加", notes="特情处置终端关系-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ContingencyRecordTerminal contingencyRecordTerminal) {
		contingencyRecordTerminalService.save(contingencyRecordTerminal);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param contingencyRecordTerminal
	 * @return
	 */
	@ApiOperation(value="特情处置终端关系-编辑", notes="特情处置终端关系-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody ContingencyRecordTerminal contingencyRecordTerminal) {
		contingencyRecordTerminalService.updateById(contingencyRecordTerminal);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="特情处置终端关系-通过id删除", notes="特情处置终端关系-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		contingencyRecordTerminalService.removeById(id);
		return Result.OK("删除成功!");
	}
	@ApiOperation(value="特情处置终端关系-批量删除", notes="特情处置终端关系-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.contingencyRecordTerminalService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	@ApiOperation(value="特情处置终端关系-通过id查询", notes="特情处置终端关系-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ContingencyRecordTerminal contingencyRecordTerminal = contingencyRecordTerminalService.getById(id);
		return Result.OK(contingencyRecordTerminal);
	}



}
