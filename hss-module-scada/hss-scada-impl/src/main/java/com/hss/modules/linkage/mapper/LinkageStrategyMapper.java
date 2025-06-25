package com.hss.modules.linkage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.linkage.entity.LinkageStrategy;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Description: 联动策略
 * @Author: jeecg-boot
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface LinkageStrategyMapper extends BaseMapper<LinkageStrategy> {

    /**
     * 分页查询
     * @param page
     * @param name
     * @return
     */
    IPage<LinkageStrategy> getPage(Page<LinkageStrategy> page, @Param("name") String name);

    /**
     * 查询使能的联动策略
     * @param expressionVar
     * @return
     */
    List<LinkageStrategy> listConditionEnableByExpression(String expressionVar);


    /**
     * 获取使能的周期任务
     * @return
     */
    List<LinkageStrategy> listEnableByType(String type);

    /**
     * 获取使能的周期任务
     * @param type 类型
     * @param alarmStrategyId 报警策略id
     * @return 列表
     */
    List<LinkageStrategy> listEnableByTypeAndAlarmStrategyId(@Param("type") String type, @Param("alarmStrategyId") String alarmStrategyId);

    /**
     * 查询关联属性id
     * @param attrId
     * @return
     */
    List<String> listOutAttrIdsByAttrId(String attrId);

    /**
     * 根据eventId
     * @param eventId
     * @return
     */
    List<LinkageStrategy> listByEventId(String eventId);

    List<LinkageStrategy> listEnableByTypeAndAlarmStrategyIds(@Param("type") String typeAlarmConfirm, @Param("ids") Set<String> ids);
}
