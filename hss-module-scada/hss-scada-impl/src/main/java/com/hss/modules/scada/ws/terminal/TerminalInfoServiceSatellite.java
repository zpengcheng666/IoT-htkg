package com.hss.modules.scada.ws.terminal;

import com.hss.modules.message.constant.MessageStateEnum;
import com.hss.modules.message.entity.PublishSatellite;
import com.hss.modules.message.service.IPublishSatelliteService;
import com.hss.modules.message.util.TimeUtil;
import com.hss.modules.system.entity.BaseTerminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/2 9:36
 */
@Service
public class TerminalInfoServiceSatellite implements TerminalInfoService{

    @Autowired
    private IPublishSatelliteService publishSatelliteService;



    @Override
    public boolean processHave(Set<String> types) {
        if (types.contains(TerminalTypeEnum.SATELLITE.getType())) {
            return true;
        }
        if (types.contains(TerminalTypeEnum.SATELLITE_ALL.getType())) {
            return true;
        }
        if (types.contains(TerminalTypeEnum.SATELLITE_SECURE.getType())) {
            return true;
        }
        return types.contains(TerminalTypeEnum.SATELLITE_NOT.getType());
    }


    @Override
    public Collection<TerminalMsg> list(BaseTerminal terminal, Set<String> types) {
        String terminalId = terminal.getId();
        List<PublishSatellite> list = publishSatelliteService.listPublishByTerminalId(terminalId);
        List<TerminalMsg> terminalMsgs = new ArrayList<>(3);
        if (types.contains(TerminalTypeEnum.SATELLITE.getType())) {
            List<PublishSatellite> list1 = list.stream().filter(s -> MessageStateEnum.ENTER.value.equals(s.getState())).collect(Collectors.toList());
            TerminalMsg terminalMsg = new TerminalMsg(TerminalTypeEnum.SATELLITE.getTypeCode(), list1);
            terminalMsgs.add(terminalMsg);
        }
        if (types.contains(TerminalTypeEnum.SATELLITE_NOT.getType())) {
            List<PublishSatellite> list1 = list.stream().filter(s -> MessageStateEnum.PUBLISH.value.equals(s.getState())).collect(Collectors.toList());
            TerminalMsg terminalMsg = new TerminalMsg(TerminalTypeEnum.SATELLITE_NOT.getTypeCode(), list1);
            terminalMsgs.add(terminalMsg);
        }
        if (types.contains(TerminalTypeEnum.SATELLITE_ALL.getType())) {
            TerminalMsg terminalMsg = new TerminalMsg(TerminalTypeEnum.SATELLITE_ALL.getTypeCode(), list);
            terminalMsgs.add(terminalMsg);
        }
        if (types.contains(TerminalTypeEnum.SATELLITE_SECURE.getType())) {
            LinkedList<SecureMessage> suList = new LinkedList<>();
            LocalDateTime suStart = LocalDateTime.now();
            List<PublishSatellite> sortedList = list.stream().sorted(Comparator.comparing(PublishSatellite::getEnterTime)).collect(Collectors.toList());
            for (PublishSatellite publishSatellite : sortedList) {
                if (suStart.isBefore(publishSatellite.getEnterTime())) {
                    SecureMessage su = new SecureMessage();
                    su.setStartTime(suStart);
                    su.setEndTime(publishSatellite.getEnterTime());
                    suList.add(su);
                }
                if (publishSatellite.getLeaveTime().isAfter(suStart)) {
                    suStart = publishSatellite.getLeaveTime();
                }
            }
            LocalDateTime suEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            if (suEnd.isAfter(suStart)) {
                SecureMessage su = new SecureMessage();
                su.setStartTime(suStart);
                su.setEndTime(suEnd);
                suList.add(su);
            }
            for (SecureMessage secureMessage : suList) {
                secureMessage.setSecure(TimeUtil.getDurationStr(secureMessage.getStartTime(), secureMessage.getEndTime()));
            }

            TerminalMsg terminalMsg = new TerminalMsg(TerminalTypeEnum.SATELLITE_SECURE.getTypeCode(), suList);
            terminalMsgs.add(terminalMsg);
        }



        return terminalMsgs;
    }
}
