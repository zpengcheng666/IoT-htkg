package com.hss.modules.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.message.entity.PublishSatelliteSecureTerminal;
import com.hss.modules.message.mapper.PublishSatelliteSecureTerminalMapper;
import com.hss.modules.message.service.IPublishSatelliteSecureTerminalService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 26060
 */
@Service
public class PublishSatelliteSecureTerminalServiceImpl extends ServiceImpl<PublishSatelliteSecureTerminalMapper, PublishSatelliteSecureTerminal> implements IPublishSatelliteSecureTerminalService {


    @Override
    public void add(String msgId, List<String> terminalIds) {
        List<PublishSatelliteSecureTerminal> list = terminalIds.stream().map(terminalId -> {
            PublishSatelliteSecureTerminal r = new PublishSatelliteSecureTerminal();
            r.setMsgId(msgId);
            r.setTerminalId(terminalId);
            return r;
        }).collect(Collectors.toList());
        saveBatch(list);
    }

    @Override
    public void removeByMsgId(String msgId) {
        baseMapper.removeByMsgId(msgId);
    }

    @Override
    public List<String> listTerminalIdByMessageId(List<String> terminalIds) {
        return baseMapper.listTerminalIdByMessageId(terminalIds);
    }
}
