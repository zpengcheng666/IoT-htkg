package com.hss.modules.door.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.door.entity.DoorPersonSys;
import com.hss.modules.door.service.IDoorPersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

 /**
 * @Description: 门禁人员
 * @Author: zpc
 * @Date:   2023-03-02
 * @Version: V1.0
 */
@Slf4j
@Api(tags="门禁人员")
@RestController
@RequestMapping("/door/doorPerson")
public class DoorPersonController extends HssController<DoorPersonSys, IDoorPersonService> {
	@Autowired
	private IDoorPersonService doorPersonService;
	
	/**
	 * 分页列表查询
	 *
	 * @param doorPersonSys
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="门禁人员-分页列表查询", notes="门禁人员-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DoorPersonSys>> queryPageList(DoorPersonSys doorPersonSys,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		if (StringUtils.isNotBlank(doorPersonSys.getName())){
			doorPersonSys.setName("*" + doorPersonSys.getName() + "*");
		}
		QueryWrapper<DoorPersonSys> queryWrapper = QueryGenerator.initQueryWrapper(doorPersonSys, req.getParameterMap());
		Page<DoorPersonSys> page = new Page<DoorPersonSys>(pageNo, pageSize);
		IPage<DoorPersonSys> pageList = doorPersonService.queryPageList(page, queryWrapper);
		return Result.OK(pageList);
	}

	@AutoLog(value = "人员信息同步")
	@ApiOperation(value="人员信息同步", notes="人员信息同步")
	@PostMapping(value = "/sync")
	public Result<?> sync() {
		//同步人员信息
		doorPersonService.sync();
		return Result.OK("同步成功！");
	}
	



}
