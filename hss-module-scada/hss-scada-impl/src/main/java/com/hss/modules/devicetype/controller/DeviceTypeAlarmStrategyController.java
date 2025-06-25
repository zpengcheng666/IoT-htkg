package com.hss.modules.devicetype.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import com.hss.modules.devicetype.service.IDeviceTypeAlarmStrategyService;
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
 * @description: 设备类型报警策略
 * @author zpc
 * @date 2024/3/20 14:59
 * @version 1.0
 */
@Slf4j
@Api(tags="设备类型报警策略")
@RestController
@RequestMapping("/scada/deviceTypeAlarmStrategy")
public class DeviceTypeAlarmStrategyController extends HssController<DeviceTypeAlarmStrategy, IDeviceTypeAlarmStrategyService> {
	@Autowired
	private IDeviceTypeAlarmStrategyService deviceTypeAlarmStrategyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param deviceTypeAlarmStrategy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="设备类型报警策略-分页列表查询", notes="设备类型报警策略-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceTypeAlarmStrategy deviceTypeAlarmStrategy,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceTypeAlarmStrategy> queryWrapper = QueryGenerator.initQueryWrapper(deviceTypeAlarmStrategy, req.getParameterMap());
		Page<DeviceTypeAlarmStrategy> page = new Page<DeviceTypeAlarmStrategy>(pageNo, pageSize);
		IPage<DeviceTypeAlarmStrategy> pageList = deviceTypeAlarmStrategyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param deviceTypeAlarmStrategy
	 * @return
	 */
	@AutoLog(value = "设备类型管理添加报警策略")
	@ApiOperation(value="设备类型报警策略-添加", notes="设备类型报警策略-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceTypeAlarmStrategy deviceTypeAlarmStrategy) {
		deviceTypeAlarmStrategyService.add(deviceTypeAlarmStrategy);
		LogUtil.setOperate(deviceTypeAlarmStrategy.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param deviceTypeAlarmStrategy
	 * @return
	 */
	@AutoLog(value = "设备类型管理编辑报警策略")
	@ApiOperation(value="设备类型报警策略-编辑", notes="设备类型报警策略-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody DeviceTypeAlarmStrategy deviceTypeAlarmStrategy) {
		deviceTypeAlarmStrategyService.edit(deviceTypeAlarmStrategy);
		LogUtil.setOperate(deviceTypeAlarmStrategy.getName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备类型管理删除报警策略")
	@ApiOperation(value="设备类型报警策略-通过id删除", notes="设备类型报警策略-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		DeviceTypeAlarmStrategy byId = deviceTypeAlarmStrategyService.getById(id);
		deviceTypeAlarmStrategyService.delete(id);
		LogUtil.setOperate(byId.getName());
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设备类型管理批量删除报警策略")
	@ApiOperation(value="设备类型报警策略-批量删除", notes="设备类型报警策略-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceTypeAlarmStrategyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="设备类型报警策略-通过id查询", notes="设备类型报警策略-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceTypeAlarmStrategy deviceTypeAlarmStrategy = deviceTypeAlarmStrategyService.getById(id);
		return Result.OK(deviceTypeAlarmStrategy);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param deviceTypeAlarmStrategy
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, DeviceTypeAlarmStrategy deviceTypeAlarmStrategy) {
      return super.exportXls(request, deviceTypeAlarmStrategy, DeviceTypeAlarmStrategy.class, "设备类型报警策略");
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
      return super.importExcel(request, response, DeviceTypeAlarmStrategy.class);
  }

}
