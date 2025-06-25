package com.hss.modules.door.service.impl;

import com.hss.core.common.util.LoginUserUtils;
import com.hss.modules.door.constant.DoorConstant;
import com.hss.modules.door.entity.DoorData;
import com.hss.modules.door.service.IDoorDataService;
import com.hss.modules.scada.additional.DeviceExecuteAdditional;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.model.DeviceExecuteDTO;
import com.hss.modules.scada.service.IConSheBeiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

;

/**
 * 远程开门 保存记录
 * @author hd
 */
@Service
public class DoorDeviceExecuteOperateImpl implements DeviceExecuteAdditional {
    
    @Autowired
    private IDoorDataService doorDataService;
    @Autowired
    private IConSheBeiService conSheBeiService;

    @Override
    public void additionalOperate(DeviceExecuteDTO dto, ConDeviceAttribute attr) {
        if (!DoorConstant.EN_REMOTE_OPEN_CTRL.equals(attr.getEnName())){
            return;
        }
        ConSheBei byId = conSheBeiService.getById(dto.getDeviceId());
        if (byId == null){
            return;
        }

        DoorData doorData = new DoorData();
        doorData.setDoorId(byId.getId());
        doorData.setDoorCode(byId.getCode());
        doorData.setDoorName(byId.getName());
        doorData.setDoorType(byId.getType());
        doorData.setDoorLocation(byId.getLocationId());
        doorData.setPerName(LoginUserUtils.getUser().getUsername());
        doorData.setOpenType("RemoteOpen");
        doorData.setSwipeTime(new Date());
        doorDataService.save(doorData);

    }

}
