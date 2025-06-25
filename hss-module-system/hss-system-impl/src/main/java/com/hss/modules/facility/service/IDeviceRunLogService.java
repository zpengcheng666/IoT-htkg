package com.hss.modules.facility.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.facility.entity.DeviceRunLog;
import com.hss.modules.facility.model.DeviceRunLogBO;
import com.hss.modules.facility.model.DeviceRunLogDTO;
import com.hss.modules.facility.model.DeviceRunLogVO;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description:
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface IDeviceRunLogService extends IService<DeviceRunLog> {

    void add(String id, DeviceRunLogBO source);

    /**
     * 分页查询
     * @param dto
     * @param typeIds
     * @return
     */
    IPage<DeviceRunLogVO> logPage(DeviceRunLogDTO dto, @Nullable List<String> typeIds);
}
