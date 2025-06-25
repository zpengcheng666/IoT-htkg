package com.hss.modules.scada.service.impl.advice;


import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.model.DeviceTypeAttrConfigOptionItem;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.model.DeviceAttrModel;
import com.hss.modules.scada.service.IConSheBeiService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassDescription:语音控制
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/2/27 14:54
 */
public class PublishSceneAttrAdvice implements SceneAttrAdvice {

    private static final String CALL = "callCtrl";
    private List<ConSheBei> deviceList;

    public PublishSceneAttrAdvice(String sceneId, IConSheBeiService deviceService) {
       deviceList = deviceService.listPublishBySceneId(sceneId);
    }

    @Override
    public DeviceAttrModel advice(DeviceTypeAttribute t, ConDeviceAttribute a, DeviceAttrModel m) {
        if (!CALL.equals(t.getCategory())) {
            return m;
        }
        String deviceId = a.getDeviceId();
        List<DeviceTypeAttrConfigOptionItem> list = deviceList.stream()
                .filter(da -> !da.getId().equals(deviceId))
                .map(da -> {
                    DeviceTypeAttrConfigOptionItem item = new DeviceTypeAttrConfigOptionItem();
                    item.setName(da.getName());
                    item.setValue(da.getId());
                    return item;
                }).collect(Collectors.toList());
        m.setInputType(INPUT_TYPE_CHECKBOX);
        m.setActList(list);
        return m;
    }
}
