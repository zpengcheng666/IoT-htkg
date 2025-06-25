package com.hss.modules.spare.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.spare.entity.Area;
import com.hss.modules.spare.service.IAreaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.hss.core.common.system.base.controller.HssController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 库区
 * @Author: zpc
 * @Date:   2024-04-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="库区")
@RestController
@RequestMapping("/spare/area")
public class AreaController extends HssController<Area, IAreaService> {
	@Autowired
	private IAreaService areaService;
	
	/**
	 * 分页列表查询
	 *
	 * @param area
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="库区-分页列表查询", notes="库区-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Area area,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Area> queryWrapper = QueryGenerator.initQueryWrapper(area, req.getParameterMap());
		Page<Area> page = new Page<>(pageNo, pageSize);
		IPage<Area> pageList = areaService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @ApiOperation(value="通过仓库id查询对应库区", notes="通过仓库id查询对应库区")
	 @GetMapping(value = "/queryByWarehouseId")
	 public Result<?> queryByWarehouseId(@RequestParam(name="warehouseId") String warehouseId) {
		 QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("WAREHOUSE_ID", warehouseId);
		 List<Area> list = areaService.list(queryWrapper);
		 return Result.OK(list);
	 }
	
	/**
	 * 添加
	 *
	 * @param area
	 * @return
	 */
	@AutoLog(value = "库区-添加")
	@ApiOperation(value="库区-添加", notes="库区-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Area area) {
		areaService.save(area);
		LogUtil.setOperate(area.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param area
	 * @return
	 */
	@AutoLog(value = "库区-编辑")
	@ApiOperation(value="库区-编辑", notes="库区-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody Area area) {
		areaService.updateById(area);
		LogUtil.setOperate(area.getName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库区-通过id删除")
	@ApiOperation(value="库区-通过id删除", notes="库区-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id") String id) {
		LogUtil.setOperate(areaService.getById(id).getName());
		areaService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "库区-批量删除")
	@ApiOperation(value="库区-批量删除", notes="库区-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.areaService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="库区-通过id查询", notes="库区-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Area area = areaService.getById(id);
		return Result.OK(area);
	}

}
