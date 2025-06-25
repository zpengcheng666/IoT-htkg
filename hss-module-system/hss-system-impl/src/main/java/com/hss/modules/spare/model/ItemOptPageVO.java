package com.hss.modules.spare.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/28 14:14
 */
@Data
public class ItemOptPageVO {
    private Integer  formType;
    private String itemName;
    private String itemNo;
    private String areaId;
    private BigDecimal quantity;
    private LocalDateTime createTime;

}
