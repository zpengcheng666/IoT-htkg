package com.hss.modules.scada.service.impl;

import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataAttrValueCachedEvent;
import com.hss.modules.scada.model.DeviceAttrData;
import com.hss.modules.scada.service.DeviceDataService;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @description: 获取属性数据、更新属性信息、根据设备id获取列表、根据设备id和属性enName查询值、获取设备id以及英文名称
* @author zpc
* @date 2024/3/19 15:17
* @version 1.0
*/
@Slf4j
@Service
public class DeviceDateServiceImpl implements DeviceDataService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IConDeviceAttributeService deviceAttributeService;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public DeviceAttrData getAttrValueByAttrId(String attrId) {
        DeviceAttrData data = (DeviceAttrData) redisUtil.get(getKey(attrId));
        if (data == null){
            ConDeviceAttribute byId = deviceAttributeService.getById(attrId);
            data = new DeviceAttrData();
            data.setDeviceId(byId.getDeviceId());
            data.setName(byId.getName());
            data.setEnName(byId.getEnName());
            data.setValue(byId.getInitValue());
            redisUtil.set(getKey(attrId), data);
        }
        return data;
    }

    @Override
    public void updateAttrValueByAttr(ConDeviceAttribute attr) {
        DeviceAttrData deviceAttrData = new DeviceAttrData();
        deviceAttrData.setDeviceId(attr.getDeviceId());
        deviceAttrData.setName(attr.getName());
        deviceAttrData.setEnName(attr.getEnName());
        deviceAttrData.setValue(attr.getInitValue());
        deviceAttrData.setVariableId(attr.getVariableId());
        deviceAttrData.setUpdateTime(attr.getUpdatedTime());
        redisUtil.set(getKey(attr.getId()), deviceAttrData);
        // 1.变量关联，2.信息发布终端，3.报警策略
        publisher.publishEvent(new ScadaDataAttrValueCachedEvent(attr));
    }

    @Override
    public List<DeviceAttrData> listByDeviceId(String deviceId) {
        List<ConDeviceAttribute> attributeList = deviceAttributeService.listByDeviceId(deviceId);
        return attributeList
                .stream()
                .map(a -> getAttrValueByAttrId(a.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public String getValueByDeviceIdAndAttrEnName(String deviceId, String attrEnName) {
        DeviceAttrData data = getByDeviceIdAndEnName(deviceId, attrEnName);
        if (data == null){
            return null;
        }
        return data.getValue();
    }

    @Override
    public DeviceAttrData getByDeviceIdAndEnName(String deviceId, String attrEnName) {
        ConDeviceAttribute attribute = deviceAttributeService.getByDeviceIdAndAttrEnName(deviceId, attrEnName);
        if (attribute == null) {
            return null;
        }
        return getAttrValueByAttrId(attribute.getId());
    }

    /**
     * 获取key
     * @param attrId
     * @return
     */
    private String getKey(String attrId){
        return ScadaConstant.REDIS_KEY_ATTR_DATA + attrId;
    }


}
