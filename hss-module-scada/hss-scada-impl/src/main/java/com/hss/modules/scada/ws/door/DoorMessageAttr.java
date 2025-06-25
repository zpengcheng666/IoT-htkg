package com.hss.modules.scada.ws.door;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 门禁属性信息
 * @author 26060
 */
@Data
public class DoorMessageAttr {

    private String name;

    private String value;

    private String enName;

    private String valueData;

    @JsonIgnore
    private JSONObject valueMap;

}
