package com.hss.modules.preplan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.preplan.entity.ContingencyPlanStage;
import com.hss.modules.preplan.service.IContingencyPlanStageService;
import com.hss.modules.system.model.ContingencyWorkModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

 /**
 * @Description: 应急预案阶段
 * @Author: zpc、wuyihan
 * @Date:   2024-03-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="应急预案阶段")
@RestController
@RequestMapping("/system/contingencyPlanStage")
public class ContingencyPlanStageController extends HssController<ContingencyPlanStage, IContingencyPlanStageService> {
	@Autowired
	private IContingencyPlanStageService contingencyPlanStageService;

	@ApiOperation(value="应急预案阶段-分页列表查询", notes="应急预案阶段-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ContingencyPlanStage contingencyPlanStage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String planId) {
		LambdaQueryWrapper<ContingencyPlanStage> queryWrapper = new LambdaQueryWrapper<>();
		if (StringUtils.isNotBlank(planId)){
			queryWrapper.eq(ContingencyPlanStage::getPlanId,planId);
		}
		Page<ContingencyPlanStage> page = new Page<>(pageNo, pageSize);
		IPage<ContingencyPlanStage> pageList = contingencyPlanStageService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 @AutoLog(value = "应急预案阶段-工作-添加")
	 @ApiOperation(value="应急预案阶段-工作-添加", notes="应急预案阶段-工作-添加")
	 @PostMapping(value = "/addWork")
	 public Result<?> addWork(@RequestBody ContingencyWorkModel model) {
		 contingencyPlanStageService.saveWork(model);
		 ContingencyPlanStage byId = contingencyPlanStageService.getById(model.getStageId());
		 LogUtil.setOperate(byId.getName());
		 return Result.OK("添加成功！");
	 }
	@AutoLog(value = "应急预案阶段-编辑")
	@ApiOperation(value="应急预案阶段-编辑", notes="应急预案阶段-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody ContingencyPlanStage contingencyPlanStage) {
		contingencyPlanStageService.updateById(contingencyPlanStage);
		ContingencyPlanStage byId = contingencyPlanStageService.getById(contingencyPlanStage.getId());
		LogUtil.setOperate(byId.getName());
		return Result.OK("编辑成功!");
	}
	@AutoLog(value = "应急预案阶段-删除")
	@ApiOperation(value="应急预案阶段-通过id删除", notes="应急预案阶段-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ContingencyPlanStage byId = contingencyPlanStageService.getById(id);
		LogUtil.setOperate(byId.getName());
		contingencyPlanStageService.removeById(id);
		return Result.OK("删除成功!");
	}
	@ApiOperation(value="应急预案阶段-批量删除", notes="应急预案阶段-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.contingencyPlanStageService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	@ApiOperation(value="应急预案阶段-通过id查询", notes="应急预案阶段-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ContingencyPlanStage contingencyPlanStage = contingencyPlanStageService.getById(id);
		return Result.OK(contingencyPlanStage);
	}


}
