package com.hss.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.message.entity.DutyGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 值班人员
 * @Author: zpc
 * @Date:   2023-04-21
 * @Version: V1.0
 */
public interface DutyGroupMapper extends BaseMapper<DutyGroup> {

    /**
     * 查询值班小组信息
     * @param dutyId 值班id
     * @return 值班小组信息
     */
    List<DutyGroup> listByDutyId(String dutyId);

    /**
     * 查询数量
     * @param dutyId 值班id
     * @param code 小组编号
     * @return 数量
     */
    int countByDutyIdAndCode(@Param("dutyId") String dutyId, @Param("code") Integer code);
}
