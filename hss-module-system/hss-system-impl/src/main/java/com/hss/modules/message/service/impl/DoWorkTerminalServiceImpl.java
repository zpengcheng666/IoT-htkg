package com.hss.modules.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.message.entity.DoWorkTerminal;
import com.hss.modules.message.mapper.DoWorkTerminalMapper;
import com.hss.modules.message.service.IDoWorkTerminalService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 手动值班终端中间表
 * @Author: zpc
 * @Date:   2023-12-06
 * @Version: V1.0
 */
@Service
public class DoWorkTerminalServiceImpl extends ServiceImpl<DoWorkTerminalMapper, DoWorkTerminal> implements IDoWorkTerminalService {

    @Override
    public List<String> listTerminalIdsByDoWorkId(String doWorkId) {
        return baseMapper.listTerminalIdsByDoWorkId(doWorkId);
    }

    @Override
    public void delByMessageId(String messageId) {
        baseMapper.delByMessageId(messageId);
    }
}
