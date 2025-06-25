package com.hss.modules.scada.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.entity.DeviceExecuteLog;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.scada.service.IDeviceExecuteLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @description: 下发设备命令日志查询、删除
* @author zpc
* @date 2024/3/19 14:39
* @version 1.0
*/
@Slf4j
@Api(tags="下发设备命令日志")
@RestController
@RequestMapping("/scada/deviceExecuteLog")
public class DeviceExecuteLogController extends HssController<DeviceExecuteLog, IDeviceExecuteLogService> {
	@Autowired
	private IDeviceExecuteLogService deviceExecuteLogService;

	@Autowired
	private IConSheBeiService conSheBeiService;

	@Autowired
	private IConDeviceAttributeService conDeviceAttributeService;

	/**
	 * 分页列表查询
	 */
	@ApiOperation(value="下发设备命令日志-分页列表查询", notes="下发设备命令日志-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DeviceExecuteLog deviceExecuteLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DeviceExecuteLog> queryWrapper = QueryGenerator.initQueryWrapper(deviceExecuteLog, req.getParameterMap());
		queryWrapper.orderByDesc("EXECUTE_TIME");
		Page<DeviceExecuteLog> page = new Page<>(pageNo, pageSize);
		IPage<DeviceExecuteLog> pageList = deviceExecuteLogService.page(page, queryWrapper);
		pageList.getRecords().forEach( e -> {
			if (StringUtils.isNotBlank(e.getDeviceId())){
				ConSheBei sheBei = this.conSheBeiService.getById(e.getDeviceId());
				if (sheBei != null){
					e.setDeviceName(sheBei.getName());
				}
			}
			if (StringUtils.isNotBlank(e.getAttrId())){
				ConDeviceAttribute attribute = this.conDeviceAttributeService.getById(e.getAttrId());
				if (attribute != null){
					e.setAttrName(attribute.getName());
				}
			}
		});

		return Result.OK(pageList);
	}

	 @ApiOperation(value="导出日志", notes="导出日志")
	 @RequestMapping(value = "/exportXls")
	 public ModelAndView exportXls(DeviceExecuteLog deviceExecuteLog) {
		 List<DeviceExecuteLog> list = deviceExecuteLogService.list(deviceExecuteLog);
		 return super.exportXls(list, DeviceExecuteLog.class, "执行日志");
	 }

	
	/**
	 * 通过id删除
	 */
	@AutoLog(value = "设备命令日志删除")
	@ApiOperation(value="下发设备命令日志-通过id删除", notes="下发设备命令日志-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id") String id) {
		DeviceExecuteLog byId = deviceExecuteLogService.getById(id);
		deviceExecuteLogService.removeById(id);
		LogUtil.setOperate(byId.getDeviceId() + "." + byId.getAttrId());
		return Result.OK("删除成功!");
	}

	/**
	 * 通过id查询
	 */
	@ApiOperation(value="下发设备命令日志-通过id查询", notes="下发设备命令日志-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id") String id) {
		DeviceExecuteLog deviceExecuteLog = deviceExecuteLogService.getById(id);
		return Result.OK(deviceExecuteLog);
	}
}
