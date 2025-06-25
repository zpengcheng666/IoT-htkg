package com.hss.modules.linkage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.linkage.entity.LinkageStrategy;
import com.hss.modules.linkage.model.ListByAlarmStrategyIdsDTO;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.model.StrategyEnable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Description: 联动策略
 * @Author: jeecg-boot
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface ILinkageStrategyService extends IService<LinkageStrategy> {

    /**
     * 联动使能
     * @param strategyEnable
     */
    void enable(StrategyEnable strategyEnable);

    /**
     * 分页查询
     * @param page
     * @param name
     * @return
     */
    IPage<LinkageStrategy> getPage(Page<LinkageStrategy> page, String name);

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(String id);


    /**
     * 执行联动
     * @param strategy
     */
    void runAction(LinkageStrategy strategy);


    /**
     * 校验联并运行联动策略
     * @param sourceAttr
     */
    void checkAndRunStrategy(ConDeviceAttribute sourceAttr);

    /**
     * 添加联动策略
     * @param linkageStrategy
     */
    void add(LinkageStrategy linkageStrategy);

    /**
     * 根据属性id查询输入的变量集合
     * @param attrId
     * @return
     */
    Set<String> getOutputByAttrId(String attrId);

    /**
     * 获取使能的周期任务
     * @return
     */
    List<LinkageStrategy> listEnableCycleTask();

    /**
     * 获取使能的定时任务
     * @return
     */
    List<LinkageStrategy> listEnableTimingTask();

    /**
     * 处理卫星临空事件
     */
    void actPublishSatellite();

    /**
     * 报警确认事件
     * @param alarmStrategyId 报警策略id
     */
    void alarmAck(String alarmStrategyId);

    /**
     * 根据报警策略id查询联动列表
     * @param alarmStrategyId 报警策略id
     * @return 联动策略列表
     */
    List<LinkageStrategy> listByAlarmStrategyId(String alarmStrategyId);


    /**
     * 根据属性id查询表达式中包含属性id
     * @param attrIds
     * @return
     */
    Set<String> listAttrIdByExpressionAttrIds(Collection<String> attrIds);

    /**
     * 根据eventId查询
     * @param eventId
     * @return
     */
    List<LinkageStrategy> listByEventId(String eventId);

    /**
     * 表达式解析
     * @param expression
     * @return
     */
    String getExpressionStr(String expression);

    /**
     * 测试
     * @param id 策略id
     */
    void test(String id);

    List<LinkageStrategy> listByAlarmStrategyIds(ListByAlarmStrategyIdsDTO dto);
}
