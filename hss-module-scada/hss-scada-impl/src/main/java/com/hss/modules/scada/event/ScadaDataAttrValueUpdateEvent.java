package com.hss.modules.scada.event;

import com.hss.modules.scada.entity.ConDeviceAttribute;
import org.springframework.context.ApplicationEvent;

/**
 * scada 属性数据更新事件
 * 接受方为：
 * 校验更新
 * 存储
 * 发布方为：
 * mqtt，alarm，执行动作==变量赋值，属性配置值变更，设备命令
 * @author hd
 */
public class ScadaDataAttrValueUpdateEvent extends ApplicationEvent {
    public ScadaDataAttrValueUpdateEvent(ConDeviceAttribute source) {
        super(source);
    }
}
