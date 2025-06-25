package com.hss.modules.door.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.door.entity.DoorPersonSys;

import java.util.List;

/**
 * @Description: 门禁人员
 * @Author: zpc
 * @Date:   2023-03-02
 * @Version: V1.0
 */
public interface DoorPersonMapper extends BaseMapper<DoorPersonSys> {


    /**
     * 查询全部id
     * @return
     */
    List<String> listAllId();

    /**
     * 根据卡号查询人员信息
     * @param cardNumber
     * @return
     */
    DoorPersonSys getByCardNumber(String cardNumber);
}
