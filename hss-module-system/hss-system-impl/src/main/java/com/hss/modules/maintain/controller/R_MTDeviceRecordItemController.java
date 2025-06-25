package com.hss.modules.maintain.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.maintain.entity.R_MTDeviceRecordItem;
import com.hss.modules.maintain.service.IR_MTDeviceRecordItemService;
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
 * @Description: 保养任务-设备关系表
 * @Author: zpc
 * @Date:   2022-12-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="保养任务-设备关系表")
@RestController
@RequestMapping("/maintain/r_MTDeviceRecordItem")
public class R_MTDeviceRecordItemController extends HssController<R_MTDeviceRecordItem, IR_MTDeviceRecordItemService> {
	@Autowired
	private IR_MTDeviceRecordItemService r_MTDeviceRecordItemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param r_MTDeviceRecordItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="保养任务-设备关系表-分页列表查询", notes="保养任务-设备关系表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(R_MTDeviceRecordItem r_MTDeviceRecordItem,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<R_MTDeviceRecordItem> queryWrapper = QueryGenerator.initQueryWrapper(r_MTDeviceRecordItem, req.getParameterMap());
		Page<R_MTDeviceRecordItem> page = new Page<R_MTDeviceRecordItem>(pageNo, pageSize);
		IPage<R_MTDeviceRecordItem> pageList = r_MTDeviceRecordItemService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param r_MTDeviceRecordItem
	 * @return
	 */
	@ApiOperation(value="保养任务-设备关系表-添加", notes="保养任务-设备关系表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody R_MTDeviceRecordItem r_MTDeviceRecordItem) {
		r_MTDeviceRecordItemService.save(r_MTDeviceRecordItem);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param r_MTDeviceRecordItem
	 * @return
	 */
	@ApiOperation(value="保养任务-设备关系表-编辑", notes="保养任务-设备关系表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody R_MTDeviceRecordItem r_MTDeviceRecordItem) {
		r_MTDeviceRecordItemService.updateById(r_MTDeviceRecordItem);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="保养任务-设备关系表-通过id删除", notes="保养任务-设备关系表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		r_MTDeviceRecordItemService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="保养任务-设备关系表-批量删除", notes="保养任务-设备关系表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.r_MTDeviceRecordItemService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="保养任务-设备关系表-通过id查询", notes="保养任务-设备关系表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		R_MTDeviceRecordItem r_MTDeviceRecordItem = r_MTDeviceRecordItemService.getById(id);
		return Result.OK(r_MTDeviceRecordItem);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param r_MTDeviceRecordItem
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, R_MTDeviceRecordItem r_MTDeviceRecordItem) {
      return super.exportXls(request, r_MTDeviceRecordItem, R_MTDeviceRecordItem.class, "保养任务-设备关系表");
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
      return super.importExcel(request, response, R_MTDeviceRecordItem.class);
  }

}
