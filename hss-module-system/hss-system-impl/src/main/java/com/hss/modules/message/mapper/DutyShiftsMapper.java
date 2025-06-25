package com.hss.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hss.modules.message.dto.DutyHistoryDTO;
import com.hss.modules.message.dto.DutyHistoryVO;
import com.hss.modules.message.entity.DutyShifts;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description: 排班班次
 * @Author: zpc
 * @Date:   2023-04-26
 * @Version: V1.0
 */
public interface DutyShiftsMapper extends BaseMapper<DutyShifts> {

    /**
     * 查询排班信号
     * @param start 开始时间
     * @param end 结束时间
     * @param dutyId 值班id
     * @return 排班列表
     */
    List<DutyShifts> listByDateAndDutyId(@Param("start") Date start, @Param("end") Date end, @Param("dutyId") String dutyId);

    /**
     * 获取值班小组id
     * @param date  日期
     * @param dutyId 值班id
     * @return 值班小组id
     */
    String getGroupIdByDateAndDutyId(@Param("date") Date date, @Param("dutyId") String dutyId);

    /**
     * 查询历史
     * @param page 分页参数
     * @param dto 开始日期
     * @return 分页信息
     */
    IPage<DutyHistoryVO> listHistory(IPage<DutyHistoryVO> page, @Param("dto") DutyHistoryDTO dto);
}
