package com.hss.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.system.vo.SelectTreeNode;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.entity.BaseLocation;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.system.service.IBaseLocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

 /**
 * @Description: 位置信息
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags="位置信息")
@RestController
@RequestMapping("/system/baseLocation")
public class BaseLocationController extends HssController<BaseLocation, IBaseLocationService> {
	@Autowired
	private IBaseLocationService baseLocationService;

	@Autowired
	private IBaseDictDataService baseDictDataService;

	/**
	 * 分页列表查询
	 *
	 * @param baseLocation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="存储位置信息-分页列表查询", notes="存储位置信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BaseLocation baseLocation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BaseLocation> queryWrapper = QueryGenerator.initQueryWrapper(baseLocation, req.getParameterMap());
		Page<BaseLocation> page = new Page<>(pageNo, pageSize);
		IPage<BaseLocation> pageList = baseLocationService.page(page, queryWrapper);
		pageList.getRecords().forEach(e ->{
			BaseDictData classId = baseDictDataService.getById(e.getClassId());
			e.setClassName(classId == null ? "" : classId.getName());
		});
		return Result.OK(pageList);
	}

	 /**
	  * 位置信息-树状查询
	  * @return
	  */
	 @ApiOperation(value="位置信息-结果树查询", notes="位置信息-结果树查询")
	 @RequestMapping(value = "/queryTreeList",method = RequestMethod.GET)
	 public Result<List<SelectTreeNode>> queryTreeList(@RequestParam(name = "ids", required = false) String ids) {
		 List<SelectTreeNode> baseLocationList = baseLocationService.queryTreeList(ids);
		 return Result.OK(baseLocationList);
	 }
	
	/**
	 * 添加
	 *
	 * @param baseLocation
	 * @return
	 */
	@AutoLog(value = "存储位置信息-添加")
	@ApiOperation(value="存储位置信息-添加", notes="存储位置信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BaseLocation baseLocation) {
		BaseDictData data = baseDictDataService.getById(baseLocation.getClassId());
		baseLocation.setClassName(data.getName());
		baseLocationService.save(baseLocation);
		BaseLocation byId = baseLocationService.getById(baseLocation.getId());
		LogUtil.setOperate(byId.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param baseLocation
	 * @return
	 */
	@AutoLog(value = "存储位置信息-编辑")
	@ApiOperation(value="存储位置信息-编辑", notes="存储位置信息-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody BaseLocation baseLocation) {
		BaseDictData data = baseDictDataService.getById(baseLocation.getClassId());
		baseLocation.setClassName(data.getName());
		baseLocationService.updateById(baseLocation);
		BaseLocation byId = baseLocationService.getById(baseLocation.getId());
		LogUtil.setOperate(byId.getName());
		return Result.OK("编辑成功!");
	}
	 /**
	  * 通过id查询
	  * @param id
	  * @return
	  */
	 @ApiOperation(value = "位置信息-通过id查询", notes = "位置信息-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		 BaseLocation locationServiceById = baseLocationService.getById(id);
		 return Result.OK(locationServiceById);
	 }
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "存储位置信息-删除")
	@ApiOperation(value="存储位置信息-删除", notes="存储位置信息-删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		BaseLocation byId = baseLocationService.getById(id);
		LogUtil.setOperate(byId.getName());
		baseLocationService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="存储位置信息-批量删除", notes="存储位置信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.baseLocationService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

}
