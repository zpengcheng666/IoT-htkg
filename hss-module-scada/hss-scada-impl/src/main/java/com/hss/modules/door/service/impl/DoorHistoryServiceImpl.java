package com.hss.modules.door.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.LoginUserUtils;
import com.hss.modules.door.constant.DoorConstant;
import com.hss.modules.door.dto.DoorHistoryAddDTO;
import com.hss.modules.door.dto.OpenDoorDTO;
import com.hss.modules.door.entity.DoorHistory;
import com.hss.modules.door.entity.DoorPerson;
import com.hss.modules.door.mapper.DoorHistoryMapper;
import com.hss.modules.door.service.IDoorHistoryService;
import com.hss.modules.door.vo.GetLastVO;
import com.hss.modules.door.vo.GetLastVOInOut;
import com.hss.modules.door.vo.OpenDoorVO;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.service.IConSheBeiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: -门禁
 * @Author: zpc
 * @Date:   2022-12-05
 * @Version: V1.0
 */
@Service
public class DoorHistoryServiceImpl extends ServiceImpl<DoorHistoryMapper, DoorHistory> implements IDoorHistoryService {

    @Autowired
    private IConSheBeiService conSheBeiService;

    @Override
    public GetLastVO getLast(String deviceId) {
        GetLastVO vo = new GetLastVO();
        vo.setIn(getLastVOInOut(deviceId, "entryCard"));
        vo.setOut(getLastVOInOut(deviceId, "exitCard"));

        return vo;
    }
    private GetLastVOInOut getLastVOInOut(String deviceId, String accessType) {
        Page<DoorHistory> pageParam = new Page<>(1,1);
        IPage<DoorHistory> pageResult = baseMapper.pageLast(pageParam, deviceId, accessType);
        if (pageResult.getRecords().isEmpty()){
            return null;
        }
        DoorHistory doorHistory = pageResult.getRecords().get(0);
        GetLastVOInOut vo = new GetLastVOInOut();
        vo.setId(doorHistory.getId());
        vo.setDoorId(doorHistory.getDoorId());
        vo.setDoorCode(doorHistory.getDoorCode());
        vo.setDoorName(doorHistory.getDoorName());
        vo.setDoorType(doorHistory.getDoorType());
        vo.setOpenTime(doorHistory.getOpenTime());
        vo.setPerName(doorHistory.getPerName1());
        vo.setPerDept(doorHistory.getPerDept1());
        vo.setPerTitle(doorHistory.getPerTitle1());
        vo.setCardCode(doorHistory.getCardCode1());
        vo.setSwipeTime(doorHistory.getSwipeTime1());
        vo.setOpenType(DoorConstant.OPEN_TYPE_STR.get(doorHistory.getOpenType()));

        vo.setId(doorHistory.getId());
        return vo;
    }

    @Override
    public IPage<OpenDoorVO> pageList(OpenDoorDTO dto) {
        IPage<OpenDoorVO> resultPage = baseMapper.pageList(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
        for (OpenDoorVO record : resultPage.getRecords()) {
            if (StringUtils.isNotEmpty(record.getAccessType())){
                if ("entryCard".equals(record.getAccessType())){
                    record.setAccessType("进门");
                }else {
                    record.setAccessType("出门");
                }
            }
            if (StringUtils.isNotBlank(record.getOpenType())){
                record.setOpenType(DoorConstant.OPEN_TYPE_STR.get(record.getOpenType()));
            }
            if (record.getRecordType() != null){
                if (record.getRecordType() == 0){
                    record.setRecordTypeStr("自动");
                }else {
                    record.setRecordTypeStr("手动");
                }
            }
        }

        return resultPage;
    }

    @Override
    public void add(DoorHistoryAddDTO dto) {
        ConSheBei device = conSheBeiService.getById(dto.getDoorId());
        if (device == null) {
            throw new HssBootException("门不存在");
        }
        DoorHistory doorHistory = new DoorHistory();
        doorHistory.setDoorId(dto.getDoorId());
        doorHistory.setDoorName(device.getName());
        doorHistory.setDoorCode(device.getCode());
        doorHistory.setDoorType(device.getType());
        doorHistory.setDoorLocation(device.getLocationId());
        doorHistory.setOpenTime(dto.getOpenTime());
        doorHistory.setPerNumRecord(dto.getPerNumRecord());
        doorHistory.setAccessType(dto.getAccessType());
        doorHistory.setRecordType(1);
        doorHistory.setEnterReason(dto.getEnterReason());
        doorHistory.setPerList(dto.getPerList().toArray(new DoorPerson[0]));
        doorHistory.setPerName1(LoginUserUtils.getUser().getUsername());
        save(doorHistory);
        LogUtil.setOperate(doorHistory.getDoorName());
    }
}
