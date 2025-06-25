package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.spare.entity.Carrier;
import com.hss.modules.spare.mapper.CarrierMapper;
import com.hss.modules.spare.service.ICarrierService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 供应商表
 * @Author: jeecg-boot
 * @Date:   2024-04-26
 * @Version: V1.0
 */
@Service
public class CarrierServiceImpl extends ServiceImpl<CarrierMapper, Carrier> implements ICarrierService {

    @Override
    public Map<String, String> nameMap(Set<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }
        return listByIds(ids).stream().collect(Collectors.toMap(Carrier::getId, Carrier::getCarrierName));
    }
}
