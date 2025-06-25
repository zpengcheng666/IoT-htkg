package com.hss.modules.scada.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.scada.entity.GsChangJingMb;
import com.hss.modules.scada.service.IGsChangJingMbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

 /**
 * @description: 场景 场景模板，更新场景模板数据
 * @author zpc
 * @date 2024/3/19 14:45
 * @version 1.0
 */
@Slf4j
@Api(tags="场景 场景模板")
@RestController
@RequestMapping("/api/scada/MB")
@CrossOrigin
public class GsSceneTemplateController extends HssController<GsChangJingMb, IGsChangJingMbService> {
	 @Autowired
	 private IGsChangJingMbService gsChangJingMbService;

	 /**
	  * 分页列表查询
	  *
	  * @param gsChangJingMb
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @ApiOperation(value = "场景 场景模板-分页列表查询", notes = "场景 场景模板-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<?> queryPageList(GsChangJingMb gsChangJingMb,
									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<GsChangJingMb> queryWrapper = QueryGenerator.initQueryWrapper(gsChangJingMb, req.getParameterMap());
		 Page<GsChangJingMb> page = new Page<GsChangJingMb>(pageNo, pageSize);
		 IPage<GsChangJingMb> pageList = gsChangJingMbService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  * 添加
	  *
	  * @param gsChangJingMb
	  * @return
	  */
	 @AutoLog(value = "添加场景模板")
	 @ApiOperation(value = "场景 场景模板-添加", notes = "场景 场景模板-添加")
	 @PostMapping(value = "/add")
	 public Result<?> add(@RequestBody GsChangJingMb gsChangJingMb) {
		 gsChangJingMbService.save(gsChangJingMb);
		 return Result.OK("添加成功！");
	 }

	 /**
	  * 编辑
	  *
	  * @param gsChangJingMb
	  * @return
	  */
	 @AutoLog(value = "编辑场景模板")
	 @ApiOperation(value = "场景 场景模板-编辑", notes = "场景 场景模板-编辑")
	 @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
	 public Result<?> edit(@RequestBody GsChangJingMb gsChangJingMb) {
		 gsChangJingMbService.updateById(gsChangJingMb);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  * 通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "删除场景模板")
	 @ApiOperation(value = "场景 场景模板-通过id删除", notes = "场景 场景模板-通过id删除")
	 @DeleteMapping(value = "/delete")
	 public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		 gsChangJingMbService.removeById(id);
		 return Result.OK("删除成功!");
	 }

	 /**
	  * 批量删除
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "批量删除场景模板")
	 @ApiOperation(value = "场景 场景模板-批量删除", notes = "场景 场景模板-批量删除")
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		 this.gsChangJingMbService.removeByIds(Arrays.asList(ids.split(",")));
		 return Result.OK("批量删除成功！");
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @ApiOperation(value = "场景 场景模板-通过id查询", notes = "场景 场景模板-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		 GsChangJingMb gsChangJingMb = gsChangJingMbService.getById(id);
		 return Result.OK(gsChangJingMb);
	 }

	 /**
	  * 导出excel
	  *
	  * @param request
	  * @param gsChangJingMb
	  */
	 @RequestMapping(value = "/exportXls")
	 public ModelAndView exportXls(HttpServletRequest request, GsChangJingMb gsChangJingMb) {
		 return super.exportXls(request, gsChangJingMb, GsChangJingMb.class, "场景 场景模板");
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
		 return super.importExcel(request, response, GsChangJingMb.class);
	 }

	 @AutoLog(value = "更新场景模板数据")
	 @ApiOperation(value = "场景-创建/更新场景模板数据", notes = "场景-创建/更新场景模板数据")
	 @PostMapping("/saveStageModuleData")
	 public Result<HashMap<String, String>> saveStageModuleData(
			 @RequestParam("stageDatajson") String stageDatajson,
			 @RequestParam("stageBase64") String stageBase64,
			 @RequestParam("name") String name,
			 @RequestParam(value = "id", required = false) String id) {
		 GsChangJingMb entity = gsChangJingMbService.getById(id);
		 if (entity == null) {
			 entity = new GsChangJingMb();
			 entity.setId(UUID.randomUUID().toString());
			 entity.setCreatedTime(new Date());
		 }
		 if (StringUtils.isNotEmpty(stageDatajson)) {
			 entity.setStagedatajson(stageDatajson);
		 }
		 if (StringUtils.isNotEmpty(stageBase64)) {
			 entity.setStagebase64(stageBase64);
		 }
		 if (StringUtils.isNotEmpty(name)) {
			 entity.setName(name);
		 }
		 entity.setUpdatedTime(new Date());
		 gsChangJingMbService.saveOrUpdate(entity);

		 HashMap<String, String> data = new HashMap<>(16);
		 data.put("id", entity.getId());

		 return Result.OK(data);
	 }

	 @ApiOperation(value = "场景-场景模板列表", notes = "场景-场景模板列表")
	 @GetMapping("/getMyMoudleStageJsonData")
	 public Result<List<GsChangJingMb>> getMyMoudleStageJsonData(GsChangJingMb gsChangJingMb,HttpServletRequest req) {
		 List<GsChangJingMb> list;
		 QueryWrapper<GsChangJingMb> queryWrapper = QueryGenerator.initQueryWrapper(gsChangJingMb, req.getParameterMap());
		 list = gsChangJingMbService.list(queryWrapper);
		 return Result.OK(list);
	 }
}
