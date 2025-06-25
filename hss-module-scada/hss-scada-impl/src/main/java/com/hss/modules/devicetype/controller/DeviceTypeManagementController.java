package com.hss.modules.devicetype.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.devicetype.entity.DeviceTypeManagement;
import com.hss.modules.devicetype.model.DeviceTypeExcel;
import com.hss.modules.devicetype.service.IDeviceTypeManagementService;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.model.ConSheBeiDoorOptions;
import com.hss.modules.scada.model.ConSheBeiOptions;
import com.hss.modules.scada.model.DeviceTypeStrategyList;
import com.hss.modules.scada.service.IConSheBeiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* @description: 设备类型管理
* @author zpc
* @date 2024/3/20 14:58
* @version 1.0
*/
@Slf4j
@Api(tags="设备类型管理")
@RestController
@RequestMapping("/scada/deviceTypeManagement")
public class DeviceTypeManagementController extends HssController<DeviceTypeManagement, IDeviceTypeManagementService> {
	@Autowired
	private IDeviceTypeManagementService deviceTypeManagementService;

	@Autowired
	private IConSheBeiService conSheBeiService;

	@ApiOperation(value="设备类型管理-分页列表查询", notes="设备类型管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DeviceTypeManagement>> queryPageList(@ApiParam("name") @RequestParam(value = "name",required = false) String name,
															 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Page<DeviceTypeManagement> page = new Page<DeviceTypeManagement>(pageNo, pageSize);
		IPage<DeviceTypeManagement> pageList = deviceTypeManagementService.getPage(page, name);
		return Result.OK(pageList);
	}

	@ApiOperation(value="获取全部", notes="获取全部")
	@GetMapping(value = "/listAll")
	public Result<List<DeviceTypeManagement>> listAll() {
		List<DeviceTypeManagement> list = deviceTypeManagementService.list();
		return Result.OK(list);
	}

	/**
	 * @description: 设备类型下拉框
	 * @author zpc
	 * @date 2023/1/9 18:38
	 * @version 1.0
	 */
	@ApiOperation(value="场景设备类型下拉框", notes="场景设备类型下拉框")
	@GetMapping(value = "/conSheBeiOptiongs")
	public Result<?> conSheBeiOptiongs(ConSheBeiDoorOptions optionModel){
		List<DeviceTypeManagement> list = deviceTypeManagementService.list();
		List<ConSheBeiDoorOptions> collect = list.stream().map(e -> {
			ConSheBeiDoorOptions model = new ConSheBeiDoorOptions();
			model.setId(e.getId());
			model.setName(e.getName());
			return model;
		}).collect(Collectors.toList());
		return Result.OK(collect);
	}

	/**
	 * @description: 根据设备类型id查询关联设备
	 * @author zpc
	 * @date 2023/1/9 18:38
	 * @version 1.0
	 */
	@ApiOperation(value="根据设备类型id查询关联设备", notes="根据设备类型id查询关联设备")
	@GetMapping(value = "/devTypeIdBydevs")
	public Result<?> devTypeIdBydevs(String devTypeIds) {
		//1.通过设备类型id获取设备
		LambdaQueryWrapper<ConSheBei> queryWrapper = new LambdaQueryWrapper<>();
		if (OConvertUtils.isNotEmpty(devTypeIds)) {
			String[] split = devTypeIds.split(",");
			List<String> stringList = Arrays.asList(split);
			//根据设备类型id查询关联设备
			queryWrapper.in(ConSheBei::getDeviceTypeId, stringList);
		}
		List<ConSheBei> list = conSheBeiService.list(queryWrapper);
		if (CollectionUtils.isEmpty(list)) {
			return new Result<>();
		}
		List<ConSheBeiOptions> collect = list.stream().map(k -> {
			ConSheBeiOptions temp = new ConSheBeiOptions();
			//设备id
			temp.setId(k.getId());
			//设备名称
			temp.setName(k.getName());
			return temp;
		}).collect(Collectors.toList());
		return Result.OK(collect);
	}

	/**
	 * 添加设备类型
	 *
	 * @param deviceTypeManagement
	 * @return
	 */
	@AutoLog(value = "添加设备类型")
	@ApiOperation(value="设备类型管理-添加", notes="设备类型管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceTypeManagement deviceTypeManagement) {
		deviceTypeManagementService.add(deviceTypeManagement);
		LogUtil.setOperate(deviceTypeManagement.getName());
		return Result.OK("添加成功！");
	}

	@AutoLog(value = "复制设备类型")
	@ApiOperation(value="设备类型管理-添加", notes="设备类型管理-添加")
	@PostMapping(value = "/copy")
	public Result<?> copy(@RequestBody DeviceTypeManagement deviceTypeManagement) {
		deviceTypeManagementService.copy(deviceTypeManagement);
		LogUtil.setOperate(deviceTypeManagement.getName());
		return Result.OK("添加成功！");
	}

	/**
	 * 编辑设备类型
	 *
	 * @param deviceTypeManagement
	 * @return
	 */
	@AutoLog(value = "编辑设备类型")
	@ApiOperation(value="设备类型管理-编辑", notes="设备类型管理-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody DeviceTypeManagement deviceTypeManagement) {
		deviceTypeManagementService.edit(deviceTypeManagement);
		LogUtil.setOperate(deviceTypeManagement.getName());
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除设备类型
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "删除设备类型")
	@ApiOperation(value="设备类型管理-通过id删除", notes="设备类型管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		DeviceTypeManagement byId = deviceTypeManagementService.getById(id);
		deviceTypeManagementService.deleteById(id);
		LogUtil.setOperate(byId.getName());
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除设备类型
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "批量删除设备类型")
	@ApiOperation(value="设备类型管理-批量删除", notes="设备类型管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceTypeManagementService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="设备类型管理-通过id查询", notes="设备类型管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<DeviceTypeManagement> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceTypeManagement deviceTypeManagement = deviceTypeManagementService.getById(id);
		return Result.OK(deviceTypeManagement);
	}


	@ApiOperation(value="设备类型管理-策略列表", notes="设备类型管理-策略列表")
	@GetMapping(value = "/listStrategy/{typeId}")
	public Result<List<DeviceTypeStrategyList>> listStrategy(@PathVariable("typeId") String typeId) {
		List<DeviceTypeStrategyList> list = deviceTypeManagementService.listStrategy(typeId);
		return Result.OK(list);
	}

	@GetMapping(value = "/addByJsonFile")
	@ApiOperation(value="设备类型管理-文件导入", notes="设备类型管理-文件导入")
	public Result<?> addByJsonFile(@RequestParam("dirName") String dirName) {
		deviceTypeManagementService.addByJsonFile(dirName);
		return Result.OK();
	}

	@GetMapping(value = "/syncAttrAndStrategy")
	@ApiOperation(value="同步属性和策略", notes="同步属性和策略")
	public Result<?> syncAttrAndStrategy() {
		deviceTypeManagementService.syncAttrAndStrategy();
		return Result.OK();
	}

	@GetMapping(value = "/exportExcel")
	@ApiOperation(value="导出excel", notes="导出excel")
	public ModelAndView addByJsonFile() {
		List<DeviceTypeExcel> list = deviceTypeManagementService.listExcel();
		return exportXls(list, DeviceTypeExcel.class, "设备类型");
	}

	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request) {
		List<DeviceTypeExcel> list = importExcel(request, DeviceTypeExcel.class);
		deviceTypeManagementService.importExcel(list);
		return Result.ok("导入成功");

	}





}
