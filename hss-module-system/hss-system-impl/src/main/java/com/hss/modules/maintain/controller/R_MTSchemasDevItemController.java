package com.hss.modules.maintain.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.maintain.entity.R_MTSchemasDevItem;
import com.hss.modules.maintain.service.IR_MTSchemasDevItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

 /**
 * @Description: 保养方案-设备类别-保养类别关系表
 * @Author: zpc
 * @Date:   2022-12-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="保养方案-设备类别-保养类别关系表")
@RestController
@RequestMapping("/maintain/r_MTSchemasDevItem")
public class R_MTSchemasDevItemController extends HssController<R_MTSchemasDevItem, IR_MTSchemasDevItemService> {
	@Autowired
	private IR_MTSchemasDevItemService r_MTSchemasDevItemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param r_MTSchemasDevItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="保养方案-设备类别-保养类别关系表-分页列表查询", notes="保养方案-设备类别-保养类别关系表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(R_MTSchemasDevItem r_MTSchemasDevItem,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<R_MTSchemasDevItem> queryWrapper = QueryGenerator.initQueryWrapper(r_MTSchemasDevItem, req.getParameterMap());
		Page<R_MTSchemasDevItem> page = new Page<R_MTSchemasDevItem>(pageNo, pageSize);
		IPage<R_MTSchemasDevItem> pageList = r_MTSchemasDevItemService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param r_MTSchemasDevItem
	 * @return
	 */
	@ApiOperation(value="保养方案-设备类别-保养类别关系表-添加", notes="保养方案-设备类别-保养类别关系表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody R_MTSchemasDevItem r_MTSchemasDevItem) {
		r_MTSchemasDevItemService.save(r_MTSchemasDevItem);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param r_MTSchemasDevItem
	 * @return
	 */
	@ApiOperation(value="保养方案-设备类别-保养类别关系表-编辑", notes="保养方案-设备类别-保养类别关系表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody R_MTSchemasDevItem r_MTSchemasDevItem) {
		r_MTSchemasDevItemService.updateById(r_MTSchemasDevItem);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="保养方案-设备类别-保养类别关系表-通过id删除", notes="保养方案-设备类别-保养类别关系表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		r_MTSchemasDevItemService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="保养方案-设备类别-保养类别关系表-批量删除", notes="保养方案-设备类别-保养类别关系表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.r_MTSchemasDevItemService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="保养方案-设备类别-保养类别关系表-通过id查询", notes="保养方案-设备类别-保养类别关系表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		R_MTSchemasDevItem r_MTSchemasDevItem = r_MTSchemasDevItemService.getById(id);
		return Result.OK(r_MTSchemasDevItem);
	}
}
