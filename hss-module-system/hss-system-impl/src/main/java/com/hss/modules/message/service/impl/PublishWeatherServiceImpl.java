package com.hss.modules.message.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.PublishWeather;
import com.hss.modules.message.entity.PublishWeatherTerminal;
import com.hss.modules.message.mapper.PublishWeatherMapper;
import com.hss.modules.message.service.IPublishWeatherService;
import com.hss.modules.message.service.IPublishWeatherTerminalService;
import com.hss.modules.message.task.WeatherOverTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 气象信息
 * @Author: zpc
 * @Date:   2022-12-06
 * @Version: V1.0
 */
@Service
public class PublishWeatherServiceImpl extends ServiceImpl<PublishWeatherMapper, PublishWeather> implements IPublishWeatherService {

    @Autowired
    private IPublishWeatherTerminalService publishWeatherTerminalService;
    @Autowired
    private WeatherOverTask weatherOverTask;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(PublishMessageDTO dto) {
        PublishWeather byId = getById(dto.getMessageId());
        byId.setState(2);
        updateById(byId);
        List<PublishWeatherTerminal> publishDutyTerminals = dto.getTerminalIds().stream().map(id -> {
            PublishWeatherTerminal publishWeatherTerminal = new PublishWeatherTerminal();
            publishWeatherTerminal.setWeatherId(dto.getMessageId());
            publishWeatherTerminal.setTerminalId(id);
            return publishWeatherTerminal;
        }).collect(Collectors.toList());
        publishWeatherTerminalService.saveBatch(publishDutyTerminals);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revocation(String id) {
        PublishWeather byId = getById(id);
        if (byId == null){
            return;
        }
        PublishWeather publishWeather = new PublishWeather();
        publishWeather.setId(id);
        publishWeather.setState(0);
        updateById(publishWeather);
        publishWeatherTerminalService.delByMessageId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(PublishWeather publishWeather) {
        check(publishWeather);
        updateById(publishWeather);
        PublishWeather byId = getById(publishWeather.getId());
        if (byId.getState() != 0){
            revocation(byId.getId());
        }
        weatherOverTask.updateTask(byId);
    }

    private void check(PublishWeather publishWeather) {
        if (Float.parseFloat(publishWeather.getHighTmp()) < Float.parseFloat(publishWeather.getLowTmp())){
            throw new HssBootException("最高气温不能小于最低气温");
        }
    }

    @Override
    public IPage<PublishWeather> queryPage(Page<PublishWeather> page, List<String> terminalIds) {
        page.setSearchCount(false);
        page.setTotal(baseMapper.queryCount(terminalIds));
        return baseMapper.queryPage(page, terminalIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        PublishWeather byId = getById(id);
        if (byId == null){
            return;
        }
        removeById(id);
        publishWeatherTerminalService.delByMessageId(id);
        weatherOverTask.removeTask(byId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<String> ids) {
        removeByIds(ids);
        publishWeatherTerminalService.deleteBatchByMessageIds(ids);

    }

    @Override
    public List<PublishWeather> getByTerminal(String terminalId) {
        List<PublishWeather> list = baseMapper.getByTerminal(terminalId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        for (PublishWeather publishWeather : list) {
            publishWeather.setWeatherTimeStr(simpleDateFormat.format(publishWeather.getWeatherTime()));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PublishWeather publishWeather) {
        check(publishWeather);
        publishWeather.setState(0);
        checkDay(publishWeather);
        save(publishWeather);
        weatherOverTask.addTask(publishWeather);
    }

    /**
     * 校验 气象日期是否唯一
     * @param publishWeather
     */
    private void checkDay(PublishWeather publishWeather) {
        Date weatherTime = publishWeather.getWeatherTime();
        int count = baseMapper.countByDay(weatherTime);
        if (count > 0){
            throw new HssBootException("所选日期已存在气象信息,不可重复添加");
        }
    }

    @Override
    public List<PublishWeather> listNotOver() {
        return baseMapper.listNotOver();
    }

    @Override
    public void overMessage(PublishWeather message) {
        PublishWeather byId = getById(message.getId());
        if (byId == null || byId.getState().equals(3)){
            return;
        }
        PublishWeather entity = new PublishWeather();
        entity.setId(message.getId());
        entity.setState(3);
        updateById(entity);
    }
}
