package com.hss.modules.scada.service.impl.advice;

import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.model.DeviceAttrData;
import com.hss.modules.scada.model.DeviceAttrModel;
import com.hss.modules.scada.service.DeviceDataService;
import com.hss.modules.scada.service.IGatewayAttachmentService;

/**
 * @ClassDescription:值增强
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/2/27 15:28
 */
public class ValueSceneAttrAdvice implements SceneAttrAdvice{

    private final DeviceDataService deviceDataService;
    private final IGatewayAttachmentService gatewayAttachmentService;

    public ValueSceneAttrAdvice(DeviceDataService deviceDataService, IGatewayAttachmentService gatewayAttachmentService) {
        this.deviceDataService = deviceDataService;
        this.gatewayAttachmentService = gatewayAttachmentService;
    }

    @Override
    public DeviceAttrModel advice(DeviceTypeAttribute t, ConDeviceAttribute a, DeviceAttrModel m) {
            if (ScadaConstant.IS_ONE.equals(t.getIsAct())) {
                return m;
            }
            DeviceAttrData data = deviceDataService.getAttrValueByAttrId(a.getId());
            if (data == null) {
                return m;
            }
            String value = gatewayAttachmentService.getValue(t.getCategory(), data.getValue(), a.getDeviceId());
            m.setValue(value);
            m.setRecordTime(data.getUpdateTime());
        return m;
    }
}
