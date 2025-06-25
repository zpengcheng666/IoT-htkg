package com.hss.modules.system.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
* @description: 菜单列表
* @author zpc
* @date 2024/3/21 13:33
* @version 1.0
*/
@Data
public class MenuTreeListModel {
    private String id;

    @ApiModelProperty(value = "组件名称")
    private java.lang.String name;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "组件")
    private String component;

    @ApiModelProperty(value = "排序")
    private Integer sortNumber;

    @ApiModelProperty(value = "上级菜单")
    private String parent;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "是否是菜单页，默认true")
    private boolean sidebar;

    @ApiModelProperty(value = "权限")
    private String auth;

    @ApiModelProperty(value = "是否右上角导航")
    private Boolean hide;

    private Boolean isLeaf;

    @ApiModelProperty(value = "菜单类型0目录，1菜单，2按钮")
    private Integer menuType;

    @ApiModelProperty(value = "权限标识符")
    private String perms;

    @ApiModelProperty(value = "是否显示菜单0不显示，1显示")
    private Integer status;

    @ApiModelProperty(value = "默认菜单跳转地址")
    private String redirect;

    @ApiModelProperty(value = "是否场景菜单，默认0")
    private Integer isScene;

    @ApiModelProperty(value = "是否是场景")
    private String selectScene;

    @ApiModelProperty(value = "场景类型")
    private String sceneType;

    private List<MenuTreeListModel> children = new ArrayList<>();

    public void addChild(MenuTreeListModel child){
        if (this.children == null){
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }
}
