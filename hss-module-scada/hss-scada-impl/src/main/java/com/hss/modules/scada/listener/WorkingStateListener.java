package com.hss.modules.scada.listener;

import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataAttrValueUpdateEvent;
import com.hss.modules.scada.service.IDeviceRunRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author zpc
 * @version 1.0
 * @description: 监听属性值变化，处理SCADA数据属性值更新事件。
 * @date 2024/3/20 9:38
 */
@Slf4j
@Component
public class WorkingStateListener implements ApplicationListener<ScadaDataAttrValueUpdateEvent> {

    @Autowired
    private IDeviceRunRecordService deviceRunRecordService;

    /**
     * 处理SCADA数据属性值更新事件。
     * 当事件源为设备属性且属性名为"workingState"时，记录设备运行状态。
     *
     * @param event 事件对象，包含更新的设备属性信息。
     */
    @Override
    public void onApplicationEvent(ScadaDataAttrValueUpdateEvent event) {
        try {
            // 将事件源转换为设备属性对象
            ConDeviceAttribute attr = (ConDeviceAttribute) event.getSource();

            // 如果属性名为"workingState"，则添加该属性到设备运行记录服务中
            if ("workingState".equals(attr.getEnName())) {
                deviceRunRecordService.add(attr);
            }
        } catch (Exception e) {
            log.error("在线离线监听器错误", e);
        }
    }
}
