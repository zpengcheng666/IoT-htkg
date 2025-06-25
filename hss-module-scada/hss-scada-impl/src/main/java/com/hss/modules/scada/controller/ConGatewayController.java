package com.hss.modules.scada.controller;

import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.UUIDGenerator;
import com.hss.modules.scada.entity.ConWangGuan;
import com.hss.modules.scada.service.IConWangGuanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
* @description: 网关的相关操作
* @author zpc
* @date 2024/3/19 14:28
* @version 1.0
*/
@Slf4j
@Api(tags="网关")
@RestController
@RequestMapping("/scada/conWangGuan")
@CrossOrigin
public class ConGatewayController extends HssController<ConWangGuan, IConWangGuanService> {
	@Autowired
	private IConWangGuanService conWangGuanService;
	
	/**
	 * 列表查询
	 */
	@ApiOperation(value="网关-分页列表查询", notes="网关-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList() {
		List<ConWangGuan> list = conWangGuanService.list();
		return Result.OK(list);
	}

	@ApiOperation(value="网关-获取唯一识别码", notes="网关-获取唯一识别码")
	@GetMapping(value = "/getCode")
	public Result<?> getCode() {
		String code = UUIDGenerator.generate();
		return Result.OK(code);
	}

	/**
	 * 添加
	 */
	@AutoLog(value = "添加网关")
	@ApiOperation(value="网关-添加", notes="网关-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ConWangGuan conWangGuan) {
		conWangGuanService.add(conWangGuan);
		LogUtil.setOperate(conWangGuan.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 * @param conWangGuan
	 * @return
	 */
	@AutoLog(value = "编辑网关")
	@ApiOperation(value="网关-编辑", notes="网关-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody ConWangGuan conWangGuan) {
		conWangGuanService.edit(conWangGuan);
		LogUtil.setOperate(conWangGuan.getName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "删除网关")
	@ApiOperation(value="网关-通过id删除", notes="网关-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id") String id) {
		ConWangGuan byId = conWangGuanService.getById(id);
		conWangGuanService.removeById(id);
		LogUtil.setOperate(byId.getName());
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "批量删除网关")
	@ApiOperation(value="网关-批量删除", notes="网关-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids") String ids) {
		this.conWangGuanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="网关-通过id查询", notes="网关-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id") String id) {
		ConWangGuan conWangGuan = conWangGuanService.getById(id);
		return Result.OK(conWangGuan);
	}

  /**
   * 导出excel
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ConWangGuan conWangGuan) {
      return super.exportXls(request, conWangGuan, ConWangGuan.class, "网关");
  }

  /**
   * 通过excel导入数据
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      return super.importExcel(request, response, ConWangGuan.class);
  }
}
