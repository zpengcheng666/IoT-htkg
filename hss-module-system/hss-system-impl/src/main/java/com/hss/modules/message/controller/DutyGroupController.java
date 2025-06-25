package com.hss.modules.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.message.entity.DutyGroup;
import com.hss.modules.message.service.IDutyGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 值班人员
 * @Author: zpc
 * @Date: 2023-04-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "值班人员")
@RestController
@RequestMapping("/message/dutyPerson")
public class DutyGroupController extends HssController<DutyGroup, IDutyGroupService> {
    @Autowired
    private IDutyGroupService dutyGroupService;

    @ApiOperation(value = "值班人员-分页列表查询", notes = "值班人员-分页列表查询")
    @GetMapping(value = "/page")
    public Result<?> page(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @ApiParam("小组名") @RequestParam(name = "name", required = false) String name,
            @ApiParam(value = "值班id", required = true)
            @RequestParam(name = "dutyId") String dutyId){
        Page<DutyGroup> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<DutyGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DutyGroup::getDutyId,dutyId);
        queryWrapper.orderByDesc(DutyGroup::getCode);
        queryWrapper.like(DutyGroup::getName,name);
        IPage<DutyGroup> pageList = dutyGroupService.page(page,queryWrapper);

        return Result.OK(pageList);
    }

    @ApiOperation(value = "值班人员-分页列表查询", notes = "值班人员-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> list(){
        List<DutyGroup> list = dutyGroupService.list();
        return Result.OK(list);
    }

    /**
     * @description: 排班-生成
     * @author zpc
     * @date 2023/4/26 15:08
     * @version 1.0
     */
//    @ApiOperation(value = "排班-生成", notes = "排班-生成")
//    @PostMapping(value = "/dutyPersonList")
//    public Result<?> dutyPersonList(@RequestBody DutyShiftsDTO dto) {
//        //获取传入时间的年、月、日
//        int year = DateUtil.year(dto.getDate());
//        int month = DateUtil.month(dto.getDate()) + 1;
//        int day = DateUtil.dayOfMonth(dto.getDate());
//
//        //1. 先删除之前的年、月份的数据
//        String param = DateFormatUtils.format(dto.getDate(), "yyyy-MM");
//        LambdaQueryWrapper<DutyShifts> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.likeRight(true, DutyShifts::getDate, param);
//        dutyShiftsService.remove(queryWrapper);
//
//        LambdaQueryWrapper<BaseDutyShiftsLog> queryWrapperLog = new LambdaQueryWrapper<>();
//        queryWrapperLog.likeRight(true, BaseDutyShiftsLog::getDate, param);
//        baseDutyShiftsLogService.remove(queryWrapperLog);
//
//        List<DutyGroup> dutyList = dutyGroupService.list();
//
//        YearMonth yearMonth = YearMonth.of(year, month);
//        int numDays = yearMonth.lengthOfMonth();//获取当前月天数
//
//        int index = dto.getCode();
//        int teamTotal = dutyList.size();
//        //2. 指定了排班的起始日期和结束日期
//        for (int i = day; i <= numDays; i ++){
//            String format =  DateFormatUtils.format(dto.getDate(), "yyyy-MM") + "-" + i;
//            DateTime parse = DateUtil.parse(format);
//
//            index = index % teamTotal;
//
//            DutyGroup duty = dutyList.get(index);
//
//            //3. 将数据存入到表中
//            DutyShifts dutyShifts = new DutyShifts();
//            dutyShifts.setDate(parse);
//            dutyShifts.setShifts(duty.getPersonList());
//            dutyShifts.setDutyGroupId(duty.getId());
//            dutyShifts.setName(duty.getName());
//            dutyShifts.setCode(duty.getCode());
//            dutyShifts.setDutyPostion(duty.getDutyPostion());
//            dutyShiftsService.save(dutyShifts);
//
//            //4.存入值班日志记录表
//            BaseDutyShiftsLog log = new BaseDutyShiftsLog();
//            log.setDate(parse);
//            log.setShifts(duty.getPersonList());
//            log.setDutyGroupId(duty.getId());
//            log.setName(duty.getName());
//            log.setCode(duty.getCode());
//            log.setDutyPostion(duty.getDutyPostion());
//            baseDutyShiftsLogService.save(log);
//
//            index++;
//        }
//        return Result.OK("自动排班成功！");
//    }

    /**
     * @description: 排班信息查询
     * @author zpc
     * @date 2023/4/26 15:10
     * @version 1.0
     */
//    @ApiOperation(value = "排班信息查询", notes = "排班信息查询")
//    @GetMapping(value = "/dutyQueryList")
//    public Result<?> dutyQueryList(@RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd")  Date date) {
//        int size = dutyShiftsService.list().size();
//        if (size == 0) {
//            //获取传入时间的年、月、日
//            int year = DateUtil.year(date);
//            int month = DateUtil.month(date) + 1;
//            int day = DateUtil.dayOfMonth(date);
//
//            List<DutyGroup> dutyList = dutyGroupService.list();
//            if(CollectionUtil.isEmpty(dutyList))
//                return Result.OK(new ArrayList<>());
//
//            YearMonth yearMonth = YearMonth.of(year, month);
//            int numDays = yearMonth.lengthOfMonth();//获取当前月天数
//            int a = numDays -day;
//
//            int index = 0;
//            int teamTotal = dutyList.size();
//            //2. 指定了排班的起始日期和结束日期
//            for (int i = 0; i <= a; i++) {
//                String format = DateFormatUtils.format(date, "yyyy-MM") + "-" + (i + day);
//                DateTime parse = DateUtil.parse(format);
//
//                index = index % teamTotal;
//
//                //3. 将数据存入到表中
//                DutyGroup duty = dutyList.get(index);
//                DutyShifts dutyShifts = new DutyShifts();
//                dutyShifts.setDate(parse);
//                dutyShifts.setShifts(duty.getPersonList());
//                dutyShifts.setDutyGroupId(duty.getId());
//                dutyShifts.setName(duty.getName());
//                dutyShifts.setCode(duty.getCode());
//                dutyShifts.setDutyPostion(duty.getDutyPostion());
//                dutyShiftsService.save(dutyShifts);
//
//                //4.存入值班日志记录表
//                BaseDutyShiftsLog log = new BaseDutyShiftsLog();
//                log.setDate(parse);
//                log.setShifts(duty.getPersonList());
//                log.setDutyGroupId(duty.getId());
//                log.setName(duty.getName());
//                log.setCode(duty.getCode());
//                log.setDutyPostion(duty.getDutyPostion());
//                baseDutyShiftsLogService.save(log);
//
//                index++;
//            }
//        }
//
//        String param = DateFormatUtils.format(date, "yyyy-MM");
//        LambdaQueryWrapper<DutyShifts> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.likeRight(true, DutyShifts::getDate, param);
//        List<DutyShifts> shiftsList = dutyShiftsService.list(queryWrapper);
//
//        return Result.OK(shiftsList);
//    }

    /**
    * @description: 值班日志
    * @date 2023/5/23 14:55
    */
//    @ApiOperation(value = "值班日志", notes = "值班日志")
//    @GetMapping(value = "/queryDutyLog")
//    public Result<?> queryDutyLog(BaseDutyShiftsLog dutyLog,
//                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
//                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
//        LambdaQueryWrapper<BaseDutyShiftsLog> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.orderByDesc(BaseDutyShiftsLog::getDate);
//        Date date = new Date();
//        queryWrapper.le(BaseDutyShiftsLog::getDate,date);
//        if (dutyLog.getBeginTime() != null) {
//            queryWrapper.ge(BaseDutyShiftsLog::getDate, dutyLog.getBeginTime());//开始时间
//        }
//        if (dutyLog.getEndTime() != null) {
//            queryWrapper.le(BaseDutyShiftsLog::getDate, dutyLog.getEndTime());//结束时间
//        }
//
//        Page<BaseDutyShiftsLog> page = new Page<>(pageNo, pageSize);
//        IPage<BaseDutyShiftsLog> pageList = baseDutyShiftsLogService.page(page, queryWrapper);
//        return Result.OK(pageList);
//    }

    @AutoLog(value = "添加值班小组")
    @ApiOperation(value = "添加值班小组", notes = "添加值班小组")
    @PostMapping(value = "/addGroup")
    public Result<?> addGroup(@RequestBody DutyGroup dutyGroup) {
        dutyGroupService.add(dutyGroup);
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "编辑值班小组")
    @ApiOperation(value = "编辑值班小组", notes = "编辑值班小组")
    @PostMapping(value = "/editById")
    public Result<?> editById(@RequestBody DutyGroup dutyGroup) {
        dutyGroupService.editById(dutyGroup);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "值班小组-删除")
    @ApiOperation(value = "值班人员-通过id删除", notes = "值班人员-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        DutyGroup byId = dutyGroupService.getById(id);
        if (byId == null) {
            return Result.OK("删除成功!");
        }
        LogUtil.setOperate(byId.getName());
        dutyGroupService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "值班人员-批量删除", notes = "值班人员-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.dutyGroupService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "值班人员-通过id查询", notes = "值班人员-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DutyGroup dutyGroup = dutyGroupService.getById(id);
        return Result.OK(dutyGroup);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param dutyGroup
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DutyGroup dutyGroup) {
        return super.exportXls(request, dutyGroup, DutyGroup.class, "值班人员");
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
        return super.importExcel(request, response, DutyGroup.class);
    }

}
