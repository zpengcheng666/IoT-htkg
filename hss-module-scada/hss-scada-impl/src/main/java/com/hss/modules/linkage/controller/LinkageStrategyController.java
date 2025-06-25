package com.hss.modules.linkage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.linkage.entity.LinkageStrategy;
import com.hss.modules.linkage.model.ListByAlarmStrategyIdsDTO;
import com.hss.modules.linkage.service.ILinkageStrategyService;
import com.hss.modules.scada.model.GetExpressionStrDTO;
import com.hss.modules.scada.model.StrategyEnable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

 /**
 * @Description: 联动策略
 * @Author: jeecg-boot
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags="联动策略")
@RestController
@RequestMapping("/scada/linkageStrategy")
public class LinkageStrategyController extends HssController<LinkageStrategy, ILinkageStrategyService> {
	@Autowired
	private ILinkageStrategyService linkageStrategyService;
	

	@ApiOperation(value="联动策略-分页列表查询", notes="联动策略-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@ApiParam(value = "name") @RequestParam(value = "name", required = false) String name,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Page<LinkageStrategy> page = new Page<LinkageStrategy>(pageNo, pageSize);
		IPage<LinkageStrategy> pageList = linkageStrategyService.getPage(page, name);
		return Result.OK(pageList);
	}

	 @ApiOperation(value="联动策略-查询表达式", notes="联动策略-查询表达式")
	 @PostMapping(value = "/getExpressionStr")
	 public Result<String> getExpressionStr(@RequestBody GetExpressionStrDTO dto) {
		 String expressionStr = linkageStrategyService.getExpressionStr(dto.getExpression());
		 return Result.OK(expressionStr);
	 }

	
	/**
	 * 添加
	 *
	 * @param linkageStrategy
	 * @return
	 */
	@AutoLog(value = "添加联动策略")
	@ApiOperation(value="联动策略-添加", notes="联动策略-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody LinkageStrategy linkageStrategy) {
		linkageStrategyService.add(linkageStrategy);
		LogUtil.setOperate(linkageStrategy.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param linkageStrategy
	 * @return
	 */
	@AutoLog(value = "编辑联动策略")
	@ApiOperation(value="联动策略-编辑", notes="联动策略-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody LinkageStrategy linkageStrategy) {
		linkageStrategyService.updateById(linkageStrategy);
		LogUtil.setOperate(linkageStrategy.getName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "删除联动策略")
	@ApiOperation(value="联动策略-通过id删除", notes="联动策略-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		LinkageStrategy byId = linkageStrategyService.getById(id);
		linkageStrategyService.deleteById(id);
		LogUtil.setOperate(byId.getName());
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "批量删除联动策略")
	@ApiOperation(value="联动策略-批量删除", notes="联动策略-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.linkageStrategyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="联动策略-通过id查询", notes="联动策略-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		LinkageStrategy linkageStrategy = linkageStrategyService.getById(id);
		return Result.OK(linkageStrategy);
	}

	 @AutoLog(value = "使能联动策略")
	 @ApiOperation(value="联动策略-使能", notes="联动策略-使能")
	 @PutMapping(value = "/enable")
	 public Result<?> enable(@RequestBody StrategyEnable strategyEnable) {
		 linkageStrategyService.enable(strategyEnable);
		 LinkageStrategy byId = linkageStrategyService.getById(strategyEnable.getId());
		 LogUtil.setOperate(byId.getName() + ":" + byId.getIsEnable());
		 return Result.OK("使能成功!");
	 }
	 @ApiOperation(value="根据报警策略id查询", notes="根据报警策略id查询")
	 @GetMapping(value = "/listByAlarmStrategyId")
	 public Result<List<LinkageStrategy>> listByAlarmStrategyId(@RequestParam(name="alarmStrategyId",required=true) String alarmStrategyId) {
		 List<LinkageStrategy> list = linkageStrategyService.listByAlarmStrategyId(alarmStrategyId);
		 return Result.OK(list);
	 }

	 @ApiOperation(value="根据报警策略ids查询", notes="根据报警策略ids查询")
	 @PostMapping(value = "/listByAlarmStrategyIds")
	 public Result<List<LinkageStrategy>> listByAlarmStrategyIds(@RequestBody ListByAlarmStrategyIdsDTO dto) {
		 List<LinkageStrategy> list = linkageStrategyService.listByAlarmStrategyIds(dto);
		 return Result.OK(list);
	 }

	 @ApiOperation(value="测试", notes="测试")
	 @PutMapping(value = "/test")
	 public Result<?> test(@RequestParam(name="id") String id) {
		 linkageStrategyService.test(id);
		 return Result.OK("执行成功");
	 }


}
