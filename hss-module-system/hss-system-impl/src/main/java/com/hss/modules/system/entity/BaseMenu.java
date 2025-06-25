package com.hss.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 系统菜单
 * @Author: zpc
 * @Date: 2022-11-03
 * @Version: V1.0
 */
@Data
@TableName(value = "BASE_MENU", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BASE_MENU对象", description = "系统菜单")
public class BaseMenu {
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
    private java.lang.String id;
    /**
     * 组件名称
     */
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
    private Boolean sidebar;

    @ApiModelProperty(value = "权限")
    private String auth;

    @ApiModelProperty(value = "是否右上角导航")
    private Boolean hide;

    @ApiModelProperty(value = "菜单类型菜单类型0目录，1菜单，2内嵌页,3按钮")
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

    /**deleted*/
    @ApiModelProperty(value = "软删除，默认0：不删除；1：删除")
    @TableField(select = true)
    @TableLogic//逻辑删除注解，同一个class中不支持多个此注解
    private java.lang.Integer deleted;
}
