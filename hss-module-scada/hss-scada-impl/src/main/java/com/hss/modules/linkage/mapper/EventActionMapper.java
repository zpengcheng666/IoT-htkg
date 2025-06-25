package com.hss.modules.linkage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.linkage.entity.EventAction;

import java.util.List;

/**
 * @Description: 事件动作
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface EventActionMapper extends BaseMapper<EventAction> {

    /**
     * 根据事件id删除
     * @param eventId
     */
    void deleteByEventId(String eventId);

    /**
     * 根据eventId获取动作
     * @param eventId
     * @return
     */
    List<EventAction> listActionByEventId(String eventId);
}
