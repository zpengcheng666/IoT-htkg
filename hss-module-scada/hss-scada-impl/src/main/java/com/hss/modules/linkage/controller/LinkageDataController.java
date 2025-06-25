package com.hss.modules.linkage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.linkage.entity.LinkageData;
import com.hss.modules.linkage.service.ILinkageDataService;
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
 * @Description: 联动数据记录
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags="联动数据记录")
@RestController
@RequestMapping("/scada/linkageData")
public class LinkageDataController extends HssController<LinkageData, ILinkageDataService> {
	@Autowired
	private ILinkageDataService linkageDataService;
	
	/**
	 * 分页列表查询
	 *
	 * @param linkageData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="联动数据记录-分页列表查询", notes="联动数据记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(LinkageData linkageData,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<LinkageData> queryWrapper = QueryGenerator.initQueryWrapper(linkageData, req.getParameterMap());
		Page<LinkageData> page = new Page<LinkageData>(pageNo, pageSize);
		IPage<LinkageData> pageList = linkageDataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param linkageData
	 * @return
	 */
	@AutoLog(value = "添加联动数据记录")
	@ApiOperation(value="联动数据记录-添加", notes="联动数据记录-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody LinkageData linkageData) {
		linkageDataService.save(linkageData);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param linkageData
	 * @return
	 */
	@AutoLog(value = "编辑联动数据记录")
	@ApiOperation(value="联动数据记录-编辑", notes="联动数据记录-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody LinkageData linkageData) {
		linkageDataService.updateById(linkageData);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "删除联动数据记录")
	@ApiOperation(value="联动数据记录-通过id删除", notes="联动数据记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		linkageDataService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "批量删除联动数据记录")
	@ApiOperation(value="联动数据记录-批量删除", notes="联动数据记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.linkageDataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="联动数据记录-通过id查询", notes="联动数据记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		LinkageData linkageData = linkageDataService.getById(id);
		return Result.OK(linkageData);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param linkageData
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, LinkageData linkageData) {
      return super.exportXls(request, linkageData, LinkageData.class, "联动数据记录");
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
      return super.importExcel(request, response, LinkageData.class);
  }

}
