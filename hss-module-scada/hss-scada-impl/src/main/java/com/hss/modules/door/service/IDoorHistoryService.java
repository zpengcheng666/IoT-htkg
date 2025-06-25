package com.hss.modules.door.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.door.dto.DoorHistoryAddDTO;
import com.hss.modules.door.dto.OpenDoorDTO;
import com.hss.modules.door.entity.DoorHistory;
import com.hss.modules.door.vo.GetLastVO;
import com.hss.modules.door.vo.OpenDoorVO;

/**
 * @Description: -门禁
 * @Author: zpc
 * @Date:   2022-12-05
 * @Version: V1.0
 */
public interface IDoorHistoryService extends IService<DoorHistory> {

    /**
     * 获取最后一次通行记录
     * @param deviceId
     * @return
     */
    GetLastVO getLast(String deviceId);


    /**
     * 分页查询
     * @param dto
     * @return
     */
    IPage<OpenDoorVO> pageList(OpenDoorDTO dto);

    /**
     * 添加
     * @param dto
     */
    void add(DoorHistoryAddDTO dto);
}
