package com.hss.modules.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.system.entity.BaseRemind;
import org.springframework.stereotype.Repository;

/**
 * @Description: 提醒设置
 * @Author: zpc
 * @Date:   2022-11-21
 * @Version: V1.0
 */
@Repository
public interface BaseRemindMapper extends BaseMapper<BaseRemind> {
    void updateValue(BaseRemind baseRemind);
}
