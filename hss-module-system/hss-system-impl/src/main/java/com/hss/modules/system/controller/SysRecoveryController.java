package com.hss.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.system.entity.SysRecovery;
import com.hss.modules.system.service.ISysRecoveryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

 /**
 * @Description: 系统备份
 * @Author: zpc
 * @Date:   2023-05-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags="系统备份")
@RestController
@RequestMapping("/system/sysRecovery")
public class SysRecoveryController extends HssController<SysRecovery, ISysRecoveryService> {
	@Autowired
	private ISysRecoveryService sysRecoveryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysRecovery
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "系统备份-分页列表查询")
	@ApiOperation(value="系统备份-分页列表查询", notes="系统备份-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysRecovery sysRecovery,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysRecovery> queryWrapper = QueryGenerator.initQueryWrapper(sysRecovery, req.getParameterMap());
		Page<SysRecovery> page = new Page<SysRecovery>(pageNo, pageSize);
		IPage<SysRecovery> pageList = sysRecoveryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	 /**
	  * 手工执行恢复
	  *
	  * @param sysRecovery
	  * @return
	  */
	 @AutoLog(value = "系统备份-手工执行恢复")
	 @ApiOperation(value="系统备份-手工执行恢复", notes="系统备份-手工执行恢复")
	 @PostMapping(value = "/recovery")
	 public Result<?> recovery(@RequestBody SysRecovery sysRecovery) {
		 return sysRecoveryService.recovery(sysRecovery);
	 }

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "系统备份-通过id删除")
	@ApiOperation(value="系统备份-通过id删除", notes="系统备份-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysRecoveryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "系统备份-批量删除")
	@ApiOperation(value="系统备份-批量删除", notes="系统备份-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysRecoveryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "系统备份-通过id查询")
	@ApiOperation(value="系统备份-通过id查询", notes="系统备份-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysRecovery sysRecovery = sysRecoveryService.getById(id);
		return Result.OK(sysRecovery);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sysRecovery
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SysRecovery sysRecovery) {
      return super.exportXls(request, sysRecovery, SysRecovery.class, "系统备份");
  }
}
