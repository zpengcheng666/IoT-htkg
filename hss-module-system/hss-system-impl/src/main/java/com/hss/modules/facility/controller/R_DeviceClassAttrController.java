package com.hss.modules.facility.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.facility.entity.R_DeviceClassAttr;
import com.hss.modules.facility.service.IR_DeviceClassAttrService;
import com.hss.modules.system.model.AttrModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

 /**
 * @Description: 设备类别与设备属性中间表
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags="设备类别与设备属性中间表")
@RestController
@RequestMapping("/facility/r_DeviceClassAttr")
public class R_DeviceClassAttrController extends HssController<R_DeviceClassAttr, IR_DeviceClassAttrService> {
	@Autowired
	private IR_DeviceClassAttrService r_DeviceClassAttrService;
	
	/**
	 * 分页列表查询
	 * @param r_DeviceClassAttr
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="设备类别与设备属性中间表-分页列表查询", notes="设备类别与设备属性中间表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(R_DeviceClassAttr r_DeviceClassAttr,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<R_DeviceClassAttr> queryWrapper = QueryGenerator.initQueryWrapper(r_DeviceClassAttr, req.getParameterMap());
		Page<R_DeviceClassAttr> page = new Page<R_DeviceClassAttr>(pageNo, pageSize);
		IPage<R_DeviceClassAttr> pageList = r_DeviceClassAttrService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * 设备类别与设备属性中间表-添加
	 * @param attrModel
	 * @return
	 */
	@ApiOperation(value="设备类别与设备属性中间表-添加", notes="设备类别与设备属性中间表-添加")
	@RequestMapping(value = "/addAttrAndClass", method = {RequestMethod.POST})
	public Result<?> addAttrAndClass(@RequestBody AttrModel attrModel) {
		r_DeviceClassAttrService.saveDeviceClassAttr(attrModel);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 * @param r_DeviceClassAttr
	 * @return
	 */
	@ApiOperation(value="设备类别与设备属性中间表-编辑", notes="设备类别与设备属性中间表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.POST})
	public Result<?> edit(@RequestBody R_DeviceClassAttr r_DeviceClassAttr) {
		r_DeviceClassAttrService.updateById(r_DeviceClassAttr);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@ApiOperation(value="设备类别与设备属性中间表-通过id删除", notes="设备类别与设备属性中间表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		r_DeviceClassAttrService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="设备类别与设备属性中间表-批量删除", notes="设备类别与设备属性中间表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.r_DeviceClassAttrService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

}
