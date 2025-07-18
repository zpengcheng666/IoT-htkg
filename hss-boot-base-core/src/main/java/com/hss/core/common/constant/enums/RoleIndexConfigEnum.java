package com.hss.core.common.constant.enums;

import com.hss.core.common.util.OConvertUtils;

import java.util.List;

/**
 * 首页自定义
 * 通过角色编码与首页组件路径配置
 * 枚举的顺序有权限高低权重作用（也就是配置多个角色，在前面的角色首页，会优先生效）
 * @author: jeecg-boot
 */
public enum RoleIndexConfigEnum {

    /**首页自定义 admin*/
    ADMIN("admin", "dashboard/Analysis"),
    //TEST("test",  "dashboard/IndexChart"),
    /**首页自定义 hr*/
    HR("hr", "dashboard/IndexBdc");
    //DM("dm", "dashboard/IndexTask"),

    /**
     * 角色编码
     */
    String roleCode;
    /**
     * 路由index
     */
    String componentUrl;

    /**
     * 构造器
     *
     * @param roleCode 角色编码
     * @param componentUrl 首页组件路径（规则跟菜单配置一样）
     */
    RoleIndexConfigEnum(String roleCode, String componentUrl) {
        this.roleCode = roleCode;
        this.componentUrl = componentUrl;
    }
    /**
     * 根据code找枚举
     * @param roleCode 角色编码
     * @return
     */
    private static RoleIndexConfigEnum getEnumByCode(String roleCode) {
        for (RoleIndexConfigEnum e : RoleIndexConfigEnum.values()) {
            if (e.roleCode.equals(roleCode)) {
                return e;
            }
        }
        return null;
    }
    /**
     * 根据code找index
     * @param roleCode 角色编码
     * @return
     */
    private static String getIndexByCode(String roleCode) {
        for (RoleIndexConfigEnum e : RoleIndexConfigEnum.values()) {
            if (e.roleCode.equals(roleCode)) {
                return e.componentUrl;
            }
        }
        return null;
    }

    public static String getIndexByRoles(List<String> roles) {
        String[] rolesArray = roles.toArray(new String[roles.size()]);
        for (RoleIndexConfigEnum e : RoleIndexConfigEnum.values()) {
            if (OConvertUtils.isIn(e.roleCode,rolesArray)){
                return e.componentUrl;
            }
        }
        return null;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getComponentUrl() {
        return componentUrl;
    }

    public void setComponentUrl(String componentUrl) {
        this.componentUrl = componentUrl;
    }
}
