package com.hss.modules.scada.ws.door;

import lombok.Data;

import java.util.List;

/**
 * 门禁动作
 * @author 26060
 */
@Data
public class DoorMessageService {

    /**
     * 动作名称
     */
    private String name;

    /**
     * 动作标识
     */
    private String enName;

    /**
     * 动作值
     */
    private String value;


    /**
     * 动作集合
     */
    private List<DoorMessageServiceValue> values;
}
