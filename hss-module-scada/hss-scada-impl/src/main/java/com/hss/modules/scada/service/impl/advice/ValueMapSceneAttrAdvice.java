package com.hss.modules.scada.service.impl.advice;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.model.DeviceTypeAttrConfigOptionItem;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.model.DeviceAttrModel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassDescription:valueMap场景属性增强
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/2/27 15:10
 */
public class ValueMapSceneAttrAdvice implements SceneAttrAdvice {

    Map<String, Map<String, String>> map = new HashMap<>();

    @Override
    public DeviceAttrModel advice(DeviceTypeAttribute t, ConDeviceAttribute a, DeviceAttrModel m) {
        if (StringUtils.isEmpty(t.getValueMap())) {
            return m;
        }
        Map<String, String> map = this.map.get(t.getId());
        if (map == null) {
            String valueMapStr = t.getValueMap();
            map = JSONObject.parseObject(valueMapStr, new TypeReference<HashMap<String, String>>(){})
                    .entrySet()
                    .stream()
                    .map(e -> {
                        DeviceTypeAttrConfigOptionItem item = new DeviceTypeAttrConfigOptionItem();
                        item.setName(e.getKey());
                        item.setValue(e.getValue());
                        return item;
                    }).collect(Collectors.toMap(
                            DeviceTypeAttrConfigOptionItem::getName,
                            DeviceTypeAttrConfigOptionItem::getValue, (o1, o2) -> o1));
            this.map.put(t.getId(), map);
        }
        m.setValueMap(map);
        return m;
    }
}
