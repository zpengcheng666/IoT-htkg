package com.hss.modules.store.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.store.entity.StoreData;
import com.hss.modules.store.service.IStoreDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

 /**
 * @Description: 设备运行时数据
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
@Slf4j
@Api(tags="设备运行时数据")
@RestController
@RequestMapping("/store/storeData")
public class StoreDataController extends HssController<StoreData, IStoreDataService> {
	@Autowired
	private IStoreDataService storeDataService;
	
	/**
	 * 分页列表查询
	 *
	 * @param storeData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="设备运行时数据-分页列表查询", notes="设备运行时数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StoreData storeData,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StoreData> queryWrapper = QueryGenerator.initQueryWrapper(storeData, req.getParameterMap());
		Page<StoreData> page = new Page<StoreData>(pageNo, pageSize);
		IPage<StoreData> pageList = storeDataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param storeData
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, StoreData storeData) {
      return super.exportXls(request, storeData, StoreData.class, "设备运行时数据");
  }

}
