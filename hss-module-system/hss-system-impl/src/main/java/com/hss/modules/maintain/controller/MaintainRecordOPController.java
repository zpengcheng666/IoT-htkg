package com.hss.modules.maintain.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.maintain.entity.MaintainRecordOP;
import com.hss.modules.maintain.service.IMaintainRecordOPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

 /**
 * @Description: 保养任务-新增修改-关联保养设备
 * @Author: zpc
 * @Date:   2022-12-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="保养任务-新增修改-关联保养设备")
@RestController
@RequestMapping("/maintain/maintainRecordOP")
public class MaintainRecordOPController extends HssController<MaintainRecordOP, IMaintainRecordOPService> {
	@Autowired
	private IMaintainRecordOPService maintainRecordOPService;
	
	/**
	 * 分页列表查询
	 *
	 * @param maintainRecordOP
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
//	@AutoLog(value = "保养任务-新增修改-关联保养设备-分页列表查询")
	@ApiOperation(value="保养任务-新增修改-关联保养设备-分页列表查询", notes="保养任务-新增修改-关联保养设备-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MaintainRecordOP maintainRecordOP,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MaintainRecordOP> queryWrapper = QueryGenerator.initQueryWrapper(maintainRecordOP, req.getParameterMap());
		Page<MaintainRecordOP> page = new Page<MaintainRecordOP>(pageNo, pageSize);
		IPage<MaintainRecordOP> pageList = maintainRecordOPService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param maintainRecordOP
	 * @return
	 */
//	@AutoLog(value = "保养任务-新增修改-关联保养设备-添加")
	@ApiOperation(value="保养任务-新增修改-关联保养设备-添加", notes="保养任务-新增修改-关联保养设备-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MaintainRecordOP maintainRecordOP) {
		maintainRecordOPService.save(maintainRecordOP);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param maintainRecordOP
	 * @return
	 */
//	@AutoLog(value = "保养任务-新增修改-关联保养设备-编辑")
	@ApiOperation(value="保养任务-新增修改-关联保养设备-编辑", notes="保养任务-新增修改-关联保养设备-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody MaintainRecordOP maintainRecordOP) {
		maintainRecordOPService.updateById(maintainRecordOP);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
//	@AutoLog(value = "保养任务-新增修改-关联保养设备-通过id删除")
	@ApiOperation(value="保养任务-新增修改-关联保养设备-通过id删除", notes="保养任务-新增修改-关联保养设备-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		maintainRecordOPService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
//	@AutoLog(value = "保养任务-新增修改-关联保养设备-批量删除")
	@ApiOperation(value="保养任务-新增修改-关联保养设备-批量删除", notes="保养任务-新增修改-关联保养设备-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.maintainRecordOPService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
//	@AutoLog(value = "保养任务-新增修改-关联保养设备-通过id查询")
	@ApiOperation(value="保养任务-新增修改-关联保养设备-通过id查询", notes="保养任务-新增修改-关联保养设备-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MaintainRecordOP maintainRecordOP = maintainRecordOPService.getById(id);
		return Result.OK(maintainRecordOP);
	}

}
