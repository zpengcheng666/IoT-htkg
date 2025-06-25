package com.hss.modules.spare.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.spare.entity.Carrier;
import com.hss.modules.spare.service.ICarrierService;

import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.spare.model.CarrierModel;
import lombok.extern.slf4j.Slf4j;
import com.hss.core.common.system.base.controller.HssController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 供应商表
 * @Author: wuyihan
 * @Date:   2024-04-26
 * @Version: V1.0
 */
@Slf4j
@Api(tags="供应商表")
@RestController
@RequestMapping("/spare/carrier")
public class CarrierController extends HssController<Carrier,ICarrierService> {
	@Autowired
	private ICarrierService carrierService;
	
	/**
	 * 分页列表查询
	 *
	 * @param carrier
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "供应商表-分页列表查询")
	@ApiOperation(value="供应商表-分页列表查询", notes="供应商表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Carrier carrier,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LambdaQueryWrapper<Carrier> queryWrapper = new LambdaQueryWrapper<>();
		//条件-编号
		if (OConvertUtils.isNotEmpty(carrier.getCarrierNo())) {
			queryWrapper.eq(Carrier::getCarrierNo, carrier.getCarrierNo());
		}
		//条件-名称
		if (OConvertUtils.isNotEmpty(carrier.getCarrierName())) {
			queryWrapper.like(Carrier::getCarrierName, carrier.getCarrierName());
		}
		//条件-地址
		if (OConvertUtils.isNotEmpty(carrier.getAddress())) {
			queryWrapper.like(Carrier::getAddress, carrier.getAddress());
		}
		//条件-手机号
		if (OConvertUtils.isNotEmpty(carrier.getMobile())) {
			queryWrapper.eq(Carrier::getMobile, carrier.getMobile());
		}
		//条件-座机号
		if (OConvertUtils.isNotEmpty(carrier.getTel())) {
			queryWrapper.eq(Carrier::getTel, carrier.getTel());
		}
		//条件-联系人
		if (OConvertUtils.isNotEmpty(carrier.getContact())) {
			queryWrapper.like(Carrier::getContact, carrier.getContact());
		}
		//条件-级别
		if (OConvertUtils.isNotEmpty(carrier.getLevel2())) {
			queryWrapper.like(Carrier::getLevel2, carrier.getLevel2());
		}
		//条件-电子邮箱
		if (OConvertUtils.isNotEmpty(carrier.getEmail())) {
			queryWrapper.eq(Carrier::getEmail, carrier.getEmail());
		}
		Page<Carrier> page = new Page<>(pageNo,pageSize);
		IPage<Carrier> pageList = carrierService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 查询筛选
	  * @return options
	  */
	 @AutoLog(value = "供应商-下拉类型查询")
	 @ApiOperation(value="供应商-下拉类型查询", notes="供应商-下拉类型查询")
	 @GetMapping(value = "/listOptions")
	 public Result<?> listOptions() {
		 List<Carrier> carrierList = carrierService.list();
		 List<CarrierModel> options = carrierList.stream().map(e -> {
			 CarrierModel carrierModel = new CarrierModel();
			 carrierModel.setId(e.getId());
			 carrierModel.setCarrierNo(e.getCarrierNo());
			 carrierModel.setCarrierName(e.getCarrierName());
			 carrierModel.setAddress(e.getAddress());
			 carrierModel.setMobile(e.getMobile());
			 carrierModel.setTel(e.getTel());
			 carrierModel.setContact(e.getContact());
			 carrierModel.setLevel(e.getLevel2());
			 carrierModel.setEmail(e.getEmail());
			 carrierModel.setRemark(e.getRemark());
			 return carrierModel;
		 }).collect(Collectors.toList());
		 return Result.OK(options);
	 }
	
	/**
	 * 添加
	 *
	 * @param carrier
	 * @return
	 */
	@AutoLog(value = "供应商表-添加")
	@ApiOperation(value="供应商表-添加", notes="供应商表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Carrier carrier) {
		carrierService.save(carrier);
		LogUtil.setOperate(carrier.getCarrierName());
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param carrier
	 * @return
	 */
	@AutoLog(value = "供应商表-编辑")
	@ApiOperation(value="供应商表-编辑", notes="供应商表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody Carrier carrier) {
		carrierService.updateById(carrier);
		LogUtil.setOperate(carrier.getCarrierName());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "供应商表-通过id删除")
	@ApiOperation(value="供应商表-通过id删除", notes="供应商表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		LogUtil.setOperate(carrierService.getById(id).getCarrierName());
		carrierService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "供应商表-批量删除")
	@ApiOperation(value="供应商表-批量删除", notes="供应商表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.carrierService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "供应商表-通过id查询")
	@ApiOperation(value="供应商表-通过id查询", notes="供应商表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Carrier carrier = carrierService.getById(id);
		return Result.OK(carrier);
	}

}
