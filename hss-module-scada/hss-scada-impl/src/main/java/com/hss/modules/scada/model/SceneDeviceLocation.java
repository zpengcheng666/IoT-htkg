package com.hss.modules.scada.model;

import lombok.Data;

/**
 * 长江设备位置信息
 * @author hd
 */
@Data
public class SceneDeviceLocation {

    /**
     * 位置id
     */
    private String id;

    /**
     * 位置名称
     */
    private String name;
}
