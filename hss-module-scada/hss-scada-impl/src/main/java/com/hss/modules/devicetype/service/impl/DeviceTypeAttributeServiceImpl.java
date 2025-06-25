package com.hss.modules.devicetype.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.event.DeviceTypeAttrAddEvent;
import com.hss.modules.devicetype.event.DeviceTypeAttrDeleteEvent;
import com.hss.modules.devicetype.event.DeviceTypeAttrEditEvent;
import com.hss.modules.devicetype.mapper.DeviceTypeAttributeMapper;
import com.hss.modules.devicetype.model.DeviceTypeAttrConfigOptionItem;
import com.hss.modules.devicetype.model.DeviceTypeAttributeSceneListByType;
import com.hss.modules.devicetype.service.IDeviceTypeAttributeService;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.service.DeviceDataService;
import com.hss.modules.scada.service.IConSheBeiService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 设备类型管理属性管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Service
public class DeviceTypeAttributeServiceImpl extends ServiceImpl<DeviceTypeAttributeMapper, DeviceTypeAttribute> implements IDeviceTypeAttributeService {



    private static final Integer IS_TRUE = 1;
    private static final Integer IS_FALSE = 0;

    @Autowired
    private IConSheBeiService deviceService;
    @Autowired
    private DeviceDataService deviceDataService;

    @Autowired
    private ApplicationEventPublisher publisher;



    @Override
    @Cacheable("typeAttr")
    public DeviceTypeAttribute getById(Serializable id) {
        return super.getById(id);
    }


    @Override
    @CacheEvict(value = "typeAttr", key = "#root.args[0].id")
    public boolean updateById(DeviceTypeAttribute entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict("typeAttr")
    public boolean removeById(Serializable id) {
        DeviceTypeAttribute byId = getById(id);
        if (byId == null) {
            return true;
        }
        boolean b = super.removeById(id);
        publisher.publishEvent(new DeviceTypeAttrDeleteEvent(byId));
        LogUtil.setOperate(byId.getName());

        return b;
    }

    @Override
    public List<DeviceTypeAttribute> listByDeviceType(String type) {
        return baseMapper.listByDeviceType(type);
    }

    @Override
    public void deleteByTypeId(String typeId) {
        baseMapper.deleteByTypeId(typeId);
    }

    @Override
    public DeviceTypeAttribute getByEnNameAndTypeId(String typeId, String enName) {
        return baseMapper.getByEnNameAndTypeId(typeId, enName);
    }

    @Override
    public List<DeviceTypeAttribute> queryDevClassIdByAttrFilter(@Param("devClassId") String devClassId) {
        return baseMapper.queryFilterAttr(devClassId);
    }

    @Override
    public DeviceTypeAttributeSceneListByType sceneListByType(String type, String deviceId) {
        ConSheBei device = deviceService.getById(deviceId);
        if (device == null) {
            return null;
        }
        List<DeviceTypeAttribute> attrList = baseMapper.listByDeviceTypeId(device.getDeviceTypeId());
        List<DeviceTypeAttribute> pointList = new ArrayList<>();
        List<DeviceTypeAttribute> configList = new ArrayList<>();
        for (DeviceTypeAttribute attr : attrList) {
            if (IS_TRUE.equals(attr.getIsConfigurable())){
                if (StringUtils.isNotBlank(attr.getConfigOptions())){
                    if ("linkedCamera".equals(attr.getCategory())){
                        List<ConSheBei> cameraList = deviceService.ListCamera();
                        attr.setConfigOptionList(cameraList.stream().map(d -> {
                            DeviceTypeAttrConfigOptionItem item = new DeviceTypeAttrConfigOptionItem();
                            item.setName(d.getName());
                            item.setValue(d.getId());
                            return item;
                        }).collect(Collectors.toList()));
                    }else {
                        attr.setConfigOptionList(JSONObject.parseArray(attr.getConfigOptions(), DeviceTypeAttrConfigOptionItem.class));
                    }

                }
                String value = deviceDataService.getValueByDeviceIdAndAttrEnName(deviceId, attr.getCategory());
                attr.setInitValue(value);
                configList.add(attr);
            }
        }
        DeviceTypeAttributeSceneListByType result = new DeviceTypeAttributeSceneListByType();
        result.setPointList(pointList);
        result.setConfigList(configList);
        return result;
    }

    @Override
    public List<DeviceTypeAttribute> listRelationByDeviceType(String deviceType) {
        return baseMapper.listRelationByDeviceType(deviceType);
    }

    @Override
    public List<DeviceTypeAttribute> listByDeviceTypeId(String deviceTypeId) {
        return baseMapper.listByDeviceTypeId(deviceTypeId);
    }

    @Override
    public List<DeviceTypeAttribute> listActByTypeId(String typeId) {
        return baseMapper.listActByTypeId(typeId);
    }

    @Override
    public IPage<DeviceTypeAttribute> pageList(Page<DeviceTypeAttribute> page, QueryWrapper<DeviceTypeAttribute> queryWrapper) {
        Page<DeviceTypeAttribute> result = page(page, queryWrapper);
        setItemList(result);
        return result;
    }

    @Override
    public void add(DeviceTypeAttribute deviceTypeAttribute) {
        checkEnName(deviceTypeAttribute);
        deviceTypeAttribute.list2JsonList();
        save(deviceTypeAttribute);
        publisher.publishEvent(new DeviceTypeAttrAddEvent(deviceTypeAttribute));

    }

    private void checkEnName(DeviceTypeAttribute deviceTypeAttribute) {
        int count = baseMapper.countByTypeIdAndEnName(deviceTypeAttribute.getTypeId(), deviceTypeAttribute.getCategory());
        if (count > 0){
            throw new HssBootException("属性英文名称冲突");
        }
    }

    @Override
    public void edit(DeviceTypeAttribute deviceTypeAttribute) {
        DeviceTypeAttribute byId = getById(deviceTypeAttribute.getId());
        if (byId == null){
            throw new HssBootException("属性不存在");
        }
        if (!byId.getCategory().equals(deviceTypeAttribute.getCategory())){
            checkEnName(deviceTypeAttribute);
        }
        deviceTypeAttribute.list2JsonList();
        updateById(deviceTypeAttribute);
        publisher.publishEvent(new DeviceTypeAttrEditEvent(deviceTypeAttribute));
    }

    @Override
    public List<DeviceTypeAttribute> lisDataTableByDeviceId(String typeId) {
        return baseMapper.lisDataTableByDeviceId(typeId);
    }

    @Override
    public List<DeviceTypeAttribute> lisDataListByDeviceId(String typeId) {
        return baseMapper.lisDataListByDeviceId(typeId);
    }

    @Override
    public List<DeviceTypeAttribute> listByDeviceTypeIdsAndDataTable(Set<String> typeIds) {
        return baseMapper.listByDeviceTypeIdsAndDataTable(typeIds);
    }

    @Override
    public List<DeviceTypeAttribute> listByDeviceTypeIds(Collection<String> types) {
        return baseMapper.listByDeviceTypeIds(types);
    }

    /**
     * 将json数据转化
     * @param result
     */
    private void setItemList(Page<DeviceTypeAttribute> result) {
        for (DeviceTypeAttribute attr : result.getRecords()) {
            attr.listJson2List();
        }
    }
}
