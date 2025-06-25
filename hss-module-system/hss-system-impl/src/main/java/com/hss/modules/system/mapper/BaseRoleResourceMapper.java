package com.hss.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.system.entity.BaseRoleMenu;
import org.springframework.stereotype.Repository;

/**
 * @Description: 角色、资源关系
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Repository
public interface BaseRoleResourceMapper extends BaseMapper<BaseRoleMenu> {

}
