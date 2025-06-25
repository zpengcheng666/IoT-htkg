package com.hss.modules.scada.listener;

import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataMqttReadEvent;
import com.hss.modules.scada.process.ProcessExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 读取mqtt消息
 * 1.处理平台额外操作
 * 2.过滤掉没有更新的数据
 * 3.发布消息
 * @author hd
 */
@Service
@Slf4j
public class ScadaDataMqttReadEventListener implements ApplicationListener<ScadaDataMqttReadEvent> {

    @Autowired
    private ProcessExecutor process;

    /**
     * 当接收到ScadaDataMqttReadEvent事件时的处理逻辑。
     * 该方法会遍历事件中携带的设备及其属性，并对每个设备的属性进行处理。
     *
     * @param event ScadaDataMqttReadEvent事件对象，包含从MQTT读取到的设备数据。
     */
    @Override
    public void onApplicationEvent(ScadaDataMqttReadEvent event) {
        // 从事件中获取设备数据映射，其中键为设备ID，值为设备属性列表
        Map<String, List<ConDeviceAttribute>> deviceMap = (Map<String, List<ConDeviceAttribute>>) event.getSource();

        // 遍历设备数据映射，对每个设备的数据进行处理
        for (Map.Entry<String, List<ConDeviceAttribute>> entry : deviceMap.entrySet()) {
            String deviceId = entry.getKey();
            List<ConDeviceAttribute> attrList = entry.getValue();
            process.process(deviceId, attrList);
        }
    }
}
