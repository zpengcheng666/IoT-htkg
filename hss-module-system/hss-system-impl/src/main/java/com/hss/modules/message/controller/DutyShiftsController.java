package com.hss.modules.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.message.dto.DutyHistoryDTO;
import com.hss.modules.message.dto.DutyHistoryVO;
import com.hss.modules.message.dto.DutyShiftsAutomaticDTO;
import com.hss.modules.message.dto.PublishDutyShiftMseeateDTO;
import com.hss.modules.message.entity.DutyShifts;
import com.hss.modules.message.service.IDutyShiftsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

 /**
 * @Description: 排班班次
 * @Author: zpc
 * @Date:   2023-04-26
 * @Version: V1.0
 */
@Slf4j
@Api(tags="排班班次")
@RestController
@RequestMapping("/message/dutyShifts")
public class DutyShiftsController extends HssController<DutyShifts, IDutyShiftsService> {
	@Autowired
	private IDutyShiftsService dutyShiftsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param dutyShifts
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="排班班次-分页列表查询", notes="排班班次-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DutyShifts dutyShifts,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DutyShifts> queryWrapper = QueryGenerator.initQueryWrapper(dutyShifts, req.getParameterMap());
		Page<DutyShifts> page = new Page<DutyShifts>(pageNo, pageSize);
		IPage<DutyShifts> pageList = dutyShiftsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @ApiOperation(value = "值班消息-发布", notes = "值班消息-发布")
	 @PutMapping(value = "/publish")
	 public Result<?> publish(@RequestBody PublishDutyShiftMseeateDTO dto) {
		 //dutyShiftsService.publish(dto);
		 return Result.OK("发布成功!");
	 }

	 @ApiOperation(value = "值班消息-撤销发布", notes = "值班消息-撤销发布")
	 @PutMapping(value = "/revocation")
	 public Result<?> revocation(@RequestParam("ids") List<String> ids) {
		 //dutyShiftsService.revocation(ids);
		 return Result.OK("撤销发布成功!");
	 }

	/**
	 * 添加
	 *
	 * @param dutyShifts
	 * @return
	 */
	@ApiOperation(value="排班班次-添加", notes="排班班次-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DutyShifts dutyShifts) {
		dutyShiftsService.save(dutyShifts);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param dutyShifts
	 * @return
	 */
	@ApiOperation(value="排班班次-编辑", notes="排班班次-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody DutyShifts dutyShifts) {
		dutyShiftsService.updateById(dutyShifts);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="排班班次-通过id删除", notes="排班班次-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		dutyShiftsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="排班班次-批量删除", notes="排班班次-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dutyShiftsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="排班班次-通过id查询", notes="排班班次-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DutyShifts dutyShifts = dutyShiftsService.getById(id);
		return Result.OK(dutyShifts);
	}

	 @ApiOperation(value="导出日志", notes="导出日志")
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(DutyHistoryDTO dto) {
	  dto.setPageSize(-1);
	  dto.setPageNo(1);
	  IPage<DutyHistoryVO> vo = dutyShiftsService.listHistory(dto);
      return super.exportXls(vo.getRecords(), DutyHistoryVO.class, "值班日志");
  }


	 @ApiOperation(value = "查询排班", notes = "查询排班")
	 @GetMapping(value = "/listShifts")
	 public Result<List<DutyShifts>> dutyQueryList(
			 @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			 @ApiParam(value = "值班安排id",required = true) @RequestParam(name = "dutyId") String dutyId
	 ) {
		 List<DutyShifts> list = dutyShiftsService.listByDateAndDutyId(date, dutyId);
	  return Result.ok(list);
	 }
	 @ApiOperation(value = "查询值班日志", notes = "查询值班日志")
	 @GetMapping(value = "/listHistory")
	 public Result<IPage<DutyHistoryVO>> listHistory(DutyHistoryDTO dto) {
		 IPage<DutyHistoryVO> vo = dutyShiftsService.listHistory(dto);
		 return Result.ok(vo);
	 }



	 @ApiOperation(value = "自动排班", notes = "自动排班")
	 @PutMapping(value = "/automatic")
	 public Result<?> automatic(@RequestBody DutyShiftsAutomaticDTO dto) {
		 dutyShiftsService.automatic(dto);
		 return Result.ok("自动排班成功");
	 }

}
