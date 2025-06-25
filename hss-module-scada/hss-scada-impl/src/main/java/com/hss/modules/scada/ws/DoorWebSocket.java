package com.hss.modules.scada.ws;

import com.alibaba.fastjson.JSONObject;
import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.model.DeviceTypeAttrConfigOptionItem;
import com.hss.modules.devicetype.service.IDeviceTypeAttributeService;
import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.model.DeviceAttrData;
import com.hss.modules.scada.service.DeviceDataService;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.scada.ws.door.*;
import com.hss.modules.system.monitorThing.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 门禁实时消息发送
 * @author hd
 */
@Component
@Slf4j
@ServerEndpoint("/doorWebSocket")
public class DoorWebSocket {

    /**
     * 连接
     */
    private static final ConcurrentHashMap<String, WebSocketSessionOption> SESSION_MAP = new ConcurrentHashMap<>();

    private Session session;
//    private static Set<String> deviceIdSet = new HashSet<>(256);

    private static IConSheBeiService conSheBeiService;
    private static IDeviceTypeAttributeService deviceTypeAttributeService;
    private static DeviceDataService deviceDataService;




    @Autowired
    public void setConSheBeiService(IConSheBeiService conSheBeiService) {
        DoorWebSocket.conSheBeiService = conSheBeiService;
    }
    @Autowired
    public void setDeviceTypeAttributeService(IDeviceTypeAttributeService deviceTypeAttributeService) {
        DoorWebSocket.deviceTypeAttributeService = deviceTypeAttributeService;
    }
    @Autowired
    public void setDeviceDataService(DeviceDataService deviceDataService) {
        DoorWebSocket.deviceDataService = deviceDataService;
    }

    /**
     * 创建链接
     * @param session session
     */
    @OnOpen
    public void onOpen(Session session) {
        WebSocketSessionOption option = new WebSocketSessionOption(session);
        SESSION_MAP.put(session.getId(), option);
        this.session = session;
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        if (log.isInfoEnabled()) {
            log.info("doorWebSocket关闭连接");
        }
        SESSION_MAP.remove(session.getId());

    }

    /**
     * 接收消息
     * @param session session
     * @param message 消息
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            WebSocketSessionOption option = SESSION_MAP.get(session.getId());
            if (option == null) {
                return;
            }
            ConSheBei byId = conSheBeiService.getById(message);
            DoorMessageInfo doorMessageInfo = new DoorMessageInfo();
            doorMessageInfo.setId(byId.getId());
            doorMessageInfo.setCode(byId.getCode());
            doorMessageInfo.setName(byId.getName());
            doorMessageInfo.setOtherName(byId.getOtherName());
            doorMessageInfo.setType(byId.getType());
            doorMessageInfo.setLocationName(byId.getLocationName());

            List<DoorMessageAttr> doorMessageAttrs = new ArrayList<>();
            doorMessageInfo.setAttrs(doorMessageAttrs);
            List<DoorMessageService> doorMessageServices = new ArrayList<>();
            doorMessageInfo.setServices(doorMessageServices);
            List<DeviceTypeAttribute> typeAttributes = deviceTypeAttributeService.listByDeviceTypeId(byId.getDeviceTypeId());
            Map<String, DeviceTypeAttribute> attrTypeMap = typeAttributes.stream().collect(Collectors.toMap(DeviceTypeAttribute::getId, o -> o));

            List<ConDeviceAttribute> attributeList = conSheBeiService.listDeviceAttrByDeviceId(message);
            for (ConDeviceAttribute attribute : attributeList) {
                if (!ScadaConstant.IS_ONE.equals(attribute.getIsAssociate()) || StringUtils.isEmpty(attribute.getVariableId())) {
                    continue;
                }
                DeviceTypeAttribute attrType = attrTypeMap.get(attribute.getAttrId());
                if (ScadaConstant.IS_ONE.equals(attrType.getIsAct())) {
                    attrType.listJson2List();
                    DoorMessageService doorMessageService = new DoorMessageService();
                    doorMessageService.setName(attribute.getName());
                    doorMessageService.setEnName(attribute.getEnName());
                    if (attrType.getActList().isEmpty()) {
                        doorMessageService.setValue("1");
                    } else {
                        if (attrType.getActList().size() == 1) {
                            DeviceTypeAttrConfigOptionItem item0 = attrType.getActList().get(0);
                            doorMessageService.setName(item0.getName());
                            doorMessageService.setValue(item0.getValue());
                        }else {
                            List<DoorMessageServiceValue> values = attrType.getActList().stream().map(t -> {
                                DoorMessageServiceValue doorMessageServiceValue = new DoorMessageServiceValue();
                                doorMessageServiceValue.setName(t.getName());
                                doorMessageServiceValue.setValue(t.getValue());
                                return doorMessageServiceValue;
                            }).collect(Collectors.toList());
                            doorMessageService.setValues(values);

                        }
                    }
                    doorMessageServices.add(doorMessageService);
                } else {
                    DeviceAttrData data = deviceDataService.getAttrValueByAttrId(attribute.getId());
                    if (data != null && data.getValue() != null) {
                        DoorMessageAttr doorMessageAttr = new DoorMessageAttr();
                        doorMessageAttr.setName(attribute.getName());
                        doorMessageAttr.setEnName(attribute.getEnName());
                        doorMessageAttr.setValueData(data.getValue());
                        String valueMap = attribute.getValueMap();

                        if (StringUtils.isNotEmpty(valueMap) && !"{}".equals(valueMap)){
                            JSONObject jsonObject = JSONObject.parseObject(valueMap);
                            doorMessageAttr.setValueMap(jsonObject);

                        }
                        String value = doorMessageAttr.getValueMap() == null ? data.getValue() : doorMessageAttr.getValueMap().getString(data.getValue());
                        doorMessageAttr.setValue(value);
                        doorMessageAttrs.add(doorMessageAttr);
                    }
                }
            }
            option.setDevice(doorMessageInfo);
            option.send(JSONObject.toJSONString(doorMessageInfo));
        } catch (Exception e) {
            log.error("消息处理错误,message={}",message, e);
        }

    }

    /**
     * 错误回调
     * @param session session
     * @param error 错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("webSocket异常", error);
    }

    public static void sendMessage(ConDeviceAttribute attribute) {
        for (WebSocketSessionOption o : SESSION_MAP.values()) {
            DoorMessageInfo device = o.getDevice();
            if (device != null && device.getId().equals(attribute.getDeviceId())) {
                for (DoorMessageAttr doorMessageAttr : device.getAttrs()) {
                    if (doorMessageAttr.getName().equals(attribute.getName())) {
                        String value = doorMessageAttr.getValueMap() == null ? attribute.getInitValue() : doorMessageAttr.getValueMap().getString(attribute.getInitValue());
                        doorMessageAttr.setValue(value);
                        doorMessageAttr.setValueData(attribute.getInitValue());
                        break;
                    }
                }
                o.send(JSONObject.toJSONString(device));
            }
        }

    }



}
