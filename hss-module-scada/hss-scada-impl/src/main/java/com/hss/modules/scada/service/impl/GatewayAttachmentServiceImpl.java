package com.hss.modules.scada.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.HashMultimap;
import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.entity.ConDianWei;
import com.hss.modules.scada.model.*;
import com.hss.modules.scada.service.*;
import com.hss.modules.scada.ws.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* @description: 网关附加操作实现类、网关掉线、网关上线、获取值、发布事件、更新数据
* @author zpc
* @date 2024/3/19 15:23
* @version 1.0
*/
@Slf4j
@Service
public class GatewayAttachmentServiceImpl implements IGatewayAttachmentService, ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private IGSChangJingSheBeiService sceneDeviceService;
    @Autowired
    private IConSheBeiService conSheBeiService;
    @Autowired
    private WebSocket webSocket;
    @Autowired
    private DeviceDataService deviceDataService;
    @Autowired
    private IConDianWeiService conDianWeiService;

    /**
     * 场景设备关联关系
     */
    private static final HashMultimap<String, String> GATEWAY_DEVICES_MAP = HashMultimap.create();

    /**
     * 网关下线的设备集合
     */
    private static final Set<String> DOWN_DEVICES = new HashSet<>();
    /**
     * 离线的网关
     */
    private static final Set<String> DOWN_GATEWAY = new HashSet<>();

    @Override
    public synchronized void down(String gatewayId) {
        DOWN_GATEWAY.add(gatewayId);
        Set<String> deviceIds = GATEWAY_DEVICES_MAP.get(gatewayId);
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            for (String deviceId : deviceIds) {
                DOWN_DEVICES.add(deviceId);
                DeviceModel deviceModel = new DeviceModel();
                deviceModel.setDeviceId(deviceId);
                DeviceAttrModel deviceAttrModel = new DeviceAttrModel();
                deviceAttrModel.setEnName(ScadaConstant.ONLINE_STATE);
                deviceAttrModel.setValue("0");
                deviceModel.setAttrs(Collections.singletonList(deviceAttrModel));
                List<String> sceneIds = sceneDeviceService.listSceneIdByDeviceId(deviceId);
                for (String sceneId : sceneIds) {
                    webSocket.sendMessage(ScadaWsMessageType.BIZ, deviceModel, sceneId);
                }
            }
        }
    }

    @Override
    public synchronized void up(String gatewayId) {
        boolean remove = DOWN_GATEWAY.remove(gatewayId);
        if (!remove) {
            return;
        }
        Set<String> deviceIds = GATEWAY_DEVICES_MAP.get(gatewayId);
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            for (String deviceId : deviceIds) {
                DOWN_DEVICES.remove(deviceId);
                DeviceModel deviceModel = new DeviceModel();
                deviceModel.setDeviceId(deviceId);
                DeviceAttrModel deviceAttrModel = new DeviceAttrModel();
                deviceAttrModel.setEnName(ScadaConstant.ONLINE_STATE);
                deviceAttrModel.setValue(deviceDataService.getValueByDeviceIdAndAttrEnName(deviceId, ScadaConstant.ONLINE_STATE));
                deviceModel.setAttrs(Collections.singletonList(deviceAttrModel));
                List<String> sceneIds = sceneDeviceService.listSceneIdByDeviceId(deviceId);
                for (String sceneId : sceneIds) {
                    webSocket.sendMessage(ScadaWsMessageType.BIZ, deviceModel, sceneId);
                }
            }
        }
    }

    @Override
    public String getValue(String attrEnName, String value, String device) {
        if (!ScadaConstant.ONLINE_STATE.equals(attrEnName)) {
            return value;
        }
        synchronized (this) {
            if (!DOWN_DEVICES.contains(device)) {
                return value;
            }
        }
        return "0";
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        List<GatewayDevice> list = conSheBeiService.listGatewayAndDeviceByEnName(ScadaConstant.ONLINE_STATE);
        for (GatewayDevice gatewayDevice : list) {
            GATEWAY_DEVICES_MAP.put(gatewayDevice.getGatewayId(), gatewayDevice.getDeviceId());
//            DOWN_GATEWAY.add(gatewayDevice.getGatewayId());
//            DOWN_DEVICES.add(gatewayDevice.getDeviceId());
        }
    }

    @Override
    public void deviceUpdate(GatewayDeviceUpdate source) {
        String oldWgid = null;
        String newWgid = null;
        if (StringUtils.isNotBlank(source.getOldPointId())) {
            ConDianWei byId = conDianWeiService.getById(source.getOldPointId());
            if (byId != null) {
                oldWgid = byId.getWgid();
            }
        }
        if (StringUtils.isNotBlank(source.getNewPointId())) {
            ConDianWei byId = conDianWeiService.getById(source.getNewPointId());
            if (byId != null) {
                newWgid = byId.getWgid();
            }
        }
        if (ObjectUtil.equal(oldWgid, newWgid)) {
            return;
        }
        String deviceId = source.getDeviceId();
        if (oldWgid != null) {
            GATEWAY_DEVICES_MAP.remove(oldWgid, deviceId);
        }
        if (newWgid != null) {
            GATEWAY_DEVICES_MAP.put(newWgid, deviceId);
            if (DOWN_GATEWAY.contains(newWgid)) {
                if (!DOWN_DEVICES.contains(deviceId)) {
                    DOWN_DEVICES.add(deviceId);
                    DeviceModel deviceModel = new DeviceModel();
                    deviceModel.setDeviceId(deviceId);
                    DeviceAttrModel deviceAttrModel = new DeviceAttrModel();
                    deviceAttrModel.setEnName(ScadaConstant.ONLINE_STATE);
                    deviceAttrModel.setValue("0");
                    deviceModel.setAttrs(Collections.singletonList(deviceAttrModel));
                    List<String> sceneIds = sceneDeviceService.listSceneIdByDeviceId(deviceId);
                    for (String sceneId : sceneIds) {
                        webSocket.sendMessage(ScadaWsMessageType.BIZ, deviceModel, sceneId);
                    }
                }
            }
        }
    }
}
