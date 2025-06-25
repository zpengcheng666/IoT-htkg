package com.hss.modules.spare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.spare.entity.Area;
import com.hss.modules.spare.model.AreaName;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Description: 库区
 * @Author: jeecg-boot
 * @Date:   2024-04-25
 * @Version: V1.0
 */
public interface AreaMapper extends BaseMapper<Area> {

    List<AreaName> listNames(@Param("areaIds") Set<String> areaIds);
}
