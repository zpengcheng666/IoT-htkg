package com.hss.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.system.entity.BaseLog;
import com.hss.modules.system.service.IBaseLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * @Description: 日志表
 * @Author: zpc
 * @Date:   2022-12-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags="日志表")
@RestController
@RequestMapping("/system/baseLog")
public class BaseLogController extends HssController<BaseLog, IBaseLogService> {
	@Autowired
	private IBaseLogService baseLogService;

	/**
	 * 分页列表查询
	 *
	 * @param baseLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="日志表-带查询条件的分页列表查询", notes="日志表-带查询条件的分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BaseLog>> queryPageList(BaseLog baseLog,
												@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												HttpServletRequest req) {

		LambdaQueryWrapper<BaseLog> queryWrapper = getLogLambdaQuery(baseLog);
		Page<BaseLog> page = new Page<>(pageNo,pageSize);
		IPage<BaseLog> pageList =  baseLogService.page(page, queryWrapper);

		return Result.OK(pageList);
	}

	@NotNull
	private static LambdaQueryWrapper<BaseLog> getLogLambdaQuery(BaseLog baseLog){
		LambdaQueryWrapper<BaseLog> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.orderByDesc(BaseLog::getStateTime);
		if(OConvertUtils.isNotEmpty(baseLog.getUsername())){
			//根据操作人姓名、操作时间进行条件查询
			queryWrapper.like(BaseLog::getUsername,baseLog.getUsername());
		}

		if (OConvertUtils.isNotEmpty(baseLog.getOperateContent())){
			//按照操作内容模糊查询
			queryWrapper.like(BaseLog::getOperateContent,baseLog.getOperateContent());
		}

		if(OConvertUtils.isNotEmpty(baseLog.getBeginTime())){
			queryWrapper.ge(BaseLog::getStateTime,baseLog.getBeginTime());
		}
		if(OConvertUtils.isNotEmpty(baseLog.getEndTime())){
			queryWrapper.le(BaseLog::getStateTime,baseLog.getEndTime());
		}
		return queryWrapper;
	}
	/**
	 * 添加
	 *
	 * @param baseLog
	 * @return
	 */
	@AutoLog(value = "日志表-添加")
	@ApiOperation(value="日志表-添加", notes="日志表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BaseLog baseLog) {
		baseLogService.save(baseLog);
		BaseLog byId = baseLogService.getById(baseLog.getId());
		LogUtil.setOperate(byId.getUsername());
		return Result.OK("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param baseLog
	 * @return
	 */
	@AutoLog(value = "日志表-编辑")
	@ApiOperation(value="日志表-编辑", notes="日志表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody BaseLog baseLog) {
		baseLogService.updateById(baseLog);
		BaseLog byId = baseLogService.getById(baseLog.getId());
		LogUtil.setOperate(byId.getUsername());
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "日志表-删除")
	@ApiOperation(value="日志表-删除", notes="日志表-删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		BaseLog byId = baseLogService.getById(id);
		LogUtil.setOperate(byId.getUsername());
		baseLogService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="日志表-批量删除", notes="日志表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.baseLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="日志表-通过id查询", notes="日志表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BaseLog baseLog = baseLogService.getById(id);
		return Result.OK(baseLog);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param baseLog
	 */
	@ApiOperation(value="日志表-导出excel", notes="日志表-导出excel")
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, BaseLog baseLog) {
		LambdaQueryWrapper<BaseLog> queryWrapper = getLogLambdaQuery(baseLog);
		return super.exportXls(request,queryWrapper, baseLog, BaseLog.class, "日志表");
	}

	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return super.importExcel(request, response, BaseLog.class);
	}

}
