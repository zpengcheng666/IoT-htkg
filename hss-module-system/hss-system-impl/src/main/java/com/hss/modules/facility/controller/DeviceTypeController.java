package com.hss.modules.facility.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.vo.SelectTreeNode;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.facility.entity.DeviceType;
import com.hss.modules.facility.service.IDeviceTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

 /**
 * @Description: 类别管理
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags="类别管理")
@RestController
@RequestMapping("/facility/deviceType")
public class DeviceTypeController extends HssController<DeviceType, IDeviceTypeService> {
	@Autowired
	private IDeviceTypeService deviceTypeService;

	 /**
	  * 设备类别-树状查询
	  * @return
	  */
	 @ApiOperation(value="设备类别-结果树查询", notes="设备类别-结果树查询")
	 @RequestMapping(value = "/queryTreeList",method = RequestMethod.GET)
	 public Result<List<SelectTreeNode>> queryTreeList(@RequestParam(name = "ids", required = false) String ids) {
		 int size = deviceTypeService.list().size();
		 if (size == 0){
			 DeviceType deviceType = new DeviceType();
			 deviceType.setId(IdWorker.getIdStr());
			 deviceType.setName("类别");
			 deviceType.setPId(null);
			 deviceTypeService.save(deviceType);
		 }
		 List<SelectTreeNode> baseOrganList = deviceTypeService.queryTreeList(ids);
		 return Result.OK(baseOrganList);
	 }
	/**
	 * 添加
	 * @param deviceType
	 * @return
	 */
	@AutoLog(value = "设备类别-添加")
	@ApiOperation(value="设备类别-添加", notes="设备类别-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceType deviceType) {
		deviceTypeService.save(deviceType);
		DeviceType byId = deviceTypeService.getById(deviceType.getId());
		LogUtil.setOperate(byId.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param deviceType
	 * @return
	 */
	@AutoLog(value = "类别管理-编辑")
	@ApiOperation(value="类别管理-编辑", notes="类别管理-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody DeviceType deviceType) {
		deviceTypeService.updateById(deviceType);
		DeviceType byId = deviceTypeService.getById(deviceType.getId());
		LogUtil.setOperate(byId.getName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "类别管理-删除")
	@ApiOperation(value="类别管理-删除", notes="类别管理-删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		DeviceType byId = deviceTypeService.getById(id);
		LogUtil.setOperate(byId.getName());
		deviceTypeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="类别管理-批量删除", notes="类别管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="类别管理-通过id查询", notes="类别管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceType deviceType = deviceTypeService.getById(id);
		return Result.OK(deviceType);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param deviceType
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, DeviceType deviceType) {
      return super.exportXls(request, deviceType, DeviceType.class, "类别管理");
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
      return super.importExcel(request, response, DeviceType.class);
  }

}
