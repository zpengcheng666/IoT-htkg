package com.hss.modules.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.message.entity.PublishNoticeTerminal;
import com.hss.modules.message.mapper.PublishNoticeTerminalMapper;
import com.hss.modules.message.service.IPublishNoticeTerminalService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 通知消息和终端关系表
 * @Author: zpc
 * @Date:   2022-12-23
 * @Version: V1.0
 */
@Service
public class PublishNoticeTerminalServiceImpl extends ServiceImpl<PublishNoticeTerminalMapper, PublishNoticeTerminal> implements IPublishNoticeTerminalService {

    @Override
    public void delByMessageId(String messageId) {
        baseMapper.delByMessageId(messageId);
    }

    @Override
    public void deleteBatchByMessageIds(List<String> ids) {
        baseMapper.deleteBatchByMessageIds(ids);
    }

    @Override
    public List<String> listTerminalIdByMessageId(List<String> messageIds) {
        return  baseMapper.listTerminalIdByMessageId(messageIds);
    }
}
