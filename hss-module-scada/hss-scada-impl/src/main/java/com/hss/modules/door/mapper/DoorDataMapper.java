package com.hss.modules.door.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.door.dto.CardDoorDTO;
import com.hss.modules.door.dto.RemoteOpenDoorDTO;
import com.hss.modules.door.entity.DoorData;
import com.hss.modules.door.vo.CardDoorVO;
import com.hss.modules.door.vo.RemoteOpenDoorVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @Description: 门禁数据表
 * @Author: zpc
 * @Date:   2023-02-17
 * @Version: V1.0
 */
public interface DoorDataMapper extends BaseMapper<DoorData> {


    /**
     * 查询终端显示的门信息
     * @param page
     * @param doorId
     * @param startTime
     * @param endTime
     * @return
     */
    IPage<DoorData> pageTerminalByDoorId(Page<DoorData> page, @Param("doorId") String doorId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 查询远程开门记录
     * @param page
     * @param dto
     * @return
     */
    IPage<RemoteOpenDoorVO> pageByRemoteOpen(Page<RemoteOpenDoorVO> page, @Param("dto") RemoteOpenDoorDTO dto);

    /**
     * 刷卡开门查询
     * @param page
     * @param dto
     * @return
     */
    IPage<CardDoorVO> pageByCard(Page<Object> page, @Param("dto") CardDoorDTO dto);

    /**
     * 获取最近三条记录
     * @param page
     * @param inout
     * @param doorId
     * @return
     */
    IPage<DoorData> listCard3ByDoorId(Page<DoorData> page, @Param("inout") String inout, @Param("doorId") String doorId);
}
