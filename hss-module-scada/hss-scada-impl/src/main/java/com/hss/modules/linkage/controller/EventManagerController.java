package com.hss.modules.linkage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.linkage.entity.EventManager;
import com.hss.modules.linkage.service.IEventManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

 /**
 * @Description: 事件管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags="事件管理")
@RestController
@RequestMapping("/scada/eventManager")
public class EventManagerController extends HssController<EventManager, IEventManagerService> {
	@Autowired
	private IEventManagerService eventManagerService;

	@ApiOperation(value="事件管理-分页列表查询", notes="事件管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@ApiParam("事件名称") @RequestParam(name = "name",required = false) String name,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Page<EventManager> page = new Page<EventManager>(pageNo, pageSize);
		IPage<EventManager> pageList = eventManagerService.getPage(page, name);
		return Result.OK(pageList);
	}

	 @ApiOperation(value="全部事件列表", notes="事件管理-全部事件列表")
	 @GetMapping(value = "/listAll")
	 public Result<List<EventManager>> listAll() {
		 List<EventManager> list = eventManagerService.listAll();
		 return Result.OK(list);
	 }
	
	/**
	 * 添加
	 *
	 * @param eventManager
	 * @return
	 */
	@AutoLog(value = "添加事件")
	@ApiOperation(value="事件管理-添加", notes="事件管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody EventManager eventManager) {
		eventManagerService.save(eventManager);
		LogUtil.setOperate(eventManager.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param eventManager
	 * @return
	 */
	@AutoLog(value = "编辑事件")
	@ApiOperation(value="事件管理-编辑", notes="事件管理-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody EventManager eventManager) {
		eventManagerService.updateById(eventManager);
		LogUtil.setOperate(eventManager.getName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "删除事件")
	@ApiOperation(value="事件管理-通过id删除", notes="事件管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		EventManager byId = eventManagerService.getById(id);
		eventManagerService.delete(id);
		LogUtil.setOperate(byId.getName());
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "批量删除事件")
	@ApiOperation(value="事件管理-批量删除", notes="事件管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.eventManagerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="事件管理-通过id查询", notes="事件管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		EventManager eventManager = eventManagerService.getById(id);
		return Result.OK(eventManager);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param eventManager
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, EventManager eventManager) {
      return super.exportXls(request, eventManager, EventManager.class, "事件管理");
  }

  /**
   * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      return super.importExcel(request, response, EventManager.class);
  }

}
