package com.hss.modules.scada.ws.terminal;

import com.hss.modules.message.service.IDutyShiftsService;
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
public class TerminalInfoServiceDuty implements TerminalInfoService{

    @Autowired
    private IDutyShiftsService dutyShiftsService;

    @Override
    public boolean processHave(Set<String> types) {
        return types.contains(TerminalTypeEnum.DUTY.getType());
    }

    @Override
    public Collection<TerminalMsg> list(BaseTerminal terminal, Set<String> types) {
        TerminalMsg msg = new TerminalMsg(TerminalTypeEnum.DUTY.getTypeCode(), dutyShiftsService.listByTerminalId(terminal.getId()));
        return Collections.singleton(msg);
    }
}
