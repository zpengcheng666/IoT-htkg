package com.hss.modules.linkage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.core.task.executor.TaskExecutor;
import com.hss.core.task.impl.SimplenessDelayTask;
import com.hss.modules.constant.StoryConstant;
import com.hss.modules.linkage.constant.LinkageConstant;
import com.hss.modules.linkage.entity.EventAction;
import com.hss.modules.linkage.entity.LinkageData;
import com.hss.modules.linkage.entity.LinkageStrategy;
import com.hss.modules.linkage.mapper.LinkageStrategyMapper;
import com.hss.modules.linkage.model.ListByAlarmStrategyIdsDTO;
import com.hss.modules.linkage.service.*;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.model.StrategyEnable;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.util.CheckCyclicDepUtil;
import com.hss.modules.util.ExpressionUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 联动策略
 * @Author: jeecg-boot
 * @Date: 2023-01-05
 * @Version: V1.0
 */
@Service
@Slf4j
public class LinkageStrategyServiceImpl extends ServiceImpl<LinkageStrategyMapper, LinkageStrategy> implements ILinkageStrategyService {

    @Autowired
    private IEventManagerService eventManagerService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ExpressionUtil expressionUtil;
    @Autowired
    private ILinkageDataService linkageDataService;
    @Autowired
    private LinkageStrategyRegisterService linkageStrategyRegisterService;
    @Autowired
    private ILinkageStrategyService linkageStrategyService;

    @Autowired
    private LinkageStrategyTask linkageStrategyTask;
    @Autowired
    private TaskExecutor sleepTaskExecutor;
    @Autowired
    private CheckCyclicDepUtil checkCyclicDepUtil;
    @Autowired
    private IConDeviceAttributeService deviceAttributeService;


    @Override
    @Cacheable(LinkageConstant.REDIS_KEY_LINKAGE_STRATEGY)
    public LinkageStrategy getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public void add(LinkageStrategy linkageStrategy) {
        checkCycleDep(linkageStrategy);
        save(linkageStrategy);
        redisUtil.del(LinkageConstant.REDIS_KEY_LINKAGE_STRATEGY_UTIL + linkageStrategy.getId());
        if (LinkageConstant.IS_ENABLE.equals(linkageStrategy.getIsEnable()) && LinkageConstant.TYPE_CONDITION.equals(linkageStrategy.getType())) {
            linkageStrategyRegisterService.register(linkageStrategy);
        }
        if (LinkageConstant.IS_ENABLE.equals(linkageStrategy.getIsEnable()) && !LinkageConstant.TYPE_CONDITION.equals(linkageStrategy.getType())) {
            linkageStrategyTask.addTask(linkageStrategy);
        }

    }

    /**
     * 校验循环依赖
     *
     * @param linkageStrategy
     */
    private void checkCycleDep(LinkageStrategy linkageStrategy) {
        if (LinkageConstant.TYPE_CONDITION.equals(linkageStrategy.getType())) {
            List<EventAction> actionList = eventManagerService.listActionByEventId(linkageStrategy.getEventId());
            if (!actionList.isEmpty()) {

                Set<String> in = expressionUtil.listValueId(linkageStrategy.getExpression());
                boolean check = checkCyclicDepUtil.check(in, actionList.stream().map(EventAction::getOperationId).collect(Collectors.toSet()));
                if (check) {
                    throw new HssBootException("存在循环依赖，请检查后操作");
                }
            }
        }
    }

    @Override
    public void enable(StrategyEnable strategyEnable) {
        LinkageStrategy byId = linkageStrategyService.getById(strategyEnable.getId());
        if (byId == null || byId.getIsEnable().equals(strategyEnable.getEnable())) {
            return;
        }
        byId.setIsEnable(strategyEnable.getEnable());
        updateById(byId);
    }

    @Override
    public IPage<LinkageStrategy> getPage(Page<LinkageStrategy> page, String name) {
        return baseMapper.getPage(page, name);
    }

    @Override
    public boolean updateById(LinkageStrategy entity) {
        checkCycleDep(entity);
        LinkageStrategy oldStrategy = linkageStrategyService.getById(entity.getId());
        boolean b = super.updateById(entity);
        redisUtil.del(LinkageConstant.REDIS_KEY_LINKAGE_STRATEGY_UTIL + entity.getId());
        LinkageStrategy newStrategy = linkageStrategyService.getById(entity.getId());
        linkageStrategyRegisterService.registerUpdate(oldStrategy, newStrategy);
        linkageStrategyTask.updateTask(newStrategy);
        return b;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        LinkageStrategy byId = linkageStrategyService.getById(id);
        removeById(id);
        redisUtil.del(LinkageConstant.REDIS_KEY_LINKAGE_STRATEGY_UTIL + id);
        if (LinkageConstant.IS_ENABLE.equals(byId.getIsEnable()) && LinkageConstant.TYPE_CONDITION.equals(byId.getType())) {
            linkageStrategyRegisterService.registerCancel(byId);
        }
        linkageStrategyTask.removeTask(byId);

    }


    /**
     * 执行联动任务。根据给定的联动策略，执行与事件相关的一系列动作。
     *
     * @param linkageStrategy 联动策略，包含需要执行的事件ID等信息。
     */
    @Override
    public void runAction(LinkageStrategy linkageStrategy) {
        // 根据事件ID获取需要执行的动作列表
        List<EventAction> actionList = eventManagerService.listActionByEventId(linkageStrategy.getEventId());
        if (actionList.isEmpty()) {
            return;
        }

        // 遍历动作列表，对每个动作决定是否需要延迟执行
        for (EventAction eventAction : actionList) {
            long delay = 0L;
            // 检查动作是否有延迟执行的时间设定
            if (StringUtils.isNotBlank(eventAction.getDelayTime())) {
                try {
                    // 尝试将延迟时间解析为长整型
                    delay = Long.parseLong(eventAction.getDelayTime());
                } catch (NumberFormatException e) {
                    // 记录解析错误日志，并抛出异常
                    log.error("解析联动动作延时错误delay={}", eventAction.getDelayTime());
                    throw new HssBootException("解析联动动作延时错误");
                }
            }
            // 如果设置了延迟时间，则使用延迟任务执行器进行延迟执行
            if (delay > 0) {
                SimplenessDelayTask simplenessDelayTask = new SimplenessDelayTask(delay, sleepTaskExecutor);
                // 设置延迟执行的任务内容
                simplenessDelayTask.setRunnable(() -> actionRun(linkageStrategy, eventAction));
                simplenessDelayTask.start();
            } else {
                // 如果没有设置延迟时间，则直接执行动作
                actionRun(linkageStrategy, eventAction);
            }
        }
    }


    /**
     * 执行动作
     *
     * @param linkageStrategy
     * @param eventAction
     */
    private void actionRun(LinkageStrategy linkageStrategy, EventAction eventAction) {
        String result = eventManagerService.action(eventAction);
        LinkageData linkageData = new LinkageData();
        linkageData.setId(IdWorker.getIdStr());
        linkageData.setLinkageStrategyId(linkageStrategy.getId());
        linkageData.setActionId(eventAction.getId());
        linkageData.setResult(result);
        linkageData.setActionTime(new Date());
        linkageDataService.save(linkageData);
    }


    /**
     * 根据源设备属性检查并执行相应的联动策略。
     *
     * @param sourceAttr 源设备属性，用于查找关联的联动策略。
     */
    @Override
    public void checkAndRunStrategy(ConDeviceAttribute sourceAttr) {
        // 根据源属性ID获取所有关联的联动策略ID集合
        Set<String> ids = linkageStrategyRegisterService.getStrategyByAttrId(sourceAttr.getId());
        if (ids.size() > 0) {
            // 遍历所有获取到的联动策略ID
            for (String id : ids) {
                // 根据策略ID获取联动策略对象
                LinkageStrategy byId = linkageStrategyService.getById(id);
                // 如果策略对象为空或未启用，则跳过当前策略
                if (byId == null || !LinkageConstant.IS_ENABLE.equals(byId.getIsEnable())) {
                    continue;
                }
                // 根据策略的表达式计算是否需要执行联动
                Boolean isLinkage = expressionUtil.getValueByAttrValue(byId.getExpression(), Boolean.class);
                // 如果计算结果为真，则执行联动动作
                if (Boolean.TRUE.equals(isLinkage)) {
                    runAction(byId);
                }
            }
        }

    }



    /**
     * 根据属性ID获取关联的输出ID集合。
     * <p>此方法通过属性ID查询关联的战略ID集合，然后遍历这些战略ID，获取每个战略对应的事件操作列表。
     * 从这些事件操作中，筛选出类型为设置的操作，将其操作ID添加到输出ID集合中。</p>
     *
     * @param attrId 属性ID，用于查询关联的战略ID集合。
     * @return 返回一个包含所有关联输出ID的集合。
     */
    @Override
    public Set<String> getOutputByAttrId(String attrId) {
        Set<String> outputIds = new HashSet<>();
        // 根据属性ID查询关联的战略ID集合
        Set<String> strategyIds = linkageStrategyRegisterService.getStrategyByAttrId(attrId);
        if (strategyIds != null) {
            for (String strategyId : strategyIds) {
                // 根据战略ID获取战略对象
                LinkageStrategy byId = linkageStrategyService.getById(strategyId);
                // 根据战略关联的事件ID，获取事件操作列表
                List<EventAction> actionList = eventManagerService.listActionByEventId(byId.getEventId());
                // 遍历事件操作列表，筛选出类型为设置的操作，将其ID添加到输出ID集合中
                for (EventAction eventAction : actionList) {
                    if (LinkageConstant.ACT_TYPE_SET.equals(eventAction.getType())) {
                        outputIds.add(eventAction.getOperationId());
                    }
                }
            }
        }
        return outputIds;
    }


    @Override
    public List<LinkageStrategy> listEnableCycleTask() {
        return baseMapper.listEnableByType(StoryConstant.TYPE_CYCLE);
    }

    @Override
    public List<LinkageStrategy> listEnableTimingTask() {
        return baseMapper.listEnableByType(StoryConstant.TYPE_TIME);
    }

    @Override
    public void actPublishSatellite() {
        List<LinkageStrategy> list = baseMapper.listEnableByType(StoryConstant.TYPE_SATELLITE);
        if (list.isEmpty()) {
            return;
        }
        for (LinkageStrategy linkageStrategy : list) {
            runAction(linkageStrategy);
        }
    }

    @Override
    public List<LinkageStrategy> listByAlarmStrategyId(String alarmStrategyId) {
        return baseMapper.listEnableByTypeAndAlarmStrategyId(StoryConstant.TYPE_ALARM_CONFIRM, alarmStrategyId);
    }

    @Override
    public void alarmAck(String alarmStrategyId) {
        List<LinkageStrategy> list = baseMapper.listEnableByTypeAndAlarmStrategyId(StoryConstant.TYPE_ALARM_CONFIRM, alarmStrategyId);
        if (list.isEmpty()) {
            return;
        }
        for (LinkageStrategy linkageStrategy : list) {
            runAction(linkageStrategy);
        }
    }

    @Override
    public Set<String> listAttrIdByExpressionAttrIds(Collection<String> attrIds) {
        Set<String> outAttrIds = new HashSet<>();
        for (String attrId : attrIds) {
            List<String> outs = baseMapper.listOutAttrIdsByAttrId(attrId);
            if (!outs.isEmpty()) {
                outAttrIds.addAll(outs);
            }
        }
        return outAttrIds;
    }

    @Override
    public List<LinkageStrategy> listByEventId(String eventId) {
        return baseMapper.listByEventId(eventId);
    }

    /**
     * 获取表达式的字符串表示形式。
     * 这个方法通过给定的表达式字符串，利用expressionUtil工具类对其进行处理，将表达式中的属性ID转换为设备属性的字符串表示。
     * 如果属性ID对应的设备属性存在，则返回该设备属性的设备名称和属性名称组成的字符串，否则返回一个默认的字符串表示。
     *
     * @param expression 表达式字符串，其中可能包含属性ID。
     * @return 经过处理后的表达式字符串，属性ID被转换为对应的设备属性字符串表示。
     */
    @Override
    public String getExpressionStr(String expression) {
        // 使用expressionUtil的函数处理expression字符串，将属性ID替换为设备属性的字符串表示
        return expressionUtil.getExpressionStr(expression, (attrid) -> {
            ConDeviceAttribute byId = deviceAttributeService.getById(attrid);
            if (byId != null) {
                // 如果设备属性存在，返回设备名称和属性名称组成的字符串
                return '[' + byId.getDeviceName() + ':' + byId.getName() + ']';
            }
            // 如果设备属性不存在，返回默认字符串
            return "[:]";
        });
    }


    @Override
    public void test(String id) {
        LinkageStrategy byId = getById(id);
        runAction(byId);
    }

    @Override
    public List<LinkageStrategy> listByAlarmStrategyIds(ListByAlarmStrategyIdsDTO dto) {
        if (dto.getIds() == null || dto.getIds().size() == 0) {
            return Collections.emptyList();
        }
        return baseMapper.listEnableByTypeAndAlarmStrategyIds(StoryConstant.TYPE_ALARM_CONFIRM, dto.getIds());
    }
}
