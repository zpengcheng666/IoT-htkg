package com.hss.modules.message.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.PublishDuty;
import com.hss.modules.message.entity.PublishDutyTerminal;
import com.hss.modules.message.entity.PublishDutyType;
import com.hss.modules.message.mapper.PublishDutyMapper;
import com.hss.modules.message.service.IPublishDutyService;
import com.hss.modules.message.service.IPublishDutyTerminalService;
import com.hss.modules.message.service.IPublishDutyTypeService;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 值班安排
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Service
public class PublishDutyServiceImpl extends ServiceImpl<PublishDutyMapper, PublishDuty> implements IPublishDutyService {
    @Autowired
    private IPublishDutyTerminalService publishDutyTerminalService;
    @Autowired
    private IPublishDutyTypeService publishDutyTypeService;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(PublishMessageDTO dto) {
        if (CollectionUtils.isEmpty(dto.getTerminalIds())){
            throw new HssBootException("发布终端不能为空");
        }
        PublishDuty byId = getById(dto.getMessageId());
        if (byId == null) {
            throw new HssBootException("值班信息不存在");
        }
        if (byId.getState() == 2) {
            return;
        }
        PublishDuty update = new PublishDuty();
        update.setId(byId.getId());
        update.setState(2);
        update.setPublishTime(new Date());
        updateById(update);
        List<PublishDutyTerminal> publishDutyTerminals = dto.getTerminalIds().stream().map(id -> {
            PublishDutyTerminal publishDutyTerminal = new PublishDutyTerminal();
            publishDutyTerminal.setDutyId(dto.getMessageId());
            publishDutyTerminal.setTerminalId(id);
            return publishDutyTerminal;
        }).collect(Collectors.toList());
        publishDutyTerminalService.saveBatch(publishDutyTerminals);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        List<String> terminalIds =  publishDutyTerminalService.listTerminalIdByMessageId(Collections.singletonList(id));
        removeById(id);
        if (!terminalIds.isEmpty()) {
            publishDutyTerminalService.delByMessageId(id);
        }

    }
    @Override
    public List<PublishDuty> listByTerminal(String terminalId) {
        List<PublishDuty> publishDuties = baseMapper.listByTerminal(terminalId);
        for (PublishDuty publishDuty : publishDuties) {
            publishDuty.setTypes( publishDutyTypeService.listByDutyId(publishDuty.getId()));
        }
        return publishDuties;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revocation(String id) {
        List<String> terminalIds =  publishDutyTerminalService.listTerminalIdByMessageId(Collections.singletonList(id));
        PublishDuty publishDuty = new PublishDuty();
        publishDuty.setId(id);
        publishDuty.setState(0);
        updateById(publishDuty);
        if (!terminalIds.isEmpty()) {
            publishDutyTerminalService.delByMessageId(id);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PublishDuty publishDuty) {
        publishDuty.setState(0);
        save(publishDuty);
    }

    @Override
    public PublishDuty queryById(String id) {
        return getById(id);
    }

    @Override
    public IPage<PublishDuty> queryPage(Page<PublishDuty> page, List<String> terminalIds) {
        IPage<PublishDuty> result = null;
        if (CollectionUtil.isEmpty(terminalIds)){
            result = page(page);
        }else {
            result = baseMapper.queryPage(page, terminalIds);
        }
        for (PublishDuty record : result.getRecords()) {
            List<String> terminalList = publishDutyTerminalService.listTerminalIdByMessageId(Collections.singletonList(record.getId()));
            record.setTerminalIds(terminalList);
            List<PublishDutyType> types = publishDutyTypeService.listByDutyId(record.getId());
            record.setTypes(types);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(PublishDuty publishDuty) {
        PublishDuty byId = getById(publishDuty.getId());
        if (byId == null) {
            return;
        }
        updateById(publishDuty);
        if (byId.getState() != 0){
            revocation(publishDuty.getId());
        }

    }
}
