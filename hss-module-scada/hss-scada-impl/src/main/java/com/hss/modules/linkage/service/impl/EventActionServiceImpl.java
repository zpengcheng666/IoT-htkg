package com.hss.modules.linkage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.linkage.constant.LinkageConstant;
import com.hss.modules.linkage.entity.EventAction;
import com.hss.modules.linkage.entity.LinkageStrategy;
import com.hss.modules.linkage.mapper.EventActionMapper;
import com.hss.modules.linkage.service.IEventActionService;
import com.hss.modules.linkage.service.ILinkageStrategyService;
import com.hss.modules.util.CheckCyclicDepUtil;
import com.hss.modules.util.ExpressionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 事件动作
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Service
public class EventActionServiceImpl extends ServiceImpl<EventActionMapper, EventAction> implements IEventActionService {
    @Autowired
    private ILinkageStrategyService linkageStrategyService;
    @Autowired
    private ExpressionUtil expressionUtil;
    @Autowired
    private CheckCyclicDepUtil checkCyclicDepUtil;

    @Override
    public void add(EventAction eventAction) {
        check(eventAction);
        save(eventAction);
    }

    @Override
    public boolean updateById(EventAction entity) {
        check(entity);
        return super.updateById(entity);
    }

    /**
     * 检查事件操作是否引入了循环依赖。
     *
     * @param eventAction 事件操作对象，包含事件ID和操作ID。
     *                    通过事件ID，检索关联的策略列表，进而分析这些策略中是否包含了循环依赖。
     */
    private void check(EventAction eventAction) {
        // 根据事件ID查询所有的关联策略
        List<LinkageStrategy> list = linkageStrategyService.listByEventId(eventAction.getEventId());
        if (!list.isEmpty()) {
            // 使用HashSet存储需要检查的属性ID，以去重
            Set<String> ins = new HashSet<>();
            for (LinkageStrategy linkageStrategy : list) {
                // 只关注类型为条件的关联策略
                if (LinkageConstant.TYPE_CONDITION.equals(linkageStrategy.getType())){
                    // 解析条件表达式，获取所有涉及的属性ID
                    Set<String> attrIds = expressionUtil.listValueId(linkageStrategy.getExpression());
                    if (!attrIds.isEmpty()) {
                        // 将属性ID添加到集合中
                        ins.addAll(attrIds);
                    }
                }
            }
            // 检查是否存在循环依赖
            boolean check = checkCyclicDepUtil.check(ins, Collections.singleton(eventAction.getOperationId()));
            if (check) {
                // 如果存在循环依赖，则抛出异常
                throw new HssBootException("存在循环依赖,请先检查后再操作");
            }
        }
    }


    @Override
    public void deleteByEventId(String eventId) {
        baseMapper.deleteByEventId(eventId);
    }

    @Override
    public List<EventAction> listActionByEventId(String eventId) {
        return baseMapper.listActionByEventId(eventId);
    }
}
