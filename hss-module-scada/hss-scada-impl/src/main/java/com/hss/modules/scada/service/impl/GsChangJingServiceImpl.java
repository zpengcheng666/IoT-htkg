package com.hss.modules.scada.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.service.IDeviceTypeAlarmStrategyService;
import com.hss.modules.devicetype.service.IDeviceTypeAttributeService;
import com.hss.modules.devicetype.service.IDeviceTypeStoreStrategyService;
import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.entity.GSChangJingSheBei;
import com.hss.modules.scada.entity.GsChangJing;
import com.hss.modules.scada.mapper.GsChangJingMapper;
import com.hss.modules.scada.model.*;
import com.hss.modules.scada.service.*;
import com.hss.modules.scada.service.impl.advice.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zpc
 * @version 1.0
 * @description: 场景管理增删改查，保存场景、获取场景但钱所有数据，发布、根据子系统id获取有存储策略的设备类型、
 * 查询场景名称和子系统，根据子系统id获取有报警策略的设备类型、场景排序、获取场景下的数据表格、获取环境场景下的设备信息
 * @date 2024/3/19 14:03
 */
@Service
public class GsChangJingServiceImpl extends ServiceImpl<GsChangJingMapper, GsChangJing> implements IGsChangJingService {
    @Autowired
    private IConSheBeiService conSheBeiService;
    @Autowired
    private IGSChangJingSheBeiService changJingSheBeiService;
    @Autowired
    private IDeviceTypeStoreStrategyService deviceTypeStoreStrategyService;
    @Autowired
    private IDeviceTypeAlarmStrategyService deviceTypeAlarmStrategyService;
    @Autowired
    private IDeviceTypeAttributeService deviceTypeAttributeService;
    @Autowired
    private DeviceDataService deviceDataService;
    @Autowired
    private IGatewayAttachmentService gatewayAttachmentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> saveStageData(GsChangJingSaveStageData data) {
        // 保存场景信息
        GsChangJing entity = saveScene(data);
        // 获取设备列表
        List<DwBindDeviceModel> dwBindArrModels = JSONObject.parseArray(data.getBindArr(), DwBindDeviceModel.class);
        Set<String> deviceSet = dwBindArrModels.stream()
                .map(DwBindDeviceModel::getDeviceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<DeviceSortVO> oldList = changJingSheBeiService.listSortDevice(entity.getId());

        List<String> delList = new ArrayList<>();

        int lastSort = 0;
        if (!oldList.isEmpty()) {
            Integer sortNumber = oldList.get(oldList.size() - 1).getSortNumber();
            lastSort = sortNumber == null ? 0 : sortNumber;
        }
        for (DeviceSortVO deviceSortVO : oldList) {
            String deviceId = deviceSortVO.getDeviceId();
            if (deviceSet.contains(deviceId)) {
                deviceSet.remove(deviceId);
            } else {
                delList.add(deviceSortVO.getId());
            }
        }
        if (!delList.isEmpty()) {
            changJingSheBeiService.removeByIds(delList);
        }
        if (!deviceSet.isEmpty()) {
            List<GSChangJingSheBei> addList = new ArrayList<>();
            for (String deviceId : deviceSet) {
                lastSort++;
                GSChangJingSheBei gsChangJingSheBei = new GSChangJingSheBei();
                gsChangJingSheBei.setSceneId(entity.getId());
                gsChangJingSheBei.setSourceSceneId(entity.getId());
                gsChangJingSheBei.setDeviceId(deviceId);
                gsChangJingSheBei.setSortNumber(lastSort);
                addList.add(gsChangJingSheBei);
            }
            changJingSheBeiService.saveBatch(addList);
        }
        Map<String, String> resultMap = new HashMap<>(16);
        resultMap.put("id", entity.getId());
        return resultMap;
    }

    private Map<String, List<ConDeviceAttribute>> getDeviceAttrsMapBySceneId(String sceneId) {
        List<ConDeviceAttribute> attributeList = conSheBeiService.listAttrBySceneId(sceneId);
        return attributeList
                .stream()
                .collect(Collectors.groupingBy(ConDeviceAttribute::getDeviceId));
    }

    private Map<String, DeviceTypeAttribute> getAttrTypeMapByDeviceList(List<ConSheBei> conSheBeis) {
        Set<String> typeIds = conSheBeis.stream().map(ConSheBei::getDeviceTypeId).collect(Collectors.toSet());
        if (typeIds.isEmpty()) {
            return null;
        }
        List<DeviceTypeAttribute> typeAttributes = deviceTypeAttributeService.listByDeviceTypeIds(typeIds);
        return typeAttributes.stream()
                .collect(Collectors.toMap(DeviceTypeAttribute::getId, o -> o));
    }

    @Override
    public List<DeviceModel> listCurrentDataBySceneId(String sceneId) {
        // 获取场景设备
        List<ConSheBei> deviceList = conSheBeiService.listBySceneId(sceneId);
        // 获取场景设备类型
        Map<String, DeviceTypeAttribute> attrTypeMap = getAttrTypeMapByDeviceList(deviceList);
        if (attrTypeMap == null) {
            return Collections.emptyList();
        }
        PublishSceneAttrAdvice publishSceneAttrAdvice = new PublishSceneAttrAdvice(sceneId, conSheBeiService);
        ValueMapSceneAttrAdvice valueMapSceneAttrAdvice = new ValueMapSceneAttrAdvice();
        ActSceneAttrAdvice actSceneAttrAdvice = new ActSceneAttrAdvice();
        ValueSceneAttrAdvice valueSceneAttrAdvice = new ValueSceneAttrAdvice(deviceDataService, gatewayAttachmentService);
        ConfigSceneAttrAdvice configSceneAttrAdvice = new ConfigSceneAttrAdvice();

        Map<String, List<ConDeviceAttribute>> deviceAttrsMap = getDeviceAttrsMapBySceneId(sceneId);

        return deviceList.stream().map(d -> {
            DeviceModel deviceModel = deviceToSceneDevice(d);
            List<ConDeviceAttribute> attributeList = deviceAttrsMap.get(d.getId());
            if (attributeList == null) {
                return deviceModel;
            }
            List<DeviceAttrModel> damList = attributeList.stream()
                    .filter(this::attrAssociateIsShow)
                    .map(a -> {
                        DeviceTypeAttribute t = attrTypeMap.get(a.getAttrId());
                        if (t == null) {
                            return null;
                        }
                        DeviceAttrModel m = new DeviceAttrModel();
                        m.setName(t.getName());
                        m.setEnName(t.getCategory());
                        m.setDisplayAreas(t.getDisplayAreas());
                        m.setIsAct(0);
                        m.setIsConfigurable(0);
                        m.setDataType(t.getDataType());
                        m.setUnit(t.getUnit());

                        // 广播设置增强
                        m = publishSceneAttrAdvice.adviceBase(t, a, m);
                        // valueMap增强
                        m = valueMapSceneAttrAdvice.adviceBase(t, a, m);
                        // 设置增强
                        m = actSceneAttrAdvice.adviceBase(t, a, m);
                        // 值增强
                        m = valueSceneAttrAdvice.adviceBase(t, a, m);
                        // 配置信息增强
                        m = configSceneAttrAdvice.adviceBase(t, a, m);
                        return m;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            deviceModel.setAttrs(damList);
            return deviceModel;
        }).collect(Collectors.toList());
    }

    /**
     * 判断组态是否显示
     *
     * @param a 属性信息
     * @return 显示
     */
    private boolean attrAssociateIsShow(ConDeviceAttribute a) {
        if (ScadaConstant.IS_ONE.equals(a.getIsAssociate())) {
            if (StringUtils.isBlank(a.getVariableId()) && StringUtils.isBlank(a.getExpression())) {
                return false;
            }
        }
        return true;
    }

    private DeviceModel deviceToSceneDevice(ConSheBei d) {
        DeviceModel deviceModel = new DeviceModel();
        deviceModel.setDeviceId(d.getId());
        deviceModel.setDeviceName(d.getName());
        deviceModel.setDeviceType(d.getType());
        deviceModel.setDeviceCode(d.getCode());
        deviceModel.setLocationName(d.getLocationId());
        deviceModel.setOtherName(d.getOtherName());
        return deviceModel;
    }

    @Override
    public void publish(ScenePublishModel model) {
        GsChangJing entity = getById(model.getId());
        entity.setIsPublished(model.getStatus());
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delById(String id) {
        removeById(id);
        List<String> deviceIds = changJingSheBeiService.listDeviceIdsBySceneId(id);
        changJingSheBeiService.deleteBySceneId(id);
        List<String> delDeviceIds = deviceIds.stream().filter(deviceId -> {
            List<String> sceneIds = changJingSheBeiService.listSceneIdByDeviceId(deviceId);
            return sceneIds.isEmpty();
        }).collect(Collectors.toList());
        if (!delDeviceIds.isEmpty()) {
            conSheBeiService.removeByIds(delDeviceIds);
        }

    }

    @Override
    public List<ConSheBeiDoorOptions> listStoreDeviceTypeBySubSystem(String subSystemId) {
        List<String> deviceIds = baseMapper.listDeviceIdBySubSystemId(subSystemId);
        if (deviceIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<ConSheBeiDoorOptions> conSheBeiDoorOptions = conSheBeiService.listDeviceTypeByDeviceIds(deviceIds);
        return conSheBeiDoorOptions.stream().filter(o -> deviceTypeStoreStrategyService.isStrategyByTypeId(o.getId())).collect(Collectors.toList());
    }

    @Override
    public List<ConSheBeiDoorOptions> listAlarmDeviceTypeBySubSystem(String subSystemId) {
        List<String> deviceIds = baseMapper.listDeviceIdBySubSystemId(subSystemId);
        if (deviceIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<ConSheBeiDoorOptions> conSheBeiDoorOptions = conSheBeiService.listDeviceTypeByDeviceIds(deviceIds);
        return conSheBeiDoorOptions.stream().filter(o -> deviceTypeAlarmStrategyService.isStrategyByTypeId(o.getId())).collect(Collectors.toList());
    }

    /**
     * 保存场景
     *
     * @param data
     * @return
     */
    @NotNull
    private GsChangJing saveScene(GsChangJingSaveStageData data) {
        GsChangJing entity = null;
        if (StringUtils.isNotEmpty(data.getStageId())) {
            entity = getById(data.getStageId());
        }
        if (entity == null) {
            entity = new GsChangJing();
            entity.setCreatedTime(new Date());
            entity.setIsPublished("0");
        }
        entity.setDatajson(data.getStageDatajson());
        entity.setDatakeyarray(data.getDataKeyArray());
        entity.setBase64(data.getStageBase64());
        entity.setName(data.getName());
        entity.setDescription(data.getDescription());

        /** 添加了场景所属子系统 */
        entity.setSubSystem(data.getSubsystemId());
        entity.setSubSystemName(data.getSubsystemId());

        /** 添加了场景所属子系统下的模块 */
        entity.setModuleId(data.getModuleId());
        entity.setModuleName(data.getModuleId());
        entity.setUpdatedTime(new Date());
        saveOrUpdate(entity);
        data.setStageId(entity.getId());
        return entity;
    }

    @Override
    public List<GsChangJing> listNameAndSystemByDeviceId(String deviceId) {
        return baseMapper.listNameAndSystemByDeviceId(deviceId);
    }

    @Override
    public List<DataTableVO> listDataTableBySceneId(String sceneId) {
        List<String> deviceIds = changJingSheBeiService.listDeviceIdsBySceneId(sceneId);
        if (deviceIds.isEmpty()) {
            return Collections.emptyList();
        }
        return conSheBeiService.listDataTableByIds(deviceIds);
    }

    @Override
    public List<EnvDeviceTableVO> listEnvDeviceBySceneId(String sceneId) {
        return conSheBeiService.listEnvDeviceBySceneId(sceneId);
    }

    @Override
    public List<DeviceSortVO> listSortDevice(String sceneId) {
        return changJingSheBeiService.listSortDevice(sceneId);
    }

    @Override
    public void saveDeviceSort(List<DeviceSortVO> list) {
        changJingSheBeiService.saveDeviceSort(list);
    }

    @Override
    public List<GsChangJing> listMenu(String sumSystem) {
        return baseMapper.listMenu(sumSystem);
    }
}
