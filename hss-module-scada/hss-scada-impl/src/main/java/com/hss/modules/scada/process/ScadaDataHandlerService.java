package com.hss.modules.scada.process;

import com.alibaba.fastjson.JSONObject;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataMqttReadEvent;
import com.hss.modules.scada.model.DefaultMqttMessage;
import com.hss.modules.scada.service.DeviceAttrPointExpressionRelationService;
import com.hss.modules.scada.service.DeviceAttrPointRelationService;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.util.ExpressionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 消息处理
 * @author hd
 */
@Service
@Slf4j
public class ScadaDataHandlerService {
    @Autowired
    private IConDeviceAttributeService deviceAttrService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private DeviceAttrPointRelationService deviceAttrPointRelationService;
    @Autowired
    private DeviceAttrPointExpressionRelationService deviceAttrPointExpressionRelationService;
    @Autowired
    private ExpressionUtil expressionUtil;
    @Autowired
    private RedisUtil redisUtil;

    private final List<ThreadPoolTaskExecutor> executorList;
    private static final int COUNT = 4;

    public ScadaDataHandlerService() {
        this.executorList = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            executorList.add(ProcessExecutorImpl.getThreadPool("mqttDataParse_", i));
        }

    }

    /**
     * 解析mqtt消息
     * @param message 解析消息
     * @return
     */
    public void parseMessage(String message){

        List<DefaultMqttMessage> defaultMqttMessages = JSONObject.parseArray(message, DefaultMqttMessage.class);
        if (defaultMqttMessages.isEmpty()) {
            return;
        }
        String deviceId = defaultMqttMessages.get(0).getDeviceId();
        if (deviceId == null) {
            deviceId = "1";
        }
        int hashCode = Math.abs(deviceId.hashCode());
        int index = hashCode % COUNT;
        ThreadPoolTaskExecutor executor = executorList.get(index);
        executor.execute(() -> {
            try {
                process(defaultMqttMessages);
            } catch (Exception e) {
                log.error("处理mqtt消息错误", e);
            }
        });

    }

    private void process(List<DefaultMqttMessage> defaultMqttMessages) {
//        long start = System.currentTimeMillis();
        List<ConDeviceAttribute> attributeList = toAttr(defaultMqttMessages);
        if (attributeList.isEmpty()) {
            return;
        }
        Date date = new Date();
        for (ConDeviceAttribute attribute : attributeList) {
            if ("onlineState".equals(attribute.getEnName())) {
                redisUtil.set("ONL:" + attribute.getId(), date);
            }
        }

        Map<String, List<ConDeviceAttribute>> deviceMap = attributeList.stream().collect(Collectors.groupingBy(ConDeviceAttribute::getDeviceId));
//        log.info("解析数据用时{}ms", System.currentTimeMillis() - start);
        publisher.publishEvent(new ScadaDataMqttReadEvent(deviceMap));
//        log.info("处理数据用时{}ms", System.currentTimeMillis() - start);
    }

    public List<ConDeviceAttribute>  toAttr(List<DefaultMqttMessage> messages){
        List<ConDeviceAttribute> attrList = new ArrayList<>();
        for (DefaultMqttMessage message : messages) {
            String variableId = message.getVariableId();
            addPointAttr(attrList, message, variableId);
            addExpressionAttr(attrList, message, variableId);
        }
        return attrList;

    }

    private void addExpressionAttr(List<ConDeviceAttribute> attrList, DefaultMqttMessage message, String variableId) {
        Collection<String> varAttrIds = deviceAttrPointExpressionRelationService.getAttrIdByPointId(variableId);
        if (varAttrIds.isEmpty()) {
            return;
        }
        for (String attrId : varAttrIds) {
            ConDeviceAttribute attr = deviceAttrService.getById(attrId);
            if (attr == null) {
                continue;
            }
            String value = getValueByExpression(attr.getExpression(), message.getValue());
            if (value == null) {
                continue;
            }
            attr.setUpdatedTime(message.getRecordTime());
            attr.setInitValue(value);
            attrList.add(attr);
        }
    }

    private void addPointAttr(List<ConDeviceAttribute> attrList, DefaultMqttMessage message, String variableId) {
        Collection<String> pointAttrIds = deviceAttrPointRelationService.getAttrIdByPointId(variableId);
        if (pointAttrIds.isEmpty()) {
            return;
        }
        for (String attrId : pointAttrIds) {
            ConDeviceAttribute attr = deviceAttrService.getById(attrId);
            if (attr == null){
                continue;
            }
            attr.setUpdatedTime(message.getRecordTime());
            attr.setInitValue(message.getValue());
            attrList.add(attr);
        }
    }

    private String getValueByExpression(String expression, String value) {
        if (StringUtils.isEmpty(expression)) {
            return null;
        }
        Set<String> ids = expressionUtil.listValueId(expression);
        if (ids.size() != 1){
            return null;
        }
        String expressionCh = expressionUtil.getExpressionStr(expression, pointId -> value);
        if (expressionCh == null) {
            return null;
        }
        return expressionUtil.getValue(expressionCh, String.class);
    }





}
