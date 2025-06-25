package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.util.LoginUserUtils;
import com.hss.modules.system.entity.BaseMenu;
import com.hss.modules.system.mapper.BaseMenuMapper;
import com.hss.modules.system.model.MenuTreeListModel;
import com.hss.modules.system.service.IBaseMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 系统菜单
 * @Author: zpc
 * @Date: 2022-11-03
 * @Version: V1.0
 */
@Service
public class BaseMenuServiceImpl extends ServiceImpl<BaseMenuMapper, BaseMenu> implements IBaseMenuService {

    @Autowired
    private IBaseMenuService baseMenuService;

    private static final String ADMIN = "admin";
    private static final String ADMINISTRATOR = "administrator";

    /**
     * @description: 查询所有菜单列表
     * @date 2023/5/18 14:49
     */
    @Override
    public List<MenuTreeListModel> queryMenuTreeList(String title, Integer status) {
        LambdaQueryWrapper<BaseMenu> query = new LambdaQueryWrapper<>();
        query.orderByAsc(BaseMenu::getSortNumber);
        query.eq(BaseMenu::getDeleted,0);
        if (status != null) {
            query.eq(BaseMenu::getStatus, status);
        }
        if (StringUtils.isNotEmpty(title)) {
            //名称模糊查询
            query.like(BaseMenu::getTitle, title);
        }
        List<BaseMenu> menuList = this.list(query);
        List<MenuTreeListModel> treeModelList = new ArrayList<>();

        //如果没有菜单名称，查询所有
        if (StringUtils.isEmpty(title)) {
            for (int i = 0; i < menuList.size(); i++) {
                BaseMenu tmp = menuList.get(i);
                if (StringUtils.isEmpty(tmp.getParent()) || tmp.getParent().equals("0")) {
                    treeModelList.add(createTreeNode(tmp, menuList));
                }
            }
        }
        //有菜单名称，支持模糊查询
        if (StringUtils.isNotEmpty(title)) {
            for (int i = 0; i < menuList.size(); i++) {
                BaseMenu tmp = menuList.get(i);
                if (StringUtils.isNotEmpty(tmp.getParent()) || !tmp.getParent().equals("0")) {
                    treeModelList.add(createTreeNode(tmp, menuList));
                }
            }
        }

        return treeModelList;
    }

    /**
     * @description: 如果是admin查询所有
     * 如果是其他用户，按照用户id查询拥有的菜单权限
     * @date 2023/5/18 14:50
     */
    @Override
    public List<MenuTreeListModel> queryMenuTreeListByUser(String userId, String title) {
        List<MenuTreeListModel> treeModelList = new ArrayList<>();
        String username = LoginUserUtils.getUser().getUsername();
        if (!ADMIN.equals(username) && !ADMINISTRATOR.equals(username)) {
            List<BaseMenu> menuList = this.baseMapper.queryMenuTreeListByUser(userId, title);
            for (int i = 0; i < menuList.size(); i++) {
                BaseMenu tmp = menuList.get(i);
                if (StringUtils.isEmpty(tmp.getParent()) || tmp.getParent().equals("0")) {
                    treeModelList.add(createTreeNode(tmp, menuList));
                }
            }
        }
        if (ADMIN.equals(username) || ADMINISTRATOR.equals(username)) {
            List<BaseMenu> queryMenuTreeListAll = this.baseMapper.queryMenuTreeListAll(userId, title);
            for (int i = 0; i < queryMenuTreeListAll.size(); i++) {
                BaseMenu tmp = queryMenuTreeListAll.get(i);
                if (StringUtils.isEmpty(tmp.getParent()) || tmp.getParent().equals("0")) {
                    treeModelList.add(createTreeNode(tmp, queryMenuTreeListAll));
                }
            }
        }

        return treeModelList;
    }

    @Override
    public int countTitle(String title) {
        return this.baseMapper.countTitle(title);
    }

    @Override
    public int countAuth(String auth) {
        return this.baseMapper.countAuth(auth);
    }

    @Override
    public List<String> getAuths(String userId) {
        String username = LoginUserUtils.getUser().getUsername();
        LambdaQueryWrapper<BaseMenu> query = new LambdaQueryWrapper<>();
        query.eq(BaseMenu::getMenuType, 3);
        query.eq(BaseMenu::getStatus, 0);

        List<String> arrayList = new ArrayList<>();

        if (ADMIN.equals(username) || ADMINISTRATOR.equals(username)) {
            List<BaseMenu> list = baseMenuService.list(query);
            List<String> authList = list.stream().map(BaseMenu::getAuth).collect(Collectors.toList());
            arrayList.addAll(authList);
        } else {
            List<BaseMenu> list = this.baseMapper.getauthListByUser(userId);
            List<String> auths = list.stream().map(BaseMenu::getAuth).filter(Objects::nonNull).collect(Collectors.toList());
            arrayList.addAll(auths);
        }

        return arrayList;
    }

    private MenuTreeListModel createTreeNode(BaseMenu menu, List<BaseMenu> list) {
        MenuTreeListModel model = new MenuTreeListModel();
        model.setId(menu.getId());
        model.setTitle(menu.getTitle());
        model.setAuth(menu.getAuth());
        model.setComponent(menu.getComponent());
        model.setHide(menu.getHide());
        model.setSidebar(menu.getSidebar());
        model.setIcon(menu.getIcon());
        model.setSortNumber(menu.getSortNumber());
        model.setName(menu.getName());
        model.setParent(menu.getParent());
        model.setPath(menu.getPath());
        model.setMenuType(menu.getMenuType());
        model.setPerms(menu.getPerms());
        model.setStatus(menu.getStatus());
        model.setRedirect(menu.getRedirect());
        model.setIsScene(menu.getIsScene());
        model.setSelectScene(menu.getSelectScene());
        model.setSceneType(menu.getSceneType());

        for (int i = 0; i < list.size(); i++) {
            BaseMenu tmp = list.get(i);
            if (StringUtils.equals(tmp.getParent(), menu.getId())) {
                model.addChild(createTreeNode(tmp, list));
            }
        }
        return model;
    }
}
