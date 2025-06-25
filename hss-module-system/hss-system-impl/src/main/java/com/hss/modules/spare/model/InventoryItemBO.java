package com.hss.modules.spare.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassDescription: 库房物料数量
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/26 9:47
 */
@Data
public class InventoryItemBO {


    /**
     * 库区id
     */
    private String areaId;

    /**
     * 物料id
     */
    private String itemId;

    /**
     * 操作数量
     */
    private BigDecimal quantity;

}
