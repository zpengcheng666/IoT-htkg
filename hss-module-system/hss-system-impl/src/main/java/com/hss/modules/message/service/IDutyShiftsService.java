package com.hss.modules.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.message.dto.DutyHistoryDTO;
import com.hss.modules.message.dto.DutyHistoryVO;
import com.hss.modules.message.dto.DutyShiftsAutomaticDTO;
import com.hss.modules.message.entity.DutyShifts;
import com.hss.modules.message.model.DutyTerminalInfoVO;

import java.util.Date;
import java.util.List;

/**
 * @Description: 排班班次
 * @Author: zpc
 * @Date:   2023-04-26
 * @Version: V1.0
 */
public interface IDutyShiftsService extends IService<DutyShifts> {

    /**
     * 大屏信息查询
     * @param terminalId 终端id
     * @return 大屏要显示的信息
     */
    List<DutyTerminalInfoVO> listByTerminalId(String terminalId);

    /**
     * 查询排班信息
     * @param date 查询日期
     * @param dutyId 值班安排id
     * @return
     */
    List<DutyShifts> listByDateAndDutyId(Date date, String dutyId);

    /**
     * 自动排班
     * @param dto 参数
     */
    void automatic(DutyShiftsAutomaticDTO dto);


    /**
     * 查询值班历史信息
     * @param dto 请求参数
     * @return 分页信息
     */
    IPage<DutyHistoryVO> listHistory(DutyHistoryDTO dto);
}
