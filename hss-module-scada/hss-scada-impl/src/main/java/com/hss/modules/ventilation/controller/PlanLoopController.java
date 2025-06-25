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
import com.hss.modules.ventilation.entity.PlanLoop;
import com.hss.modules.ventilation.service.IPlanLoopService;
import com.hss.modules.ventilation.vo.PlanListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @description: 全自动方案
* @author zpc
* @date 2024/3/20 10:20
* @version 1.0
*/
@Slf4j
@Api(tags="全自动方案")
@RestController
@RequestMapping("/ventilation/planLoop")
public class PlanLoopController extends HssController<PlanLoop, IPlanLoopService> {
	@Autowired
	private IPlanLoopService planLoopService;

	 @Autowired
	 private IConSheBeiService conSheBeiService;


	/**
	 * 分页列表查询
	 *
	 * @param planLoop
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="全自动方案-分页列表查询", notes="全自动方案-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PlanLoop planLoop,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PlanLoop> queryWrapper = QueryGenerator.initQueryWrapper(planLoop, req.getParameterMap());
		Page<PlanLoop> page = new Page<PlanLoop>(pageNo, pageSize);
		IPage<PlanLoop> pageList = planLoopService.page(page, queryWrapper);
		pageList.getRecords().forEach(e ->{
			ConSheBei byId = conSheBeiService.getById(e.getDdcId());
			e.setControlSystemName(byId == null ? "" : byId.getName());
		});
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param planLoop
	 * @return
	 */
	@AutoLog(value = "添加全自动方案")
	@ApiOperation(value="全自动方案-添加", notes="全自动方案-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PlanLoop planLoop) {
		planLoopService.save(planLoop);
		LogUtil.setOperate(planLoop.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param planLoop
	 * @return
	 */
	@AutoLog(value = "编辑全自动方案")
	@ApiOperation(value="全自动方案-编辑", notes="全自动方案-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody PlanLoop planLoop) {
		planLoopService.updateById(planLoop);
		LogUtil.setOperate(planLoop.getName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "删除全自动方案")
	@ApiOperation(value="全自动方案-通过id删除", notes="全自动方案-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		PlanLoop byId = planLoopService.getById(id);
		planLoopService.removeById(id);
		LogUtil.setOperate(byId.getName());
		return Result.OK("删除成功!");
	}

	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="全自动方案-通过id查询", notes="全自动方案-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PlanLoop planLoop = planLoopService.getById(id);
		return Result.OK(planLoop);
	}

	 @ApiOperation(value="查询方案", notes="查询方案")
	 @GetMapping(value = "/listPlan")
	 public Result<List<PlanListVO>> listPlan(@ApiParam(value = "设备id", required = true) @RequestParam("deviceId") String deviceId) {
		 List<PlanListVO> list = planLoopService.listPlan(deviceId);
		 return Result.OK(list);
	 }


}
