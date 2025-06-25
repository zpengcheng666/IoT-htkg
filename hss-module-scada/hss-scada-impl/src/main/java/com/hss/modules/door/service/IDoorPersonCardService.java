package com.hss.modules.door.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.door.entity.DoorPersonCard;

import java.util.List;

/**
 * @Description: 门禁人员卡
 * @Author: zpc
 * @Date:   2023-03-02
 * @Version: V1.0
 */
public interface IDoorPersonCardService extends IService<DoorPersonCard> {

    /**
     * 删除全部
     */
    void delAll();

    /**
     * 获取卡列表
     * @param personId
     * @return
     */
    List<DoorPersonCard> listCardByPersonId(String personId);
}
