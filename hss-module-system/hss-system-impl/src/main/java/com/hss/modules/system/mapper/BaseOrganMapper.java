package com.hss.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.system.entity.BaseOrgan;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Description: 组织机构
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface BaseOrganMapper extends BaseMapper<BaseOrgan> {
    /**
     * 根据pid查询下级组织结构
     * @param pid 父节点ID
     * @return List<BaseOrgan>
     */
    List<BaseOrgan> queryTreeListByPid(@Param("pid") String pid);
}
