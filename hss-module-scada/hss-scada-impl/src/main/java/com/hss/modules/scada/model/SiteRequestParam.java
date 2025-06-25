package com.hss.modules.scada.model;

import lombok.Data;

/**
 * 通过网关发送数据参数
 * @author hd
 */
@Data
public class SiteRequestParam {

    /**
     * 地址
     */
    private String url;

    /**
     * json数据
     */
    private String jsonData;

}
