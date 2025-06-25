package com.hss.modules.scada.event;

import com.hss.modules.scada.entity.ConDeviceAttribute;
import org.springframework.context.ApplicationEvent;

/**
 * scada 数据更新缓存完成
 * 接收方
 * 变量关联
 * 联动
 * alarm
 * ws
 * @author hd
 */
public class ScadaDataAttrValueCachedEvent extends ApplicationEvent {
    public ScadaDataAttrValueCachedEvent(ConDeviceAttribute source) {
        super(source);
    }
}
