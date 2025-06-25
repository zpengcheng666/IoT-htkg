package com.hss.modules.alarm.service.impl;

import com.hss.modules.alarm.constant.AlarmConstant;
import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.service.AlarmStrategyRegisterService;
import com.hss.modules.util.ExpressionUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @description: 将报警关联的变量注册进redis
* @author zpc
* @date 2024/3/20 14:54
* @version 1.0
*/
@Service
@Slf4j
public class AlarmStrategyRegisterServiceImpl implements AlarmStrategyRegisterService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ExpressionUtil expressionUtil;

    @Override
    public void register(AlarmStrategy strategy) {
        if (!AlarmConstant.IS_ENABLE.equals(strategy.getIsEnable())){
            return;
        }
        Set<String> attrIds = getAttrIds(strategy);
        registerTransactionAfter(attrIds, strategy.getId());
    }
    private void registerTransactionAfter(Set<String> attrIds, String strategyId) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    register(attrIds, strategyId);
                }
            });
        }
        else {
            register(attrIds, strategyId);
        }
    }


    /**
     * 注册
     * @param attrIds 属性集合
     * @param strategyId 策略id
     */
    private void register(Set<String> attrIds, String strategyId) {
        if (!attrIds.isEmpty()){
            for (String attrId : attrIds) {
                redisUtil.sSet(getKey(attrId), strategyId);
            }
        }
    }

    private void registerCancelTransactionAfter(Set<String> attrIds, String strategyId) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    registerCancel(attrIds, strategyId);
                }
            });
        }
        else {
            registerCancel(attrIds, strategyId);
        }
    }

    @Override
    public void registerCancel(Set<String> attrIds, String strategyId) {
        if (!attrIds.isEmpty()){
            for (String attrId : attrIds) {
                redisUtil.setRemove(getKey(attrId), strategyId);
            }
        }
    }

    @NotNull
    private String getKey(String attrId) {
        return AlarmConstant.REDIS_KEY_REGISTER + attrId;
    }

    @Override
    public void registerCancel(AlarmStrategy strategy) {
        Set<String> attrIds = getAttrIds(strategy);
        registerCancelTransactionAfter(attrIds, strategy.getId());
    }

    @Override
    public void registerUpdate(AlarmStrategy oldStrategy, AlarmStrategy newStrategy) {
        Set<String> oldAttrIds = getAttrIds(oldStrategy);
        Set<String> newAttrIdList = getAttrIds(newStrategy);
        Iterator<String> iterator = newAttrIdList.iterator();
        while (iterator.hasNext()) {
            String exist = iterator.next();
            oldAttrIds.remove(exist);
            iterator.remove();
        }
        registerTransactionAfter(newAttrIdList, newStrategy.getId());
        registerCancelTransactionAfter(oldAttrIds, oldStrategy.getId());
    }

    @Override
    public Set<String> getStrategyByAttrId(String attrId) {
        Set<Object> objects = redisUtil.sGet(getKey(attrId));
        if (objects == null){
            return null;
        }
        return objects.stream().map(o -> (String) o).collect(Collectors.toSet());
    }

    /**
     * 获取属性id
     * @param strategy 策略
     * @return 属性id集合
     */
    @NotNull
    private Set<String> getAttrIds(AlarmStrategy strategy) {
        Set<String> attrIds = expressionUtil.listValueId(strategy.getExpression());
        attrIds.addAll(expressionUtil.listValueId(strategy.getClearExpression()));
        return attrIds;
    }
}
