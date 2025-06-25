package com.hss.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.system.entity.SysBackup;
import com.hss.modules.system.service.ISysBackupService;
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
@RequestMapping("/system/sysBackup")
public class SysBackupController extends HssController<SysBackup, ISysBackupService> {
	@Autowired
	private ISysBackupService sysBackupService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysBackup
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="系统备份-分页列表查询", notes="系统备份-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysBackup sysBackup,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysBackup> queryWrapper = QueryGenerator.initQueryWrapper(sysBackup, req.getParameterMap());
		queryWrapper.orderByDesc("START_TIME");
		Page<SysBackup> page = new Page<SysBackup>(pageNo, pageSize);
		IPage<SysBackup> pageList = sysBackupService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 手动备份
	  * @param sysBackup
	  * @return
	  */
	 @AutoLog(value = "系统备份-手动备份")
	 @ApiOperation(value="系统备份-手动备份", notes="系统备份-手动备份")
	 @PostMapping(value = "/backup")
	 public Result<?> backup(@RequestBody SysBackup sysBackup) {
		 sysBackupService.backup(sysBackup);
		 return Result.OK("手动备份成功！");
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
		sysBackupService.removeById(id);
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
		this.sysBackupService.removeByIds(Arrays.asList(ids.split(",")));
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
		SysBackup sysBackup = sysBackupService.getById(id);
		return Result.OK(sysBackup);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sysBackup
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SysBackup sysBackup) {
      return super.exportXls(request, sysBackup, SysBackup.class, "系统备份");
  }
}
