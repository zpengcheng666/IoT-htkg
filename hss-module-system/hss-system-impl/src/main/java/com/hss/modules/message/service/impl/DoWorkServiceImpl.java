package com.hss.modules.message.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.message.constant.MessageStateEnum;
import com.hss.modules.message.dto.DoWorkPageDTO;
import com.hss.modules.message.dto.DoWorkPageVO;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.DoWork;
import com.hss.modules.message.entity.DoWorkTerminal;
import com.hss.modules.message.mapper.DoWorkMapper;
import com.hss.modules.message.model.DoWorkTerminalInfoVO;
import com.hss.modules.message.model.MessageStateBO;
import com.hss.modules.message.service.IDoWorkService;
import com.hss.modules.message.service.IDoWorkTerminalService;
import com.hss.modules.message.service.PublishCheckService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 手动值班
 * @Author: zpc
 * @Date:   2023-12-06
 * @Version: V1.0
 */
@Service
public class DoWorkServiceImpl extends ServiceImpl<DoWorkMapper, DoWork> implements IDoWorkService, PublishCheckService {

    @Autowired
    private IDoWorkTerminalService doWorkTerminalService;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public IPage<DoWorkPageVO> pageList(DoWorkPageDTO dto) {
        Page<DoWorkPageVO> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        IPage<DoWorkPageVO> doWorkPageVoPage = baseMapper.pageList(page, dto);
        for (DoWorkPageVO vo : doWorkPageVoPage.getRecords()) {
            vo.setTerminalIds(doWorkTerminalService.listTerminalIdsByDoWorkId(vo.getId()));
        }
        return doWorkPageVoPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(PublishMessageDTO dto) {
        MessageStateBO bo = getBoById(dto.getMessageId());
        ZonedDateTime zonedDateTime = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault());
        bo.checkPublish(Date.from(zonedDateTime.toInstant()));
        DoWork update = new DoWork();
        update.setId(bo.getId());
        update.setState(bo.getState());
        updateById(update);
        List<DoWorkTerminal> rList = dto.getTerminalIds().stream().map(id -> {
            DoWorkTerminal r = new DoWorkTerminal();
            r.setTerminalId(id);
            r.setDoWorkId(dto.getMessageId());
            return r;
        }).collect(Collectors.toList());
        doWorkTerminalService.saveBatch(rList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revocation(String id) {
        MessageStateBO bo = getBoById(id);
        if (bo.checkRevocation()) {
            DoWork update = new DoWork();
            update.setId(bo.getId());
            update.setState(bo.getState());
            updateById(update);
            doWorkTerminalService.delByMessageId(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkTimeOut() {

        Instant instant = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        List<DoWork> list = baseMapper.listOut(Date.from(instant));
        if (list.isEmpty()) {
            return;
        }
        for (DoWork doWork : list) {
            doWork.setState(MessageStateEnum.OVER.value);
        }
        updateBatchById(list);
        for (DoWork doWork : list) {
            doWorkTerminalService.delByMessageId(doWork.getId());
        }

    }


    @Override
    public List<DoWorkTerminalInfoVO> listShowWorkByTerminalId(String terminalId) {
        List<DoWork> doWorks = baseMapper.listShowWorkByTerminalId(terminalId);
       return doWorks.stream().map(d -> {
            DoWorkTerminalInfoVO vo = new DoWorkTerminalInfoVO();
            vo.setWorkName(d.getWorkName());
            vo.setPersons(d.getPersons());
            LocalDate start = d.getWorkDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = d.getWorkEndDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (start.equals(end)) {
                vo.setDay(dtf.format(start));
            } else {
                vo.setDay(dtf.format(start) + " - " + dtf.format(end));
            }
            return vo;
        }).collect(Collectors.toList());

    }

    @Override
    public List<DoWork> listExcel(DoWorkPageDTO dto) {
        return baseMapper.lisExcel(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(DoWork doWork) {
        check(doWork);
        this.save(doWork);
    }

    @Override
    public void edit(DoWork doWork) {
        check(doWork);
        updateById(doWork);
    }

    @Override
    public void addList(List<DoWork> list) {
        for (DoWork doWork : list) {
            check(doWork);
        }
        saveBatch(list);
    }

    private void check(DoWork doWork) {
        if (StringUtils.isBlank(doWork.getWorkName())) {
            throw new HssBootException("工作名称不能为空");
        }
        if (StringUtils.isBlank(doWork.getPersons())) {
            throw new HssBootException("值班人员列表不能为空");
        }
        if (doWork.getWorkDay() == null) {
            throw new HssBootException("值班开始日期不能为空");
        }
        if (doWork.getWorkEndDay() == null) {
            throw new HssBootException("值班结束日期不能为空");
        }
        if (doWork.getWorkDay().getTime() < doWork.getWorkEndDay().getTime()) {
            throw new HssBootException("值班开始日期不能小于结束日期");
        }
        doWork.setState(MessageStateEnum.NOT_PUBLISH.value);
    }

    @Override
    public MessageStateBO getBoById(String id) {
        DoWork byId = getById(id);
        if (byId == null) {
            throw new HssBootException("数据不存在");
        }
        return toBo(byId);
    }



    private MessageStateBO toBo(DoWork entity) {
        MessageStateBO bo = new MessageStateBO();
        bo.setId(entity.getId());
        bo.setState(entity.getState());
        bo.setEnterDate(entity.getWorkDay());
        bo.setExitDate(entity.getWorkEndDay());
        return bo;
    }


}
