package com.hss.modules.door.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.door.entity.DoorPersonCard;

import java.util.List;

/**
 * @Description: 门禁人员卡
 * @Author: zpc
 * @Date:   2023-03-02
 * @Version: V1.0
 */
public interface DoorPersonCardMapper extends BaseMapper<DoorPersonCard> {

    /**
     * 查询全部
     * @return
     */
    List<String> listId();


    /**
     * 获取卡列表
     * @param personId
     * @return
     */
    List<DoorPersonCard> listByCardByPersonId(String personId);
}
