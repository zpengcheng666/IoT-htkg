package com.hss.modules.spare.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassDescription: 库存操作实体
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/26 9:35
 */
@Data
public class InventoryBO {
    /**
     *  单据id
     */
    private String formId;

    /**
     * 单据类型
     */
    private Integer formType;


    /**
     * 仓库id
     */
    private String warehouseId;


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
