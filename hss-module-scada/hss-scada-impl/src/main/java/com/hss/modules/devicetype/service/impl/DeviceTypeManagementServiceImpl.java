package com.hss.modules.devicetype.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.devicetype.entity.*;
import com.hss.modules.devicetype.event.DeviceTypeNameChangeEvent;
import com.hss.modules.devicetype.mapper.DeviceTypeManagementMapper;
import com.hss.modules.devicetype.model.DeviceTypeExcel;
import com.hss.modules.devicetype.service.*;
import com.hss.modules.scada.model.DeviceTypeIdNameBO;
import com.hss.modules.scada.model.DeviceTypeStrategyList;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.system.monitorThing.StringUtils;
import com.hss.modules.util.ExpressionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
* @description: 设备类型管理
* @author zpc
* @date 2024/3/20 14:58
* @version 1.0
*/
@Service
public class DeviceTypeManagementServiceImpl extends ServiceImpl<DeviceTypeManagementMapper, DeviceTypeManagement> implements IDeviceTypeManagementService {

    @Autowired
    private IDeviceTypeAlarmStrategyService alarmStrategyService;

    @Autowired
    private IDeviceTypeStoreStrategyService storeStrategyService;

    @Autowired
    private IDeviceTypeManagementStateService stateService;

    @Autowired
    private IDeviceTypeAttributeService attributeService;
    @Autowired
    private ExpressionUtil expressionUtil;
    @Autowired
    private IConSheBeiService conSheBeiService;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public List<DeviceTypeStrategyList> listStrategy(String typeId) {
        //根据类型id查询设备类型报警策略
        List<DeviceTypeStrategyList> alarmList = alarmStrategyService.listCommonByTypeId(typeId);
        //根据类型id查询设备类型存储策略
        List<DeviceTypeStrategyList> storeList = storeStrategyService.listCommonByTypeId(typeId);
        alarmList.addAll(storeList);
        return alarmList;
    }

    @Override
    public IPage<DeviceTypeManagement> getPage(Page<DeviceTypeManagement> page, String name) {
        IPage<DeviceTypeManagement> result = baseMapper.getPage( page, name);
        for (DeviceTypeManagement record : result.getRecords()) {
            List<DeviceTypeManagementState> states = stateService.listByTypeId(record.getId());
            record.setStateNames(states.stream().map(DeviceTypeManagementState::getStateName).collect(Collectors.toList()));
        }
        return result;
    }


    @Override
    public void add(DeviceTypeManagement deviceTypeManagement) {
        DeviceTypeManagement byType = getByType(deviceTypeManagement.getType());
        if (byType != null) {
            throw new HssBootException("设备类型已经存在");
        }
        save(deviceTypeManagement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(DeviceTypeManagement deviceTypeManagement) {
        DeviceTypeManagement byId = getById(deviceTypeManagement.getId());
        if (byId == null) {
            throw new HssBootException("设备类型不存在");
        }
        DeviceTypeManagement byType = getByType(deviceTypeManagement.getType());
        if (byType != null && !byType.getId().equals(byId.getId()))  {
            throw new HssBootException("设备类型重复");
        }
        updateById(deviceTypeManagement);
        if (!byId.getName().equals(deviceTypeManagement.getName())) {
            publisher.publishEvent(new DeviceTypeNameChangeEvent(deviceTypeManagement));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        //判断设备类型是否存在
        boolean deviceExist = conSheBeiService.existByDeviceTypeId(id);
        if (deviceExist) {
            throw new HssBootException("设备正在使用,请先删除设备");
        }
        //根据typeId删除设备类型报警策略
        alarmStrategyService.deleteByTypeId(id);
        //根据类型删除设备类型存储策略
        storeStrategyService.deleteByTypeId(id);
        //根据typeId删除设备类型管理状态
        stateService.deleteByTypeId(id);
        //根据typeId删除设备类型管理属性
        attributeService.deleteByTypeId(id);
        removeById(id);
    }

    @Override
    public DeviceTypeManagement getByType(String type) {

        return baseMapper.getByType(type);
    }

    @Override
    public void copy(DeviceTypeManagement deviceTypeManagement) {
        DeviceTypeManagement byId = getById(deviceTypeManagement.getId());
        if (byId == null) {
            throw new HssBootException("复制的设备不存在");
        }
        DeviceTypeManagement byType = getByType(deviceTypeManagement.getType());
        if (byType != null) {
            throw new HssBootException("设备类型已经存在");
        }
        deviceTypeManagement.setId(null);
        save(deviceTypeManagement);
        //根据类型id查询设备属性
        List<DeviceTypeAttribute> attrList = attributeService.listByDeviceTypeId(byId.getId());
        Map<String, String> oldIdNewIdMap = null;
        if (!attrList.isEmpty()){
            Map<String, String> oldEnNameIdMap = attrList.stream().collect(Collectors.toMap(DeviceTypeAttribute::getCategory, DeviceTypeAttribute::getId));
            for (DeviceTypeAttribute deviceTypeAttribute : attrList) {
                deviceTypeAttribute.setId(null);
                deviceTypeAttribute.setTypeId(deviceTypeManagement.getId());
            }
            attributeService.saveBatch(attrList);
            oldIdNewIdMap = attrList.stream().collect(Collectors.toMap(a -> oldEnNameIdMap.get(a.getCategory()), DeviceTypeAttribute::getId));
        }
        //根据类型id查询设备类型管理状态
        List<DeviceTypeManagementState> states = stateService.listByTypeId(byId.getId());
        if (!states.isEmpty()) {
            for (DeviceTypeManagementState state : states) {
                state.setId(null);
                state.setTypeId(deviceTypeManagement.getId());
            }
            stateService.saveBatch(states);
        }
        if (oldIdNewIdMap == null) {
            return;
        }

        //根据typeId查询设备类型报警策略
        List<DeviceTypeAlarmStrategy> alarmStrategyList = alarmStrategyService.listByTypeId(byId.getId());
        if (!alarmStrategyList.isEmpty()){
            for (DeviceTypeAlarmStrategy alarmStrategy : alarmStrategyList) {
                alarmStrategy.setId(null);
                alarmStrategy.setTypeId(deviceTypeManagement.getId());

                alarmStrategy.setStatusVarId(oldIdNewIdMap.get(alarmStrategy.getStatusVarId()));
                alarmStrategy.setExpression(expressionUtil.getExpressionOriginalStr(oldIdNewIdMap, alarmStrategy.getExpression()));
                alarmStrategy.setClearExpression(expressionUtil.getExpressionOriginalStr(oldIdNewIdMap, alarmStrategy.getClearExpression()));
                alarmStrategy.setRange(expressionUtil.getExpressionOriginalStr(oldIdNewIdMap, alarmStrategy.getRange()));
                alarmStrategy.setOriginVarId(oldIdNewIdMap.get(alarmStrategy.getOriginVarId()));
                alarmStrategy.setValueExpression(expressionUtil.getExpressionOriginalStr(oldIdNewIdMap, alarmStrategy.getValueExpression()));
            }
            alarmStrategyService.saveBatch(alarmStrategyList);
        }

        List<DeviceTypeStoreStrategy> storeStrategies = storeStrategyService.listByTypeId(byId.getId());
        if (!storeStrategies.isEmpty()){
            for (DeviceTypeStoreStrategy storeStrategy : storeStrategies) {
                storeStrategy.setId(null);
                storeStrategy.setTypeId(deviceTypeManagement.getId());
                storeStrategy.setVariableId(oldIdNewIdMap.get(storeStrategy.getVariableId()));
            }
            storeStrategyService.saveBatch(storeStrategies);

        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addByJsonFile(String dirName) {

        FileReader fileReader = new FileReader(dirName + "/" + "config.json");
        String s = fileReader.readString();
        JSONObject jsonObject = JSONObject.parseObject(s);
        DeviceTypeManagement device = new DeviceTypeManagement();
        device.setTags(jsonObject.getJSONArray("tags").stream().map(a->(String)a).collect(Collectors.joining(",")));
        device.setName(jsonObject.getString("name"));
        device.setDefaultState(jsonObject.getString("defaultState"));
        device.setType(jsonObject.getString("type"));
        device.setIsCameraLinked(jsonObject.getInteger("isCameraLinked"));
        device.setIsShowDataTable(jsonObject.getInteger("isShowDataTable"));
        device.setIsShowInScene(jsonObject.getInteger("isShowInScene"));
        save(device);

        List<DeviceTypeManagementState> stateList = jsonObject.getJSONArray("states").stream().map(state -> {
            JSONObject st = (JSONObject) state;
            DeviceTypeManagementState deviceTypeManagementState = new DeviceTypeManagementState();
            deviceTypeManagementState.setTypeId(device.getId());
            deviceTypeManagementState.setStateName(st.getString("name"));
            deviceTypeManagementState.setStateEnName(st.getString("enName"));
            return deviceTypeManagementState;
        }).collect(Collectors.toList());
        stateService.saveBatch(stateList);


        FileReader attrReader = new FileReader(dirName + "/" + "attributes.json");
        String attrJson = attrReader.readString();
        JSONObject attrJsonObject = JSONObject.parseObject(attrJson);
        List<DeviceTypeAttribute> collect = attrJsonObject.entrySet().stream().map(en -> {
            JSONObject value = (JSONObject) en.getValue();
            DeviceTypeAttribute attribute = new DeviceTypeAttribute();
            attribute.setTypeId(device.getId());
            attribute.setName(value.getString("name"));
            attribute.setCategory(value.getString("category"));
            attribute.setDataType(value.getString("type"));
            attribute.setInitValue(value.getString("initValue"));
            attribute.setMinValue(value.getString("minValue"));
            attribute.setMaxValue(value.getString("maxValue"));
            attribute.setIsAct(value.getInteger("isAct"));
            attribute.setIsAssociate(value.getInteger("isAssociate"));
            attribute.setIsConfigurable(value.getInteger("isConfigurable"));
            attribute.setIsSave(value.getInteger("isSave"));
            attribute.setValueMap(value.getJSONObject("valueMap").toJSONString());
            attribute.setActOrders(value.getJSONArray("actOrders") != null ? value.getJSONArray("actOrders").toJSONString() : null);
            attribute.setConfigOptions(value.getJSONArray("configOptions") != null ? value.getJSONArray("configOptions").toJSONString() : null);
            attribute.setDisplayAreas(value.getJSONArray("displayAreas").stream().map(a->(String)a).collect(Collectors.joining(",")));
            return attribute;

        }).collect(Collectors.toList());
        attributeService.saveBatch(collect);
        addByJsonFileStrategy(dirName, device.getId());

    }

    @Override
    public void addByJsonFileStrategy(String dirName, String typeId) {

        List<DeviceTypeAttribute> deviceTypeAttributes = attributeService.listByDeviceTypeId(typeId);
        Map<String, String> attrEnNameMap = deviceTypeAttributes.stream().collect(Collectors.toMap(DeviceTypeAttribute::getCategory, DeviceTypeAttribute::getId));
        String filePath = dirName + "/" + "strategies.json";
        if (!FileUtil.exist(filePath)) {
            return;
        }
        FileReader attrReader = new FileReader(filePath);
        String jsonStr = attrReader.readString();
        JSONObject attrJsonObject = JSONObject.parseObject(jsonStr);

        addAlarmStrategyByJson(typeId, attrEnNameMap, attrJsonObject);

        addStoreStrategyByJson(typeId, attrEnNameMap, attrJsonObject);


    }

    private void addStoreStrategyByJson(String typeId, Map<String, String> attrEnNameMap, JSONObject attrJsonObject) {
        JSONArray storageStrategyJson = attrJsonObject.getJSONArray("storageStrategy");
        if (storageStrategyJson == null){
            return;
        }
        List<DeviceTypeStoreStrategy> collect = storageStrategyJson
                .stream()
                .map(o -> (JSONObject) o)
                .map(jo -> {
                    DeviceTypeStoreStrategy strategy = new DeviceTypeStoreStrategy();
                    strategy.setName(jo.getString("name"));
                    strategy.setType(jo.getString("type"));
                    strategy.setUnit(jo.getString("unit"));
                    strategy.setUnit(jo.getString("unit"));
                    strategy.setPeriod(jo.getString("period"));
                    strategy.setExpression(jo.getString("expression"));
                    strategy.setVariableId(attrEnNameMap.get(jo.getString("variableId")));
                    strategy.setIsEnable(jo.getString("isEnable"));
                    strategy.setDescription(jo.getString("description"));
                    strategy.setDeleted(0);
                    strategy.setTypeId(typeId);
                    return strategy;
                }).collect(Collectors.toList());
        if (!collect.isEmpty()){
            storeStrategyService.saveBatch(collect);
        }
    }

    private void addAlarmStrategyByJson(String typeId, Map<String, String> attrEnNameMap, JSONObject attrJsonObject) {
        JSONArray alarmStrategy = attrJsonObject.getJSONArray("alarmStrategy");
        if (alarmStrategy == null){
            return;
        }
        List<DeviceTypeAlarmStrategy> list = alarmStrategy
                .stream()
                .map(o -> (JSONObject) o)
                .map(jo -> {
                    DeviceTypeAlarmStrategy strategy = new DeviceTypeAlarmStrategy();
                    strategy.setName(jo.getString("name"));
                    strategy.setType(jo.getString("type"));
                    strategy.setLevelId(jo.getString("levelId"));
                    strategy.setStatusVarId(attrEnNameMap.get(jo.getString("statusVarId")));
                    strategy.setExpression(expressionUtil.getExpressionStr(attrEnNameMap, jo.getString("expression")));
                    strategy.setClearExpression(expressionUtil.getExpressionStr(attrEnNameMap, jo.getString("clearExpression")));
                    strategy.setRange(expressionUtil.getExpressionStr(attrEnNameMap, jo.getString("range")));
                    strategy.setOriginVarId(attrEnNameMap.get(jo.getString("originVarId")));
                    strategy.setStoreCondition(jo.getString("storeCondition"));
                    strategy.setAlarmPushCondition(jo.getString("alarmPushCondition"));
                    strategy.setIsEnable(jo.getString("isEnable"));
                    strategy.setValueExpression(expressionUtil.getExpressionStr(attrEnNameMap, jo.getString("valueExpression")));
                    strategy.setDescription(jo.getString("description"));
                    strategy.setDelayBegin(0);
                    strategy.setDelayRemove(0);
                    strategy.setTypeId(typeId);
                    strategy.setDeleted(0);
                    return strategy;
                }).collect(Collectors.toList());
        if (!list.isEmpty()){
            alarmStrategyService.saveBatch(list);
        }
    }

    @Override
    public void syncAttrAndStrategy() {
        List<DeviceTypeManagement> typeList = list();
        if (typeList.isEmpty()){
            return;
        }
        for (DeviceTypeManagement type : typeList) {
            syncType(type);
        }

    }

    /**
     * 同步设备名称
     * @param type
     */
    private void syncType(DeviceTypeManagement type) {
        conSheBeiService.syncByType(type);

    }

    @Override
    public List<DeviceTypeIdNameBO> listNameByIds(Collection<String> ids) {
        if (CollectionUtils.isEmpty(ids)){
            return Collections.emptyList();
        }
        return baseMapper.listNameByIds(ids);
    }

    @Override
    public List<DeviceTypeExcel> listExcel() {
        return baseMapper.listExcel();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(List<DeviceTypeExcel> list) {
        if (list.isEmpty()) {
            return;
        }
        for (DeviceTypeExcel deviceTypeExcel : list) {
            DeviceTypeManagement type = new DeviceTypeManagement();
            if (StringUtils.isBlank(deviceTypeExcel.getName()))  {
                throw new HssBootException("设备名不能为空");
            }
            type.setName(deviceTypeExcel.getName());
            type.setType(deviceTypeExcel.getType());
            add(type);
            Map<String, Integer> enNameMap = new HashMap<>();
            List<DeviceTypeAttribute> attrList = deviceTypeExcel.getAttrList().stream().map(e -> {
                Integer count = enNameMap.get(e.getCategory());
                if (count != null) {
                    throw new HssBootException("属性英文名称重复");
                }else {
                    enNameMap.put(e.getCategory(),1);
                }
                DeviceTypeAttribute a = new DeviceTypeAttribute();
                a.setName(e.getName());
                a.setCategory(e.getCategory());
                a.setDataType(e.getDataType());
                a.setInitValue(e.getInitValue());
                a.setMinValue(e.getMinValue());
                a.setMaxValue(e.getMaxValue());
                a.setUnit(e.getUnit());
                a.setSortNumber(e.getSortNumber());
                a.setDisplayAreas(e.getDisplayAreas());
                a.setIsAct(e.getIsAct());
                a.setIsAssociate(e.getIsAssociate());
                a.setIsAssociateVar(e.getIsAssociateVar());
                a.setIsConfigurable(e.getIsConfigurable());
                a.setIsSave(e.getIsSave());
                a.setTypeId(type.getId());
                a.setActOrders(e.getActOrders());
                a.setConfigOptions(e.getConfigOptions());
                a.setValueMap(e.getValueMap());
                return a;
            }).collect(Collectors.toList());
            attributeService.saveBatch(attrList);
        }


    }
}
