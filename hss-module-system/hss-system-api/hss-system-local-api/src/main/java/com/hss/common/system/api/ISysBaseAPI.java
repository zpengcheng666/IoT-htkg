package com.hss.common.system.api;

import com.hss.core.common.api.CommonAPI;

import java.util.List;
import java.util.Set;

/**
 * @Description 底层共通业务API，提供其他独立模块调用
 * @Author zpc
 * @Date 2019-4-20
 * @Version V1.0
 */
public interface ISysBaseAPI extends CommonAPI {
    /**
     * 17查询指定table的 text code 获取字典，包含text和value
     *
     * @param table
     * @param text
     * @param code
     * @param keyArray
     * @return
     */
    @Deprecated
    List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray);

    /**
     * 31获取用户的角色集合
     *
     * @param username
     * @return
     */
    Set<String> getUserRoleSet(String username);

    /**
     * 32获取用户的权限集合
     *
     * @param username
     * @return
     */
    Set<String> getUserPermissionSet(String username);

}
