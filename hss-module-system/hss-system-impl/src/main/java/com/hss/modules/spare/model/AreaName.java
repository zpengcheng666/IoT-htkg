package com.hss.modules.spare.model;

import lombok.Data;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/28 16:23
 */
@Data
public class AreaName {

    /**
     * 库区id
     */
    String areaId;

    /**
     * 仓库名
     */
    String warehouseName;

    /**
     * 库区名
     */
    String areaName;
}
