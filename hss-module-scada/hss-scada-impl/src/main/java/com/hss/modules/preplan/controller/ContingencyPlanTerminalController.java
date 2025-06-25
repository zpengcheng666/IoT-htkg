package com.hss.modules.preplan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.preplan.entity.ContingencyPlanTerminal;
import com.hss.modules.preplan.service.IContingencyPlanTerminalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

 /**
 * @Description: 应急预案终端关系
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Slf4j
@Api(tags="应急预案终端关系")
@RestController
@RequestMapping("/system/contingencyPlanTerminal")
public class ContingencyPlanTerminalController extends HssController<ContingencyPlanTerminal, IContingencyPlanTerminalService> {
	@Autowired
	private IContingencyPlanTerminalService contingencyPlanTerminalService;
	@ApiOperation(value="应急预案终端关系-分页列表查询", notes="应急预案终端关系-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ContingencyPlanTerminal contingencyPlanTerminal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ContingencyPlanTerminal> queryWrapper = QueryGenerator.initQueryWrapper(contingencyPlanTerminal, req.getParameterMap());
		Page<ContingencyPlanTerminal> page = new Page<ContingencyPlanTerminal>(pageNo, pageSize);
		IPage<ContingencyPlanTerminal> pageList = contingencyPlanTerminalService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	@ApiOperation(value="应急预案终端关系-添加", notes="应急预案终端关系-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ContingencyPlanTerminal contingencyPlanTerminal) {
		contingencyPlanTerminalService.save(contingencyPlanTerminal);
		return Result.OK("添加成功！");
	}
	@ApiOperation(value="应急预案终端关系-编辑", notes="应急预案终端关系-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody ContingencyPlanTerminal contingencyPlanTerminal) {
		contingencyPlanTerminalService.updateById(contingencyPlanTerminal);
		return Result.OK("编辑成功!");
	}
	@ApiOperation(value="应急预案终端关系-通过id删除", notes="应急预案终端关系-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		contingencyPlanTerminalService.removeById(id);
		return Result.OK("删除成功!");
	}
	@ApiOperation(value="应急预案终端关系-批量删除", notes="应急预案终端关系-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.contingencyPlanTerminalService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	@ApiOperation(value="应急预案终端关系-通过id查询", notes="应急预案终端关系-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ContingencyPlanTerminal contingencyPlanTerminal = contingencyPlanTerminalService.getById(id);
		return Result.OK(contingencyPlanTerminal);
	}


}
