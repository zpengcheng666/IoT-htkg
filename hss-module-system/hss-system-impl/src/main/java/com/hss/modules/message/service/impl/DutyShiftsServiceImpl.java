package com.hss.modules.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.message.dto.DutyHistoryDTO;
import com.hss.modules.message.dto.DutyHistoryVO;
import com.hss.modules.message.dto.DutyShiftsAutomaticDTO;
import com.hss.modules.message.entity.DutyGroup;
import com.hss.modules.message.entity.DutyShifts;
import com.hss.modules.message.entity.PublishDuty;
import com.hss.modules.message.entity.PublishDutyTerminal;
import com.hss.modules.message.mapper.DutyShiftsMapper;
import com.hss.modules.message.model.DutyPersonModel;
import com.hss.modules.message.model.DutyTerminalInfoVO;
import com.hss.modules.message.service.IDutyGroupService;
import com.hss.modules.message.service.IDutyShiftsService;
import com.hss.modules.message.service.IPublishDutyService;
import com.hss.modules.message.service.IPublishDutyTerminalService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 排班班次
 * @Author: zpc
 * @Date: 2023-04-26
 * @Version: V1.0
 */
@Service
public class DutyShiftsServiceImpl extends ServiceImpl<DutyShiftsMapper, DutyShifts> implements IDutyShiftsService {
    @Autowired
    private IPublishDutyTerminalService publishDutyTerminalService;
    @Autowired
    private IDutyGroupService dutyGroupService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IPublishDutyService publishDutyService;


    @Scheduled(cron = "0 0 0 * * ?")
    public void checkDate() {
        List<PublishDutyTerminal> terminals = publishDutyTerminalService.list();
        if (terminals.isEmpty()) {
            return;
        }
        Set<String> terminalIds = terminals.stream().map(PublishDutyTerminal::getTerminalId).collect(Collectors.toSet());
    }

    @Override
    public List<DutyTerminalInfoVO> listByTerminalId(String terminalId) {
        List<String> dutyIds = publishDutyTerminalService.listDutyIdsByTerminalId(terminalId);
        if (dutyIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<DutyTerminalInfoVO> list = new ArrayList<>();
        for (String dutyId : dutyIds) {
            DutyShifts dutyShifts = listTodayByDutyId(dutyId);
            if (dutyShifts == null) {
                continue;
            }
            PublishDuty byId = publishDutyService.getById(dutyId);
            if (byId == null) {
                continue;
            }
            DutyPersonModel[] shifts = dutyShifts.getShifts();
            if (shifts == null || shifts.length == 0) {
                continue;
            }
            for (DutyPersonModel shift : shifts) {
                DutyTerminalInfoVO vo = new DutyTerminalInfoVO();
                vo.setDutyName(byId.getName());
                vo.setName(shift.getName());
                vo.setDutyPost(shift.getDutyPost());
                vo.setDutySjd(shift.getDutySjd());
                list.add(vo);
            }
        }

        return list;
    }

    /**
     * 查询当天排班信息
     * @param dutyId
     * @return
     */
    private DutyShifts listTodayByDutyId(String dutyId) {
        Date today = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        LambdaQueryWrapper<DutyShifts> wp = new LambdaQueryWrapper<DutyShifts>()
                .eq(DutyShifts::getDutyId, dutyId)
                .eq(DutyShifts::getDate, today);
        return getOne(wp);
    }

    @Override
    public List<DutyShifts> listByDateAndDutyId(Date date, String dutyId) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startDate = localDate.withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1L);

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        LambdaQueryWrapper<DutyShifts> wp = new LambdaQueryWrapper<DutyShifts>()
                .eq(DutyShifts::getDutyId, dutyId)
                .ge(DutyShifts::getDate, start)
                .lt(DutyShifts::getDate, end);
        return list(wp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void automatic(DutyShiftsAutomaticDTO dto) {
//        LocalDate dateLocal = LocalDate.now();
//        Date now =Date.from(dateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        if (dto.getDate().compareTo(now) < 0) {
//            throw new HssBootException("排班日期必须大于当前日期");
//        }
//        LocalDate day = dto.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate lastDay = day.with(TemporalAdjusters.lastDayOfMonth());
//        Date start = Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        Date end = Date.from(lastDay.plusDays(1L).atStartOfDay(ZoneId.systemDefault()).toInstant());

        LocalDate dateLocal = LocalDate.now();
        Date start = Date.from(dateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = DateUtils.addDays(start, dto.getDays());
        List<DutyShifts> list = baseMapper.listByDateAndDutyId(start, end, dto.getDutyId());
        if (!list.isEmpty()) {
            List<String> ids = list.stream().map(DutyShifts::getId).collect(Collectors.toList());
            removeByIds(ids);
        }
        List<DutyGroup> groups = getGroups(dto.getDays(), dto.getDutyId());

//        int dayCount = lastDay.getDayOfMonth() - day.getDayOfMonth() + 1;
        List<DutyShifts> dutyShifts = new ArrayList<>(dto.getDays());
        for (int i = 0; i < dto.getDays(); i++) {
            int index = i % groups.size();
            DutyGroup dutyGroup = groups.get(index);
            DutyShifts entity = new DutyShifts();
            entity.setDate(Date.from(dateLocal.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            entity.setShifts(dutyGroup.getPersonList());
            entity.setDutyGroupId(dutyGroup.getId());
            entity.setName(dutyGroup.getName());
            entity.setCode(dutyGroup.getCode());
            entity.setDutyPostion(dutyGroup.getDutyPostion());
            entity.setDutyId(dto.getDutyId());
            dutyShifts.add(entity);
        }
        saveBatch(dutyShifts);
    }

    @NotNull
    private List<DutyGroup> getGroups(Integer days, String dutyId) {
        LambdaQueryWrapper<DutyGroup> wrapper = new LambdaQueryWrapper<DutyGroup>().eq(DutyGroup::getDutyId, dutyId).orderByAsc(DutyGroup::getCode);
        List<DutyGroup> list = dutyGroupService.list(wrapper);
        if (list.isEmpty()) {
            throw new HssBootException("值班小组为空,请先设置值班小组");
        }
        LocalDate dateLocal = LocalDate.now();
        Date start = Date.from(dateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date = DateUtils.addDays(start, days);

        Instant upInstant = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .plusDays(-1L)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        String groupId = baseMapper.getGroupIdByDateAndDutyId(Date.from(upInstant), dutyId);
        if (groupId == null || list.size() == 1 || groupId.equals(list.get(0).getId())){
            return list;
        }
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            DutyGroup group = list.get(i);
            if (group.getId().equals(groupId)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return list;
        }
        List<DutyGroup> groups = new ArrayList<>(list.size());
        while (true) {
            DutyGroup group = list.get(index);
            groups.add(group);
            index ++;
            if (groups.size() == list.size()){
                break;
            }
            if (index == list.size()) {
                index = 0;
            }
        }
        return groups;
    }

    @Override
    public IPage<DutyHistoryVO> listHistory(DutyHistoryDTO dto) {
        Date today = new Date();
        if (dto.getEndDate() == null || dto.getEndDate().after(today)) {
            dto.setEndDate(today);
        }
        dto.setEndDate(Date.from(dto.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1L).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (dto.getStartDate() != null) {
            Date date = Date.from(dto.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            dto.setStartDate(date);
        }

        IPage<DutyHistoryVO> vo = baseMapper.listHistory(new Page<DutyHistoryVO>(dto.getPageNo(), dto.getPageSize()), dto);
        for (DutyHistoryVO record : vo.getRecords()) {
            if (StringUtils.isNotBlank(record.getShiftsJson())){
                record.setShifts(JSONObject.parseArray(record.getShiftsJson(), DutyPersonModel.class));
            }
        }

        return vo;
    }
}
