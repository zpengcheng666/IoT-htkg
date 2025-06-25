package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.system.entity.BaseMenu;
import com.hss.modules.system.model.MenuTreeListModel;

import java.util.List;

/**
 * @Description: 系统菜单
 * @Author: zpc
 * @Date: 2022-11-03
 * @Version: V1.0
 */
public interface IBaseMenuService extends IService<BaseMenu> {

    /**
     * @description: 查询所有菜单
     * @date 2023/5/18 14:49
     */
    List<MenuTreeListModel> queryMenuTreeList(String title, Integer status);

    /**
     * @description: admin查询所有菜单
     * 如果是其他用户，按照用户id查询拥有的菜单权限
     * @date 2023/5/18 14:50
     */
    List<MenuTreeListModel> queryMenuTreeListByUser(String userId, String title);

    int countTitle(String title);

    int countAuth(String auth);

    /**
     * @param userId
     * @return
     * @description: 获取当前用户权限标识符
     */
    List<String> getAuths(String userId);

}
