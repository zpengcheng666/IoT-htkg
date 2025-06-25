package com.hss.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.DateUtils;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.system.entity.BaseDictType;
import com.hss.modules.system.model.OptionModel;
import com.hss.modules.system.service.IBaseDictTypeService;
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
import java.util.stream.Collectors;

 /**
 * @Description: 字典类型
 * @Author: zpc、wuyihan
 * @Date:   2024-03-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="字典类型")
@RestController
@RequestMapping("/system/baseDictType")
public class BaseDictTypeController extends HssController<BaseDictType, IBaseDictTypeService> {
	@Autowired
	private IBaseDictTypeService baseDictTypeService;

	 /**
	  * 新分页列表查询
	  *
	  * @param baseDictType
	  * @return
	  */
	 @ApiOperation(value="字典类型-新分页列表查询", notes="字典类型-新分页列表查询")
	 @GetMapping(value = "/listDict")
	 public Result<?> queryDict(BaseDictType baseDictType,
								@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		 LambdaQueryWrapper<BaseDictType> wrapper = new LambdaQueryWrapper<>();
		 if(StrUtil.isNotEmpty(baseDictType.getId())){
			 wrapper.eq(BaseDictType::getId,baseDictType.getId());
		 }
		 Page<BaseDictType> page = new Page<>(pageNo,pageSize);
		 IPage<BaseDictType> pageList = baseDictTypeService.page(page,wrapper);
		 return Result.OK(pageList);
	 }

	/**
	 * 添加
	 *
	 * @param baseDictType
	 * @return
	 */
	@AutoLog(value = "字典类型-添加")
	@ApiOperation(value="字典类型-添加", notes="字典类型-添加")
	@PostMapping(value = "/addDictType")
	public Result<?> addDictType(@RequestBody BaseDictType baseDictType) {
		baseDictType.setCreatedTime(DateUtils.getDate());
		baseDictType.setEditable(0);
		baseDictType.setDeleted(0);
		baseDictTypeService.save(baseDictType);
		BaseDictType byId = baseDictTypeService.getById(baseDictType.getId());
		LogUtil.setOperate(byId.getName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param baseDictType
	 * @return
	 */
	@AutoLog(value = "字典类型-编辑")
	@ApiOperation(value="字典类型-编辑", notes="字典类型-编辑")
	@RequestMapping(value = "/editDictType", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> editDictType(@RequestBody BaseDictType baseDictType) {
		baseDictType.setUpdatedTime(DateUtils.getDate());
		baseDictTypeService.updateById(baseDictType);
		BaseDictType byId = baseDictTypeService.getById(baseDictType.getId());
		LogUtil.setOperate(byId.getName());
		return Result.OK("编辑成功!");
	}

	 /**
	  * 查询筛选
	  * @return options
	  */
	 @ApiOperation(value="字典数据-数据字典类型Options", notes="字典数据-数据字典类型Options")
	 @GetMapping(value = "/listOptions")
	 public Result<?> listOptions() {
		 List<BaseDictType> lsit = baseDictTypeService.list();
		 List<OptionModel> options = lsit.stream().map(e -> {
			 OptionModel tmp = new OptionModel();
			 tmp.setId(e.getId());
			 tmp.setName(e.getName());
			 return tmp;
		 }).collect(Collectors.toList());
		 return Result.OK(options);
	 }

	 /**
	  * 添加字典类型与字典码值
	  *
	  * @param baseDictType
	  * @return
	  */
	 @AutoLog(value = "添加字典类型与字典码值")
	 @ApiOperation(value="添加字典类型与字典码值", notes="添加字典类型与字典码值")
	 @PostMapping(value = "/addDictTypeAndCode")
	 public Result<?> addDictTypeAndCode(@RequestBody BaseDictType baseDictType){
		 baseDictType.setCreatedTime(DateUtils.getDate());
		 baseDictType.setEditable(0);
		 baseDictType.setDeleted(0);
		 baseDictTypeService.save(baseDictType);
		 BaseDictType byId = baseDictTypeService.getById(baseDictType.getId());
		 LogUtil.setOperate(byId.getName());
		 return Result.OK("添加成功！");
	 }

	 /**
	  * 编辑字典类型与字典码值
	  *
	  * @param baseDictType
	  * @return
	  */
	 @AutoLog(value = "编辑字典类型与字典码值")
	 @ApiOperation(value="编辑字典类型与字典码值", notes="编辑字典类型与字典码值")
	 @RequestMapping(value = "/editDictTypeAndCode", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<?> editDictTypeAndCode(@RequestBody BaseDictType baseDictType){
		 baseDictTypeService.updateNameAndEnName(baseDictType);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  * 通过id删除字典类型
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "字典类型-通过id删除字典类型")
	 @ApiOperation(value="字典类型-通过id删除字典类型", notes="字典类型-通过id删除字典类型")
	 @DeleteMapping(value = "/deleteDict")
	 public Result<?> deleteDict(@RequestParam(name="id",required=true) String id) {
		 baseDictTypeService.deleteById(id);
		 return Result.OK("删除成功!");
	 }

 }
