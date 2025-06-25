package com.hss.modules.scada.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.scada.entity.GsChangJingDianWei;
import com.hss.modules.scada.service.IGsChangJingDianWeiService;
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
 * @description: 场景中点位
 * @author zpc
 * @date 2024/3/19 14:44
 * @version 1.0
 */
@Slf4j
@Api(tags="场景 点位")
@RestController
@RequestMapping("/scada/gsChangJingDianWei")
public class GsScenePointController extends HssController<GsChangJingDianWei, IGsChangJingDianWeiService> {
	@Autowired
	private IGsChangJingDianWeiService gsChangJingDianWeiService;
	
	/**
	 * 分页列表查询
	 *
	 * @param gsChangJingDianWei
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="场景 点位-分页列表查询", notes="场景 点位-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GsChangJingDianWei gsChangJingDianWei,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<GsChangJingDianWei> queryWrapper = QueryGenerator.initQueryWrapper(gsChangJingDianWei, req.getParameterMap());
		Page<GsChangJingDianWei> page = new Page<GsChangJingDianWei>(pageNo, pageSize);
		IPage<GsChangJingDianWei> pageList = gsChangJingDianWeiService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param gsChangJingDianWei
	 * @return
	 */
	@AutoLog(value = "场景 点位-添加")
	@ApiOperation(value="场景 点位-添加", notes="场景 点位-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GsChangJingDianWei gsChangJingDianWei) {
		gsChangJingDianWeiService.save(gsChangJingDianWei);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param gsChangJingDianWei
	 * @return
	 */
	@AutoLog(value = "场景 点位-编辑")
	@ApiOperation(value="场景 点位-编辑", notes="场景 点位-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody GsChangJingDianWei gsChangJingDianWei) {
		gsChangJingDianWeiService.updateById(gsChangJingDianWei);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "场景 点位-通过id删除")
	@ApiOperation(value="场景 点位-通过id删除", notes="场景 点位-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		gsChangJingDianWeiService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "场景 点位-批量删除")
	@ApiOperation(value="场景 点位-批量删除", notes="场景 点位-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.gsChangJingDianWeiService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "场景 点位-通过id查询")
	@ApiOperation(value="场景 点位-通过id查询", notes="场景 点位-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		GsChangJingDianWei gsChangJingDianWei = gsChangJingDianWeiService.getById(id);
		return Result.OK(gsChangJingDianWei);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param gsChangJingDianWei
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, GsChangJingDianWei gsChangJingDianWei) {
      return super.exportXls(request, gsChangJingDianWei, GsChangJingDianWei.class, "场景 点位");
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
      return super.importExcel(request, response, GsChangJingDianWei.class);
  }

}
