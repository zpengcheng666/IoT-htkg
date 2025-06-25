package com.hss.modules.scada.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.scada.entity.GsChangJing;

import java.util.List;

/**
* @description: 场景管理相关sql查询
* @author zpc
* @date 2024/3/19 14:12
* @version 1.0
*/
public interface GsChangJingMapper extends BaseMapper<GsChangJing> {

    /**
     * 查询已发布的场景的设备id
     * @return
     */
    List<String> listDeviceIdPublishChangJing();

    /**
     * 查村发布场景的ids
     * @return
     */
    List<String> listIdsByPublish();

    /**
     * 根据子系统id查询设备id列表
     * @param subSystemId
     * @return
     */
    List<String> listDeviceIdBySubSystemId(String subSystemId);

    /**
     * 根据设备id查询场景的基本信息
     * @param deviceId
     * @return
     */
    List<GsChangJing> listNameAndSystemByDeviceId(String deviceId);

    /**
     * 场景菜单
     * @param sumSystem
     * @return
     */
    List<GsChangJing> listMenu(String sumSystem);
}
