package com.hss.modules.scada.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.config.ScadaConfigProperties;
import com.hss.core.common.exception.HssBootException;
import com.hss.core.common.util.LogUtil;
import com.hss.core.task.executor.TaskExecutor;
import com.hss.core.task.impl.SimplenessDelayTask;
import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.service.IAlarmStrategyService;
import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.entity.DeviceTypeManagement;
import com.hss.modules.devicetype.entity.DeviceTypeStoreStrategy;
import com.hss.modules.devicetype.service.IDeviceTypeAlarmStrategyService;
import com.hss.modules.devicetype.service.IDeviceTypeAttributeService;
import com.hss.modules.devicetype.service.IDeviceTypeManagementService;
import com.hss.modules.devicetype.service.IDeviceTypeStoreStrategyService;
import com.hss.modules.scada.additional.DeviceExecuteAdditional;
import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.entity.*;
import com.hss.modules.scada.event.ScadaDataAttrValueUpdateEvent;
import com.hss.modules.scada.event.ScadaDeviceAttrPointEvent;
import com.hss.modules.scada.mapper.ConSheBeiMapper;
import com.hss.modules.scada.model.*;
import com.hss.modules.scada.service.*;
import com.hss.modules.store.entity.StoreStrategy;
import com.hss.modules.store.model.vo.LineStateVO;
import com.hss.modules.store.model.vo.PieStateVO;
import com.hss.modules.store.service.IStoreStrategyService;
import com.hss.modules.system.entity.BaseLocation;
import com.hss.modules.system.service.IBaseLocationService;
import com.hss.modules.system.service.IBaseParamService;
import com.hss.modules.util.ExpressionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
* @description: 场景设备，列表查询，增删改查、关联属性、执行动作、变量关联、存储策略、点位关联、设备类型以及在线统计等
* @author zpc
* @date 2024/3/19 15:08
* @version 1.0
*/
@Service
@Slf4j
public class ConSheBeiServiceImpl extends ServiceImpl<ConSheBeiMapper, ConSheBei> implements IConSheBeiService {
    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;
    @Autowired
    private IDeviceTypeAttributeService deviceTypeAttributeService;
    @Autowired
    private IDeviceTypeAlarmStrategyService deviceTypeAlarmStrategyService;
    @Autowired
    private IDeviceTypeStoreStrategyService deviceTypeStoreStrategyService;
    @Autowired
    private IGSChangJingSheBeiService changJingSheBeiService;
    @Autowired
    private IConWangGuanService conWangGuanService;
    @Autowired
    private IAlarmStrategyService alarmStrategyService;
    @Autowired
    private IStoreStrategyService storeStrategyService;
    @Autowired
    private ExpressionUtil expressionUtil;
    @Autowired
    private IConSheBeiService deviceService;
    @Autowired
    private ScadaConfigProperties scadaConfigProperties;
    @Autowired(required = false)
    private List<DeviceExecuteAdditional> deviceExecuteAdditionalList;
    @Autowired
    private IDeviceExecuteLogService deviceExecuteLogService;
    @Autowired
    private IBaseLocationService baseLocationService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private DeviceDataService deviceDataService;
    @Autowired
    private IConDianWeiService conDianWeiService;
    @Autowired
    private TaskExecutor sleepTaskExecutor;
    @Autowired
    private IGsChangJingService gsChangJingService;

    @Autowired
    private IDeviceTypeManagementService deviceTypeManagementService;
    @Autowired
    private IBaseParamService baseParamService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ConSheBei device) {
        DeviceTypeManagement type = deviceTypeManagementService.getById(device.getDeviceTypeId());
        if (type == null) {
            throw new HssBootException("设备类型不存在");
        }
        device.setType(type.getName());
        save(device);
        List<ConDeviceAttribute> attrList = addAttrByDevice(device);
        addStrategyByDevice(device, attrList);
        LogUtil.setOperate(device.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAdd(List<ConSheBei> list) {
        list.forEach(device -> {
            DeviceTypeManagement type = deviceTypeManagementService.getById(device.getDeviceTypeId());
            if (type == null) {
                throw new HssBootException("设备类型不存在");
            }
            device.setType(type.getName());
            save(device);
            List<ConDeviceAttribute> attrList = addAttrByDevice(device);
            addStrategyByDevice(device, attrList);
        });
        LogUtil.setOperate("批量添加设备");
    }

    @NotNull
    private List<ConDeviceAttribute> addAttrByDevice(ConSheBei device) {
        List<DeviceTypeAttribute> attrTypeList = deviceTypeAttributeService.listByDeviceTypeId(device.getDeviceTypeId());
        List<ConDeviceAttribute> attrList = attrTypeList.stream().map(t -> {
            ConDeviceAttribute attr = new ConDeviceAttribute();
            attr.setAttrId(t.getId());
            attr.setDeviceTypeId(t.getTypeId());
            attr.setName(t.getName());
            attr.setEnName(t.getCategory());
            attr.setMinValue(t.getMinValue());
            attr.setMaxValue(t.getMaxValue());
            attr.setIsSave(t.getIsSave());
            attr.setIsAssociate(t.getIsAssociate());
            attr.setIsAssociateVar(t.getIsAssociateVar());
            attr.setIsConfigurable(t.getIsConfigurable());
            attr.setSortNumber(t.getSortNumber());
            attr.setDataType(t.getDataType());
            attr.setUnit(t.getUnit());
            attr.setInitValue(t.getInitValue());
            attr.setDeviceId(device.getId());
            attr.setDeviceName(device.getName());
            attr.setValueMap(t.getValueMap());
            return attr;
        }).collect(Collectors.toList());
        if (!attrList.isEmpty()) {
            conDeviceAttributeService.saveBatch(attrList);
        }
        return attrList;
    }


    /**
     * 根据设备信息添加相应的报警策略和存储策略。
     * @param device 设备信息，包含设备类型ID等。
     * @param attrList 设备属性列表，用于映射属性ID和类型ID的关系。
     */
    private void addStrategyByDevice(ConSheBei device, List<ConDeviceAttribute> attrList) {
        // 获取设备类型ID
        String deviceTypeId = device.getDeviceTypeId();
        // 构建属性类型ID到属性ID的映射
        Map<String, String> attrTypeAttrIdMap = attrList
                .stream()
                .collect(Collectors.toMap(ConDeviceAttribute::getAttrId, ConDeviceAttribute::getId));

        // 根据设备类型ID，查询并转换报警策略
        List<DeviceTypeAlarmStrategy> deviceTypeAlarmStrategies = deviceTypeAlarmStrategyService.listByTypeId(deviceTypeId);
        List<AlarmStrategy> alarmStrategies = deviceTypeAlarmStrategies
                .stream()
                .map(t -> alarmStrategyType2strategy(t, attrTypeAttrIdMap, device.getId())).collect(Collectors.toList());

        // 根据设备类型ID，查询并转换存储策略
        List<DeviceTypeStoreStrategy> deviceTypeStoreStrategies = deviceTypeStoreStrategyService.listByTypeId(deviceTypeId);
        List<StoreStrategy> storeStrategies = deviceTypeStoreStrategies
                .stream()
                .map(t -> storeStrategyType2strategy(t, attrTypeAttrIdMap, device.getId())).collect(Collectors.toList());

        // 保存报警策略，如果存在
        if (!alarmStrategies.isEmpty()) {
            alarmStrategyService.saveBatch(alarmStrategies);
        }
        // 保存存储策略，如果存在
        if (!storeStrategies.isEmpty()) {
            storeStrategyService.saveBatch(storeStrategies);
        }
    }


    /**
     * 编辑设备信息。
     * 该方法首先根据设备ID获取设备信息，如果不存在，则不进行任何操作。
     * 如果存在，则更新设备信息。如果设备名称发生改变，会更新与该设备关联的所有设备属性中的设备名称。
     *
     * @param device 设备对象，包含需要编辑的设备信息。
     * @Transactional 注解指明该方法是一个事务方法，回滚对于任何异常。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(ConSheBei device) {
        ConSheBei byId = deviceService.getById(device.getId());
        if (byId == null) {
            return;
        }
        deviceService.updateById(device);
        // 如果设备名称发生变化
        if (!byId.getName().equals(device.getName())) {
            // 获取与该设备关联的所有设备属性
            List<ConDeviceAttribute> attributeList = conDeviceAttributeService.listByDeviceId(device.getId());
            // 如果存在关联的设备属性
            if (!attributeList.isEmpty()) {
                // 更新所有关联设备属性的设备名称
                for (ConDeviceAttribute attribute : attributeList) {
                    attribute.setDeviceName(device.getName());
                }
                // 批量更新设备属性
                conDeviceAttributeService.updateBatchById(attributeList);
            }
        }
        LogUtil.setOperate(device.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = ScadaConstant.REDIS_KEY_DEVICE_CACHE, key = "#entity.id")
    public boolean updateById(ConSheBei entity) {
        return super.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = ScadaConstant.REDIS_KEY_DEVICE_CACHE, key = "#id")
    public void delete(String id) {
        ConSheBei byId = getById(id);
        if (byId == null) {
            return;
        }
        List<GsChangJing> sceneList = gsChangJingService.listNameAndSystemByDeviceId(id);
        if (!sceneList.isEmpty()) {
            String errStr = sceneList.stream().map(GsChangJing::getName).collect(Collectors.joining(","));
            throw new HssBootException("设备在场景中存在请先删除场景[" + errStr + "]中的设备");
        }
        removeById(id);
        delCacheByIds(Collections.singletonList(id));
        LogUtil.setOperate(byId.getName());

    }

    @Override
    @Cacheable(ScadaConstant.REDIS_KEY_DEVICE_CACHE)
    public ConSheBei getById(Serializable id) {
        ConSheBei byId = super.getById(id);
        if (byId == null) {
            return null;
        }
        byId.setLocationName(byId.getLocationId());
        return byId;
    }

    private String getLocationName(String locationId) {
        if (StringUtils.isNotBlank(locationId)) {
            BaseLocation location = baseLocationService.getById(locationId);
            if (location != null) {
                return location.getName();
            }
        }
        return null;
    }

    @Override
    @CacheEvict(value = ScadaConstant.REDIS_KEY_DEVICE_CACHE, allEntries = true)
    public boolean saveBatch(Collection<ConSheBei> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheEvict(value = ScadaConstant.REDIS_KEY_DEVICE_CACHE, allEntries = true)
    public boolean updateBatchById(Collection<ConSheBei> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @CacheEvict(value = ScadaConstant.REDIS_KEY_DEVICE_CACHE, allEntries = true)
    public boolean removeByIds(Collection<?> list) {
        boolean b = super.removeByIds(list);
        delCacheByIds(list);
        return b;
    }

    private void delCacheByIds(Collection<?> list) {
        for (Object deviceId : list) {
            String deviceId1 = (String) deviceId;
            // 删除属性
            conDeviceAttributeService.deleteByDeviceId(deviceId1);
            // 删除场景设备关联关系
            changJingSheBeiService.deleteByDeviceId(deviceId1);
            // 删除报警策略
            alarmStrategyService.deleteByDeviceId(deviceId1);
            // 删除存储策略
            storeStrategyService.deleteByDeviceId(deviceId1);
        }
    }

    @Override
    public IPage<ConSheBei> page(Page<ConSheBei> page, String sceneId, String name,String deviceTypeId) {
        return baseMapper.queryPage(page, sceneId, name,deviceTypeId);
    }

    @Override
    public IPage<ConSheBei> listPoint(Page<ConSheBei> page, String sceneId, String name,String deviceTypeId) {
        IPage<ConSheBei> conSheBeiPage;
        if (StringUtils.isNotBlank(sceneId)) {
            conSheBeiPage = page(page, sceneId, name,deviceTypeId);
        } else {
            conSheBeiPage = baseMapper.notScenePage(page, name,deviceTypeId);
        }
        if (conSheBeiPage.getRecords().isEmpty()) {
            return conSheBeiPage;
        }
        for (ConSheBei device : conSheBeiPage.getRecords()) {
            DeviceAttrCountConfigAndBinding binding = conDeviceAttributeService.countBindingByDeviceId(device.getId());
            if (StringUtils.isNotEmpty(device.getLocationId())) {
                device.setLocationName(device.getLocationId());
            }
            device.setCountStr(binding.getBindingCount() + "/" + binding.getAttrCount());
        }
        return conSheBeiPage;
    }

    @Override
    public IPage<ConSheBei> listVariable(Page<ConSheBei> page, String sceneId, String name) {
        List<String> deviceIdsBySceneId = null;
        if (StringUtils.isNotBlank(sceneId)) {
            deviceIdsBySceneId = changJingSheBeiService.listDeviceIdsBySceneId(sceneId);
        } else {
            deviceIdsBySceneId = baseMapper.ListIdByNotScenePage(name);
        }
        if (deviceIdsBySceneId.isEmpty()) {
            return page;
        }
        IPage<ConSheBei> sheBeiPage = pageVariable(page, deviceIdsBySceneId, name);
        if (sheBeiPage.getRecords().isEmpty()) {
            return sheBeiPage;
        }
        for (ConSheBei device : sheBeiPage.getRecords()) {
            if (StringUtils.isNotEmpty(device.getLocationId())) {
                device.setLocationName(device.getLocationId());
            }
            DeviceAttrCountConfigAndBinding binding = conDeviceAttributeService.countVariableByTypeId(device.getId());
            device.setCountStr(binding.getBindingCount() + "/" + binding.getAttrCount());
        }
        return sheBeiPage;
    }

    private IPage<ConSheBei> pageVariable(Page<ConSheBei> page, List<String> deviceIds, String name) {
        return baseMapper.pageVariable(page, deviceIds, name);
    }

    @Override
    public List<DeviceAttrRelation> listDeviceAttrVariable(String deviceId) {
        List<DeviceAttrRelation> deviceAttrRelations = conDeviceAttributeService.listDeviceAttrVariable(deviceId);
        for (DeviceAttrRelation deviceAttrRelation : deviceAttrRelations) {
            deviceAttrRelation.setRelationType(ScadaConstant.RELATION_TYPE_VAR);
            String varExpression = deviceAttrRelation.getVarExpression();
            if (StringUtils.isNotBlank(varExpression) && varExpression.contains("[")) {
                String expressionCh = expressionUtil.getExpressionStr(varExpression, this::getAttrCh);
                deviceAttrRelation.setVarExpressionCh(expressionCh);

            }

        }
        return deviceAttrRelations;
    }

    public String getAttrCh(String attrId) {
        ConDeviceAttribute byId = conDeviceAttributeService.getById(attrId);
        if (byId == null) {
            return null;
        }
        return byId.getDeviceName() + ":" + byId.getName();
    }

    @Override
    public List<DeviceAttrAct> listActByDeviceId(String deviceId) {
        ConSheBei byId = deviceService.getById(deviceId);
        List<DeviceTypeAttribute> list = deviceTypeAttributeService.listActByTypeId(byId.getDeviceTypeId());
        return list.stream().map(a -> {
            DeviceAttrAct act = new DeviceAttrAct();
            act.setName(a.getName());
            act.setEnName(a.getCategory());
            List<DeviceAttrActItem> deviceAttrActItems = JSONObject.parseArray(a.getActOrders(), DeviceAttrActItem.class);
            act.setOrderList(deviceAttrActItems);
            return act;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ConDeviceAttribute> listDeviceAttrByDeviceId(String deviceId) {
        return conDeviceAttributeService.listByDeviceId(deviceId);
    }

    @Override
    public List<ConSheBei> listPublishBySceneId(String sceneId) {
        return baseMapper.listPublishBySceneId(scadaConfigProperties.getPublishTypeList(), sceneId);
    }

    @Override
    public String getCameraByDeviceId(String deviceId) {
        ConDianWei point = getPointByDeviceIdAndEnName(deviceId, "onlineState");
        return conWangGuanService.getCameraByDeviceId(point.getWgid(), point.getDeviceId());
    }

    @Override
    public ConDianWei getPointByDeviceIdAndEnName(String deviceId, String attrEnName) {
        ConDeviceAttribute attribute = conDeviceAttributeService.getByDeviceIdAndAttrEnName(deviceId, attrEnName);
        if (attribute == null || StringUtils.isEmpty(attribute.getVariableId())) {
            throw new HssBootException("设备没有绑定");
        }
        ConDianWei point = conDianWeiService.getById(attribute.getVariableId());
        if (point == null) {
            throw new HssBootException("绑定的已经设备不存在了");
        }
        return point;
    }

    @Override
    public List<ConSheBei> ListCamera() {
        return baseMapper.ListCamera(scadaConfigProperties.getCameraTypeList());
    }

    @Override
    public List<DeviceAttrPointRelationVO> listDeviceAttrRelation(String deviceId) {
        List<ConDeviceAttribute> attributeList = conDeviceAttributeService.listByDeviceId(deviceId);
        return attributeList.stream()
                .filter(a -> ScadaConstant.IS_ONE.equals(a.getIsAssociate()))
                .map(a -> {
                    DeviceAttrPointRelationVO vo = new DeviceAttrPointRelationVO();
                    vo.setAttrId(a.getId());
                    vo.setAttrName(a.getName());
                    if (StringUtils.isNotBlank(a.getVariableId())) {
                        ConDianWei point = conDianWeiService.getById(a.getVariableId());
                        if (point != null) {
                            vo.setPointId(a.getVariableId());
                            vo.setGatewayId(point.getWgid());
                            vo.setDeviceId(point.getDeviceId());
                            vo.setExpressionStr(point.getDeviceName() + ":" + point.getName());
                        }
                    } else {
                        String expression = a.getExpression();
                        if (StringUtils.isNotBlank(expression)) {
                            vo.setExpression(expression);
                            String expressionCh = expressionUtil.getExpressionStr(expression, conDianWeiService::getPointNameByPointId);
                            vo.setExpressionStr(expressionCh);
                        }
                    }
                    return vo;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDeviceAttrRelation(DeviceAttrRelationSave deviceAttrRelationSave) {
        List<ConDeviceAttribute> attributeList = conDeviceAttributeService.listByDeviceId(deviceAttrRelationSave.getDeviceId());
        Map<String, String> relationMap = new HashMap<>(16);
        for (DeviceAttrRelation deviceAttrRelation : deviceAttrRelationSave.getDeviceAttrRelationList()) {
            relationMap.put(deviceAttrRelation.getRelationId(), deviceAttrRelation.getVarExpression());
        }
        List<DeviceAttrChangeVar> changeVars = attributeList
                .stream()
                .filter(a -> ScadaConstant.IS_ONE.equals(a.getIsAssociateVar()))
                .map(a -> {
                    DeviceAttrChangeVar deviceAttrChangeVar = new DeviceAttrChangeVar();
                    deviceAttrChangeVar.setAttrId(a.getId());
                    deviceAttrChangeVar.setNewExpression(relationMap.get(a.getId()));
                    deviceAttrChangeVar.setOldExpression(a.getVarExpression());
                    return deviceAttrChangeVar;
                }).filter(r -> !ObjectUtil.equal(r.getNewExpression(), r.getOldExpression()))
                .collect(Collectors.toList());
        if (changeVars.size() > 0) {
            conDeviceAttributeService.updateRelationVar(changeVars);
            List<ConDeviceAttribute> updateAttrList = changeVars.stream().map(r -> {
                ConDeviceAttribute deviceAttribute = new ConDeviceAttribute();
                deviceAttribute.setId(r.getAttrId());
                deviceAttribute.setVarExpression(r.getNewExpression() == null ? "" : r.getNewExpression());
                return deviceAttribute;
            }).collect(Collectors.toList());
            conDeviceAttributeService.updateBatchById(updateAttrList);
        }
    }


    @Override
    public List<ConSheBei> listBySceneId(String stageId) {
        return baseMapper.listBySceneId(stageId);
    }

    @Override
    public List<ConDeviceAttribute> listAttrBySceneId(String stageId) {
        return baseMapper.listAttrIdAndDeviceIdBySceneId(stageId);
    }

    private List<ConDeviceAttribute> listAttrIdAndDeviceIdBySceneId(String stageId) {
        return baseMapper.listAttrIdAndDeviceIdBySceneId(stageId);
    }


    /**
     * 添加策略
     *
     * @param deviceList 设备列表
     */
    private void addStrategy(List<ConSheBei> deviceList) {
        List<AlarmStrategy> alarmStrategies = new ArrayList<>();
        List<StoreStrategy> storeStrategies = new ArrayList<>();
        for (ConSheBei conSheBei : deviceList) {
            String deviceTypeId = conSheBei.getDeviceTypeId();
            Map<String, String> attrTypeAttrIdMap = conSheBei.getAttrList()
                    .stream()
                    .collect(Collectors.toMap(ConDeviceAttribute::getAttrId, ConDeviceAttribute::getId));
            // 添加报警策略
            List<DeviceTypeAlarmStrategy> deviceTypeAlarmStrategies = deviceTypeAlarmStrategyService.listByTypeId(deviceTypeId);
            List<AlarmStrategy> alarmStrategyList = deviceTypeAlarmStrategies
                    .stream()
                    .map(t -> alarmStrategyType2strategy(t, attrTypeAttrIdMap, conSheBei.getId())).collect(Collectors.toList());
            alarmStrategies.addAll(alarmStrategyList);

            // 添加存储策略
            List<DeviceTypeStoreStrategy> deviceTypeStoreStrategies = deviceTypeStoreStrategyService.listByTypeId(deviceTypeId);
            List<StoreStrategy> storeStrategyList = deviceTypeStoreStrategies
                    .stream()
                    .map(t -> storeStrategyType2strategy(t, attrTypeAttrIdMap, conSheBei.getId())).collect(Collectors.toList());
            storeStrategies.addAll(storeStrategyList);

        }
        if (!alarmStrategies.isEmpty()) {
            alarmStrategyService.saveBatch(alarmStrategies);
        }
        if (!storeStrategies.isEmpty()) {
            storeStrategyService.saveBatch(storeStrategies);
        }

    }

    /**
     * 执行设备操作。
     * 根据传入的设备执行DTO，执行相应的设备操作，并记录操作日志。
     *
     * @param dto 设备执行DTO，包含设备ID、属性英文名、属性值等信息。
     * @throws HssBootException 如果属性点不存在，则抛出异常。
     */
    @Override
    public void execute(DeviceExecuteDTO dto) {
        // 创建设备执行日志对象并设置基本信息
        DeviceExecuteLog deviceExecuteLog = new DeviceExecuteLog();
        deviceExecuteLog.setDeviceId(dto.getDeviceId());
        deviceExecuteLog.setValue(dto.getValue());
        deviceExecuteLog.setExecuteTime(new Date());

        // 根据设备ID和属性英文名获取属性信息
        ConDeviceAttribute attribute = conDeviceAttributeService.getByDeviceIdAndAttrEnName(dto.getDeviceId(), dto.getAttrEnName());
        if (attribute == null) {
            throw new HssBootException("属性点不存在");
        }
        deviceExecuteLog.setAttrId(attribute.getId());

        // 如果存在附加操作，则遍历执行
        if (deviceExecuteAdditionalList != null) {
            for (DeviceExecuteAdditional deviceExecuteAdditional : deviceExecuteAdditionalList) {
                try {
                    deviceExecuteAdditional.additionalOperate(dto, attribute);
                } catch (Exception e) {
                    log.error("执行动作附加操作异常 dto={},e={}", dto, e);
                }
            }
        }

        // 获取设备信息并设置操作日志
        ConSheBei byId = deviceService.getById(dto.getDeviceId());
        LogUtil.setOperate(byId.getName() + "." + attribute.getName() + ":" + dto.getValue());

        try {
            // 根据属性值执行命令
            executeCommandByValueExpression(attribute, dto.getValue());
            deviceExecuteLog.setRemark("执行成功");
            deviceExecuteLog.setExecuteResult(1);
        } catch (Exception e) {
            deviceExecuteLog.setRemark(e.getMessage());
            deviceExecuteLog.setExecuteResult(0);
            throw e;
        } finally {
            // 保存设备执行日志
            deviceExecuteLogService.save(deviceExecuteLog);
        }

    }

    @Override
    public void executeCommandByValueExpression(ConDeviceAttribute attribute, String valueExpression) {
        String value = parseValueExpressionAndNewTask(attribute, valueExpression);
        executeCommandByValue(attribute, value);
    }

    /**
     * 解析value表达式并创建任务
     *
     * @param attribute
     * @param valueExpression
     * @return
     */
    @Nullable
    private String parseValueExpressionAndNewTask(ConDeviceAttribute attribute, String valueExpression) {
        String pointDeviceId = getValueIfPublish(attribute, valueExpression);
        if (pointDeviceId != null) {
            return pointDeviceId;
        }
        return getValueIfMultiCom(attribute, valueExpression);
    }

    /**
     * 解析并添加多条命令任务。该方法会检查输入的值表达式是否包含连字符"-”，如果包含且格式为"值-等待时间-命令"，则会将第一部分作为当前返回值，
     * 同时解析出等待时间和命令，并创建一个延迟任务执行该命令。
     *
     * @param attribute 设备属性，用于命令执行时的参数。
     * @param valueExpression 值表达式，可能包含多个命令的执行参数。
     * @return 返回解析后的初始值部分。
     */
    private String getValueIfMultiCom(ConDeviceAttribute attribute, String valueExpression) {
        String value = valueExpression;
        // 检查值表达式是否包含连字符"-”且格式正确
        if (valueExpression != null && valueExpression.contains("-")) {
            String[] split = valueExpression.split("-");
            // 确保分割后有且仅有三部分，分别代表值、等待时间和命令
            if (split.length == 3) {
                value = split[0]; // 当前返回解析出的初始值
                long waitTime = 0;
                // 尝试解析等待时间，如果失败则抛出异常
                try {
                    waitTime = Long.parseLong(split[1]);
                } catch (NumberFormatException e) {
                    throw new HssBootException("解析value表达式错误");
                }
                // 创建延迟任务，并设置执行逻辑为执行指定的命令
                SimplenessDelayTask delayTask = new SimplenessDelayTask(waitTime, sleepTaskExecutor);
                delayTask.setRunnable(() -> executeCommandByValue(attribute, split[2]));
                delayTask.start(); // 启动延迟任务
            }
        }
        return value;
    }


    /**
     * 对讲命令，需要获取对讲设备的点位设备id
     *
     * @param attribute
     * @param value
     * @return
     */
    @Nullable
    private String getValueIfPublish(ConDeviceAttribute attribute, String value) {
        if (ScadaConstant.CALL.equals(attribute.getEnName())) {
            String pointDeviceId = baseMapper.getPointDeviceIdByDeviceIdAndAttrEnName(value, "talkState");
            if (StringUtils.isBlank(pointDeviceId)) {
                throw new HssBootException("设备不存在或对讲状态未绑定");
            }
            return pointDeviceId;
        }
        return null;
    }

    /**
     * 根据值执行命令
     *
     * @param attribute
     * @param value
     */
    private void executeCommandByValue(ConDeviceAttribute attribute, String value) {
        if (attribute.getIsAssociate() == 1) {
            executeCommandToDevice(attribute, value);
        } else {
            executeCommandToPlatform(attribute, value);
        }
    }

    /**
     * 下发命令到平台
     *
     * @param attribute
     * @param value
     */
    private void executeCommandToPlatform(ConDeviceAttribute attribute, String value) {
        DeviceAttrData data = deviceDataService.getAttrValueByAttrId(attribute.getId());
        if (data == null || ObjectUtil.notEqual(value, data.getValue())) {
            attribute.setInitValue(value);
            publisher.publishEvent(new ScadaDataAttrValueUpdateEvent(attribute));
        }
    }

    /**
     * 下发命令到设备
     *
     * @param attribute
     * @param value
     */
    private void executeCommandToDevice(ConDeviceAttribute attribute, String value) {
        if (StringUtils.isBlank(attribute.getVariableId())) {
            throw new HssBootException("设备属性没有绑定点位");
        }
        ConDianWei point = conDianWeiService.getById(attribute.getVariableId());

        if (point == null) {
            throw new HssBootException("点位不存在");
        }
        conWangGuanService.executeCommand(point.getWgid(), attribute.getVariableId(), value);

    }


    @Override
    public AlarmStrategy alarmStrategyType2strategy(DeviceTypeAlarmStrategy type, Map<String, String> idMap, String deviceId) {
        AlarmStrategy strategy = new AlarmStrategy();
        strategy.setName(type.getName());
        strategy.setType(type.getType());
        strategy.setLevelId(type.getLevelId());
        strategy.setGroupId(type.getGroupId());
        strategy.setPeriod(type.getPeriod());
        strategy.setFrequency(type.getFrequency());
        strategy.setStatusVarId(idMap.get(type.getStatusVarId()));
        strategy.setExpression(expressionUtil.getExpressionOriginalStr(idMap, type.getExpression()));
        strategy.setClearExpression(expressionUtil.getExpressionOriginalStr(idMap, type.getClearExpression()));
        strategy.setRange(expressionUtil.getExpressionOriginalStr(idMap, type.getRange()));
        strategy.setOriginVarId(idMap.get(type.getOriginVarId()));
        strategy.setStoreCondition(type.getStoreCondition());
        strategy.setAlarmPushCondition(type.getAlarmPushCondition());
        strategy.setIsEnable(type.getIsEnable());
        strategy.setValueExpression(expressionUtil.getExpressionOriginalStr(idMap, type.getValueExpression()));
        strategy.setDeviceId(deviceId);
        strategy.setDescription(type.getDescription());
        strategy.setDelayBegin(type.getDelayBegin());
        strategy.setDelayRemove(type.getDelayRemove());
        strategy.setAlarmMode(0);
        strategy.setDeleted(0);
        strategy.setStrategyId(type.getId());
        return strategy;
    }

    /**
     * 存储策略转换
     *
     * @param type     预定义策略
     * @param idMap    预定义属性id 和 属性id 的map
     * @param deviceId 设备id
     * @return 新增策略
     */
    @Override
    public StoreStrategy storeStrategyType2strategy(DeviceTypeStoreStrategy type, Map<String, String> idMap, String deviceId) {
        StoreStrategy strategy = new StoreStrategy();
        strategy.setName(type.getName());
        strategy.setType(type.getType());
        strategy.setUnit(type.getUnit());
        strategy.setPeriod(type.getPeriod());
        strategy.setExpression(type.getExpression());
        strategy.setVariableId(idMap.get(type.getVariableId()));
        strategy.setGroupId(type.getGroupId());
        strategy.setIsEnable(type.getIsEnable());
        strategy.setDeviceId(deviceId);
        strategy.setDescription(type.getDescription());
        strategy.setStoreMode(0);
        strategy.setDeleted(0);
        strategy.setStrategyId(type.getId());
        return strategy;
    }

    @Override
    public List<DeviceAttrDataTable> lisDataTableByDeviceId(String deviceId) {
        ConSheBei device = deviceService.getById(deviceId);
        if (device == null) {
            return Collections.emptyList();
        }
        List<DeviceTypeAttribute> list = deviceTypeAttributeService.lisDataTableByDeviceId(device.getDeviceTypeId());
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(t -> {
            DeviceAttrDataTable attrDataTable = new DeviceAttrDataTable();
            attrDataTable.setName(t.getName());
            attrDataTable.setEnName(t.getCategory());
            String valueMapJson = t.getValueMap();
            if (StringUtils.isNotBlank(valueMapJson) && !"{}".equals(valueMapJson)) {
                JSONObject jsonObject = JSONObject.parseObject(valueMapJson);
                HashMap<String, String> map = new HashMap<>(16);
                jsonObject.forEach((k, v) -> map.put(k, ((String) v)));
                attrDataTable.setValueMap(map);
            }
            return attrDataTable;

        }).collect(Collectors.toList());

    }

    /**
     * 根据设备ID列出设备属性数据表
     * @param deviceId 设备ID
     * @return 设备属性数据表列表。如果找不到设备或设备没有属性，则返回空列表。
     */
    @Override
    public List<DeviceAttrDataTable> lisDataListByDeviceId(String deviceId) {
        // 通过设备ID获取设备信息
        ConSheBei device = deviceService.getById(deviceId);
        // 如果设备不存在，则返回空列表
        if (device == null) {
            return Collections.emptyList();
        }
        // 根据设备类型ID获取设备属性列表
        List<DeviceTypeAttribute> list = deviceTypeAttributeService.lisDataListByDeviceId(device.getDeviceTypeId());
        // 如果属性列表为空，则返回空列表
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        // 遍历属性列表，转换为DeviceAttrDataTable对象，并收集到列表中
        return list.stream().map(t -> {
            DeviceAttrDataTable attrDataTable = new DeviceAttrDataTable();
            attrDataTable.setName(t.getName()); // 设置属性名
            attrDataTable.setEnName(t.getCategory()); // 设置属性英文名
            // 判断并处理属性值映射
            String valueMapJson = t.getValueMap();
            if (StringUtils.isNotBlank(valueMapJson) && !"{}".equals(valueMapJson)) {
                JSONObject jsonObject = JSONObject.parseObject(valueMapJson);
                HashMap<String, String> map = new HashMap<>(16);
                jsonObject.forEach((k, v) -> map.put(k, ((String) v)));
                attrDataTable.setValueMap(map); // 设置属性值映射
            }
            return attrDataTable;
        }).collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDeviceAttrPointRelation(DeviceAttrPointRelationDTO dto) {
        // key 属性id  value 点位id
        Map<String, DeviceAttrPointRelationItem> relationMap = new HashMap<>(16);
        for (DeviceAttrPointRelationItem deviceAttrRelation : dto.getAttrList()) {
            relationMap.put(deviceAttrRelation.getAttrId(), deviceAttrRelation);
        }
        List<ConDeviceAttribute> attributeList = conDeviceAttributeService.listByDeviceId(dto.getDeviceId());
        // 需要删除的点位缓存
        List<String> delPointIds = new ArrayList<>();
        // 需要删除的点位表达式缓存
        List<String> delExpression = new ArrayList<>();
        // 需要更新的属性列表
        List<ConDeviceAttribute> updateAttrList = new ArrayList<>();
        GatewayDeviceUpdate gatewayDeviceUpdate = new GatewayDeviceUpdate();
        for (ConDeviceAttribute attribute : attributeList) {
            if (!ScadaConstant.IS_ONE.equals(attribute.getIsAssociate())) {
                continue;
            }
            DeviceAttrPointRelationItem relationItem = relationMap.get(attribute.getId());
            if (relationItem == null) {
                continue;
            }
            if (StringUtils.isNotBlank(relationItem.getExpression())) {
                Set<String> strings = expressionUtil.listValueId(relationItem.getExpression());
                if (strings.size() != 1) {
                    throw new HssBootException("只支持1个变量的计算");
                }
            }
            boolean updateAttrFlag = false;
            if (!strEqual(attribute.getVariableId(), relationItem.getPointId())) {
                updateAttrFlag = true;
                if (StringUtils.isNotBlank(attribute.getVariableId())) {
                    delPointIds.add(attribute.getVariableId());
                }
                if (StringUtils.isNotBlank(relationItem.getPointId())) {
                    delPointIds.add(relationItem.getPointId());
                }
                if (ScadaConstant.ONLINE_STATE.equals(attribute.getEnName())) {
                    gatewayDeviceUpdate.setDeviceId(attribute.getDeviceId());
                    gatewayDeviceUpdate.setNewPointId(relationItem.getPointId());
                    gatewayDeviceUpdate.setOldPointId(attribute.getVariableId());
                }
            }
            if (!strEqual(attribute.getVarExpression(), relationItem.getExpression())) {
                updateAttrFlag = true;
                if (StringUtils.isNotBlank(attribute.getVarExpression())) {
                    delExpression.add(attribute.getExpression());
                }
                if (StringUtils.isNotBlank(relationItem.getExpression())) {
                    delExpression.add(relationItem.getExpression());
                }
            }
            if (updateAttrFlag) {
                ConDeviceAttribute updateAttr = new ConDeviceAttribute();
                updateAttr.setId(attribute.getId());
                updateAttr.setVariableId(relationItem.getPointId() == null ? "" : relationItem.getPointId());
                updateAttr.setExpression(relationItem.getExpression() == null ? "" : relationItem.getExpression());
                updateAttrList.add(updateAttr);

            }

        }
        if (!updateAttrList.isEmpty()) {
            conDeviceAttributeService.updateBatchById(updateAttrList);
        }
        // 删除缓存
        if (!delPointIds.isEmpty()) {
            conDeviceAttributeService.delPointRelation(delPointIds);
        }
        if (!delExpression.isEmpty()) {
            conDeviceAttributeService.delPointExpressionRelation(delPointIds);
        }
        // 缓存双删
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (!delPointIds.isEmpty()) {
                    conDeviceAttributeService.delPointRelation(delPointIds);
                }
                if (!delExpression.isEmpty()) {
                    conDeviceAttributeService.delPointExpressionRelation(delPointIds);
                }
                if (StringUtils.isNotBlank(gatewayDeviceUpdate.getDeviceId())) {
                    publisher.publishEvent(new ScadaDeviceAttrPointEvent(gatewayDeviceUpdate));
                }
            }
        });

    }


    private boolean strEqual(String s1, String s2) {
        if (StringUtils.isBlank(s1) && StringUtils.isBlank(s2)) {
            return true;
        }
        return ObjectUtil.equal(s1, s2);
    }

    /**
     * 校验点位关联
     *
     * @param r 关联关系
     * @return 如果相同返回false 如果不同返回true
     */
    private boolean checkChangePointRelation(DeviceAttrChangePoint r) {
        boolean pointEqual = false;
        boolean expressionEqual = false;
        if (StringUtils.isBlank(r.getOldPointId()) && StringUtils.isBlank(r.getNewPointId())) {
            pointEqual = true;
        }
        if (StringUtils.isBlank(r.getOldExpression()) && StringUtils.isBlank(r.getNewExpression())) {
            expressionEqual = true;
        }
        if (pointEqual && expressionEqual) {
            return false;
        }
        if (ObjectUtil.equal(r.getNewPointId(), r.getOldPointId())) {
            pointEqual = true;
        }
        if (ObjectUtil.equal(r.getOldExpression(), r.getNewExpression())) {
            expressionEqual = true;
        }
        if (pointEqual && expressionEqual) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDeviceAttrConfig(DeviceAttrConfigDTO dto) {
        if (StringUtils.isBlank(dto.getDeviceId())) {
            throw new HssBootException("设备id不能为空");
        }
        if (CollectionUtils.isEmpty(dto.getAttrList())) {
            throw new HssBootException("属性列表不能为空");
        }
        List<ConDeviceAttribute> attributeList = conDeviceAttributeService.listByDeviceId(dto.getDeviceId());
        ArrayList<ConDeviceAttribute> publishList = new ArrayList<>();
        Map<String, ConDeviceAttribute> attrMap = attributeList.stream().collect(Collectors.toMap(ConDeviceAttribute::getEnName, o -> o));
        List<ConDeviceAttribute> attrUpdateList = dto.getAttrList()
                .stream()
                .filter(a -> {
                    ConDeviceAttribute attribute = attrMap.get(a.getEnName());
                    if (attribute == null) {
                        return false;
                    }
                    if (a.getValue() == null) {
                        a.setValue("");
                    }
                    DeviceAttrData data = deviceDataService.getAttrValueByAttrId(attribute.getId());
                    if (ObjectUtil.equal(data.getValue(), a.getValue())) {
                        return false;
                    }
                    a.setId(attribute.getId());
                    attribute.setInitValue(a.getValue());
                    publishList.add(attribute);
                    return true;
                })
                .map(c -> {
                    ConDeviceAttribute attribute = new ConDeviceAttribute();
                    attribute.setId(c.getId());
                    attribute.setInitValue(c.getValue());
                    return attribute;
                })
                .collect(Collectors.toList());
        if (attrUpdateList.isEmpty()) {
            return;
        }
        conDeviceAttributeService.updateBatchById(attrUpdateList);
        for (ConDeviceAttribute attribute : publishList) {
            publisher.publishEvent(new ScadaDataAttrValueUpdateEvent(attribute));
        }
    }

    @Override
    public List<ConDeviceAttribute> listAttrNotRedis() {
        return conDeviceAttributeService.listAttrNotRedis();
    }

    @Override
    public List<ConSheBeiDoorOptions> listDeviceTypeByDeviceIds(List<String> deviceIds) {
        return baseMapper.listDeviceTypeByDeviceIds(deviceIds);
    }

    @Override
    public List<String> listIdBySubsystem(String subsystem) {
        return baseMapper.listIdBySubsystem(subsystem);
    }

    @Override
    public List<ConSheBei> listByDeviceTypeId(String typeId) {
        return baseMapper.listByDeviceTypeId(typeId);
    }

    @Override
    public IPage<DeviceListVO> deviceList(Page<DeviceListVO> page, DeviceListDTO dto) {
        List<String> ids = null;
        if (StringUtils.isNotBlank(dto.getSceneId())) {
            ids = changJingSheBeiService.listDeviceIdsBySceneId(dto.getSceneId());
            if (ids.isEmpty()) {
                return page;
            }
        }
        IPage<DeviceListVO> list = baseMapper.deviceList(page, dto, ids);
        if (list.getRecords().isEmpty()) {
            return list;
        }
        for (DeviceListVO vo : list.getRecords()) {
            String locationId = vo.getLocationId();
            if (StringUtils.isNotEmpty(locationId)) {
                vo.setLocationId(StringUtils.trim(locationId));
            }
            //2023-11-04修改，新增别名
            String otherName = vo.getOtherName();
            if (StringUtils.isNotEmpty(otherName)) {
                vo.setLocationId(StringUtils.trim(otherName));
            }
            List<GsChangJing> gsChangJings = gsChangJingService.listNameAndSystemByDeviceId(vo.getId());
            List<String> sceneNames = new ArrayList<>(gsChangJings.size());
            HashSet<String> subList = new HashSet<>();
            for (GsChangJing gsChangJing : gsChangJings) {
                sceneNames.add(gsChangJing.getName());
                if (StringUtils.isNotEmpty(gsChangJing.getSubSystem())) {
                    //chushubin 2024-03-20修改，将模块配置数据从json文件中迁移到yml配置文件中
                    String subSystemName = this.scadaConfigProperties.getSubSystemName(gsChangJing.getSubSystem());
                    if (StringUtils.isNotEmpty(subSystemName)) {
                        subList.add(subSystemName);
                    }
                }
            }
            vo.setSceneList(sceneNames);
            vo.setSubsystemList(subList);
        }
        return list;
    }

    @Override
    public List<DataTableVO> listDataTableByIds(Collection<String> deviceIds) {
        List<ConSheBei> deviceList = listByIds(deviceIds);
        Map<String, List<ConSheBei>> deviceMap = deviceList.stream().collect(Collectors.groupingBy(ConSheBei::getDeviceTypeId));
        List<DeviceTypeAttribute> deviceTypeAttributes = deviceTypeAttributeService.listByDeviceTypeIdsAndDataTable(deviceMap.keySet());
        if (deviceTypeAttributes.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, List<DeviceTypeAttribute>> typeMapping = deviceTypeAttributes.stream().collect(Collectors.groupingBy(DeviceTypeAttribute::getTypeId));
        List<DataTableVO> voList = typeMapping.entrySet().stream()
                .map(typeEntry -> {
                    String typeId = typeEntry.getKey();
                    List<DeviceTypeAttribute> typeAttrList = typeEntry.getValue();
                    DataTableVO vo = new DataTableVO();
                    vo.setDeviceTypeId(typeId);
                    List<ConSheBei> deviceListByType = deviceMap.get(typeId);
                    ConSheBei firstDevice = deviceListByType.get(0);
                    vo.setDeviceTypeName(firstDevice.getType());
                    vo.setDeviceList(getDataTableVODevices(typeAttrList, deviceListByType));
                    return vo;
                }).collect(Collectors.toList());
        return voList;
    }

    /**
     * 数据表格获取设备列表
     *
     * @param typeAttrList
     * @param deviceListByType
     * @return
     */
    private List<DataTableVODevice> getDataTableVODevices(List<DeviceTypeAttribute> typeAttrList, List<ConSheBei> deviceListByType) {
        List<DataTableVODevice> deviceVoList = deviceListByType.stream().map(d -> {
            DataTableVODevice dataTableDevice = new DataTableVODevice();
            dataTableDevice.setId(d.getId());
            dataTableDevice.setName(d.getName());
            dataTableDevice.setAttrList(getDataTableVODeviceAttrs(typeAttrList));
            return dataTableDevice;
        }).collect(Collectors.toList());
        return deviceVoList;
    }

    /**
     * 数据表格获取属性裂开表
     *
     * @param typeAttrList
     * @return
     */
    private List<DataTableVODeviceAttr> getDataTableVODeviceAttrs(List<DeviceTypeAttribute> typeAttrList) {
        List<DataTableVODeviceAttr> attrVoList = typeAttrList.stream().map(a -> {
            DataTableVODeviceAttr dataAttr = new DataTableVODeviceAttr();
            dataAttr.setName(a.getName());
            dataAttr.setEnName(a.getCategory());
            return dataAttr;
        }).collect(Collectors.toList());
        return attrVoList;
    }

    @Override
    public List<ConSheBei> listByTypeCode(String typeCode) {
        DeviceTypeManagement byType = deviceTypeManagementService.getByType(typeCode);
        if (byType == null) {
            return Collections.emptyList();
        }
        return listByDeviceTypeId(byType.getId());
    }

    @Override
    public List<ConSheBei> listFanBySystemDeviceId(String systemDeviceId) {
        List<String> sceneIds = changJingSheBeiService.listSceneIdByDeviceId(systemDeviceId);
        if (sceneIds.isEmpty()) {
            return Collections.emptyList();
        }
        return baseMapper.listDeviceIdAndNameBySceneIdsAndAttrEnName(sceneIds, "selectCtrl");

    }

    @Override
    public List<EnvDeviceTableVO> listEnvDeviceBySceneId(String sceneId) {
        //确定场景子系统
        String subSystem = gsChangJingService.getById(sceneId).getSubSystem();
        //处理环境表格配置信息
        List<EnvDeviceConfigJson> types = getEnvDeviceConfigJsons(subSystem);
        if (types.isEmpty()) {
            return Collections.emptyList();
        }
        return types.stream()
                .map(t -> {
                    //环境实时数据表格数据处理
                    EnvDeviceTableVO vo = new EnvDeviceTableVO();
                    vo.setTitle(t.getTitle());
                    vo.setGroup(t.getGroup());
                    vo.setAttrList(t.getAttrList().stream().map(this::defToVO).collect(Collectors.toList()));
                    vo.setDeviceList(getList(sceneId, t.getTypes(), t.getAttrList()));
                    return vo;
                }).collect(Collectors.toList());
    }

    private List<EnvDeviceTableVODevice> getList(String sceneId, List<String> types, List<EnvDeviceConfigJsonAttr> attrList) {
        if (CollectionUtils.isEmpty(types)) {
            return Collections.emptyList();
        }
        List<ConSheBei> list = baseMapper.listBySceneIdAndType(sceneId, types);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> enNames = attrList.stream().map(def -> {
            List<String> codes = new ArrayList<>();
            codes.add(def.getEnName());
            codes.add(def.getLlEnName());
            codes.add(def.getHhEnName());
            codes.add(def.getAlarmEnName());
            return codes;
        }).flatMap(Collection::stream).collect(Collectors.toList());
        List<String> deviceIds = list.stream().map(ConSheBei::getId).collect(Collectors.toList());
        List<ConDeviceAttribute> alist = conDeviceAttributeService.listByDeviceIdsAndEnNames(deviceIds, enNames);
        Map<String, List<ConDeviceAttribute>> deviceMap = alist.stream().collect(Collectors.groupingBy(ConDeviceAttribute::getDeviceId));
        return list.stream()
                .sorted(this::sortNumber)
                .map(d -> {
                    EnvDeviceTableVODevice vo = new EnvDeviceTableVODevice();
                    vo.setId(d.getId());
                    vo.setName(d.getName());
                    vo.setOtherName(d.getOtherName());
                    List<ConDeviceAttribute> attributeList = deviceMap.get(d.getId());
                    if (attributeList != null) {
                        Map<String, String> data = attributeList.stream().collect(Collectors.toMap(ConDeviceAttribute::getEnName, o -> {
                            DeviceAttrData attrData = deviceDataService.getAttrValueByAttrId(o.getId());
                            if (attrData != null) {
                                String value = attrData.getValue();
                                return value == null ? "" : value;
                            }
                            return "";
                        }));
                        vo.setData(data);

                    }
                    return vo;
        }).collect(Collectors.toList());
    }

    @NotNull
    private EnvDeviceTableVOAttr defToVO(EnvDeviceConfigJsonAttr a) {
        EnvDeviceTableVOAttr attrVo = new EnvDeviceTableVOAttr();
        attrVo.setName(a.getName());
        attrVo.setEnName(a.getEnName());
        attrVo.setAlarmEnName(a.getAlarmEnName());
        attrVo.setHhEnName(a.getHhEnName());
        attrVo.setLlEnName(a.getLlEnName());
        return attrVo;
    }

    private int sortNumber(ConSheBei d1, ConSheBei d2) {
        if (d1.getSortNumber() == null && d2.getSortNumber() == null) {
            return 1;
        }
        if (d1.getSortNumber() != null && d2.getSortNumber() == null) {
            return 1;
        }
        if (d1.getSortNumber() == null && d2.getSortNumber() != null) {
            return -1;
        }
        return d1.getSortNumber() - d2.getSortNumber();
    }

    private List<EnvDeviceConfigJson> getEnvDeviceConfigJsons(String subSystem) {
        List<EnvDeviceConfigJson> list = new ArrayList<>();
        //增加了判断后备配置文件用2023-11-04
        if (ScadaConstant.HUANJING.equals(subSystem)) {
            String value = baseParamService.getParamByCode(ScadaConstant.ENV_PARAM);
            if (value == null) {
                return Collections.emptyList();
            }
            return JSONObject.parseArray(value, EnvDeviceConfigJson.class);
        }
        return list;
    }

    @Override
    public boolean existByDeviceTypeId(String deviceTypeId) {
        int count = baseMapper.countByDeviceTypeId(deviceTypeId);
        return count > 0;
    }

    @Override
    public List<ConDeviceAttribute> listDeviceTendencyAttr(String deviceId) {
        List<ConDeviceAttribute> attributeList = conDeviceAttributeService.listByDeviceId(deviceId);
        return attributeList.stream()
                .filter(a -> ScadaConstant.IS_ONE.equals(a.getIsAssociate()))
                .filter(a -> "float".equals(a.getDataType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConSheBeiOptions> listDoorOption() {
        return listDeviceOptionByParamType("scada_device_door_types");

    }

    @NotNull
    private List<ConSheBeiOptions> listDeviceOptionByParamType(String paramTypeCode) {
        String types = baseParamService.getParamByCode(paramTypeCode);
        if (StringUtils.isBlank(types)) {
            return Collections.emptyList();
        }
        String[] doorTypeArray = types.split(",");
        return Arrays.stream(doorTypeArray)
                .map(t -> {
                    DeviceTypeManagement byType = deviceTypeManagementService.getByType(t);
                    if (byType == null) {
                        return null;
                    }
                    return listByDeviceTypeId(byType.getId());
                }).filter(ds -> !CollectionUtils.isEmpty(ds))
                .flatMap(Collection::stream)
                .sorted(this::sortNumber)
                .map(d -> {
                    ConSheBeiOptions conSheBeiOptions = new ConSheBeiOptions();
                    conSheBeiOptions.setId(d.getId());
                    conSheBeiOptions.setName(d.getName());
                    return conSheBeiOptions;
                }).collect(Collectors.toList());
    }

    @Override
    public List<ConSheBeiOptions> listCheckDoorOption() {
        return listDeviceOptionByParamType("scada_device_check_door_types");
    }

    @Override
    public void syncByType(DeviceTypeManagement type) {
        List<ConSheBei> conSheBeis = listByDeviceTypeId(type.getId());
        if (conSheBeis.isEmpty()) {
            return;
        }
        for (ConSheBei conSheBei : conSheBeis) {
            syncDeviceByType(type, conSheBei);
        }

    }

    private void syncDeviceByType(DeviceTypeManagement type, ConSheBei conSheBei) {
        if (!conSheBei.getType().equals(type.getName())) {
            ConSheBei update = new ConSheBei();
            update.setId(conSheBei.getId());
            update.setType(type.getName());
            updateById(update);
        }
        conDeviceAttributeService.syncByDevice(conSheBei);
        alarmStrategyService.syncByDevice(conSheBei);
        storeStrategyService.syncByDevice(conSheBei);
    }

    @Override
    public List<GatewayDevice> listGatewayAndDeviceByEnName(String attrEnName) {
        return baseMapper.listGatewayAndDeviceByEnName(attrEnName);
    }

    @Override
    public LineStateVO statByDeviceType() {
        List<StateByDeviceTypeDeviceBO> list = baseMapper.statByDeviceType();
        Map<String, BigDecimal> dataMap = list.stream().collect(Collectors.toMap(StateByDeviceTypeDeviceBO::getDeviceTypeId, c -> new BigDecimal(c.getCount())));
        List<DeviceTypeIdNameBO> names = deviceTypeManagementService.listNameByIds(dataMap.keySet());
        LineStateVO vo = new LineStateVO();
        vo.setLegend(Collections.singletonList("设备类型"));
        List<String> x = new ArrayList<>(names.size());
        List<BigDecimal> date = new ArrayList<>(names.size());
        for (DeviceTypeIdNameBO name : names) {
            x.add(name.getName());
            date.add(dataMap.get(name.getId()));
        }
        vo.setXAxis(x);
        vo.setSeries(Collections.singletonList(date));
        return vo;
    }

    @Override
    public List<PieStateVO> statByDeviceState() {
        List<DeviceIdAndAttrIdEnNameBO> list = conDeviceAttributeService.listDeviceIdAttrIdByEnName("onlineState");

        int up = 0;
        int down = 0;
        for (DeviceIdAndAttrIdEnNameBO bo : list) {
            DeviceAttrData value = deviceDataService.getAttrValueByAttrId(bo.getAttrId());
            if ("1".equals(value)) {
                up++;
            } else {
                down++;
            }
        }
        int sum = up + down;


        PieStateVO upVo = new PieStateVO();
        upVo.setName("在线");
        upVo.setValue(new BigDecimal(up));
        upVo.setRatio(sum == 0 ? BigDecimal.ZERO : new BigDecimal(up * 100F / sum));

        PieStateVO downVo = new PieStateVO();
        downVo.setName("离线");
        downVo.setValue(new BigDecimal(down));
        downVo.setRatio(sum == 0 ? BigDecimal.ZERO : new BigDecimal(down * 100F / sum));

        ArrayList<PieStateVO> vo = new ArrayList<>(2);
        vo.add(upVo);
        vo.add(downVo);
        return vo;
    }

    @Override
    public List<ConSheBei> listAllCamera() {
        return baseMapper.ListCamera(scadaConfigProperties.getCameraTypeList());
    }

    @Override
    public List<ConSheBei> listAllPublish() {
        return baseMapper.ListCamera(scadaConfigProperties.getPublishTypeList());
    }

    @Override
    public LineStateVO stateCountDecices(String subSystems) {
        List<StateByDeviceTypeDeviceBO> list = baseMapper.statByDevices(subSystems);
        Map<String, BigDecimal> dataMap = list.stream().collect(Collectors.toMap(StateByDeviceTypeDeviceBO::getDeviceTypeId, c -> new BigDecimal(c.getCount())));
        List<DeviceTypeIdNameBO> names = deviceTypeManagementService.listNameByIds(dataMap.keySet());
        LineStateVO vo = new LineStateVO();
        vo.setLegend(Collections.singletonList("设备类型"));
        List<String> x = new ArrayList<>(names.size());
        List<BigDecimal> date = new ArrayList<>(names.size());
        for (DeviceTypeIdNameBO name : names) {
            x.add(name.getName());
            date.add(dataMap.get(name.getId()));
        }
        vo.setXAxis(x);
        vo.setSeries(Collections.singletonList(date));
        return vo;
    }
}
