package com.hss.modules.spare.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.spare.entity.*;
import com.hss.modules.spare.model.CarrierModel;
import com.hss.modules.spare.model.ItemModel;
import com.hss.modules.spare.service.IAreaService;
import com.hss.modules.spare.service.IItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.spare.service.IMaterialClassificationService;
import com.hss.modules.spare.service.IWareHouseService;
import com.hss.modules.system.entity.BaseDictData;
import lombok.extern.slf4j.Slf4j;
import com.hss.core.common.system.base.controller.HssController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 物料管理
 * @Author: wuyihan
 * @Date:   2024-04-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags="物料管理")
@RestController
@RequestMapping("/spare/item")
public class ItemController extends HssController<ItemEntity, IItemService> {
	@Autowired
	private IItemService itemService;

	@Autowired
	private IMaterialClassificationService materialClassificationService;

	@Autowired
	private IAreaService areaService;
	
	/**
	 * 分页列表查询
	 *
	 * @param itemEntity
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "物料管理-分页列表查询")
	@ApiOperation(value="物料管理-分页列表查询", notes="物料管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ItemEntity itemEntity,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LambdaQueryWrapper<ItemEntity> queryWrapper = new LambdaQueryWrapper<>();
		if (OConvertUtils.isNotEmpty(itemEntity.getItemNo())) {
			//编号
			queryWrapper.eq(ItemEntity::getItemNo, itemEntity.getItemNo());
		}
		if (OConvertUtils.isNotEmpty(itemEntity.getItemName())) {
			//名称
			queryWrapper.like(ItemEntity::getItemName, itemEntity.getItemName());
		}
		if(OConvertUtils.isNotEmpty(itemEntity.getItemType())){
			//物料分类
			queryWrapper.eq(ItemEntity::getItemType,itemEntity.getItemType());
		}
		Page<ItemEntity> page = new Page<>(pageNo, pageSize);
		IPage<ItemEntity> pageList = itemService.page(page, queryWrapper);
		//分类下拉
		pageList.getRecords().forEach(e -> {
			MaterialClassification byId = materialClassificationService.getById(e.getItemType());
			e.setItemTypeName(byId == null ? "" : byId.getTypeName());
		});
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param itemEntity
	 * @return
	 */
	@AutoLog(value = "物料管理-添加")
	@ApiOperation(value="物料管理-添加", notes="物料管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ItemEntity itemEntity) {
		itemService.save(itemEntity);
		ItemEntity byId = itemService.getById(itemEntity.getId());
		LogUtil.setOperate(byId.getItemName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param itemEntity
	 * @return
	 */
	@AutoLog(value = "物料管理-编辑")
	@ApiOperation(value="物料管理-编辑", notes="物料管理-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody ItemEntity itemEntity) {
		itemService.updateById(itemEntity);
		LogUtil.setOperate(itemEntity.getItemName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物料管理-通过id删除")
	@ApiOperation(value="物料管理-通过id删除", notes="物料管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		LogUtil.setOperate(itemService.getById(id).getItemName());
		itemService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "物料管理-批量删除")
	@ApiOperation(value="物料管理-批量删除", notes="物料管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.itemService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物料管理-通过id查询")
	@ApiOperation(value="物料管理-通过id查询", notes="物料管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ItemEntity ItemEntity = itemService.getById(id);
		return Result.OK(ItemEntity);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param ItemEntity
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ItemEntity ItemEntity) {
      return super.exportXls(request, ItemEntity, ItemEntity.class, "物料管理");
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
      return super.importExcel(request, response, ItemEntity.class);
  }


	 /**
	  * 物料下拉框查询
	  * @return options
	  */
	 @AutoLog(value = "物料下拉框查询")
	 @ApiOperation(value="物料下拉框查询", notes="物料下拉框查询")
	 @GetMapping(value = "/listOptions")
	 public Result<?> listOptions() {
		 List<ItemEntity> itemEntityList = itemService.list();
		 List<ItemModel> options = itemEntityList.stream().map(e -> {
			 ItemModel itemModel = new ItemModel();
			 itemModel.setId(e.getId());
			 itemModel.setItemNo(e.getItemNo());
			 itemModel.setItemName(e.getItemName());
			 itemModel.setSpecification(e.getSpecification());
			 itemModel.setBrand(e.getBrand());
			 itemModel.setItemType(e.getItemType());
			 itemModel.setUnit(e.getUnit());
			 itemModel.setQuantity(e.getQuantity());
			 itemModel.setRackId(e.getRackId());
			 itemModel.setAreaId(e.getAreaId());
			 itemModel.setWarehouseId(e.getWarehouseId());
			 itemModel.setProductionDate(e.getProductionDate());
			 itemModel.setExpiryDate(e.getExpiryDate());
			 itemModel.setBatch(e.getBatch());
			 itemModel.setStatus(e.getStatus());
			 return itemModel;
		 }).collect(Collectors.toList());
		 return Result.OK(options);
	 }

}
