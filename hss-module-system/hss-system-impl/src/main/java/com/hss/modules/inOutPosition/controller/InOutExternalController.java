package com.hss.modules.inOutPosition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.inOutPosition.entity.InOutExternal;
import com.hss.modules.inOutPosition.service.IInOutExternalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 外部人员审批表
 * @Author: zpc
 * @Date:   2022-12-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="外部人员审批表")
@RestController
@RequestMapping("/inOutPosition/inOutExternal")
public class InOutExternalController extends HssController<InOutExternal, IInOutExternalService> {
	@Autowired
	private IInOutExternalService inOutExternalService;
	
	/**
	 * 分页列表查询
	 *
	 * @param inOutExternal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="外部人员审批表-分页列表查询", notes="外部人员审批表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(InOutExternal inOutExternal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<InOutExternal> queryWrapper = QueryGenerator.initQueryWrapper(inOutExternal, req.getParameterMap());
		Page<InOutExternal> page = new Page<InOutExternal>(pageNo, pageSize);
		IPage<InOutExternal> pageList = inOutExternalService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param inOutExternal
	 * @return
	 */
	@AutoLog(value = "外部人员-添加")
	@ApiOperation(value="外部人员-添加", notes="外部人员-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody InOutExternal inOutExternal) {
		inOutExternalService.save(inOutExternal);
		InOutExternal byId = inOutExternalService.getById(inOutExternal.getId());
		LogUtil.setOperate(byId.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param inOutExternal
	 * @return
	 */
	@AutoLog(value = "外部人员审批表-编辑")
	@ApiOperation(value="外部人员审批表-编辑", notes="外部人员审批表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody InOutExternal inOutExternal) {
		inOutExternalService.updateById(inOutExternal);
		InOutExternal byId = inOutExternalService.getById(inOutExternal.getId());
		LogUtil.setOperate(byId.getName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "外部人员审批表-通过id删除")
	@ApiOperation(value="外部人员审批表-通过id删除", notes="外部人员审批表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		InOutExternal byId = inOutExternalService.getById(id);
		LogUtil.setOperate(byId.getName());
		inOutExternalService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="外部人员审批表-批量删除", notes="外部人员审批表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.inOutExternalService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="外部人员审批表-通过id查询", notes="外部人员审批表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		InOutExternal inOutExternal = inOutExternalService.getById(id);
		return Result.OK(inOutExternal);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param inOutExternal
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, InOutExternal inOutExternal) {
      return super.exportXls(request, inOutExternal, InOutExternal.class, "外部人员审批表");
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
      return super.importExcel(request, response, InOutExternal.class);
  }

}
