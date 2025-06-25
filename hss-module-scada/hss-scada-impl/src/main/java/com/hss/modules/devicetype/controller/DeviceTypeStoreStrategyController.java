package com.hss.modules.devicetype.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.devicetype.entity.DeviceTypeStoreStrategy;
import com.hss.modules.devicetype.service.IDeviceTypeStoreStrategyService;
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
 * @description: 设备类型存储策略
 * @author zpc
 * @date 2024/3/20 14:59
 * @version 1.0
 */
@Slf4j
@Api(tags="设备类型存储策略")
@RestController
@RequestMapping("/scada/deviceTypeStoreStrategy")
public class DeviceTypeStoreStrategyController extends HssController<DeviceTypeStoreStrategy, IDeviceTypeStoreStrategyService> {
	@Autowired
	private IDeviceTypeStoreStrategyService deviceTypeStoreStrategyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param deviceTypeStoreStrategy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="设备类型存储策略-分页列表查询", notes="设备类型存储策略-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceTypeStoreStrategy deviceTypeStoreStrategy,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceTypeStoreStrategy> queryWrapper = QueryGenerator.initQueryWrapper(deviceTypeStoreStrategy, req.getParameterMap());
		Page<DeviceTypeStoreStrategy> page = new Page<DeviceTypeStoreStrategy>(pageNo, pageSize);
		IPage<DeviceTypeStoreStrategy> pageList = deviceTypeStoreStrategyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param deviceTypeStoreStrategy
	 * @return
	 */
	@AutoLog(value = "设备类型管理添加存储策略")
	@ApiOperation(value="设备类型存储策略-添加", notes="设备类型存储策略-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceTypeStoreStrategy deviceTypeStoreStrategy) {
		deviceTypeStoreStrategyService.add(deviceTypeStoreStrategy);
		LogUtil.setOperate(deviceTypeStoreStrategy.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param deviceTypeStoreStrategy
	 * @return
	 */
	@AutoLog(value = "设备类型管理编辑存储策略")
	@ApiOperation(value="设备类型存储策略-编辑", notes="设备类型存储策略-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody DeviceTypeStoreStrategy deviceTypeStoreStrategy) {
		deviceTypeStoreStrategyService.edit(deviceTypeStoreStrategy);
		LogUtil.setOperate(deviceTypeStoreStrategy.getName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备类型管理删除存储策略")
	@ApiOperation(value="设备类型存储策略-通过id删除", notes="设备类型存储策略-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		DeviceTypeStoreStrategy byId = deviceTypeStoreStrategyService.getById(id);
		deviceTypeStoreStrategyService.delete(id);
		LogUtil.setOperate(byId.getName());
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设备类型管理批量删除存储策略")
	@ApiOperation(value="设备类型存储策略-批量删除", notes="设备类型存储策略-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceTypeStoreStrategyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="设备类型存储策略-通过id查询", notes="设备类型存储策略-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceTypeStoreStrategy deviceTypeStoreStrategy = deviceTypeStoreStrategyService.getById(id);
		return Result.OK(deviceTypeStoreStrategy);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param deviceTypeStoreStrategy
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, DeviceTypeStoreStrategy deviceTypeStoreStrategy) {
      return super.exportXls(request, deviceTypeStoreStrategy, DeviceTypeStoreStrategy.class, "设备类型存储策略");
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
      return super.importExcel(request, response, DeviceTypeStoreStrategy.class);
  }

}
