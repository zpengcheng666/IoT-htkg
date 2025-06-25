package com.hss.modules.scada.listener;

import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataAttrValueUpdateEvent;
import com.hss.modules.store.service.IStoreHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author zpc
 * @version 1.0
 * @description: 当SCADA属性数据更新属性值更新事件发生时的处理逻辑。
 * 所有的属性值，如果需要保存，都需要先通过存储策略，只有配置了存储策略后，才会根据属性的isSave属性确定是否保存
 * 也就是说，一个属性值是否需要保存， 是需要2个步骤的：
 * 1. 必须满足存储策略
 * 2. 满足了存储策略后，还得是属性的isSave属性为true才会保存
 * 所以，这个Listener就不需要了，原因是现在只有2种存储策略，周期存储、和定时存储
 * 都是由Job发起的，而且在Job中没有publish Event，而是直接保存了
 * 所以，这个Listener就不需要了。
 * @date 2024/3/20 9:37
 */
//@Service
@Slf4j
public class ScadaDataSaveAttrValueUpdateEventListener implements ApplicationListener<ScadaDataAttrValueUpdateEvent> {

    @Autowired
    private IStoreHistoryService storeHistoryService;

    /**
     * 当SCADA属性数据更新属性值更新事件发生时的处理逻辑。
     *
     * @param event SCADA属性数据更新属性值更新事件，携带了更新的属性信息。
     *  该事件源为具体的设备属性对象，包含了属性的标识、值等信息。
     *  通过该事件，可以响应设备属性值的更新，并执行相应的存储逻辑。
     */
    @Override
    public void onApplicationEvent(ScadaDataAttrValueUpdateEvent event) {
        try {
            // 将事件源转换为具体的设备属性对象
            ConDeviceAttribute attr = (ConDeviceAttribute) event.getSource();

            // 判断属性是否需要保存，若需要则调用存储服务进行存储
            if (ScadaConstant.IS_ONE.equals(attr.getIsSave())) {
                storeHistoryService.saveByVariableId(attr, attr.getInitValue());
            }
        } catch (Exception e) {
            log.error("执行存储策略失败", e);
        }
    }
}
