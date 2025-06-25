package com.hss.modules.store.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.constant.StoryConstant;
import com.hss.modules.devicetype.entity.DeviceTypeStoreStrategy;
import com.hss.modules.devicetype.service.IDeviceTypeStoreStrategyService;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.model.DeviceAttrData;
import com.hss.modules.scada.model.StrategyEnable;
import com.hss.modules.scada.service.DeviceDataService;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.store.constant.StoreConstant;
import com.hss.modules.store.entity.StoreStrategy;
import com.hss.modules.store.mapper.StoreStrategyMapper;
import com.hss.modules.store.service.IStoreHistoryService;
import com.hss.modules.store.service.IStoreStrategyService;
import com.hss.modules.store.service.StoryStrategyTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 设备运行时数据存储策略
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
@Service
public class StoreStrategyServiceImpl extends ServiceImpl<StoreStrategyMapper, StoreStrategy> implements IStoreStrategyService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private IStoreHistoryService storeHistoryService;
    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;
    @Autowired
    private DeviceDataService deviceDataService;
    @Autowired
    private StoryStrategyTask storyStrategyTask;
    @Autowired
    private IDeviceTypeStoreStrategyService deviceTypeStoreStrategyService;
    @Autowired
    private IConSheBeiService conSheBeiService;
    @Autowired
    private IStoreStrategyService storeStrategyService;

    @Override
    public IPage<StoreStrategy> getPage(Page<StoreStrategy> page, String name, String deviceId) {
        return baseMapper.getPage(page, name, deviceId);
    }

    @Override
    public void enable(StrategyEnable strategyEnable) {
        StoreStrategy storeStrategy = new StoreStrategy();
        storeStrategy.setId(strategyEnable.getId());
        storeStrategy.setIsEnable(strategyEnable.getEnable());
        updateById(storeStrategy);
    }

    @Override
    public void add(StoreStrategy storeStrategy) {
        save(storeStrategy);
        storyStrategyTask.addTask(storeStrategy);


    }

    @Override
    public void edit(StoreStrategy storeStrategy) {
        updateById(storeStrategy);
    }

    @Override
    public boolean updateById(StoreStrategy entity) {
        StoreStrategy oldStrategy = getById(entity.getId());
        boolean b = super.updateById(entity);
        StoreStrategy newStrategy = getById(entity.getId());
        if (!StoreConstant.IS_ENABLE.equals(oldStrategy.getIsEnable()) && !StoreConstant.IS_ENABLE.equals(newStrategy.getIsEnable())){
            return b;
        }
        storyStrategyTask.updateTask(newStrategy);
        return b;
    }

    @Override
    public void delete(String id) {
        StoreStrategy byId = getById(id);
        removeById(id);
        storyStrategyTask.removeTask(byId);
    }


    @Override
    public void runStory(StoreStrategy strategy) {
        // 获取值
        DeviceAttrData attrValueByAttrId = deviceDataService.getAttrValueByAttrId(strategy.getVariableId());
        if (attrValueByAttrId == null){
            return;
        }
        ConDeviceAttribute deviceAttribute = conDeviceAttributeService.getById(strategy.getVariableId());
        if (deviceAttribute == null){
            return;
        }
        // 存储
        deviceAttribute.setUpdatedTime(new Date());
        storeHistoryService.saveByVariableId(deviceAttribute, attrValueByAttrId.getValue());
    }

    @Override
    public void deleteByDeviceId(String deviceId) {
        List<StoreStrategy> list = baseMapper.listByDeviceId(deviceId);
        baseMapper.deleteByDeviceId(deviceId);
        for (StoreStrategy storeStrategy : list) {
            if (StoreConstant.IS_ENABLE.equals(storeStrategy.getIsEnable())){
                redisTemplate.opsForZSet().remove(StoryConstant.REDIS_KEY_STORY_JOB, storeStrategy.getId());
            }
        }
    }

    @Override
    public List<StoreStrategy> listByTypeStrategyId(String typeId) {
        return baseMapper.listByTypeStrategyId(typeId);
    }

    @Override
    public List<StoreStrategy> listEnableByType(String type) {
        return baseMapper.listEnableByType(type);
    }

    @Override
    public void syncByDevice(ConSheBei device) {
        String deviceTypeId = device.getDeviceTypeId();
        List<DeviceTypeStoreStrategy> types = deviceTypeStoreStrategyService.listByTypeId(deviceTypeId);
        List<StoreStrategy> list = baseMapper.listByDeviceId(device.getId());
        if (types.isEmpty() && list.isEmpty()){
            return;
        }
        Map<String, StoreStrategy> map = list.stream().collect(Collectors.toMap(StoreStrategy::getStrategyId, o -> o));

        Map<String, String> attrTypeAttrIdMap = conDeviceAttributeService.listByDeviceId(device.getId())
                .stream()
                .collect(Collectors.toMap(ConDeviceAttribute::getAttrId, ConDeviceAttribute::getId));

        for (DeviceTypeStoreStrategy type : types) {
            StoreStrategy alarmStrategy = map.get(type.getId());
            if (alarmStrategy == null) {
                StoreStrategy alarmStrategy1 = conSheBeiService.storeStrategyType2strategy(type, attrTypeAttrIdMap, device.getId());
                add(alarmStrategy1);
            }else {
                map.remove(type.getId());
                StoreStrategy alarmStrategy1 = conSheBeiService.storeStrategyType2strategy(type, attrTypeAttrIdMap, device.getId());
                alarmStrategy1.setId(alarmStrategy.getId());
                storeStrategyService.updateById(alarmStrategy1);

            }
        }
        if (!map.isEmpty()){
            for (StoreStrategy value : map.values()) {
                storeStrategyService.removeById(value.getId());
            }
        }
    }
}
