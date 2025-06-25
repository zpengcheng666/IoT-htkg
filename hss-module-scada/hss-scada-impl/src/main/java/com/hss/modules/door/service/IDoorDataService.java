package com.hss.modules.door.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.door.dto.CardDoorDTO;
import com.hss.modules.door.dto.RemoteOpenDoorDTO;
import com.hss.modules.door.entity.DoorData;
import com.hss.modules.door.vo.CardDoorVO;
import com.hss.modules.door.vo.RemoteOpenDoorVO;

import java.util.Date;
import java.util.List;

/**
 * @Description: 门禁数据表
 * @Author: zpc
 * @Date:   2023-02-17
 * @Version: V1.0
 */
public interface IDoorDataService extends IService<DoorData> {

    /**
     * 终端查询门禁信息
     * @param page
     * @param terminalId
     * @param startTime
     * @param endTime
     * @return
     */
    IPage<DoorData> pageByTerminalId(Page<DoorData> page, String terminalId, Date startTime, Date endTime);

    /**
     * 查询远程开门记录
     * @param dto
     * @return
     */
    IPage<RemoteOpenDoorVO> pageByRemoteOpen(RemoteOpenDoorDTO dto);


    /**
     * 查询刷卡记录
     * @param dto
     * @return
     */
    IPage<CardDoorVO> pageByCard(CardDoorDTO dto);

    /**
     * 根据门id获取最近3条的刷卡记录
     * @param inout 进出类型
     * @param doorId 门id
     * @param count 查询数量
     * @return 记录
     */
    List<DoorData> listCard3ByDoorId(String inout, String doorId, int count);
}
