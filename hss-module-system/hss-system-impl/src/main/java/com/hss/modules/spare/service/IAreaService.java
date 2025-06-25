package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.Area;

import java.util.Map;
import java.util.Set;

/**
 * @Description: 库区
 * @Author: zpc
 * @Date:   2024-04-25
 * @Version: V1.0
 */
public interface IAreaService extends IService<Area> {

    /**
     * 获取库区名称map
     *
     * @param areaIds
     * @return
     */
    Map<String, String> mapAreaIdMap(Set<String> areaIds);
}
