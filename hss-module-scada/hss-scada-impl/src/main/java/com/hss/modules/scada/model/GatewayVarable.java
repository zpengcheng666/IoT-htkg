package com.hss.modules.scada.model;

import lombok.Data;

/**
 * 网关变量
 * @author 26060
 */
@Data
public class GatewayVarable {

    /**
     * 变量名称
     */
    private String namev;

    /**
     * 变量id
     */
    private String id;

    /**
     * 变量类型
     */
    private String datatype;
}
