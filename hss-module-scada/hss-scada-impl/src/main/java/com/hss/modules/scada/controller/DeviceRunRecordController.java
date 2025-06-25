package com.hss.modules.scada.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.scada.entity.DeviceRunRecord;
import com.hss.modules.scada.service.IDeviceRunRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @description: 设备运行记录列表
* @author zpc
* @date 2024/3/19 14:43
* @version 1.0
*/
@Slf4j
@Api(tags = "设备运行记录表")
@RestController
@RequestMapping("/scada/deviceRunRecord")
public class DeviceRunRecordController extends HssController<DeviceRunRecord, IDeviceRunRecordService> {
    @Autowired
    private IDeviceRunRecordService deviceRunRecordService;

    /**
     * 分页列表查询
     *
     * @param deviceRunRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "设备运行记录表-分页列表查询")
    @ApiOperation(value = "设备运行记录表-分页列表查询", notes = "设备运行记录表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceRunRecord deviceRunRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<DeviceRunRecord> queryWrapper = QueryGenerator.initQueryWrapper(deviceRunRecord, req.getParameterMap());
        Page<DeviceRunRecord> page = new Page<>(pageNo, pageSize);
        IPage<DeviceRunRecord> pageList = deviceRunRecordService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @GetMapping(value = "/list2")
    public Result<?> queryPageList2() {
        return null;
    }

    /**
     * 添加
     *
     * @param deviceRunRecord
     * @return
     */
    @AutoLog(value = "设备运行记录表-添加")
    @ApiOperation(value = "设备运行记录表-添加", notes = "设备运行记录表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceRunRecord deviceRunRecord) {
        deviceRunRecordService.save(deviceRunRecord);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param deviceRunRecord
     * @return
     */
    @AutoLog(value = "设备运行记录表-编辑")
    @ApiOperation(value = "设备运行记录表-编辑", notes = "设备运行记录表-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody DeviceRunRecord deviceRunRecord) {
        deviceRunRecordService.updateById(deviceRunRecord);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备运行记录表-通过id删除")
    @ApiOperation(value = "设备运行记录表-通过id删除", notes = "设备运行记录表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        deviceRunRecordService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "设备运行记录表-批量删除")
    @ApiOperation(value = "设备运行记录表-批量删除", notes = "设备运行记录表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.deviceRunRecordService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备运行记录表-通过id查询")
    @ApiOperation(value = "设备运行记录表-通过id查询", notes = "设备运行记录表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        DeviceRunRecord deviceRunRecord = deviceRunRecordService.getById(id);
        return Result.OK(deviceRunRecord);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceRunRecord
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceRunRecord deviceRunRecord) {
        return super.exportXls(request, deviceRunRecord, DeviceRunRecord.class, "设备运行记录表");
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
        return super.importExcel(request, response, DeviceRunRecord.class);
    }

}
