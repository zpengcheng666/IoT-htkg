package com.hss.modules.door.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.door.dto.CardDoorDTO;
import com.hss.modules.door.dto.RemoteOpenDoorDTO;
import com.hss.modules.door.entity.DoorData;
import com.hss.modules.door.service.IDoorDataService;
import com.hss.modules.door.vo.CardDoorVO;
import com.hss.modules.door.vo.RemoteOpenDoorVO;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.service.IBaseDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

 /**
 * @Description: 门禁数据表
 * @Author: zpc
 * @Date:   2023-02-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags="门禁数据表")
@RestController
@RequestMapping("/door/doorData")
public class DoorDataController extends HssController<DoorData, IDoorDataService> {
	@Autowired
	private IDoorDataService doorDataService;

	 @Autowired
	 private IBaseDictDataService baseDictDataService;

	 @Autowired
	 private IConSheBeiService conSheBeiService;


	/**
	 * 分页列表查询
	 *
	 * @param doorData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="门禁数据表-分页列表查询", notes="门禁数据表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DoorData doorData,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DoorData> queryWrapper = QueryGenerator.initQueryWrapper(doorData, req.getParameterMap());
		Page<DoorData> page = new Page<DoorData>(pageNo, pageSize);
		IPage<DoorData> pageList = doorDataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	 @ApiOperation(value = "远程开门记录-分页列表查询", notes = "远程开门记录-分页列表查询")
	 @GetMapping(value = "/remoteOpen")
	 public Result<IPage<RemoteOpenDoorVO>> remoteOpen(RemoteOpenDoorDTO dto) {
		 IPage<RemoteOpenDoorVO> pageResult = doorDataService.pageByRemoteOpen(dto);
		 return Result.OK(pageResult);
	 }


	 @ApiOperation(value = "远程开门记录-门禁-导出", notes = "远程开门记录-门禁-导出")
	 @RequestMapping(value = "/openDoor/exportXls")
	 public ModelAndView openDoorExportXls(RemoteOpenDoorDTO dto) {
		 dto.setPageNo(1);
		 dto.setPageSize(-1);
		 IPage<RemoteOpenDoorVO> resultPage = doorDataService.pageByRemoteOpen(dto);
		 return super.exportXls(resultPage.getRecords(), RemoteOpenDoorVO.class, "远程开门记录");
	 }

	 /**
	 * @description: 刷卡记录
	 * @author zpc
	 * @date 2023/2/17 11:08
	 * @version 1.0
	 */
	 @ApiOperation(value = "刷卡记录表-分页列表查询", notes = "刷卡记录表-分页列表查询")
	 @GetMapping(value = "/cardRecordList")
	 public Result<IPage<CardDoorVO>> cardRecordList(CardDoorDTO dto) {
		 IPage<CardDoorVO> resultPage = doorDataService.pageByCard(dto);
		 return Result.OK(resultPage);
	 }

	 /**
	  * @description: 刷卡记录导出
	  * @author wuyihan
	  * @date 2024/3/20 16:21
	  * @version 1.0
	  */
	 @ApiOperation(value = "/entry/exportXls", notes = "/entry/exportXls")
	 @RequestMapping(value = "/entry/exportXls")
	 public ModelAndView doorEntryDataExportXls(CardDoorDTO dto) {
		 dto.setPageNo(1);
		 dto.setPageSize(-1);
		 //查询刷卡记录
		 IPage<CardDoorVO> resultPage = doorDataService.pageByCard(dto);
		 return super.exportXls(resultPage.getRecords(), CardDoorVO.class, "刷卡记录");

	 }

	 @NotNull
	 private static LambdaQueryWrapper<DoorData> getDataLambdaQueryWrapper(DoorData doorData) {
		 LambdaQueryWrapper<DoorData> queryWrapper = new LambdaQueryWrapper<>();
		 queryWrapper.ne(DoorData::getOpenType,"RemoteOpen");
		 if (OConvertUtils.isNotEmpty(doorData.getDoorId())) {
			 //门名称.可以多选
			 String[] split = doorData.getDoorId().split(",");
			 List<String> stringList = Arrays.asList(split);
			 queryWrapper.in(DoorData::getDoorId, stringList);
		 }
		 if (OConvertUtils.isNotEmpty(doorData.getPerName())) {
			 //人员姓名
			 queryWrapper.like(DoorData::getPerName, doorData.getPerName());
		 }
		 if (OConvertUtils.isNotEmpty(doorData.getCardCode())) {
			 //卡号
			 queryWrapper.like(DoorData::getCardCode, doorData.getCardCode());
		 }
		 if (OConvertUtils.isNotEmpty(doorData.getAccessType())) {
			 //进出类型
			 queryWrapper.like(DoorData::getAccessType, doorData.getAccessType());
		 }
		 //按照刷卡时间，时间段查询
		 if (OConvertUtils.isNotEmpty(doorData.getStart_swipeTime())) {
			 //开始时间
			 queryWrapper.ge(DoorData::getSwipeTime, doorData.getStart_swipeTime());
		 }
		 if (OConvertUtils.isNotEmpty(doorData.getEnd_swipeTime())) {
			 //结束时间
			 queryWrapper.le(DoorData::getSwipeTime, doorData.getEnd_swipeTime());
		 }
		 return queryWrapper;
	 }

	 /**
	 * 添加门禁数据
	 *
	 * @param doorData
	 * @return
	 */
	@AutoLog(value = "添加门禁数据")
	@ApiOperation(value="门禁数据表-添加", notes="门禁数据表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DoorData doorData) {
		doorDataService.save(doorData);
		LogUtil.setOperate(doorData.getDoorName());
		return Result.OK("添加成功！");
	}

	/**
	 * 编辑门禁数据
	 *
	 * @param doorData
	 * @return
	 */
	@AutoLog(value = "编辑门禁数据")
	@ApiOperation(value="门禁数据表-编辑", notes="门禁数据表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody DoorData doorData) {
		doorDataService.updateById(doorData);
		LogUtil.setOperate(doorData.getDoorName());
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除门禁数据
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "删除门禁数据")
	@ApiOperation(value="门禁数据表-通过id删除", notes="门禁数据表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		DoorData byId = doorDataService.getById(id);
		doorDataService.removeById(id);
		LogUtil.setOperate(byId.getDoorName());
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除刷卡记录
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "批量删除刷卡记录")
	@ApiOperation(value="刷卡记录-批量删除", notes="刷卡记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.doorDataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="门禁数据表-通过id查询", notes="门禁数据表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DoorData doorData = doorDataService.getById(id);
		return Result.OK(doorData);
	}

	 @Override
	 protected void hanleDataDetail(DoorData e) {
		 //开门类型
		 BaseDictData type = baseDictDataService.getById(e.getOpenType());
		 e.setOpenType_disp(type == null ? "" : type.getName());
		 //刷卡结果
		 BaseDictData cardId = baseDictDataService.getById(e.getCardResult());
		 e.setCardResult_disp(cardId == null ? "" : cardId.getName());
		 //门名称
		 ConSheBei doorId = conSheBeiService.getById(e.getDoorId());
		 e.setDoorName(doorId == null ? "" : doorId.getName());
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
		 return super.importExcel(request, response, DoorData.class);
	 }

	 @ApiOperation(value="大屏查看刷卡信息", notes="大屏查看刷卡信息")
	 @GetMapping(value = "/pageByTerminalId")
	 public Result<?> pageByTerminalId(
			 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			 @RequestParam(name="pageSize", defaultValue="20") Integer pageSize,
			 @RequestParam(name="startTime") Date startTime,
			 @RequestParam(name="endTime") Date endTime,
			 @RequestParam("terminalId") @ApiParam("终端id") String terminalId) {
		 Page<DoorData> page = new Page<DoorData>(pageNo, pageSize);
		 IPage<DoorData> pageList = doorDataService.pageByTerminalId(page, terminalId, startTime, endTime);
		 return Result.OK(pageList);
	 }
 }
