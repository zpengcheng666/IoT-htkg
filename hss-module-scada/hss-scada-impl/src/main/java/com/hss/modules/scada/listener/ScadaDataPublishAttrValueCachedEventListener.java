package com.hss.modules.scada.listener;

import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataAttrValueCachedEvent;
import com.hss.modules.scada.model.DeviceAttrModel;
import com.hss.modules.scada.model.DeviceModel;
import com.hss.modules.scada.model.ScadaWsMessageType;
import com.hss.modules.scada.service.IGSChangJingSheBeiService;
import com.hss.modules.scada.ws.DoorWebSocket;
import com.hss.modules.scada.ws.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author zpc
 * @version 1.0
 * @description: 当ScadaDataAttrValueCachedEvent事件发生时的处理逻辑。
 *       该方法会尝试提取事件源中的设备属性，并发送相关信息。
 * @date 2024/3/20 9:36
 */
@Service
@Slf4j
public class ScadaDataPublishAttrValueCachedEventListener implements ApplicationListener<ScadaDataAttrValueCachedEvent> {

    @Autowired
    private WebSocket webSocket;
    @Autowired
    private IGSChangJingSheBeiService sceneDeviceService;

    /**
     * 当ScadaDataAttrValueCachedEvent事件发生时的处理逻辑。
     * 该方法会尝试提取事件源中的设备属性，并发送相关信息。
     * @author zpc
     * @param event 事件对象，包含设备属性值缓存更新的信息。
     */
    @Override
    public void onApplicationEvent(ScadaDataAttrValueCachedEvent event) {
        // 将事件源转换为具体的设备属性对象
        ConDeviceAttribute attr = (ConDeviceAttribute) event.getSource();
        try {
            // 提取并处理设备属性
            extracted(attr);
            // 发送设备属性信息给WebSocket客户端
            DoorWebSocket.sendMessage(attr);
        } catch (Exception e) {
            log.error("执行消费发布失败", e);
        }
    }

    /**
     * 处理设备属性提取的逻辑。
     * @author zpc
     * @param attr 设备属性对象，包含设备的详细属性信息。
     */
    private void extracted(ConDeviceAttribute attr) {
        // 创建设备模型对象，并设置设备ID
        DeviceModel deviceModel = new DeviceModel();
        deviceModel.setDeviceId(attr.getDeviceId());

        // 创建设备属性模型对象，并设置属性的英文名称、初始值和记录时间
        DeviceAttrModel deviceAttrModel = new DeviceAttrModel();
        deviceAttrModel.setEnName(attr.getEnName());
        deviceAttrModel.setValue(attr.getInitValue());
        deviceAttrModel.setRecordTime(attr.getUpdatedTime());
        // 将设备属性模型对象添加到设备模型中
        deviceModel.setAttrs(Collections.singletonList(deviceAttrModel));

        // 根据设备ID获取关联的场景ID列表
        List<String> sceneIds = sceneDeviceService.listSceneIdByDeviceId(attr.getDeviceId());
        // 遍历场景ID列表，向每个关联的场景发送设备属性更新消息
        for (String sceneId : sceneIds) {
            webSocket.sendMessage(ScadaWsMessageType.BIZ, deviceModel, sceneId);
        }
    }

}
