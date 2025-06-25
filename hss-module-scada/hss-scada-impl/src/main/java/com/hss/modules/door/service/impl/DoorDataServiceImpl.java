package com.hss.modules.door.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.door.constant.DoorConstant;
import com.hss.modules.door.dto.CardDoorDTO;
import com.hss.modules.door.dto.RemoteOpenDoorDTO;
import com.hss.modules.door.entity.DoorData;
import com.hss.modules.door.mapper.DoorDataMapper;
import com.hss.modules.door.service.IDoorDataService;
import com.hss.modules.door.vo.CardDoorVO;
import com.hss.modules.door.vo.RemoteOpenDoorVO;
import com.hss.modules.system.entity.BaseTerminal;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.system.service.IBaseTerminalService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 门禁数据表
 * @Author: zpc
 * @Date:   2023-02-17
 * @Version: V1.0
 */
@Service
public class DoorDataServiceImpl extends ServiceImpl<DoorDataMapper, DoorData> implements IDoorDataService {
    @Autowired
    private IBaseTerminalService baseTerminalService;
    @Autowired
    private IBaseDictDataService baseDictDataService;

    @Override
    public IPage<DoorData> pageByTerminalId(Page<DoorData> page, String terminalId, Date startTime, Date endTime) {
        BaseTerminal terminal = baseTerminalService.getById(terminalId);
        if (terminal == null || StringUtils.isBlank(terminal.getDoorId())){
            return page;
        }
        //查询终端显示的门信息
        IPage<DoorData> result = baseMapper.pageTerminalByDoorId(page, terminal.getDoorId(), startTime, endTime);
        //根据字典类型id查询map
        Map<String,String> openTypeMap = baseDictDataService.mapByTypeId("ba30903ff2d39df1102d28be5e69c747");
        Map<String,String> cardResultMap = baseDictDataService.mapByTypeId("37d28d3d7444f601cf24e1d4d6f97b4a");

        for (DoorData doorData : result.getRecords()) {
            // 设置进出类型
            setInOutType(doorData);
            // 设置你开门类型
            String openType = doorData.getOpenType();
            if (StringUtils.isNotBlank(openType)){
                doorData.setOpenType_disp(openTypeMap.get(openType));
            }
            // 设置刷卡结果
            String cardResult = doorData.getCardResult();
            if (StringUtils.isNotBlank(cardResult)){
                doorData.setCardResult_disp(cardResultMap.get(cardResult));
            }
        }
        return result;
    }

    @Override
    public IPage<RemoteOpenDoorVO> pageByRemoteOpen(RemoteOpenDoorDTO dto) {
        //查询远程开门记录
        IPage<RemoteOpenDoorVO> resultPage = baseMapper.pageByRemoteOpen(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
        for (RemoteOpenDoorVO record : resultPage.getRecords()) {
            record.setOpenType("远程开门");
        }
        return resultPage;
    }

    @Override
    public IPage<CardDoorVO> pageByCard(CardDoorDTO dto) {
        IPage<CardDoorVO> resultPage = baseMapper.pageByCard(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
        for (CardDoorVO record : resultPage.getRecords()) {
            if (StringUtils.isNotBlank(record.getOpenType())){
                record.setOpenType(DoorConstant.OPEN_TYPE_STR.get(record.getOpenType()));
            }
            if (StringUtils.isNotEmpty(record.getAccessType())){
                if ("entryCard".equals(record.getAccessType())){
                    record.setAccessType("进门");
                }else {
                    record.setAccessType("出门");
                }
            }
            if (StringUtils.isNotEmpty(record.getCardResult())){
                record.setCardResult(DoorConstant.CARD_RESULT_STR.get(record.getCardResult()));
            }
        }
        return resultPage;
    }

    private void setInOutType(DoorData doorData) {
        String accessType = doorData.getAccessType();
        if ("entryCard".equals(accessType)){
            accessType = "进门";
        }else if ("exitCard".equals(accessType)){
            accessType = "出门";
        }
        doorData.setAccessType(accessType);
    }

    @Override
    public List<DoorData> listCard3ByDoorId(String inout, String doorId, int count) {
        Page<DoorData> doorDataPage = new Page<>(1, count);
        doorDataPage.setOptimizeCountSql(false);
        //获取最近三条记录
        IPage<DoorData> page = baseMapper.listCard3ByDoorId(doorDataPage, inout, doorId);
        return page.getRecords();
    }
}
