package com.hss.modules.scada.ws.terminal;

import com.hss.modules.alarm.entity.AlarmData;
import com.hss.modules.alarm.service.IAlarmDataService;
import com.hss.modules.system.entity.BaseTerminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class TerminalInfoServiceAlarm implements TerminalInfoService{

    @Autowired
    private IAlarmDataService alarmDataService;

    @Override
    public boolean processHave(Set<String> types) {
        return types.contains(TerminalTypeEnum.ALARM.getType());
    }

    @Override
    public Collection<TerminalMsg> list(BaseTerminal terminal, Set<String> types) {
        List<AlarmData> list = alarmDataService.getAlarmData(terminal.getAlarmLevel());
        return Collections.singleton(new TerminalMsg(TerminalTypeEnum.ALARM.getTypeCode(), list));
    }
}
