package com.hss.modules.preplan.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.core.common.system.vo.LoginUser;
import com.hss.modules.preplan.entity.*;
import com.hss.modules.preplan.mapper.ContingencyRecordMapper;
import com.hss.modules.preplan.pojo.ProcessWorkDTO;
import com.hss.modules.preplan.service.*;
import com.hss.modules.system.model.ContingencyRecordAlarmTerminal;
import org.apache.shiro.SecurityUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description: 特情处置记录
 * @Author: zpc
 * @Date: 2023-02-13
 * @Version: V1.0
 */
@Service
public class ContingencyRecordServiceImpl extends ServiceImpl<ContingencyRecordMapper, ContingencyRecord> implements IContingencyRecordService {

    @Autowired
    private IContingencyRecordTerminalService contingencyRecordTerminalService;
    @Autowired
    private IContingencyPlanService contingencyPlanService;
    @Autowired
    private IContingencyPlanStageService contingencyPlanStageService;
    @Autowired
    private IContingencyRecordStageService contingencyRecordStageService;
    @Autowired
    private IContingencyRecordWorkitemService contingencyRecordWorkitemService;
    @Autowired
    private ApplicationContext applicationContext;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void  saveAlarmTerminal(ContingencyRecordAlarmTerminal contingencyRecordAlarmTerminal) {
        String terminalIdsStr = contingencyRecordAlarmTerminal.getTermianlIds();
        if (StringUtils.isBlank(terminalIdsStr)){
            throw new HssBootException("终端不能为空");
        }
        //获取终端ids
        String[] terminalIds = terminalIdsStr.split(",");
        for (String terminalId : terminalIds) {
            //根据terminalId查询
            ContingencyRecordTerminal r = contingencyRecordTerminalService.getByTerminalId(terminalId);
            if (r != null) {
                throw new HssBootException("有终端正在执行任务,请刷新重试");
            }
        }
        //获取预案id
        String contingencyId = contingencyRecordAlarmTerminal.getContingencyId();
        ContingencyPlan record = contingencyPlanService.getById(contingencyId);
        //预案名称
        String contingencyName = record.getName();
        // 记录主表
        String id = saveMain(contingencyRecordAlarmTerminal, contingencyId, contingencyName);
        // 终端关联关系
        saveRelation(terminalIds, id);
        // 保存阶段
        saveStage(contingencyId, id);
    }

    /**
     * 保存阶段
     * @param contingencyId
     * @param id
     */
    private void saveStage(String contingencyId, String id) {
        List<ContingencyPlanStage> stageList = contingencyPlanStageService.listStageAndWorksByContingencyId(contingencyId);
        if (stageList.isEmpty()) {
            throw new HssBootException("预案阶段不能为空");
        }
        for (ContingencyPlanStage contingencyPlanStage : stageList) {
            ContingencyRecordStage recordStage = new ContingencyRecordStage();
            recordStage.setPlanId(id);
            recordStage.setIndex1(contingencyPlanStage.getIndex1());
            recordStage.setDeleted(0);
            recordStage.setName(contingencyPlanStage.getName());
            recordStage.setIsCompleted(0);
            recordStage.setCreatedTime(new Date());
            contingencyRecordStageService.save(recordStage);
            List<ContingencyPlanWorkitem> workList = contingencyPlanStage.getWorkitemList();
            saveWorks(contingencyPlanStage, recordStage, workList);
        }
    }

    /**
     * 保存工作
     * @param contingencyPlanStage
     * @param recordStage
     * @param workList
     */
    private void saveWorks(ContingencyPlanStage contingencyPlanStage, ContingencyRecordStage recordStage, List<ContingencyPlanWorkitem> workList) {
        if (workList.isEmpty()){
            throw new HssBootException("阶段:" + contingencyPlanStage.getName() +" 工作列表为空");
        }
        for (ContingencyPlanWorkitem work : workList) {
            ContingencyRecordWorkitem workitem = new ContingencyRecordWorkitem();
            workitem.setName(work.getName());
            workitem.setContent(work.getContent());
            workitem.setIndex1(work.getIndex1());
            workitem.setIsCompleted(0);//完成状态
            workitem.setStageId(recordStage.getId());
            workitem.setWorkProcess("");
            contingencyRecordWorkitemService.save(workitem);
        }
    }

    /**
     * 保存终端关系
     * @param terminalIds
     * @param id
     */
    private void saveRelation(String[] terminalIds, String id) {
        for (String s : terminalIds) {
            ContingencyRecordTerminal contingencyRecordTerminal = new ContingencyRecordTerminal();
            contingencyRecordTerminal.setTerminalId(s);
            contingencyRecordTerminal.setRecordId(id);
            contingencyRecordTerminalService.save(contingencyRecordTerminal);
        }
    }
    /**
     * 保存记录主表
     * @param contingencyRecordAlarmTerminal
     * @param contingencyId
     * @param contingencyName
     * @return
     */
    @NotNull
    private String saveMain(ContingencyRecordAlarmTerminal contingencyRecordAlarmTerminal, String contingencyId, String contingencyName) {
        ContingencyRecord contingencyRecord = new ContingencyRecord();
        contingencyRecord.setPlanId(contingencyId);
        contingencyRecord.setName(contingencyName);
        contingencyRecord.setAlarmRecordId(contingencyRecordAlarmTerminal.getAlarmId());
        contingencyRecord.setInitiator(contingencyRecordAlarmTerminal.getSponsor());
        contingencyRecord.setStartTime(new Date());
        contingencyRecord.setIsCompleted(0);
        contingencyRecord.setDeleted(0);
        contingencyRecord.setCreatedTime(new Date());
        this.save(contingencyRecord);
        return contingencyRecord.getId();
    }

    @Override
    public ContingencyRecord getByTerminalId(String terminalId) {
        ContingencyRecordTerminal recordTerminal = contingencyRecordTerminalService.getByTerminalId(terminalId);
        if (recordTerminal == null) {
            return null;
        }
        String recordId = recordTerminal.getRecordId();
        ContingencyRecord record = getById(recordId);
        boolean flag = false;
        int step1 = 0;
        boolean flag2 = false;
        List<ContingencyRecordStage>  stageList =  contingencyRecordStageService.listByRecordId(recordId);
        for (ContingencyRecordStage contingencyRecordStage : stageList) {
            int step2= 0;
            for (ContingencyRecordWorkitem work : contingencyRecordStage.getWorkList()) {
                if (work.getIsCompleted() == 0){
                    if (flag) {
                        work.setIsCompleted(3);
                    }else {
                        flag = true;
                    }
                }else {
                    step2 ++;
                }
                if (flag2) {
                    step2 = -1;
                }
                contingencyRecordStage.setStepIndex(step2);
            }
            if (!flag) {
                step1++;
            }else {
                flag2 = true;
            }
        }
        record.setStepIndex(step1);

        record.setStageList(stageList);
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processWork(ProcessWorkDTO dto) {
        ContingencyRecordWorkitem work = contingencyRecordWorkitemService.getById(dto.getWorkId());
        if (work == null) {
            throw new HssBootException("预案工作不存在");
        }
        final Integer IS_COMPLETED = 1;
        if (IS_COMPLETED.equals(work.getIsCompleted())) {
            return;
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ContingencyRecordWorkitem updateWork = new ContingencyRecordWorkitem();
        updateWork.setId(work.getId());
        updateWork.setSubmitTime(new Date());
        updateWork.setIsCompleted(IS_COMPLETED);
        updateWork.setSubmitter(sysUser.getUsername());
        contingencyRecordWorkitemService.updateById(updateWork);
        checkStateCompletedAndUpdate(work, IS_COMPLETED);

    }

    /**
     * 校验阶段完成并更新
     * @param work
     * @param IS_COMPLETED
     */
    private void checkStateCompletedAndUpdate(ContingencyRecordWorkitem work, Integer IS_COMPLETED) {
        int notCompletedCount = contingencyRecordWorkitemService.countNotCompletedByStageId(work.getStageId());
        if (notCompletedCount == 0) {
            ContingencyRecordStage stage = contingencyRecordStageService.getById(work.getStageId());
            ContingencyRecordStage updateStage = new ContingencyRecordStage();
            updateStage.setId(stage.getId());
            updateStage.setIsCompleted(IS_COMPLETED);
            contingencyRecordStageService.updateById(updateStage);
            checkRecordCompletedAndUpdate(IS_COMPLETED, stage);
        }
    }

    /**
     * 校验记录完成并更新
     * @param IS_COMPLETED
     * @param stage
     */
    private void checkRecordCompletedAndUpdate(Integer IS_COMPLETED, ContingencyRecordStage stage) {
        int notCompletedCountStage = contingencyRecordStageService.countNotCompletedByRecordId(stage.getPlanId());
        if (notCompletedCountStage == 0) {
            ContingencyRecord record = getById(stage.getPlanId());
            ContingencyRecord updateRecord = new ContingencyRecord();
            updateRecord.setId(record.getId());
            updateRecord.setIsCompleted(IS_COMPLETED);
            updateRecord.setEndTime(new Date());
            updateById(updateRecord);
        }
    }

    @Override
    public boolean isHaveYuAn(String terminalId) {
        int count = baseMapper.countNotCompletedByTerminalId(terminalId);
        return count > 0;
    }
}
