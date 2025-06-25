package com.hss.modules.scada.ws.terminal;

import com.hss.modules.message.service.IDoWorkService;
import com.hss.modules.system.entity.BaseTerminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/2 9:36
 */
@Service
public class TerminalInfoServiceDoWork implements TerminalInfoService{

    @Autowired
    private IDoWorkService doWorkService;

    @Override
    public boolean processHave(Set<String> types) {
        return types.contains(TerminalTypeEnum.DO_WORK.getType());
    }

    @Override
    public Collection<TerminalMsg> list(BaseTerminal terminal, Set<String> types) {
        TerminalMsg msg = new TerminalMsg(TerminalTypeEnum.DO_WORK.getTypeCode(), doWorkService.listShowWorkByTerminalId(terminal.getId()));
        return Collections.singleton(msg);
    }
}
