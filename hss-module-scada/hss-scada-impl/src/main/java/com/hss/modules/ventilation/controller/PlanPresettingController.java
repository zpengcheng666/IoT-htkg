package com.hss.modules.ventilation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.ventilation.entity.PlanPresetting;
import com.hss.modules.ventilation.service.IPlanPresettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

 /**
 * @Description: 预设方案
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="预设方案")
@RestController
@RequestMapping("/ventilation/planPresetting")
public class PlanPresettingController extends HssController<PlanPresetting, IPlanPresettingService> {
	@Autowired
	private IPlanPresettingService planPresettingService;

	 @Autowired
	 private IConSheBeiService conSheBeiService;

	/**
	 * 分页列表查询
	 *
	 * @param planPresetting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="预设方案-分页列表查询", notes="预设方案-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PlanPresetting planPresetting,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PlanPresetting> queryWrapper = QueryGenerator.initQueryWrapper(planPresetting, req.getParameterMap());
		Page<PlanPresetting> page = new Page<PlanPresetting>(pageNo, pageSize);
		IPage<PlanPresetting> pageList = planPresettingService.page(page, queryWrapper);
		pageList.getRecords().forEach(e ->{
			ConSheBei byId = conSheBeiService.getById(e.getDdcId());
			e.setControlSystemName(byId == null ? "" : byId.getName());
		});
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param planPresetting
	 * @return
	 */
	@AutoLog(value = "添加预设方案")
	@ApiOperation(value="预设方案-添加", notes="预设方案-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PlanPresetting planPresetting) {
		planPresettingService.save(planPresetting);
		LogUtil.setOperate(planPresetting.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param planPresetting
	 * @return
	 */
	@AutoLog(value = "编辑预设方案")
	@ApiOperation(value="预设方案-编辑", notes="预设方案-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody PlanPresetting planPresetting) {
		planPresettingService.updateById(planPresetting);
		LogUtil.setOperate(planPresetting.getName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "删除预设方案")
	@ApiOperation(value="预设方案-通过id删除", notes="预设方案-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		PlanPresetting planPresetting = planPresettingService.getById(id);
		planPresettingService.removeById(id);
		LogUtil.setOperate(planPresetting.getName());
		return Result.OK("删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="预设方案-通过id查询", notes="预设方案-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PlanPresetting planPresetting = planPresettingService.getById(id);
		return Result.OK(planPresetting);
	}
}
