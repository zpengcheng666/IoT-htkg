package com.hss.modules.alarm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.alarm.entity.AlarmData;
import com.hss.modules.alarm.model.AlarmAckDTO;
import com.hss.modules.alarm.model.AlarmBatchAckDTO;
import com.hss.modules.alarm.service.IAlarmDataService;
import com.hss.modules.preplan.entity.ContingencyPlan;
import com.hss.modules.preplan.service.IContingencyPlanService;
import com.hss.modules.preplan.service.IContingencyRecordService;
import com.hss.modules.scada.model.TerminalModel;
import com.hss.modules.scada.model.YuAnModel;
import com.hss.modules.system.entity.BaseMessage;
import com.hss.modules.system.entity.BaseTerminal;
import com.hss.modules.system.monitorThing.AjaxResult;
import com.hss.modules.system.service.IBaseMessageService;
import com.hss.modules.system.service.IBaseTerminalService;
import io.minio.errors.MinioException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.minio.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.hss.modules.system.monitorThing.AjaxResult.success;

/**
 * @author zpc
 * @version 1.0
 * @description: 报警数据
 * @date 2024/3/20 11:36
 */
@Slf4j
@Api(tags = "报警数据")
@RestController
@RequestMapping("/alarm/alarmData")
public class AlarmDataController extends HssController<AlarmData, IAlarmDataService> {
    @Autowired
    private IAlarmDataService alarmDataService;
    @Autowired
    private IBaseMessageService baseMessageService;
    @Autowired
    private IBaseTerminalService baseTerminalService;
    @Autowired
    private IContingencyPlanService contingencyPlanService;
    @Autowired
    private IContingencyRecordService contingencyRecordService;

    @Autowired
    private MinioUtils minioUtil;

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传请求（单个）", notes = "上传请求（单个）")
    public String uploadFile(MultipartFile file) throws IOException, MinioException {
        // 获取文件名
        String originalFilename = file.getOriginalFilename();

        // 去掉文件名后缀
//        String filenameWithoutExtension = Objects.requireNonNull(originalFilename).substring(0, originalFilename.lastIndexOf('.'));
        // 转换为目录路径
//        String dirPath = FileUploadUtils.convertFileNameToPath("ZT40-WS·2023-Y-B-0001");

        // 上传文件到MinIO
        return minioUtil.putObjectSafe(originalFilename, file.getInputStream(), Long.valueOf(file.getSize()), "Common");
    }

    /**
     * 分页列表查询
     *
     * @param alarmData
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "报警数据-分页列表查询", notes = "报警数据-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AlarmData alarmData,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<AlarmData> queryWrapper = QueryGenerator.initQueryWrapper(alarmData, req.getParameterMap());
        queryWrapper.orderByDesc("ID");
        Page<AlarmData> page = new Page<>(pageNo, pageSize);
        //查询报警数据分页列表
        IPage<AlarmData> pageList = alarmDataService.queryPageList(page, queryWrapper);
        return Result.OK(pageList);
    }

    @ApiOperation(value = "终端下拉框", notes = "终端下拉框")
    @GetMapping(value = "/terminalOptiongs")
    public Result<?> terminalOptiongs(TerminalModel terminalModel) {
        List<BaseTerminal> list = baseTerminalService.list();
        Integer yjcz = (Integer) 1;
        //筛选过后，重新生成下拉框
        List<TerminalModel> listDoor = list.stream()
                .filter(t -> yjcz.equals(t.getYjcz()))
                //查看终端id是否有源
                .filter(t -> !contingencyRecordService.isHaveYuAn(t.getId()))
                .map(e -> {
                    TerminalModel model = new TerminalModel();
                    model.setId(e.getId());
                    model.setName(e.getName());
                    return model;
                }).collect(Collectors.toList());
        return Result.OK(listDoor);
    }

    /**
     * @description: 预案下拉框
     * @author zpc
     * @date 2023/2/28 13:21
     * @version 1.0
     */
    @ApiOperation(value = "预案下拉框", notes = "预案下拉框")
    @GetMapping(value = "/contingencyOptiongs")
    public Result<?> contingencyOptiongs(YuAnModel yuAnModel) {
        List<ContingencyPlan> list = contingencyPlanService.list();
        List<YuAnModel> listDoor = list.stream().map(e -> {
            YuAnModel model = new YuAnModel();
            model.setId(e.getId());
            model.setName(e.getName());
            model.setDescribe(e.getDescription());
            return model;
        }).collect(Collectors.toList());
        return Result.OK(listDoor);
    }

    /**
     * 添加实时报警
     *
     * @param alarmData
     * @return
     */
    @AutoLog(value = "添加实时报警")
    @ApiOperation(value = "报警数据-添加", notes = "报警数据-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AlarmData alarmData) {
        alarmDataService.save(alarmData);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑实时报警
     *
     * @param alarmData
     * @return
     */
    @AutoLog(value = "编辑实时报警")
    @ApiOperation(value = "报警数据-编辑", notes = "报警数据-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody AlarmData alarmData) {
        alarmDataService.updateById(alarmData);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除实时报警
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除实时报警")
    @ApiOperation(value = "报警数据-通过id删除", notes = "报警数据-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        AlarmData byId = alarmDataService.getById(id);
        alarmDataService.removeById(id);
        if (byId != null) {
            LogUtil.setOperate(byId.getOriginVarName());
        }

        return Result.OK("删除成功!");
    }

    /**
     * 批量删除实时报警
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "批量删除实时报警")
    @ApiOperation(value = "报警数据-批量删除", notes = "报警数据-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.alarmDataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "报警数据-通过id查询", notes = "报警数据-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AlarmData alarmData = alarmDataService.getById(id);
        return Result.OK(alarmData);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param alarmData
     */
    @ApiOperation(value = "报警数据-导出Excel", notes = "报警数据-导出Excel")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AlarmData alarmData) {
        return super.exportXls(request, alarmData, AlarmData.class, "报警数据");
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
        return super.importExcel(request, response, AlarmData.class);
    }

    @AutoLog(value = "确认报警")
    @ApiOperation(value = "报警数据-确认报警", notes = "报警数据-确认报警")
    @PutMapping(value = "/ack")
    public Result<?> ack(@RequestBody AlarmAckDTO dto) {
        //确认报警
        alarmDataService.ack(dto);
        return Result.OK("确认报警成功!");
    }

    @AutoLog(value = "批量确认报警")
    @ApiOperation(value = "报警数据-批量确认报警", notes = "报警数据-批量确认报警")
    @PutMapping(value = "/batchAck")
    public Result<?> batchAck(@RequestBody AlarmBatchAckDTO dto) {
        //批量确认报警
        alarmDataService.batchAck(dto);
        return Result.OK("确认报警成功!");
    }

    @ApiOperation(value = "查询报警数据、消息通知、提醒数量", notes = "查询报警数据、消息通知、提醒数量")
    @GetMapping(value = "/findNoAckAlarmCount")
    public Result<?> findNoAckAlarmCount() {
        //获取报警数据列表
        int alarmCnt = alarmDataService.getCount();
        //消息通知列表数据
        List<BaseMessage> messageList = baseMessageService.list();
        int notifyCnt = messageList.size();
        List<Map<String, Integer>> list = new ArrayList<>();
        Map<String, Integer> msg = new HashMap<>();
        msg.put("alarmCnt", alarmCnt);
        msg.put("notifyCnt", notifyCnt);
        msg.put("remindCnt", 0);
        list.add(msg);
        return Result.OK(list);
    }
}
