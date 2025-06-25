package com.hss.modules.facility.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.facility.entity.DeviceRunLog;
import com.hss.modules.facility.mapper.DeviceRunLogMapper;
import com.hss.modules.facility.model.DeviceRunLogBO;
import com.hss.modules.facility.model.DeviceRunLogDTO;
import com.hss.modules.facility.model.DeviceRunLogVO;
import com.hss.modules.facility.service.IDeviceRunLogService;
import com.hss.modules.tool.TimeStr;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 动用使用
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Service
public class DeviceRunLogServiceImpl extends ServiceImpl<DeviceRunLogMapper, DeviceRunLog> implements IDeviceRunLogService {


    @Override
    public void add(String id, DeviceRunLogBO source) {
        int total = 0;
        DeviceRunLog last = getLastByDeviceId(id);
        if (last  != null) {
            total = last.getTotalDuration();
        }
        int duration = 0;
        if (source.getEndDate() != null) {
            long ms = source.getEndDate().getTime() - source.getStartDate().getTime();
            int s = (int)(ms / 1000L);
            duration = s;
            total = total + s;
        }

        if (source.getEndDate() == null) {
            DeviceRunLog deviceRunLog = new DeviceRunLog();
            deviceRunLog.setDevId(id);
            deviceRunLog.setStartTime(source.getStartDate());
            deviceRunLog.setStopTime(source.getEndDate());
            deviceRunLog.setDuration(duration );
            deviceRunLog.setTotalDuration(total);
            save(deviceRunLog);
        } else {
            if (last == null) {
                return;
            }
            DeviceRunLog deviceRunLog = new DeviceRunLog();
            deviceRunLog.setId(last.getId());
            deviceRunLog.setStopTime(source.getEndDate());
            deviceRunLog.setDuration(duration);
            deviceRunLog.setTotalDuration(total);
            updateById(deviceRunLog);
        }



    }

    @Override
    public IPage<DeviceRunLogVO> logPage(DeviceRunLogDTO dto, List<String> typeIds) {
        IPage<DeviceRunLog> page = baseMapper.page(new Page<>(dto.getPageNo(), dto.getPageSize()), dto.getName(), typeIds, dto.getDevId());
        return page.convert(e -> {
            DeviceRunLogVO vo = new DeviceRunLogVO();
            vo.setId(e.getId());
            vo.setDevId(e.getDevId());
            vo.setDevId_disp(e.getDevName());
            vo.setStartTime(e.getStartTime());
            vo.setStopTime(e.getStopTime());
            int d = e.getStopTime() == null ? (int) ((System.currentTimeMillis() - e.getStartTime().getTime()) / 1000L) : e.getDuration();
            vo.setRemark(e.getStopTime() == null ? "运行中" : "已停止");
            vo.setDuration(TimeStr.getTimeStr(d));
            vo.setTotalDuration(TimeStr.getTimeStr(e.getStartTime() == null ? e.getTotalDuration() + d : e.getTotalDuration()));
            return vo;
        });
    }




    private DeviceRunLog getLastByDeviceId(String deviceId) {
        return baseMapper.getLastByDeviceId(deviceId);
    }
}
