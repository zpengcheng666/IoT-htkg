package com.hss.modules.door.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.door.entity.DoorPersonCard;
import com.hss.modules.door.mapper.DoorPersonCardMapper;
import com.hss.modules.door.service.IDoorPersonCardService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 门禁人员卡
 * @Author: zpc
 * @Date:   2023-03-02
 * @Version: V1.0
 */
@Service
public class DoorPersonCardServiceImpl extends ServiceImpl<DoorPersonCardMapper, DoorPersonCard> implements IDoorPersonCardService {

    @Override
    public void delAll() {
        //查询全部id
        List<String> ids = baseMapper.listId();
        if (!ids.isEmpty()){
            removeByIds(ids);
        }

    }

    @Override
    public List<DoorPersonCard> listCardByPersonId(String personId) {
        //获取卡列表
        return baseMapper.listByCardByPersonId(personId);
    }
}
