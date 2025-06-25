package com.hss.modules.door.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.door.entity.DoorPersonSys;

/**
 * @Description: 门禁人员
 * @Author: zpc
 * @Date:   2023-03-02
 * @Version: V1.0
 */
public interface IDoorPersonService extends IService<DoorPersonSys> {

    /**
     * 同步人员信息
     */
    void sync();

    /**
     * 根据卡号查询人员信息
     * @param cardNumber
     * @return
     */
    DoorPersonSys getByCardNumber(String cardNumber);

    IPage<DoorPersonSys> queryPageList(Page<DoorPersonSys> page, QueryWrapper<DoorPersonSys> queryWrapper);
}
