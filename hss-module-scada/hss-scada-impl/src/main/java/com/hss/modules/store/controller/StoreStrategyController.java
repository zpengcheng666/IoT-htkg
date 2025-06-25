package com.hss.modules.store.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.scada.model.StrategyEnable;
import com.hss.modules.store.entity.StoreStrategy;
import com.hss.modules.store.service.IStoreStrategyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 设备运行时数据存储策略
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
@Slf4j
@Api(tags="设备运行时数据存储策略")
@RestController
@RequestMapping("/store/storeStrategy")
public class StoreStrategyController extends HssController<StoreStrategy, IStoreStrategyService> {
	@Autowired
	private IStoreStrategyService storeStrategyService;
	

	@ApiOperation(value="设备运行时数据存储策略-分页列表查询", notes="设备运行时数据存储策略-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@ApiParam("name") @RequestParam(value = "name") String name,
								   @ApiParam("deviceId") @RequestParam(value = "deviceId") String deviceId,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<StoreStrategy> page = new Page<StoreStrategy>(pageNo, pageSize);
		IPage<StoreStrategy> pageList = storeStrategyService.getPage(page, name, deviceId);
		return Result.OK(pageList);
	}
	

	@AutoLog(value = "添加存储策略")
	@ApiOperation(value="设备运行时数据存储策略-添加", notes="设备运行时数据存储策略-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StoreStrategy storeStrategy) {
		storeStrategyService.add(storeStrategy);
		LogUtil.setOperate(storeStrategy.getName());
		return Result.OK("添加成功！");
	}

	@AutoLog(value = "编辑存储策略")
	@ApiOperation(value="设备运行时数据存储策略-编辑", notes="设备运行时数据存储策略-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody StoreStrategy storeStrategy) {
		storeStrategyService.edit(storeStrategy);
		LogUtil.setOperate(storeStrategy.getName());
		return Result.OK("编辑成功!");
	}

	 @AutoLog(value = "使能存储策略")
	 @ApiOperation(value="设备运行时数据存储策略-使能", notes="设备运行时数据存储策略-使能")
	 @PutMapping(value = "/enable")
	 public Result<?> enable(@RequestBody StrategyEnable strategyEnable) {
		 storeStrategyService.enable(strategyEnable);
		 StoreStrategy byId = storeStrategyService.getById(strategyEnable.getId());
		 LogUtil.setOperate(byId.getName() + ":" + strategyEnable.getEnable());
		 return Result.OK("修改使能成功!");
	 }
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "删除存储策略")
	@ApiOperation(value="设备运行时数据存储策略-通过id删除", notes="设备运行时数据存储策略-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		StoreStrategy byId = storeStrategyService.getById(id);
		storeStrategyService.delete(id);
		LogUtil.setOperate(byId.getName());
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "批量删除存储策略")
	@ApiOperation(value="设备运行时数据存储策略-批量删除", notes="设备运行时数据存储策略-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.storeStrategyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="设备运行时数据存储策略-通过id查询", notes="设备运行时数据存储策略-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StoreStrategy storeStrategy = storeStrategyService.getById(id);
		return Result.OK(storeStrategy);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param storeStrategy
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, StoreStrategy storeStrategy) {
      return super.exportXls(request, storeStrategy, StoreStrategy.class, "设备运行时数据存储策略");
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
      return super.importExcel(request, response, StoreStrategy.class);
  }

}
