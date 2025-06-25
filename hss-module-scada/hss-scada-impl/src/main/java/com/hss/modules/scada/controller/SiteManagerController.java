package com.hss.modules.scada.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.modules.scada.entity.SiteManager;
import com.hss.modules.scada.model.SetSiteStateDTO;
import com.hss.modules.scada.model.SiteStateUpdateDTO;
import com.hss.modules.scada.service.ISiteManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @description: 站点管理
 * @author zpc
 * @date 2024/3/19 13:44
 * @version 1.0
 */
@Slf4j
@Api(tags="站点管理")
@RestController
@RequestMapping("/scada/siteManager")
public class SiteManagerController extends HssController<SiteManager, ISiteManagerService> {
	@Autowired
	private ISiteManagerService siteManagerService;

	@ApiOperation(value="站点管理-分页列表查询", notes="站点管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(String name,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Page<SiteManager> page = new Page<>(pageNo, pageSize);
		IPage<SiteManager> pageList = siteManagerService.pageList(page, name);
		return Result.OK(pageList);
	}

	@AutoLog(value = "添加站点")
	@ApiOperation(value="添加站点", notes="添加站点")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SiteManager siteManager) {
		siteManagerService.add(siteManager);
		return Result.OK("添加成功！");
	}

	@AutoLog(value = "编辑站点")
	@ApiOperation(value="站点管理-编辑", notes="站点管理-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody SiteManager siteManager) {
		siteManagerService.edit(siteManager);
		return Result.OK("编辑成功!");
	}

	@AutoLog(value = "删除站点")
	@ApiOperation(value="删除站点", notes="删除站点")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		siteManagerService.removeById(id);
		return Result.OK("删除成功!");
	}

	@ApiOperation(value="站点管理-通过id查询", notes="站点管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SiteManager siteManager = siteManagerService.getById(id);
		return Result.OK(siteManager);
	}

	 @ApiOperation(value="保存网关列表", notes="保存网关列表")
	 @PostMapping(value = "/saveGatewayList")
	 public Result<?> saveGatewayList(@RequestBody SiteManager siteManager) {
		 siteManagerService.saveGatewayList(siteManager);
		 return Result.OK("保存成功！");
	 }

	 @ApiOperation(value="切换状态", notes="切换状态")
	 @PutMapping(value = "/updateState")
	 public Result<?> updateState(@RequestBody SiteStateUpdateDTO dto) {
		 siteManagerService.updateState(dto);
		 return Result.OK("切换成功！");
	 }

	 @ApiOperation(value="设置站点状态", notes="设置站点状态")
	 @PostMapping(value = "/setState")
	 public Result<?> setState(@RequestBody SetSiteStateDTO dto) {
		 siteManagerService.setLocalState(dto);
		 return Result.OK("设置成功！");
	 }

	 @ApiOperation(value="获取站点状态", notes="获取站点状态")
	 @PostMapping(value = "/getState")
	 public Result<Integer> getState(@RequestBody SetSiteStateDTO dto) {
		 Integer state = siteManagerService.getLocalState(dto);
		 return Result.ok(state);
	 }




}
