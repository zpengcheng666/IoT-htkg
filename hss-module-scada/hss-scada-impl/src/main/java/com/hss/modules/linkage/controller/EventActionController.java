package com.hss.modules.linkage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.linkage.entity.EventAction;
import com.hss.modules.linkage.service.IEventActionService;
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
 * @Description: 事件动作
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags="事件动作")
@RestController
@RequestMapping("/scada/eventAction")
public class EventActionController extends HssController<EventAction, IEventActionService> {
	@Autowired
	private IEventActionService eventActionService;
	
	/**
	 * 分页列表查询
	 *
	 * @param eventAction
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="事件动作-分页列表查询", notes="事件动作-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(EventAction eventAction,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<EventAction> queryWrapper = QueryGenerator.initQueryWrapper(eventAction, req.getParameterMap());
		Page<EventAction> page = new Page<EventAction>(pageNo, pageSize);
		IPage<EventAction> pageList = eventActionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param eventAction
	 * @return
	 */
	@AutoLog(value = "添加事件动作")
	@ApiOperation(value="事件动作-添加", notes="事件动作-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody EventAction eventAction) {
		eventActionService.add(eventAction);
		LogUtil.setOperate(eventAction.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param eventAction
	 * @return
	 */
	@AutoLog(value = "编辑事件动作")
	@ApiOperation(value="事件动作-编辑", notes="事件动作-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody EventAction eventAction) {
		eventActionService.updateById(eventAction);
		LogUtil.setOperate(eventAction.getName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "删除事件动作")
	@ApiOperation(value="事件动作-通过id删除", notes="事件动作-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		EventAction byId = eventActionService.getById(id);
		eventActionService.removeById(id);
		LogUtil.setOperate(byId.getName());
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "批量删除事件动作")
	@ApiOperation(value="事件动作-批量删除", notes="事件动作-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.eventActionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="事件动作-通过id查询", notes="事件动作-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		EventAction eventAction = eventActionService.getById(id);
		return Result.OK(eventAction);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param eventAction
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, EventAction eventAction) {
      return super.exportXls(request, eventAction, EventAction.class, "事件动作");
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
      return super.importExcel(request, response, EventAction.class);
  }

}
