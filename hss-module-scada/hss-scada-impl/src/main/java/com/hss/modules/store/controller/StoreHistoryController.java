package com.hss.modules.store.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.modules.devicetype.entity.DeviceTypeManagement;
import com.hss.modules.devicetype.service.IDeviceTypeManagementService;
import com.hss.modules.store.constant.StoreConstant;
import com.hss.modules.store.entity.StoreHistory;
import com.hss.modules.store.model.*;
import com.hss.modules.store.model.vo.LineStateVO;
import com.hss.modules.store.model.vo.PieStateVO;
import com.hss.modules.store.service.IStoreHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Description: 设备运行时历史数据
 * @Author: handong、wuyihan、chushubin、zpc
 * @Date:   2024-3-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="设备运行时历史数据")
@RestController
@RequestMapping("/store/storeHistory")
public class StoreHistoryController extends HssController<StoreHistory, IStoreHistoryService> {
	@Autowired
	private IStoreHistoryService storeHistoryService;

	@Autowired
	private IDeviceTypeManagementService deviceTypeManagementService;

	/**
	 * 分页列表查询
	 *
	 * @param storeHistory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="设备运行时历史数据-分页列表查询", notes="设备运行时历史数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreHistory>> queryPageList(StoreHistory storeHistory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Page<StoreHistory> page = new Page<>(pageNo, pageSize);
		LambdaQueryWrapper<StoreHistory> queryWrapper = this.getStoreHistoryQueryWrapper(storeHistory);
		IPage<StoreHistory> pageList = storeHistoryService.page(page, queryWrapper);
		pageList.getRecords().forEach(e ->{
			//设备类型名称
			DeviceTypeManagement type = deviceTypeManagementService.getById(e.getDeviceType());
			e.setDeviceType_disp(type == null ? "": type.getName());
		});
		return Result.OK(pageList);
	}

	/**
	 * 处理历史数据的查询条件
	 * @param storeHistory
	 * @return
	 */
	@NotNull
	private  LambdaQueryWrapper<StoreHistory> getStoreHistoryQueryWrapper(StoreHistory storeHistory) {
		LambdaQueryWrapper<StoreHistory> queryWrapper = new LambdaQueryWrapper<>();
		if (StringUtils.isNotEmpty(storeHistory.getSubsystem())){
			queryWrapper.eq(StoreHistory::getSubsystem,storeHistory.getSubsystem());
		}
		//开始时间与结束时间的条件，查询的结果降序展示
		if (storeHistory.getStartTime() != null){
			queryWrapper.ge(StoreHistory::getRecordTime, storeHistory.getStartTime());
		}
		if (storeHistory.getEndTime() != null){
			queryWrapper.le(StoreHistory::getRecordTime, storeHistory.getEndTime());
		}
		queryWrapper.orderByDesc(StoreHistory::getRecordTime);
		//设备属性
		String deviceType = storeHistory.getDeviceType();
		//设备属性id
		String attrId = storeHistory.getAttrId();
		//设备id
		String deviceId = storeHistory.getDeviceId();
		//对查询条件：deviceType、attrId、deviceId进行判断，如果存在值就拼接进sql
		if(StringUtils.isNotEmpty(deviceType)){
			String[] split = deviceType.split(",");
			List<String> stringList = Arrays.asList(split);
			queryWrapper.in(StoreHistory::getDeviceType, stringList);
		}
		if(StringUtils.isNotEmpty(attrId)){
			String[] split = attrId.split(",");
			List<String> attrIdList = Arrays.asList(split);
			queryWrapper.in(StoreHistory::getAttrId, attrIdList);
		}
		if(StringUtils.isNotEmpty(deviceId)){
			String[] split = deviceId.split(",");
			List<String> devIdsList = Arrays.asList(split);
			queryWrapper.in(StoreHistory::getDeviceId, devIdsList);
		}
		return queryWrapper;
	}


	/**
	* @description: 设备类型导出转码
	* @author zpc
	* @date 2023/3/9 10:32
	* @version 1.0
	*/
	@Override
	protected void hanleDataDetail(StoreHistory e) {
		//设备类型名称
		DeviceTypeManagement type = deviceTypeManagementService.getById(e.getDeviceType());
		e.setDeviceType_disp(type == null ? "": type.getName());
	}

	@ApiOperation(value="历史曲线统计", notes="历史曲线统计")
	@GetMapping(value = "/historyLineStatistics")
	public Result<LineStateVO> historyLineStatistics(StoreHistoryLineStatisticsDTO dto){
		//历史曲线统计
		LineStateVO vo = storeHistoryService.historyLineStatistics(dto);
		return Result.OK(vo);
	}

	 @ApiOperation(value="设备运行时历史数据-统计分析", notes="设备运行时历史数据-统计分析")
	 @GetMapping(value = "/stat")
	 public Result<Map<String, Object>> stat(DataHistoryStatSearchModel model) {
		//设备运行时历史数据——统计分析
		 List<StoreHistoryStatWrapper> list = storeHistoryService.stat(model.getDeviceType(), model.getDeviceId(),
				 model.getAttrName(), model.getStartTime(), model.getEndTime(), model.getSubsystem());
		 //1. xAxis
		 List<String> xAxis = list.stream().map(StoreHistoryStatWrapper::getRecordTime).distinct().collect(Collectors.toList());
		 // 2. legend
		 List<String> legend = list.stream().map(StoreHistoryStatWrapper::getAttrName).distinct().collect(Collectors.toList());
		 // 3. series
		 List<List<Object>> series = new ArrayList<>();
		 legend.forEach(le -> {
			 // legend
			 List<Object> s = new ArrayList<>();
			 xAxis.forEach(x -> {
				 // xAxis
				 AtomicReference<Long> cnt = new AtomicReference<>(0L);
				 list.forEach(e -> {
					 if (e.getAttrName().equals(le) && e.getRecordTime().equals(x)){
						 cnt.set(e.getCnt());
					 }
				 });
				 // series
				 s.add(cnt.get());
			 });
			 series.add(s);
		 });
		 Map<String, Object> retMap = new HashMap<>(16);
		 retMap.put("legend", legend);
		 retMap.put("series", series);
		 retMap.put("xAxis", xAxis);
		 return Result.OK(retMap);
	 }

  /**
   * 导出excel
   *
   * @param request
   * @param storeHistory
   */
  @ApiOperation(value="导出历史记录", notes="导出历史记录")
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, StoreHistory storeHistory) {
	  LambdaQueryWrapper<StoreHistory> query = getStoreHistoryQueryWrapper(storeHistory);
      return super.exportXls(request, query, storeHistory, StoreHistory.class, "设备运行时历史数据");
  }

	@ApiOperation(value="统计分析-分布统计", notes="统计分析-分布统计")
	@GetMapping(value = "/distribute/state")
	public Result<List<PieStateVO>> distributeState(EnvGoodRatioDTO dto){
		List<PieStateVO> list = storeHistoryService.distributeState(dto);
		return Result.OK(list);

	}
	@ApiOperation(value="环境历史数据报表", notes="环境历史数据报表")
	@GetMapping(value = "/env/report")
	public Result<List<EnvReportResult>> evnReport(EnvReportModel request){
		List<EnvReportResult> list = this.storeHistoryService.reportEnv(request.getDeviceType(),
				request.getDeviceId(),
				request.getReportType(),
				request.getDate());
	  	return Result.OK(list);
	}

	@ApiOperation(value="下载环境历史数据报表", notes="下载环境历史数据报表")
	@GetMapping(value = "/env/exportReport")
	public ModelAndView exportEvnReport(EnvReportModel request){
		List<EnvReportResult> list = this.storeHistoryService.reportEnv(request.getDeviceType(),
				request.getDeviceId(),
				request.getReportType(),
				request.getDate());
		return this.exportXls(list, EnvReportResult.class, "环境温湿度");
	}
}
