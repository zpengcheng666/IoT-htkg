package com.hss.modules.alarm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.alarm.entity.AlarmHistory;
import com.hss.modules.alarm.model.AlarmHistoryHandlerDTO;
import com.hss.modules.alarm.model.AlarmHistoryStatSearchModel;
import com.hss.modules.alarm.service.IAlarmHistoryService;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.store.model.EnvGoodRatioDTO;
import com.hss.modules.store.model.vo.LineStateVO;
import com.hss.modules.store.model.vo.PieStateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 报警历史数据
 * @Author: jeecg-boot
 * @Date: 2022-12-01
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "报警历史数据")
@RestController
@RequestMapping("/alarm/alarmHistory")
public class AlarmHistoryController extends HssController<AlarmHistory, IAlarmHistoryService> {
    @Autowired
    private IAlarmHistoryService alarmHistoryService;
    @Autowired
    private IConSheBeiService conSheBeiService;

    /**
     * 分页列表查询
     *
     * @param model
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "报警历史数据-分页列表查询", notes = "报警历史数据-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AlarmHistory model,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LambdaQueryWrapper<AlarmHistory> queryWrapper = getAlarmHistoryQueryWrapper(model);
        Page<AlarmHistory> page = new Page<>(pageNo, pageSize);
        IPage<AlarmHistory> pageList = alarmHistoryService.queryPageList(page, queryWrapper);
        return Result.OK(pageList);
    }

    @NotNull
    private  LambdaQueryWrapper<AlarmHistory> getAlarmHistoryQueryWrapper(AlarmHistory model) {
        LambdaQueryWrapper<AlarmHistory> queryWrapper = new LambdaQueryWrapper<>();
        //按照开始时间降序排列
        queryWrapper.orderByDesc(AlarmHistory::getAlarmStartTime);
        //查询设备类型列表
        if(OConvertUtils.isNotEmpty(model.getSubsystem()) && !"alarmQuery".equals(model.getSubsystem())){
            List<String> deviceIds = conSheBeiService.listIdBySubsystem(model.getSubsystem());
            if (!deviceIds.isEmpty()) {
                queryWrapper.in(AlarmHistory::getDeviceId, deviceIds);
            }
        }
        if (OConvertUtils.isNotEmpty(model.getIsHandle())){
            //是否处理
            queryWrapper.eq(AlarmHistory::getIsHandle,model.getIsHandle());
        }

        //查询条件拼接
        if (model.getStartTime() != null) {
            queryWrapper.ge(AlarmHistory::getAlarmStartTime, model.getStartTime());
        }
        if (model.getEndTime() != null) {
            queryWrapper.le(AlarmHistory::getAlarmStartTime, model.getEndTime());
        }
        if (StringUtils.isNotBlank(model.getLevelId())){
            queryWrapper.eq(AlarmHistory::getLevelId, model.getLevelId());
        }
        if (StringUtils.isNotBlank(model.getAlarmTypeId())){
            queryWrapper.eq(AlarmHistory::getAlarmTypeId, model.getAlarmTypeId());
        }

        //设备类型id
        String deviceTypeId = model.getDeviceTypeId();
        //设备id
        String deviceId = model.getDeviceId();
        //设备属性id
        String attrId = model.getAttrId();

        if (StringUtils.isNotEmpty(deviceTypeId)) {
            String[] split = deviceTypeId.split(",");
            List<String> deviceTypeIdList = Arrays.asList(split);
            queryWrapper.in(AlarmHistory::getDeviceTypeId, deviceTypeIdList);
        }
        if (StringUtils.isNotEmpty(deviceId)) {
            String[] split = deviceId.split(",");
            List<String> deviceIdList = Arrays.asList(split);
            queryWrapper.in(AlarmHistory::getDeviceId, deviceIdList);
        }
        if (StringUtils.isNotEmpty(attrId)) {
            String[] split = attrId.split(",");
            List<String> attrIdList = Arrays.asList(split);
            queryWrapper.in(AlarmHistory::getAttrId, attrIdList);
        }
        return queryWrapper;
    }

    @ApiOperation(value = "报警历史数据-统计分析", notes = "报警历史数据-统计分析")
    @GetMapping(value = "/stat")
    public Result<LineStateVO> stat(AlarmHistoryStatSearchModel model) {
        LineStateVO stat = alarmHistoryService.stat(model);
        return Result.OK(stat);
    }

    /**
     * 编辑
     *
     * @param alarmHistory
     * @return
     */
    @AutoLog(value = "编辑历史报警")
    @ApiOperation(value = "报警历史数据-编辑", notes = "报警历史数据-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody AlarmHistory alarmHistory) {
        //设定初始值
        alarmHistory.setHandler(alarmHistory.getHandler());
        alarmHistory.setHandleTime(new Date());
        alarmHistory.setHandleMethod(alarmHistory.getHandleMethod());
        alarmHistory.setIsHandle("1");
        alarmHistoryService.saveOrUpdate(alarmHistory);
        return Result.OK("处理成功!");
    }

    @AutoLog(value = "批量处理")
    @ApiOperation(value = "批量处理", notes = "批量处理")
    @PutMapping(value = "/handler")
    public Result<?> handler(@RequestBody AlarmHistoryHandlerDTO dto) {
        alarmHistoryService.handler(dto);
        return Result.OK("批量处理成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "报警历史数据-通过id删除", notes = "报警历史数据-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        alarmHistoryService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "批量历史报警")
    @ApiOperation(value = "报警历史数据-批量删除", notes = "报警历史数据-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.alarmHistoryService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "报警历史数据-通过id查询", notes = "报警历史数据-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AlarmHistory alarmHistory = alarmHistoryService.getById(id);
        return Result.OK(alarmHistory);
    }

    @ApiOperation(value="统计分析-合格率统计", notes="统计分析-合格率统计")
    @GetMapping(value = "/goodRatio/state")
    public Result<List<PieStateVO>> goodRatio(EnvGoodRatioDTO dto){
        List<PieStateVO> vo = alarmHistoryService.goodRatio(dto);
        return Result.OK(vo);
    }

    @ApiOperation(value="报警数量统计", notes="报警数量统计")
    @GetMapping(value = "/stateCountLastMonth")
    public Result<LineStateVO> stateCountLastMonth(){
        LineStateVO vo = alarmHistoryService.stateCountLastMonth();
        return Result.OK(vo);
    }
}
