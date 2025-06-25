package com.hss.modules.maintain.model;

import com.hss.modules.maintain.entity.MaintainOperate;
import lombok.Data;
import org.springframework.beans.BeanUtils;
/**
* @description: 保养操作
* @author zpc
* @date 2024/3/21 10:13
* @version 1.0
*/
@Data
public class MaintainOperateWrapper extends MaintainOperate {

    public MaintainOperateWrapper() {
    }

    public MaintainOperateWrapper(MaintainOperate source) {
        super();
        BeanUtils.copyProperties(source, this);
    }

    private String deviceId;

    private String deviceName;

    private String deviceClassName;

}
