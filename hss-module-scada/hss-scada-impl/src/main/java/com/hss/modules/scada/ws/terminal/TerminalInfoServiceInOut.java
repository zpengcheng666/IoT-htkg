package com.hss.modules.scada.ws.terminal;

import com.hss.modules.inOutPosition.service.IInOutListService;
import com.hss.modules.system.entity.BaseTerminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/2 9:36
 */
@Service
public class TerminalInfoServiceInOut implements TerminalInfoService{

    @Autowired
    private IInOutListService inOutListService;


    @Override
    public boolean processHave(Set<String> types) {
        return types.contains(TerminalTypeEnum.IN_OUT.getType());
    }

    @Override
    public Collection<TerminalMsg> list(BaseTerminal terminal, Set<String> types) {
        TerminalMsg msg = new TerminalMsg(TerminalTypeEnum.IN_OUT.getTypeCode(), inOutListService.listByPublish(new Date()));
        return Collections.singleton(msg);
    }
}
