package com.hss.modules.devicetype.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.devicetype.entity.DeviceTypeManagementState;
import com.hss.modules.devicetype.service.IDeviceTypeManagementStateService;
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
 * @description: 设备类型管理状态管理
 * @author zpc
 * @date 2024/3/20 14:59
 * @version 1.0
 */
@Slf4j
@Api(tags="设备类型管理状态管理")
@RestController
@RequestMapping("/scada/deviceTypeManagementState")
public class DeviceTypeManagementStateController extends HssController<DeviceTypeManagementState, IDeviceTypeManagementStateService> {
	@Autowired
	private IDeviceTypeManagementStateService deviceTypeManagementStateService;
	

	@ApiOperation(value="设备类型管理状态管理-分页列表查询", notes="设备类型管理状态管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceTypeManagementState deviceTypeManagementState,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceTypeManagementState> queryWrapper = QueryGenerator.initQueryWrapper(deviceTypeManagementState, req.getParameterMap());
		Page<DeviceTypeManagementState> page = new Page<DeviceTypeManagementState>(pageNo, pageSize);
		IPage<DeviceTypeManagementState> pageList = deviceTypeManagementStateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	

	@AutoLog(value = "设备类型管理添加状态")
	@ApiOperation(value="设备类型管理状态管理-添加", notes="设备类型管理状态管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceTypeManagementState deviceTypeManagementState) {
		deviceTypeManagementStateService.save(deviceTypeManagementState);
		LogUtil.setOperate(deviceTypeManagementState.getStateName());
		return Result.OK("添加成功！");
	}

	@AutoLog(value = "设备类型管理编辑状态")
	@ApiOperation(value="设备类型管理状态管理-编辑", notes="设备类型管理状态管理-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody DeviceTypeManagementState deviceTypeManagementState) {
		deviceTypeManagementStateService.updateById(deviceTypeManagementState);
		LogUtil.setOperate(deviceTypeManagementState.getStateName());
		return Result.OK("编辑成功!");
	}

	 @AutoLog(value = "设备类型管理设置默认状态")
	 @ApiOperation(value="设备类型管理状态管理-设置默认", notes="设备类型管理状态管理-设置默认")
	 @PutMapping(value = "/setDefault/{id}")
	 public Result<?> setDefault(@PathVariable("id") String id) {
		 DeviceTypeManagementState byId = deviceTypeManagementStateService.getById(id);
		 deviceTypeManagementStateService.setDefault(id);
		 LogUtil.setOperate(byId.getStateName());
		 return Result.OK("编辑成功!");
	 }
	

	@AutoLog(value = "设备类型管理删除状态")
	@ApiOperation(value="设备类型管理状态管理-通过id删除", notes="设备类型管理状态管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		DeviceTypeManagementState byId = deviceTypeManagementStateService.getById(id);
		deviceTypeManagementStateService.removeById(id);
		LogUtil.setOperate(byId.getStateName());
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设备类型管理批量删除状态")
	@ApiOperation(value="设备类型管理状态管理-批量删除", notes="设备类型管理状态管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceTypeManagementStateService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="设备类型管理状态管理-通过id查询", notes="设备类型管理状态管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceTypeManagementState deviceTypeManagementState = deviceTypeManagementStateService.getById(id);
		return Result.OK(deviceTypeManagementState);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param deviceTypeManagementState
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, DeviceTypeManagementState deviceTypeManagementState) {
      return super.exportXls(request, deviceTypeManagementState, DeviceTypeManagementState.class, "设备类型管理状态管理");
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
      return super.importExcel(request, response, DeviceTypeManagementState.class);
  }

}
