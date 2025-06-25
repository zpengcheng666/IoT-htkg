package com.hss.modules.scada.service.impl.advice;

import com.alibaba.fastjson.JSONObject;
import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.model.DeviceTypeAttrConfigOptionItem;
import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.model.DeviceAttrModel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassDescription:关联摄像机
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/2/27 15:36
 */
public class ConfigSceneAttrAdvice implements SceneAttrAdvice{

    private static final String CAMERA = "linkedCamera";
    private final Map<String, List<DeviceTypeAttrConfigOptionItem>> actMap = new HashMap<>();
    @Override
    public DeviceAttrModel advice(DeviceTypeAttribute t, ConDeviceAttribute a, DeviceAttrModel m) {
        if (!ScadaConstant.IS_ONE.equals(t.getIsConfigurable())) {
            return m;
        }
        if (CAMERA.equals(t.getCategory())) {
            return null;
        }
        m.setIsConfigurable(ScadaConstant.IS_ONE);
        m.setInputType(SceneAttrAdvice.INPUT_TYPE_INPUT);
        if (m.getActList() != null) {
            return m;
        }
        if (StringUtils.isBlank(t.getConfigOptions())) {
            return m;
        }
        List<DeviceTypeAttrConfigOptionItem> list = actMap.get(t.getId());
        if (list == null) {
            list = JSONObject.parseArray(t.getConfigOptions(), DeviceTypeAttrConfigOptionItem.class);
            actMap.put(t.getId(), list);
        }
        m.setActList(list);
        if (!list.isEmpty()) {
            m.setInputType(SceneAttrAdvice.INPUT_TYPE_SELECT);
        }
        return m;
    }
}
