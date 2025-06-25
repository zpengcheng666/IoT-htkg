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
import java.util.stream.Collectors;

/**
 * @ClassDescription: 设置增强
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/2/27 15:05
 */
public class ActSceneAttrAdvice implements SceneAttrAdvice{

    private Map<String, List<DeviceTypeAttrConfigOptionItem>> actMap = new HashMap<>();


    @Override
    public DeviceAttrModel advice(DeviceTypeAttribute t, ConDeviceAttribute a, DeviceAttrModel m) {
        if (!ScadaConstant.IS_ONE.equals(t.getIsAct())) {
            return m;
        }
        m.setIsAct(t.getIsAct());
        m.setInputType("input");
        if (m.getActList() != null) {
            return m;
        }
        if (StringUtils.isBlank(t.getActOrders())) {
            return m;
        }

        List<DeviceTypeAttrConfigOptionItem> list = actMap.get(t.getId());
        if (list == null) {
            list = JSONObject.parseArray(t.getActOrders()).stream().map(o -> {
                JSONObject jo = (JSONObject) o;
                DeviceTypeAttrConfigOptionItem item = new DeviceTypeAttrConfigOptionItem();
                item.setName(jo.getString("name"));
                item.setValue(jo.getString("order"));
                return item;
            }).collect(Collectors.toList());
            actMap.put(t.getId(), list);
        }
        m.setActList(list);
        m.setInputType("select");
        return m;
    }
}
