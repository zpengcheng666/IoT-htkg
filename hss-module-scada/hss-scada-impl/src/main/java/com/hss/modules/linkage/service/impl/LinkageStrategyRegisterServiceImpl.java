package com.hss.modules.linkage.service.impl;

import com.hss.modules.linkage.constant.LinkageConstant;
import com.hss.modules.linkage.entity.LinkageStrategy;
import com.hss.modules.linkage.service.LinkageStrategyRegisterService;
import com.hss.modules.util.ExpressionUtil;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.RedisUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hd
 */
@Service
public class LinkageStrategyRegisterServiceImpl implements LinkageStrategyRegisterService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ExpressionUtil expressionUtil;

    @Override
    public void register(LinkageStrategy strategy) {
        Set<String> attrIds = getAttrIds(strategy.getExpression());
        registerTransactionAfter(attrIds, strategy.getId());
    }

    private void registerTransactionAfter(Set<String> attrIds, String id) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    register(attrIds,id);
                }
            });
        }
        else {
            register(attrIds,id);
        }
    }


    /**
     * 注册
     * @param attrIds 属性集合
     * @param id 策略id
     */
    private void register(Set<String> attrIds, String id) {
        if (!attrIds.isEmpty()){
            for (String attrId : attrIds) {
                redisUtil.sSet(getKey(attrId), id);
            }
        }
    }


    private void registerCancelTransactionAfter(Set<String> attrIds, String id) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    registerCancel(attrIds, id);
                }
            });
        }
        else {
            registerCancel(attrIds, id);
        }
    }


    @Override
    public void registerCancel(Set<String> attrIds, String id) {
        if (!attrIds.isEmpty()){
            for (String attrId : attrIds) {
                redisUtil.setRemove(getKey(attrId), id);
            }
        }
    }

    @NotNull
    private String getKey(String attrId) {
        return LinkageConstant.REDIS_KEY_REGISTER + attrId;
    }

    @Override
    public void registerCancel(LinkageStrategy strategy) {
        Set<String> attrIds = getAttrIds(strategy.getExpression());
        registerCancelTransactionAfter(attrIds, strategy.getId());

    }

    @Override
    public void registerUpdate(LinkageStrategy oldStrategy, LinkageStrategy newStrategy) {
        if (!LinkageConstant.TYPE_CONDITION.equals(oldStrategy.getType()) && !LinkageConstant.TYPE_CONDITION.equals(newStrategy.getType())){
            return;
        }
        Set<String> oldAttrIds = null;
        if (LinkageConstant.TYPE_CONDITION.equals(oldStrategy.getType()) && LinkageConstant.IS_ENABLE.equals(oldStrategy.getIsEnable())){
            oldAttrIds = getAttrIds(oldStrategy.getExpression());
        }
        Set<String> newAttrIdList = null;
        if (LinkageConstant.TYPE_CONDITION.equals(newStrategy.getType()) && LinkageConstant.IS_ENABLE.equals(newStrategy.getIsEnable())){
            newAttrIdList = getAttrIds(newStrategy.getExpression());
        }
//        if (oldAttrIds != null && newAttrIdList != null){
//            Iterator<String> iterator = newAttrIdList.iterator();
//            while (iterator.hasNext()) {
//                String exist = iterator.next();
//                oldAttrIds.remove(exist);
//                iterator.remove();
//            }
//        }
        if (oldAttrIds != null){
            registerCancelTransactionAfter(oldAttrIds, oldStrategy.getId());
        }
        if (newAttrIdList != null){
            registerTransactionAfter(newAttrIdList, newStrategy.getId());
        }
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
     * @param expression 表达式
     * @return 属性id集合
     */
    @NotNull
    private Set<String> getAttrIds(String expression) {
        if (StringUtils.isBlank(expression)){
            return Collections.emptySet();
        }
        return expressionUtil.listValueId(expression);
    }
}
