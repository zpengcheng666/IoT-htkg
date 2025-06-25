package com.hss.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.system.entity.BaseMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 系统菜单
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface BaseMenuMapper extends BaseMapper<BaseMenu> {
    List<BaseMenu> queryByUser(@Param("userId") String userId);

    List<BaseMenu> queryMenuTreeListByUser(String userId ,String title);

    int countTitle(String title);

    int countAuth(String auth);

    List<BaseMenu> getauthListByUser(String userId);

    List<BaseMenu> queryMenuTreeListAll(String userId ,String title);
}
