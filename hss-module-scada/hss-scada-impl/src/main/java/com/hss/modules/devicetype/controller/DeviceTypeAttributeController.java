package com.hss.modules.devicetype.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.model.DeviceTypeAttributeSceneListByType;
import com.hss.modules.devicetype.service.IDeviceTypeAttributeService;
import com.hss.modules.scada.model.ConSheBeiOptions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* @description: 设备类型管理属性管理
* @author zpc
* @date 2024/3/20 14:59
* @version 1.0
*/
@Slf4j
@Api(tags="设备类型管理属性管理")
@RestController
@RequestMapping("/scada/deviceTypeAttribyte")
public class DeviceTypeAttributeController extends HssController<DeviceTypeAttribute, IDeviceTypeAttributeService> {
	@Autowired
	private IDeviceTypeAttributeService deviceTypeAttribyteService;


	/**
	 * 分页列表查询
	 *
	 * @param deviceTypeAttribute
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="设备类型管理属性管理-分页列表查询", notes="设备类型管理属性管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DeviceTypeAttribute>> queryPageList(DeviceTypeAttribute deviceTypeAttribute,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		if (StringUtils.isNotBlank(deviceTypeAttribute.getName())){
			deviceTypeAttribute.setName("*" + deviceTypeAttribute.getName() + "*");
		}
		QueryWrapper<DeviceTypeAttribute> queryWrapper = QueryGenerator.initQueryWrapper(deviceTypeAttribute, req.getParameterMap());
		queryWrapper.orderByAsc("SORT_NUMBER");
		Page<DeviceTypeAttribute> page = new Page<DeviceTypeAttribute>(pageNo, pageSize);
		IPage<DeviceTypeAttribute> pageList = deviceTypeAttribyteService.pageList(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * @description: 根据设备类型id查询-设备属性下拉框
	 * @author zpc
	 * @date 2023/1/9 18:38
	 * @version 1.0
	 */
	@ApiOperation(value="根据设备类型id查询-设备属性下拉框", notes="根据设备类型id查询-设备属性下拉框")
	@GetMapping(value = "/devClassIdBydevAttr")
	public Result<?> devClassIdBydevAttr(String devClassId) {
		//1.通过设备类型id获取属性
		LambdaQueryWrapper<DeviceTypeAttribute> queryWrapper = new LambdaQueryWrapper<>();
		if (OConvertUtils.isNotEmpty(devClassId)) {
			queryWrapper.eq(DeviceTypeAttribute::getTypeId, devClassId);//根据设备类型id查询-设备属性
		}
		queryWrapper.orderByAsc(DeviceTypeAttribute::getSortNumber);

		List<DeviceTypeAttribute> list = deviceTypeAttribyteService.list(queryWrapper);

		List<ConSheBeiOptions> collect = list.stream().map(k -> {
			ConSheBeiOptions temp = new ConSheBeiOptions();
			temp.setId(k.getId());//属性id
			temp.setName(k.getName());//属性名称
			temp.setEnName(k.getCategory());//属性英文名称

			return temp;
		}).collect(Collectors.toList());

		return Result.OK(collect);
	}

	/**
	 * @description: 根据设备类型id查询-设备属性，已有的设备属性过滤
	 * @author zpc
	 * @date 2023/1/12 10:36
	 * @version 1.0
	 */
	@ApiOperation(value="根据设备类型id查询-设备属性，已有的设备属性过滤", notes="根据设备类型id查询-设备属性，已有的设备属性过滤")
	@GetMapping(value = "/devClassIdByAttrFilter")
	public Result<?> devClassIdByAttrFilter(String devClassId){
		List<DeviceTypeAttribute> attributeList = deviceTypeAttribyteService.queryDevClassIdByAttrFilter(devClassId);

		List<ConSheBeiOptions> collect = attributeList.stream().map(k -> {
			ConSheBeiOptions temp = new ConSheBeiOptions();
			temp.setId(k.getId());//属性id
			temp.setName(k.getName());//属性名称
			temp.setEnName(k.getCategory());//属性英文名称

			return temp;
		}).collect(Collectors.toList());

		return Result.OK(collect);
	}

	/**
	 * 添加
	 *
	 * @param deviceTypeAttribute
	 * @return
	 */
	@AutoLog(value = "设备类型管理添加属性")
	@ApiOperation(value="设备类型管理属性管理-添加", notes="设备类型管理属性管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DeviceTypeAttribute deviceTypeAttribute) {
		deviceTypeAttribyteService.add(deviceTypeAttribute);
		LogUtil.setOperate(deviceTypeAttribute.getName());
		return Result.OK("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param deviceTypeAttribute
	 * @return
	 */
	@AutoLog(value = "设备类型管理编辑属性")
	@ApiOperation(value="设备类型管理属性管理-编辑", notes="设备类型管理属性管理-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody DeviceTypeAttribute deviceTypeAttribute) {
		deviceTypeAttribyteService.edit(deviceTypeAttribute);
		LogUtil.setOperate(deviceTypeAttribute.getName());
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备类型管理删除属性")
	@ApiOperation(value="设备类型管理属性管理-通过id删除", notes="设备类型管理属性管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		deviceTypeAttribyteService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设备类型管理批量删除属性")
	@ApiOperation(value="设备类型管理属性管理-批量删除", notes="设备类型管理属性管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.deviceTypeAttribyteService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="设备类型管理属性管理-通过id查询", notes="设备类型管理属性管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<DeviceTypeAttribute> queryById(@RequestParam(name="id",required=true) String id) {
		DeviceTypeAttribute deviceTypeAttribute = deviceTypeAttribyteService.getById(id);
		return Result.OK(deviceTypeAttribute);
	}



	@ApiOperation(value="by组态查询设备属性", notes="by组态查询设备属性")
	@GetMapping(value = "/sceneListByType")
	public Result<DeviceTypeAttributeSceneListByType> sceneListByType(
			@ApiParam(value = "设备类型", required = true) @RequestParam(name="type") String type,
			@ApiParam(value = "设备Id") @RequestParam(name="deviceId",required = false) String deviceId
	) {
		DeviceTypeAttributeSceneListByType result = deviceTypeAttribyteService.sceneListByType(type, deviceId);
		return Result.OK(result);
	}

}
