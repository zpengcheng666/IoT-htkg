package com.hss.modules.facility.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.modules.facility.entity.DeviceBI;
import com.hss.modules.facility.entity.DeviceRunLog;
import com.hss.modules.facility.model.DeviceRunLogDTO;
import com.hss.modules.facility.model.DeviceRunLogVO;
import com.hss.modules.facility.service.IDeviceBIService;
import com.hss.modules.facility.service.IDeviceRunLogService;
import com.hss.modules.facility.service.IDeviceTypeService;
import com.hss.modules.system.model.OptionModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 动用使用
 * @Author: zpc
 * @Date: 2022-12-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "动用使用")
@RestController
@RequestMapping("/facility/deviceRunLog")
public class DeviceRunLogController extends HssController<DeviceRunLog, IDeviceRunLogService> {
    @Autowired
    private IDeviceRunLogService deviceRunLogService;
    @Autowired
    private IDeviceBIService deviceBIService;
    @Autowired
    private IDeviceTypeService deviceTypeService;

    @ApiOperation(value = "动用使用-分页列表查询", notes = "动用使用-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<DeviceRunLogVO>> runLogList(DeviceRunLogDTO dto) {
        List<String> typeIds = null;
        if (StringUtils.isNotBlank(dto.getType())) {
            typeIds = deviceTypeService.listIdByType(dto.getType());
        }
        return Result.OK(deviceRunLogService.logPage(dto, typeIds));
    }

    /**
     * @description: 设备名称下拉Options
     * @author zpc
     * @date 2022/12/12 14:54
     * @version 1.0
     */
    @ApiOperation(value = "设备名称下拉Options", notes = "设备名称下拉Options")
    @GetMapping(value = "/listDeviceOptions")
    public Result<?> listDeviceOptions() {
        List<DeviceBI> list = this.deviceBIService.list();
        List<OptionModel> options = list.stream().map((DeviceBI e) -> {
            OptionModel tmp = new OptionModel();
            tmp.setId(e.getId());
            tmp.setName(e.getName());
            return tmp;
        }).collect(Collectors.toList());
        return Result.OK(options);
    }

    @ApiOperation(value = "导出动用使用记录", notes = "导出动用使用记录")
    @GetMapping(value = "/export")
    public ModelAndView export(DeviceRunLogDTO dto) {
        dto.setPageNo(0);
        dto.setPageSize(Integer.MAX_VALUE);
        List<String> typeIds = null;
        if (StringUtils.isNotBlank(dto.getType())) {
            typeIds = deviceTypeService.listIdByType(dto.getType());
        }
        IPage<DeviceRunLogVO> page = deviceRunLogService.logPage(dto, typeIds);
        List<DeviceRunLogVO> list = page.getRecords();
        return this.exportXls(list, DeviceRunLogVO.class, "设备动用使用");
    }
}
