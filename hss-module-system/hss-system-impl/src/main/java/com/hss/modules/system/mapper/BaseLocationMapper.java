package com.hss.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.system.entity.BaseLocation;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Description: 存储位置信息
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface BaseLocationMapper extends BaseMapper<BaseLocation> {
    /**
     * 根据pid查询下级存储位置信息
     * @param pid 父节点ID
     * @return List<BaseLocation>
     */
    List<BaseLocation> queryTreeListByPid(@Param("pid") String pid);

}
