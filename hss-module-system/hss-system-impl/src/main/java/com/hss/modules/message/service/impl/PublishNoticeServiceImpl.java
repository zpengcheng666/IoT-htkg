package com.hss.modules.message.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.message.dto.PublishNoticeMessageDTO;
import com.hss.modules.message.entity.PublishNotice;
import com.hss.modules.message.entity.PublishNoticeTerminal;
import com.hss.modules.message.mapper.PublishNoticeMapper;
import com.hss.modules.message.service.IPublishNoticeService;
import com.hss.modules.message.service.IPublishNoticeTerminalService;
import com.hss.modules.message.task.NoticeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 通知公告
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Service
public class PublishNoticeServiceImpl extends ServiceImpl<PublishNoticeMapper, PublishNotice> implements IPublishNoticeService {

    @Autowired
    private IPublishNoticeTerminalService publishNoticeTerminalService;
    @Autowired
    private NoticeTask noticeTask;
    @Autowired
    private ApplicationContext applicationContext;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(PublishNoticeMessageDTO dto) {
        PublishNotice byId = getById(dto.getMessageId());
        if (byId == null){
            return;
        }
        PublishNotice publishNotice = new PublishNotice();
        int state = 1;
        if ("immediate".equals(dto.getPublishType())){
            state = 2;
        }
        publishNotice.setId(dto.getMessageId());
        publishNotice.setState(state);
        publishNotice.setPublishTime(new Date());
        publishNotice.setPublisher(dto.getPublisher());
        publishNotice.setEffectiveTime(state == 2 ? new Date() : dto.getEffectiveTime());
        publishNotice.setOverdueTime(dto.getOverdueTime());
        publishNotice.setPublishType(dto.getPublishType());
        updateById(publishNotice);
        List<PublishNoticeTerminal> publishDutyTerminals = dto.getTerminalIds().stream().map(id -> {
            PublishNoticeTerminal publishNoticeTerminal = new PublishNoticeTerminal();
            publishNoticeTerminal.setNoticeId(dto.getMessageId());
            publishNoticeTerminal.setTerminalId(id);
            return publishNoticeTerminal;
        }).collect(Collectors.toList());
        publishNoticeTerminalService.saveBatch(publishDutyTerminals);
        noticeTask.addTask(getById(dto.getMessageId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PublishNotice publishNotice) {
        publishNotice.setState(0);
        save(publishNotice);
    }

    @Override
    public void delete(String id) {
        PublishNotice byId = getById(id);
        if (id == null){
            return;
        }
        List<String> terminalIds =  publishNoticeTerminalService.listTerminalIdByMessageId(Collections.singletonList(id));
        removeById(id);
        publishNoticeTerminalService.delByMessageId(id);
        noticeTask.removeTask(byId);
    }



    @Override
    public List<PublishNotice> listByTerminal(String terminalId) {
        return baseMapper.listByTerminal(new Date(),terminalId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revocation(String id) {
        PublishNotice byId = getById(id);
        if (byId == null){
            return;
        }
        List<String> terminalIds =  publishNoticeTerminalService.listTerminalIdByMessageId(Collections.singletonList(id));
        PublishNotice publishNotice = new PublishNotice();
        publishNotice.setId(id);
        publishNotice.setState(0);
        updateById(publishNotice);
        publishNoticeTerminalService.delByMessageId(id);



    }


    @Override
    public IPage<PublishNotice> queryPage(Page<PublishNotice> page1, List<String> terminalIds) {
        IPage<PublishNotice> result = null;
        if (CollectionUtil.isEmpty(terminalIds)) {
            result = page(page1);
        }else {
            result =  baseMapper.queryPage(page1,terminalIds);
        }
        for (PublishNotice record : result.getRecords()) {
            record.setTerminalIds(publishNoticeTerminalService.listTerminalIdByMessageId(Collections.singletonList(record.getId())));
        }

        return result;
    }

    @Override
    public void edit(PublishNotice publishNotice) {
        updateById(publishNotice);
        PublishNotice byId = getById(publishNotice.getId());
        if (byId.getState() != 0){
            revocation(publishNotice.getId());
        }
        noticeTask.updateTask(getById(publishNotice.getId()));
    }

    @Override
    public List<PublishNotice> listNotOverList() {
        return baseMapper.listNotOverList();
    }

    @Override
    public void overMessage(PublishNotice message) {
        PublishNotice byId = getById(message.getId());
        if (byId == null || byId.getState() == 3){
            return;
        }

        PublishNotice entity = new PublishNotice();
        entity.setId(byId.getId());
        entity.setState(3);
        updateById(entity);

    }

    @Override
    public void inMessage(PublishNotice message) {
        PublishNotice byId = getById(message.getId());
        if (byId == null || byId.getState() == 2){
            return;
        }

        PublishNotice entity = new PublishNotice();
        entity.setId(byId.getId());
        entity.setState(2);
        updateById(entity);

    }
}
