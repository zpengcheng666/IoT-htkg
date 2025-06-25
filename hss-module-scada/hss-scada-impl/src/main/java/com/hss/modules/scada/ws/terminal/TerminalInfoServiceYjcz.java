package com.hss.modules.scada.ws.terminal;

import com.hss.modules.preplan.service.IContingencyRecordService;
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
public class TerminalInfoServiceYjcz implements TerminalInfoService{

    @Autowired
    private IContingencyRecordService contingencyRecordService;

    @Override
    public boolean processHave(Set<String> types) {
        return types.contains(TerminalTypeEnum.YJCZ.getType());
    }

    @Override
    public Collection<TerminalMsg> list(BaseTerminal terminal, Set<String> types) {
        TerminalMsg msg = new TerminalMsg(TerminalTypeEnum.YJCZ.getTypeCode(), contingencyRecordService.getByTerminalId(terminal.getId()));
        return Collections.singleton(msg);
    }
}
