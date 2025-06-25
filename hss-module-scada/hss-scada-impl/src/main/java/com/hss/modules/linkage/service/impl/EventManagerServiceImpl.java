package com.hss.modules.linkage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.linkage.constant.LinkageConstant;
import com.hss.modules.linkage.entity.EventAction;
import com.hss.modules.linkage.entity.EventManager;
import com.hss.modules.linkage.mapper.EventManagerMapper;
import com.hss.modules.linkage.service.IEventActionService;
import com.hss.modules.linkage.service.IEventManagerService;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.scada.service.IConSheBeiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.StringJoiner;

/**
 * @Description: 事件管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Service
@Slf4j
public class EventManagerServiceImpl extends ServiceImpl<EventManagerMapper, EventManager> implements IEventManagerService {

    @Autowired
    private IEventActionService eventActionService;
    @Autowired
    private IConSheBeiService conSheBeiService;
    @Autowired
    private IConDeviceAttributeService deviceAttributeService;
    @Override
    public IPage<EventManager> getPage(Page<EventManager> page, String name) {
        IPage<EventManager> list = baseMapper.list(page, name);
        List<EventManager> records = list.getRecords();
        for (EventManager record : records) {
            record.setActionList(eventActionService.listActionByEventId(record.getId()));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        removeById(id);
        eventActionService.deleteByEventId(id);
    }

    @Override
    public List<EventAction> listActionByEventId(String eventId) {
        return eventActionService.listActionByEventId(eventId);
    }

    /**
     * 处理事件动作的执行。
     *
     * @param eventAction 事件动作对象，包含执行所需的所有信息，如类型、操作ID和操作值等。
     * @return 执行结果，成功返回"成功"，失败返回"失败"。
     */
    @Override
    public String action(EventAction eventAction) {
        // 检查事件动作类型，只处理特定的类型
        if (
                LinkageConstant.ACT_TYPE_SET.equals(eventAction.getType())
                || LinkageConstant.ACT_TYPE_PUBLISH_SH.equals(eventAction.getType())
                || LinkageConstant.ACT_TYPE_PUBLISH_LH.equals(eventAction.getType())
                || LinkageConstant.ACT_TYPE_PRE_LOCATION.equals(eventAction.getType())
        ){
            // 根据操作ID获取设备属性
            ConDeviceAttribute byId = deviceAttributeService.getById(eventAction.getOperationId());
            // 如果属性不存在，则记录错误信息并返回失败
            if (byId == null){
                log.error("执行联动失败,属性不存在attrId={}, eventActionId={}", eventAction.getOperationId(), eventAction.getId());
                return "失败";
            }
            // 如果是预置位置类型，则特殊处理，构造新的操作值
            if (LinkageConstant.ACT_TYPE_PRE_LOCATION.equals(eventAction.getType())) {
                StringJoiner values = new StringJoiner(",");
                values.add("16");
                values.add(eventAction.getOperationValue());
                values.add(eventAction.getChannelId());
                eventAction.setOperationValue(values.toString());
            }
            try {
                // 下发命令到设备，并传入设备属性和操作值
                conSheBeiService.executeCommandByValueExpression(byId, eventAction.getOperationValue());
            } catch (Exception e) {
                log.error("下发命令失败attr={},value={}", JSONObject.toJSONString(byId), eventAction.getOperationValue(), e);
                return "失败";
            }
        }
        return "成功";
    }

    @Override
    public List<EventManager> listAll() {
        return list();
    }
}
