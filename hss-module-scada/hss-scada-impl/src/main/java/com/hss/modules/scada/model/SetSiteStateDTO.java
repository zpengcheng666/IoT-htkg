package com.hss.modules.scada.model;

import lombok.Data;

/**
 * @author 26060
 */
@Data
public class SetSiteStateDTO {

    /**
     * 站点状态
     */
    private Integer siteState;

    /**
     * 站点编号
     */
    private String siteCode;
}
