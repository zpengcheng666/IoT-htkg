package com.hss.modules.scada.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.entity.GSChangJingSheBei;
import com.hss.modules.scada.mapper.GSChangJingSheBeiMapper;
import com.hss.modules.scada.model.DeviceSortVO;
import com.hss.modules.scada.service.IGSChangJingSheBeiService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author zpc
 * @version 1.0
 * @description: 场景、设备关联关系表，获取设备ids、根据场景id删除场景中对应设备、根据设备id获取场景id、根据设备id删除场景中设备、
 * 查询设备关联的场景id、获取排序列表、保存场景设备排序
 * @date 2024/3/19 15:34
 */
@Service
public class GSChangJingSheBeiServiceImpl extends ServiceImpl<GSChangJingSheBeiMapper, GSChangJingSheBei> implements IGSChangJingSheBeiService {

    @Override
    public List<String> listDeviceIdsBySceneId(String sceneId) {
        return baseMapper.listDeviceIdsBySceneId(sceneId);
    }

    @Override
    public void deleteBySceneId(String sceneId) {
        baseMapper.deleteBySceneId(sceneId);
    }

    @Override
    public String getSceneIdByDeviceId(String deviceId) {
        return baseMapper.getSceneIdByDeviceId(deviceId);
    }

    @Override
    public void deleteByDeviceId(String deviceId) {
        baseMapper.deleteByDeviceId(deviceId);
    }

    @Override
    @CacheEvict(value = ScadaConstant.REDIS_KEY_DEVICE_SCENE, allEntries = true)
    public boolean removeByIds(Collection<?> list) {
        return super.removeByIds(list);
    }

    @Override
    @CacheEvict(value = ScadaConstant.REDIS_KEY_DEVICE_SCENE, allEntries = true)
    public boolean saveBatch(Collection<GSChangJingSheBei> entityList, int batchSize) {
        return super.saveBatch(entityList, batchSize);
    }

    @Override
    @Cacheable(ScadaConstant.REDIS_KEY_DEVICE_SCENE)
    public boolean saveBatch(Collection<GSChangJingSheBei> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @Cacheable(ScadaConstant.REDIS_KEY_DEVICE_SCENE)
    public List<String> listSceneIdByDeviceId(String deviceId) {
        return baseMapper.listSceneIdByDeviceId(deviceId);
    }

    @Override
    public List<DeviceSortVO> listSortDevice(String sceneId) {
        return baseMapper.listSortDevice(sceneId);
    }

    @Override
    public void saveDeviceSort(List<DeviceSortVO> list) {
        ArrayList<GSChangJingSheBei> updateList = new ArrayList<>(list.size());
        int i = 1;
        for (DeviceSortVO deviceSortVO : list) {
            GSChangJingSheBei gsChangJingSheBei = new GSChangJingSheBei();
            gsChangJingSheBei.setId(deviceSortVO.getId());
            gsChangJingSheBei.setSortNumber(i);
            updateList.add(gsChangJingSheBei);
            i++;
        }
        updateBatchById(updateList);
    }
}
