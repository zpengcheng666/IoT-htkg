package com.hss.modules.system.controller;

import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.vo.SelectTreeNode;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.system.entity.BaseOrgan;
import com.hss.modules.system.service.IBaseOrganService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

 /**
 * @Description: 组织机构
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags="组织机构")
@RestController
@RequestMapping("/system/baseOrgan")
public class BaseOrganController extends HssController<BaseOrgan, IBaseOrganService> {
	@Autowired
	private IBaseOrganService baseOrganService;

	 /**
	  * 组织机构-树状查询
	  * @return
	  */
	 @ApiOperation(value="组织机构-结果树查询", notes="组织机构-结果树查询")
	 @RequestMapping(value = "/queryTreeList",method = RequestMethod.GET)
	 public Result<List<SelectTreeNode>> queryTreeList(@RequestParam(name = "ids", required = false) String ids) {
		 List<SelectTreeNode> baseOrganList = baseOrganService.queryTreeList(ids);
		 return Result.OK(baseOrganList);
	 }
	/**
	 * 添加
	 *
	 * @param baseOrgan
	 * @return
	 */
	@AutoLog(value = "组织机构-添加")
	@ApiOperation(value="组织机构-添加", notes="组织机构-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BaseOrgan baseOrgan) {
		baseOrganService.save(baseOrgan);
		BaseOrgan byId = baseOrganService.getById(baseOrgan.getId());
		LogUtil.setOperate(byId.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param baseOrgan
	 * @return
	 */
	@AutoLog(value = "组织机构-编辑")
	@ApiOperation(value="组织机构-编辑", notes="组织机构-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody BaseOrgan baseOrgan) {
		baseOrganService.updateById(baseOrgan);
		BaseOrgan byId = baseOrganService.getById(baseOrgan.getId());
		LogUtil.setOperate(byId.getName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "组织机构-删除")
	@ApiOperation(value="组织机构-删除", notes="组织机构-删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		BaseOrgan byId = baseOrganService.getById(id);
		LogUtil.setOperate(byId.getName());
		baseOrganService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="组织机构-批量删除", notes="组织机构-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.baseOrganService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

}
