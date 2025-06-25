package com.hss.modules.door.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.door.dto.OpenDoorDTO;
import com.hss.modules.door.entity.DoorHistory;
import com.hss.modules.door.vo.OpenDoorVO;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: -门禁
 * @Author: zpc
 * @Date:   2022-12-05
 * @Version: V1.0
 */
public interface DoorHistoryMapper extends BaseMapper<DoorHistory> {

    IPage<DoorHistory> pageLast(Page<DoorHistory> pageParam, @Param("doorId") String doorId, @Param("accessType") String accessType);


    /**
     * 分页查询
     * @param page
     * @param dto
     * @return
     */
    IPage<OpenDoorVO> pageList(Page<OpenDoorVO> page, @Param("dto") OpenDoorDTO dto);
}
