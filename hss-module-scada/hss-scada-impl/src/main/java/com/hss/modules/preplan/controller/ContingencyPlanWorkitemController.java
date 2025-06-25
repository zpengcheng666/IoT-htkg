package com.hss.modules.preplan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.preplan.entity.ContingencyPlanWorkitem;
import com.hss.modules.preplan.service.IContingencyPlanWorkitemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

 /**
 * @Description: 应急预案工作
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Slf4j
@Api(tags="应急预案工作")
@RestController
@RequestMapping("/system/contingencyPlanWorkitem")
public class ContingencyPlanWorkitemController extends HssController<ContingencyPlanWorkitem, IContingencyPlanWorkitemService> {
	@Autowired
	private IContingencyPlanWorkitemService contingencyPlanWorkitemService;

	@ApiOperation(value="应急预案工作-分页列表查询", notes="应急预案工作-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ContingencyPlanWorkitem contingencyPlanWorkitem,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String stageId) {
		LambdaQueryWrapper<ContingencyPlanWorkitem> queryWrapper = new LambdaQueryWrapper<>();
		if (StringUtils.isNotBlank(stageId)){
			queryWrapper.eq(ContingencyPlanWorkitem::getStageId,stageId);
		}
		Page<ContingencyPlanWorkitem> page = new Page<>(pageNo, pageSize);
		IPage<ContingencyPlanWorkitem> pageList = contingencyPlanWorkitemService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	@AutoLog(value = "应急预案工作项-编辑")
	@ApiOperation(value="应急预案工作项-编辑", notes="应急预案工作项-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody ContingencyPlanWorkitem contingencyPlanWorkitem) {
		contingencyPlanWorkitemService.updateById(contingencyPlanWorkitem);
		ContingencyPlanWorkitem byId = contingencyPlanWorkitemService.getById(contingencyPlanWorkitem.getId());
		LogUtil.setOperate(byId.getName());
		return Result.OK("编辑成功!");
	}
	@AutoLog(value = "应急预案工作项-删除")
	@ApiOperation(value="应急预案工作-通过id删除", notes="应急预案工作-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ContingencyPlanWorkitem byId = contingencyPlanWorkitemService.getById(id);
		LogUtil.setOperate(byId.getName());
		contingencyPlanWorkitemService.removeById(id);
		return Result.OK("删除成功!");
	}
	@ApiOperation(value="应急预案工作-批量删除", notes="应急预案工作-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.contingencyPlanWorkitemService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	@ApiOperation(value="应急预案工作-通过id查询", notes="应急预案工作-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ContingencyPlanWorkitem contingencyPlanWorkitem = contingencyPlanWorkitemService.getById(id);
		return Result.OK(contingencyPlanWorkitem);
	}


}
