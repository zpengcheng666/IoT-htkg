package com.hss.modules.inOutPosition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.inOutPosition.entity.InOutInternal;
import com.hss.modules.inOutPosition.service.IInOutInternalService;
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
 * @Description: 内部人员审批表
 * @Author: zpc
 * @Date:   2022-12-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="内部人员审批表")
@RestController
@RequestMapping("/inOutPosition/inOutInternal")
public class InOutInternalController extends HssController<InOutInternal, IInOutInternalService> {
	@Autowired
	private IInOutInternalService inOutInternalService;
	
	/**
	 * 分页列表查询
	 *
	 * @param inOutInternal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="内部人员审批表-分页列表查询", notes="内部人员审批表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(InOutInternal inOutInternal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<InOutInternal> queryWrapper = QueryGenerator.initQueryWrapper(inOutInternal, req.getParameterMap());
		Page<InOutInternal> page = new Page<InOutInternal>(pageNo, pageSize);
		IPage<InOutInternal> pageList = inOutInternalService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param inOutInternal
	 * @return
	 */
	@AutoLog(value = "内部人员-添加")
	@ApiOperation(value="内部人员-添加", notes="内部人员-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody InOutInternal inOutInternal) {
		inOutInternalService.save(inOutInternal);
		InOutInternal byId = inOutInternalService.getById(inOutInternal.getId());
		LogUtil.setOperate(byId.getPersonId());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param inOutInternal
	 * @return
	 */
	@AutoLog(value = "内部人员-编辑")
	@ApiOperation(value="内部人员-编辑", notes="内部人员-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody InOutInternal inOutInternal) {
		inOutInternalService.updateById(inOutInternal);
		InOutInternal byId = inOutInternalService.getById(inOutInternal.getId());
		LogUtil.setOperate(byId.getPersonId());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "内部人员-删除")
	@ApiOperation(value="内部人员-删除", notes="内部人员-删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		InOutInternal byId = inOutInternalService.getById(id);
		LogUtil.setOperate(byId.getPersonId());
		inOutInternalService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="内部人员审批表-批量删除", notes="内部人员审批表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.inOutInternalService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="内部人员审批表-通过id查询", notes="内部人员审批表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		InOutInternal inOutInternal = inOutInternalService.getById(id);
		return Result.OK(inOutInternal);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param inOutInternal
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, InOutInternal inOutInternal) {
      return super.exportXls(request, inOutInternal, InOutInternal.class, "内部人员审批表");
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
      return super.importExcel(request, response, InOutInternal.class);
  }

}
