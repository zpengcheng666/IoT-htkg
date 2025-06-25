package com.hss.modules.scada.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.service.IDeviceTypeAttributeService;
import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.event.ScadaDataAttrValueUpdateEvent;
import com.hss.modules.scada.mapper.ConDeviceAttributeMapper;
import com.hss.modules.scada.model.*;
import com.hss.modules.scada.service.*;
import com.hss.modules.util.CheckCyclicDepUtil;
import com.hss.modules.util.ExpressionUtil;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zpc
 * @version 1.0
 * @description: 设备属性、变量绑定,维护了缓存  增删改的时候需要额外维护缓存
 * @date 2024/3/19 15:06
 */
@Service
public class ConDeviceAttributeServiceImpl extends ServiceImpl<ConDeviceAttributeMapper, ConDeviceAttribute> implements IConDeviceAttributeService {

    @Autowired
    private DeviceAttrVarExpressionRationService deviceAttrVarExpressionRationService;
    @Autowired
    private DeviceAttrPointRelationService deviceAttrPointRelationService;
    @Autowired
    private DeviceAttrPointExpressionRelationService deviceAttrPointExpressionRelationService;
    @Autowired
    private ExpressionUtil expressionUtil;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IConDeviceAttributeService deviceAttributeService;
    @Autowired
    private DeviceDataService deviceDataService;
    @Autowired
    private IDeviceTypeAttributeService deviceTypeAttributeService;
    @Autowired
    private CheckCyclicDepUtil checkCyclicDepUtil;

    @Override
    @Cacheable(value = ScadaConstant.REDIS_KEY_ATTR)
    public ConDeviceAttribute getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean updateBatchById(Collection<ConDeviceAttribute> entityList) {
        for (ConDeviceAttribute attribute : entityList) {
            deviceAttributeService.updateById(attribute);
        }
        return true;
    }

    @Override
    @CacheEvict(value = ScadaConstant.REDIS_KEY_ATTR, key = "#entity.id")
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ConDeviceAttribute entity) {
        return super.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ConDeviceAttribute entity) {
        return super.save(entity);
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        for (Object o : list) {
            deviceAttributeService.removeById((String) o);
        }
        return true;
    }

    @Override
    @CacheEvict(value = ScadaConstant.REDIS_KEY_ATTR, key = "#id")
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        ConDeviceAttribute byId = getById(id);
        if (byId == null) {
            return true;
        }
        if (ScadaConstant.IS_ONE.equals(byId.getIsAssociate()) && StringUtils.isNotBlank(byId.getVariableId())) {
            deviceAttrPointRelationService.del(byId.getVariableId());
            deviceAttrPointExpressionRelationService.del(byId.getExpression());
        }
        redisUtil.del(ScadaConstant.REDIS_KEY_ATTR_DATA + byId.getId());
        return super.removeById(id);
    }

    @Override
    public void deleteByDeviceId(String deviceId) {
        List<ConDeviceAttribute> attributeList = listByDeviceId(deviceId);
        removeByIds(attributeList.stream().map(ConDeviceAttribute::getId).collect(Collectors.toList()));
    }

    @Override
    public List<ConDeviceAttribute> listByDeviceId(String deviceId) {
        return baseMapper.listByDeviceId(deviceId);
    }

    @Override
    public ConDeviceAttribute getByDeviceIdAndAttrEnName(String deviceId, String enName) {
        return baseMapper.getByDeviceIdAndAttrEnName(deviceId, enName);
    }

    @Override
    public DeviceAttrCountConfigAndBinding countBindingByDeviceId(String deviceId) {
        return baseMapper.countBindingByTypeId(deviceId);
    }

    @Override
    public DeviceAttrCountConfigAndBinding countVariableByTypeId(String deviceId) {
        return baseMapper.countVariableByTypeId(deviceId);
    }

    @Override
    public List<DeviceAttrRelation> listDeviceAttrVariable(String deviceId) {
        return baseMapper.listDeviceAttrVariable(deviceId);
    }

    @Override
    public void updateDataByRelationByAttrId(ConDeviceAttribute sourceAttr) {
        if (ScadaConstant.IS_ONE.equals(sourceAttr.getIsAssociateVar())) {
            return;
        }
        Collection<String> attrIds = deviceAttrVarExpressionRationService.getVarRelationAttrIdsByAttrId(sourceAttr.getId());
        if (CollectionUtils.isEmpty(attrIds)) {
            return;
        }
        for (String id : attrIds) {
            ConDeviceAttribute byId = getById(id);
            String value = expressionUtil.getValueByAttrValue(byId.getVarExpression(), String.class);
            if (StringUtils.isNotBlank(value)) {
                byId.setInitValue(value);
                DeviceAttrData data1 = deviceDataService.getAttrValueByAttrId(byId.getId());
                if (data1 == null || ObjectUtil.notEqual(value, data1.getValue())) {
                    publisher.publishEvent(new ScadaDataAttrValueUpdateEvent(byId));
                }
            }
        }
    }

    @Override
    public void updateRelation(List<DeviceAttrChangePoint> updateRelationList) {
        for (DeviceAttrChangePoint deviceAttrChangePoint : updateRelationList) {
            deviceAttrPointRelationService.del(deviceAttrChangePoint.getNewPointId());
            deviceAttrPointRelationService.del(deviceAttrChangePoint.getOldPointId());
            deviceAttrPointExpressionRelationService.del(deviceAttrChangePoint.getNewExpression());
            deviceAttrPointExpressionRelationService.del(deviceAttrChangePoint.getOldExpression());
        }
    }

    @Override
    public void delPointRelation(List<String> pointIds) {
        for (String pointId : pointIds) {
            deviceAttrPointRelationService.del(pointId);
        }
    }

    @Override
    public void delPointExpressionRelation(List<String> expressions) {
        for (String expression : expressions) {
            deviceAttrPointExpressionRelationService.del(expression);
        }
    }

    @Override
    public void updateRelationVar(List<DeviceAttrChangeVar> updateRelationList) {
        for (DeviceAttrChangeVar deviceAttrChangeVar : updateRelationList) {
            Set<String> ins = expressionUtil.listValueId(deviceAttrChangeVar.getNewExpression());
            if (!ins.isEmpty()) {
                boolean check = checkCyclicDepUtil.check(ins, Collections.singleton(deviceAttrChangeVar.getAttrId()));
                if (check) {
                    throw new HssBootException("存在循环依赖");
                }
            }
            deviceAttrVarExpressionRationService.delByExpression(deviceAttrChangeVar.getOldExpression());
            deviceAttrVarExpressionRationService.delByExpression(deviceAttrChangeVar.getNewExpression());
        }
    }

    @Override
    public List<ConDeviceAttribute> listAttrNotRedis() {
        List<ConDeviceAttribute> list = baseMapper.listPoint();
        return list.stream().filter(a -> {
            Collection<String> strategyByAttrId = deviceAttrPointRelationService.getAttrIdByPointId(a.getVariableId());
            return CollectionUtils.isEmpty(strategyByAttrId);
        }).collect(Collectors.toList());
    }

    @Override
    public List<ConSheBeiOptions> listStoreAttrByDeviceIds(String devIds) {
        if (StringUtils.isBlank(devIds)) {
            return Collections.emptyList();
        }
        List<String> deviceIds = Arrays.asList(devIds.split(","));
        return baseMapper.listStoreAttrByDeviceIds(deviceIds);
    }

    @Override
    public List<ConSheBeiOptions> listAlarmAttrByDeviceIds(String devIds) {
        if (StringUtils.isBlank(devIds)) {
            return Collections.emptyList();
        }
        List<String> deviceIds = Arrays.asList(devIds.split(","));
        return baseMapper.listAlarmAttrByDeviceIds(deviceIds);
    }

    @Override
    public List<ConDeviceAttribute> listByTypeAttrId(String typeId) {
        return baseMapper.listByTypeAttrId(typeId);
    }

    @Override
    public List<String> listIdByPointId(String pointId) {
        return baseMapper.listIdByPointId(pointId);
    }

    @Override
    public List<String> listVarRelationAttrIdByAttrId(String attrId) {
        return baseMapper.listVarRelationAttrIdByAttrId(attrId);
    }

    @Override
    public List<String> listIdByPointIdWithExpression(String pointId) {
        return baseMapper.listIdByPointIdWithExpression(pointId);
    }

    @Override
    public List<ConDeviceAttribute> listByDeviceIdsAndEnName(Collection<String> deviceIds, String enName) {
        return baseMapper.listByDeviceIdsAndEnName(deviceIds, enName);
    }

    @Override
    public void syncByDevice(ConSheBei conSheBei) {
        List<ConDeviceAttribute> attributeList = listByDeviceId(conSheBei.getId());
        List<DeviceTypeAttribute> typeAttributes = deviceTypeAttributeService.listByDeviceTypeId(conSheBei.getDeviceTypeId());
        if (attributeList.isEmpty() && typeAttributes.isEmpty()) {
            return;
        }
        Map<String, ConDeviceAttribute> attrMap = attributeList.stream().collect(Collectors.toMap(ConDeviceAttribute::getAttrId, o -> o));
        for (DeviceTypeAttribute typeAttribute : typeAttributes) {
            ConDeviceAttribute attr = attrMap.get(typeAttribute.getId());
            if (attr == null) {
                addByDeviceAndType(typeAttribute, conSheBei);
            } else {
                attrMap.remove(typeAttribute.getId());
                upDateByType(typeAttribute, attr);
            }
        }
        if (!attrMap.isEmpty()) {
            for (ConDeviceAttribute value : attrMap.values()) {
                deviceAttributeService.removeById(value.getId());
            }
        }
    }

    @Override
    public void addByDeviceAndType(DeviceTypeAttribute source, ConSheBei conSheBei) {
        ConDeviceAttribute deviceAttribute = new ConDeviceAttribute();
        // 属性信息
        deviceAttribute.setAttrId(source.getId());
        deviceAttribute.setDeviceTypeId(source.getTypeId());
        deviceAttribute.setName(source.getName());
        deviceAttribute.setEnName(source.getCategory());
        deviceAttribute.setMinValue(source.getMinValue());
        deviceAttribute.setMaxValue(source.getMaxValue());
        deviceAttribute.setIsSave(source.getIsSave());
        deviceAttribute.setIsAssociate(source.getIsAssociate());
        deviceAttribute.setIsAssociateVar(source.getIsAssociateVar());
        deviceAttribute.setIsConfigurable(source.getIsConfigurable());
        deviceAttribute.setSortNumber(source.getSortNumber());
        deviceAttribute.setDataType(source.getDataType());
        deviceAttribute.setUnit(source.getUnit());
        deviceAttribute.setInitValue(source.getInitValue());
        deviceAttribute.setValueMap(source.getValueMap());
        deviceAttribute.setDeviceId(conSheBei.getId());
        deviceAttribute.setDeviceName(conSheBei.getName());
        deviceAttributeService.save(deviceAttribute);
    }

    @Override
    public void upDateByType(DeviceTypeAttribute source, ConDeviceAttribute attribute) {
        attribute.setName(source.getName());
        attribute.setEnName(source.getCategory());
        attribute.setMinValue(source.getMinValue());
        attribute.setMaxValue(source.getMaxValue());
        attribute.setIsSave(source.getIsSave());
        attribute.setIsAssociate(source.getIsAssociate());
        attribute.setIsAssociateVar(source.getIsAssociateVar());
        attribute.setIsConfigurable(source.getIsConfigurable());
        attribute.setSortNumber(source.getSortNumber());
        attribute.setDataType(source.getDataType());
        attribute.setUnit(source.getUnit());
        attribute.setValueMap(source.getValueMap());
        deviceAttributeService.updateById(attribute);
    }

    @Override
    public Set<String> listIdByVarExpressionAttrIds(Collection<String> attrIds) {
        Set<String> ids = new HashSet<>();
        for (String attrId : attrIds) {
            List<String> list = baseMapper.listIdByVarExpressionAttrId(attrId);
            if (!list.isEmpty()) {
                ids.addAll(list);
            }
        }
        return ids;
    }

    @Override
    public List<DeviceIdAndAttrIdEnNameBO> listDeviceIdAttrIdByEnName(String enName) {
        return baseMapper.listDeviceIdAttrIdByEnName(enName);
    }

    @Override
    public List<String> listIdByEnName(String enName) {
        return baseMapper.listIdByEnName(enName);
    }

    @Override
    public List<ConDeviceAttribute> listByDeviceIdsAndEnNames(List<String> deviceIds, List<String> enNames) {
        return baseMapper.listByDeviceIdsAndEnNames(deviceIds, enNames);
    }
}
