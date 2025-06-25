package com.hss.modules.scada.ws.terminal;

import com.hss.modules.message.entity.PublishSatelliteSecure;
import com.hss.modules.message.service.IPublishSatelliteSecureService;
import com.hss.modules.system.entity.BaseTerminal;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/2 9:36
 */
//@Service
public class TerminalInfoServiceSatelliteSecure implements TerminalInfoService{
    @Autowired
    private IPublishSatelliteSecureService publishSatelliteSecureService;

    @Override
    public boolean processHave(Set<String> types) {
        return types.contains(TerminalTypeEnum.SATELLITE_SECURE.getType());
    }


    @Override
    public Collection<TerminalMsg> list(BaseTerminal terminal, Set<String> types) {
        String terminalId = terminal.getId();
        List<PublishSatelliteSecure> list = publishSatelliteSecureService.listPublishByTerminalId(terminalId);
        TerminalMsg terminalMsg = new TerminalMsg(TerminalTypeEnum.SATELLITE_SECURE.getTypeCode(), list);
        return Collections.singleton(terminalMsg);
    }
}
