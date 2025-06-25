package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.spare.entity.Area;
import com.hss.modules.spare.mapper.AreaMapper;
import com.hss.modules.spare.model.AreaName;
import com.hss.modules.spare.service.IAreaService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 库区
 * @Author: jeecg-boot
 * @Date:   2024-04-25
 * @Version: V1.0
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {

    @Override
    public Map<String, String> mapAreaIdMap(Set<String> areaIds) {
        if (CollectionUtils.isEmpty(areaIds)) {
            return new HashMap<>();
        }
        List<AreaName> list = baseMapper.listNames(areaIds);
        return list.stream().collect(Collectors.toMap(AreaName::getAreaId, o -> o.getWarehouseName() + "/" + o.getAreaName()));
    }
}
