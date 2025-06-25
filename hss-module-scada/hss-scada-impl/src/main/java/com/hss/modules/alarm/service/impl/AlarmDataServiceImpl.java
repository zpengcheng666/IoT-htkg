package com.hss.modules.alarm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.alarm.entity.AlarmData;
import com.hss.modules.alarm.event.AlarmAckActLinkageEvent;
import com.hss.modules.alarm.event.AlarmAckEvent;
import com.hss.modules.alarm.event.AlarmAckEventData;
import com.hss.modules.alarm.mapper.AlarmDataMapper;
import com.hss.modules.alarm.model.AlarmAckDTO;
import com.hss.modules.alarm.model.AlarmAckLinkage;
import com.hss.modules.alarm.model.AlarmBatchAckDTO;
import com.hss.modules.alarm.service.IAlarmDataService;
import com.hss.modules.alarm.service.IAlarmHistoryService;
import com.hss.modules.message.dto.AlarmWebSocketMessage;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.scada.ws.AlarmWebSocket;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.system.service.IBaseTerminalService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Select;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 报警数据
 * @Author: jeecg-boot
 * @Date: 2022-12-01
 * @Version: V1.0
 */
@Service
public class AlarmDataServiceImpl extends ServiceImpl<AlarmDataMapper, AlarmData> implements IAlarmDataService {
    @Autowired
    private IAlarmHistoryService alarmHistoryService;
    @Autowired
    private IBaseTerminalService baseTerminalService;
    @Autowired
    private IBaseDictDataService baseDictDataService;
    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public IPage<AlarmData> queryPageList(Page<AlarmData> page, QueryWrapper<AlarmData> queryWrapper) {
        Page<AlarmData> page1 = page(page, queryWrapper);
        for (AlarmData record : page1.getRecords()) {
            String originVarId = record.getOriginVarId();
            if (StringUtils.isEmpty(originVarId)){
                continue;
            }
            ConDeviceAttribute byId = conDeviceAttributeService.getById(originVarId);
            if (byId == null){
                continue;
            }
            String valueMap = byId.getValueMap();
            if (StringUtils.isEmpty(valueMap) || "{}".equals(valueMap)){
                continue;
            }
            JSONObject jsonObject = JSONObject.parseObject(valueMap);
            record.setRecordValue(jsonObject.getString(record.getRecordValue()));
            record.setRange(jsonObject.getString(record.getRange()));
        }
        return page1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ack(AlarmAckDTO dto) {
        AlarmData byId = getById(dto.getId());
        if (byId == null) {
            return;
        }
        removeById(dto.getId());
        alarmHistoryService.ack(dto);
        checkLinkageAndRun(dto.getLinkageStrategyList());
        LogUtil.setOperate(byId.getOriginVarName());
        ackWs();
    }

    private void ackWs() {
        AlarmWebSocketMessage msg = AlarmWebSocketMessage.buildAlarmMessage(
            null,null,null,null);
        AlarmWebSocket.sentMessage(msg);
    }

    /**
     * 检测并执行联动
     * @param linkageStrategyList 联动列表
     */
    private void checkLinkageAndRun(List<AlarmAckLinkage> linkageStrategyList) {
        if (CollectionUtils.isNotEmpty(linkageStrategyList)) {
            for (AlarmAckLinkage alarmAckLinkage : linkageStrategyList) {
                if (Boolean.TRUE.equals(alarmAckLinkage.getAct())){
                    publisher.publishEvent(new AlarmAckActLinkageEvent(alarmAckLinkage.getId()));
                }
            }
        }
    }

    @Override
    public void batchAck(AlarmBatchAckDTO dto) {
        if (dto.getIds() == null || dto.getIds().isEmpty()) {
            return;
        }
        List<AlarmData> list = listByIds(dto.getIds());
        removeByIds(dto.getIds());
        alarmHistoryService.batchAck(dto);
        checkLinkageAndRun(dto.getLinkageStrategyList());


        AlarmAckEventData alarmAckEventData = new AlarmAckEventData();
        alarmAckEventData.setDeviceId(list.get(0).getDeviceId());
        alarmAckEventData.setAttrId(list.get(0).getOriginVarId());
        alarmAckEventData.setAckType(dto.getType());
        alarmAckEventData.setAlarmTime(list.get(0).getRecordTime());
        alarmAckEventData.setAlarmStrategyId(list.get(0).getStrategyId());
        AlarmAckEvent ackEvent = new AlarmAckEvent(alarmAckEventData);
        publisher.publishEvent(ackEvent);
        ackWs();
    }
    @Override
    @NotNull
    public List<AlarmData> getAlarmData(String alarmLevel) {
        LambdaQueryWrapper<AlarmData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AlarmData::getAlarmLevel, alarmLevel);
        List<AlarmData> alarmDataList = this.list(queryWrapper);
        alarmDataList.forEach(e ->{
            //报警级别
            BaseDictData lev = baseDictDataService.getById(e.getAlarmLevel());
            e.setAlarmLevel_disp(lev == null ? "" : lev.getName());
        });
        return alarmDataList;
    }

    @Override
    public int getCount() {
        return baseMapper.getCount();
    }
}
