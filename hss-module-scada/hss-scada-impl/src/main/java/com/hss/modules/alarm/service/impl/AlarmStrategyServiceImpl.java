package com.hss.modules.alarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.alarm.constant.AlarmConstant;
import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.hander.AlarmStrategyHandlerService;
import com.hss.modules.alarm.mapper.AlarmStrategyMapper;
import com.hss.modules.alarm.service.AlarmStrategyRegisterService;
import com.hss.modules.alarm.service.IAlarmStrategyService;
import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import com.hss.modules.devicetype.service.IDeviceTypeAlarmStrategyService;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.model.StrategyEnable;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.util.CheckCyclicDepUtil;
import com.hss.modules.util.ExpressionUtil;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
* @description: 报警策略
* @author zpc
* @date 2024/3/20 14:56
* @version 1.0
*/
@Service
public class AlarmStrategyServiceImpl extends ServiceImpl<AlarmStrategyMapper, AlarmStrategy> implements IAlarmStrategyService {

    @Autowired
    private AlarmStrategyRegisterService alarmStrategyRegisterService;
    @Autowired
    private AlarmStrategyHandlerService alarmStrategyHandlerService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IAlarmStrategyService alarmStrategyService;
    @Autowired
    private IDeviceTypeAlarmStrategyService deviceTypeAlarmStrategyService;
    @Autowired
    private IConSheBeiService conSheBeiService;
    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;
    @Autowired
    private CheckCyclicDepUtil checkCyclicDepUtil;
    @Autowired
    private ExpressionUtil expressionUtil;


    /**
     * 通过ID获取报警策略对象。
     * 这个方法覆盖了基类的getById方法，并且使用了@Cacheable注解来实现缓存。
     * 当调用这个方法时，如果缓存中存在对应的报警策略对象，则直接从缓存中返回，否则从数据库中查询，
     * 并将查询结果放入缓存中，以提高后续查询的效率。
     *
     * @param id 报警策略的序列化ID，类型为Serializable，是对象的唯一标识。
     * @return 返回查询到的AlarmStrategy对象，如果不存在，则返回null。
     */
    @Override
    @Cacheable(AlarmConstant.REDIS_KEY_ALARM_STRATEGY)
    public AlarmStrategy getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 启用或禁用指定的报警策略。
     * @param strategyEnable 包含策略ID和启用状态的对象，用于指定要修改的报警策略及其启用状态。
     */
    @Override
    public void enable(StrategyEnable strategyEnable) {
        // 根据策略ID获取对应的报警策略
        AlarmStrategy byId = getById(strategyEnable.getId());
        if (byId == null || byId.getIsEnable().equals(strategyEnable.getId())){
            return;
        }
        // 更新报警策略的启用状态
        byId.setIsEnable(strategyEnable.getEnable());
        // 根据更新后的对象更新数据库中的记录
        updateById(byId);
        LogUtil.setOperate(byId.getName() + ":" + byId.getIsEnable());

    }


    /**
     * 更新注册
     * @param id
     */
    private void updateRegister(String id) {
        AlarmStrategy byId = alarmStrategyService.getById(id);
        if (AlarmConstant.IS_ENABLE.equals(byId.getIsEnable())){
            alarmStrategyRegisterService.register(byId);
        }else {
            alarmStrategyRegisterService.registerCancel(byId);
        }
    }

    @Override
    public boolean updateById(AlarmStrategy entity) {
        checkCyclicDep(entity);
        boolean b = super.updateById(entity);
        redisUtil.del(AlarmConstant.REDIS_KEY_ALARM_STRATEGY_UTIL + entity.getId());
        updateRegister(entity.getId());
        alarmStrategyHandlerService.process(alarmStrategyService.getById(entity.getId()));
        return b;
    }

    @Override
    public boolean removeById(Serializable id) {
        alarmStrategyRegisterService.registerCancel(alarmStrategyService.getById(id));
        boolean b = super.removeById(id);
        redisUtil.del(AlarmConstant.REDIS_KEY_ALARM_STRATEGY_UTIL + id);
        return b;
    }

    @Override
    public boolean saveBatch(Collection<AlarmStrategy> entityList) {
        for (AlarmStrategy alarmStrategy : entityList) {
            add(alarmStrategy);
        }
        return true;
    }

    @Override
    public void add(AlarmStrategy alarmStrategy) {
        checkCyclicDep(alarmStrategy);

        save(alarmStrategy);
        redisUtil.del(AlarmConstant.REDIS_KEY_ALARM_STRATEGY_UTIL + alarmStrategy.getId());
        alarmStrategyRegisterService.register(alarmStrategy);
        LogUtil.setOperate(alarmStrategy.getName());
    }

    /**
     * 校验循环依赖。
     * 该方法用于检查给定的报警策略中是否存在循环依赖关系。循环依赖指的是在报警策略的表达式中使用了该策略自身状态变量的情况。
     * 如果存在循环依赖，则抛出异常。
     *
     * @param alarmStrategy 报警策略对象，包含需要校验的表达式和清除表达式，以及状态变量ID。
     */
    private void checkCyclicDep(AlarmStrategy alarmStrategy) {
        // 初始化用于存储表达式中所有属性ID的集合
        Set<String> ins = new HashSet<>();
        // 获取并添加报警表达式中的属性ID到集合中
        Set<String> expressionAttrIds = expressionUtil.listValueId(alarmStrategy.getExpression());
        if (!expressionAttrIds.isEmpty()) {
            ins.addAll(expressionAttrIds);
        }
        // 获取并添加清除表达式中的属性ID到集合中
        Set<String> clearExpressionAttrIds = expressionUtil.listValueId(alarmStrategy.getClearExpression());
        if (!clearExpressionAttrIds.isEmpty()) {
            ins.addAll(clearExpressionAttrIds);
        }
        // 如果集合非空，则检查是否存在循环依赖
        if (!ins.isEmpty()) {
            // 调用工具类方法检查是否存在循环依赖
            boolean check = checkCyclicDepUtil.check(ins, Collections.singleton(alarmStrategy.getStatusVarId()));
            if (check) {
                throw new HssBootException("存在循环依赖，请先检查后在操作");
            }
        }
    }


    @Override
    public void deleteByDeviceId(String deviceId) {
        List<AlarmStrategy> list = baseMapper.listByDeviceId(deviceId);
        baseMapper.deleteByDeviceId(deviceId);
        for (AlarmStrategy alarmStrategy : list) {
            redisUtil.del(AlarmConstant.REDIS_KEY_ALARM_STRATEGY_UTIL + alarmStrategy.getId());
            alarmStrategyRegisterService.registerCancel(alarmStrategy);
        }
    }


    @Override
    public void checkAndRunStrategy(String attrId) {
        Set<String> ids = alarmStrategyRegisterService.getStrategyByAttrId(attrId);
        if (ids.size() > 0){
            for (String id : ids) {
                AlarmStrategy byId = alarmStrategyService.getById(id);
                alarmStrategyHandlerService.process(byId);
            }
        }
    }

    @Override
    public Set<String> getOutputByAttrId(String attrId){
        HashSet<String> outputAttrIds = new HashSet<>();
        Set<String> strategyIds = alarmStrategyRegisterService.getStrategyByAttrId(attrId);
        if (strategyIds != null){
            for (String strategyId : strategyIds) {
                AlarmStrategy byId = alarmStrategyService.getById(strategyId);
                outputAttrIds.add(byId.getStatusVarId());
            }
        }
        return outputAttrIds;
    }

    @Override
    public List<AlarmStrategy> listByTypeStrategyId(String typeId) {
        return baseMapper.listByTypeStrategyId(typeId);
    }

    /**
     * 根据设备同步报警策略。
     * 对于给定的设备，该方法将设备类型报警策略与设备已有的报警策略进行同步，
     * 包括创建新的报警策略和更新现有的报警策略。
     *
     * @param device 传入的设备对象，用于获取设备相关的信息，包括设备ID和设备类型ID。
     */
    @Override
    public void syncByDevice(ConSheBei device) {
        // 获取设备类型ID
        String deviceTypeId = device.getDeviceTypeId();
        // 根据设备类型ID查询相应的报警策略类型列表
        List<DeviceTypeAlarmStrategy> types = deviceTypeAlarmStrategyService.listByTypeId(deviceTypeId);
        // 根据设备ID查询已有的报警策略列表
        List<AlarmStrategy> list = baseMapper.listByDeviceId(device.getId());
        // 如果查询到的报警策略类型和已有的报警策略都为空，则直接返回
        if (types.isEmpty() && list.isEmpty()){
            return;
        }
        // 将已有的报警策略列表转换为策略ID到策略对象的映射
        Map<String, AlarmStrategy> map = list.stream().collect(Collectors.toMap(AlarmStrategy::getStrategyId, o -> o));

        // 查询设备属性，并将其转换为属性ID到属性对象ID的映射
        Map<String, String> attrTypeAttrIdMap = conDeviceAttributeService.listByDeviceId(device.getId())
                .stream()
                .collect(Collectors.toMap(ConDeviceAttribute::getAttrId, ConDeviceAttribute::getId));

        // 遍历报警策略类型列表
        for (DeviceTypeAlarmStrategy type : types) {
            // 尝试从映射中获取对应的报警策略
            AlarmStrategy alarmStrategy = map.get(type.getId());
            // 如果报警策略不存在，则创建新的报警策略并添加
            if (alarmStrategy == null) {
                AlarmStrategy alarmStrategy1 = conSheBeiService.alarmStrategyType2strategy(type, attrTypeAttrIdMap, device.getId());
                add(alarmStrategy1);
            } else {
                // 如果报警策略已存在，则更新报警策略
                map.remove(type.getId());
                AlarmStrategy alarmStrategy1 = conSheBeiService.alarmStrategyType2strategy(type, attrTypeAttrIdMap, device.getId());
                alarmStrategy1.setId(alarmStrategy.getId());
                alarmStrategyService.updateById(alarmStrategy1);
            }
        }
        // 删除映射中剩余的报警策略，这些策略在最新的设备类型报警策略中不存在
        if (!map.isEmpty()){
            for (AlarmStrategy value : map.values()) {
                alarmStrategyService.removeById(value.getId());
            }
        }
    }

    @Override
    public Set<String> listAttrIdsByAttrIds(Collection<String> attrIds) {
        Set<String> outs = new HashSet<>();
        for (String attrId : attrIds) {
            List<String> list = baseMapper.listOutAttrIdByAttrId(attrId);
            if (!list.isEmpty()) {
                outs.addAll(list);
            }
        }
        return outs;
    }
}
