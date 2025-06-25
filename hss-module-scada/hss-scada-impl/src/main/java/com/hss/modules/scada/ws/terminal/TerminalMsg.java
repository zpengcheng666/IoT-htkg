package com.hss.modules.scada.ws.terminal;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hss.modules.message.util.TerminalWebSocketMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassDescription: 消息对象
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/7 13:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerminalMsg {
    /**
     * 消息编码
     */
    private String code;

    /**
     * 消息体
     */
    private Object body;

    public String toMsg() {
        TerminalWebSocketMessage message = new TerminalWebSocketMessage();
        message.setType(code);
        message.setMessage(body);
        return JSONObject.toJSONString(message, SerializerFeature.WriteDateUseDateFormat);
    }


}
