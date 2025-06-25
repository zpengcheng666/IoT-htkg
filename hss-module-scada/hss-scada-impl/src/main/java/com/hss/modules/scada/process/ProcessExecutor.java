package com.hss.modules.scada.process;

import com.hss.modules.scada.entity.ConDeviceAttribute;

import java.util.List;

/**
 * @ClassDescription: 处理mqtt消息，进出门结果
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/3/6 9:41
 */
public interface ProcessExecutor {

    void process(String deviceId, List<ConDeviceAttribute> attrList);

}
