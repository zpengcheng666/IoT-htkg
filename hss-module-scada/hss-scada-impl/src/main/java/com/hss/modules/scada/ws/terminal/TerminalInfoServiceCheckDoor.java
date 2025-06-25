package com.hss.modules.scada.ws.terminal;

import com.hss.modules.scada.model.DeviceAttrData;
import com.hss.modules.scada.service.DeviceDataService;
import com.hss.modules.system.entity.BaseTerminal;
import org.apache.commons.lang3.StringUtils;
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
public class TerminalInfoServiceCheckDoor implements TerminalInfoService{

    @Autowired
    private DeviceDataService deviceDataService;

    @Override
    public boolean processHave(Set<String> types) {
        return types.contains(TerminalTypeEnum.CHECK_DOOR.getType());
    }

    @Override
    public Collection<TerminalMsg> list(BaseTerminal terminal, Set<String> types) {
        return Collections.singleton( new TerminalMsg(TerminalTypeEnum.CHECK_DOOR.getTypeCode(), get(terminal.getCheckDoorId())));
    }
    private CheckDoorInfo get(String doorId) {
        CheckDoorInfo info = new CheckDoorInfo();
        if (StringUtils.isBlank(doorId)) {
            return info;
        }
        List<DeviceAttrData> checkDeviceDataList = deviceDataService.listByDeviceId(doorId);
        for (DeviceAttrData data : checkDeviceDataList) {
            if ("jinrenshu".equals(data.getEnName())) {
                info.in = StringUtils.isEmpty(data.getValue()) ? 0 : Integer.parseInt(data.getValue());
                continue;
            }
            if ("churnshu".equals(data.getEnName())) {
                info.out = StringUtils.isEmpty(data.getValue()) ? 0 : Integer.parseInt(data.getValue());
            }
        }
        return info;

    }

    public static class CheckDoorInfo {
        public int in;
        public int out;
    }

}

