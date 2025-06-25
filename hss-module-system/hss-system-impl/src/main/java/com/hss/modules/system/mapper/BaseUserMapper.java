package com.hss.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.system.entity.BaseUser;

/**
 * @Description: 用户表
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface BaseUserMapper extends BaseMapper<BaseUser> {
    void changePwd(BaseUser baseUser);

    String organName(String organizationId);

    String papersName(String papersId);
}
