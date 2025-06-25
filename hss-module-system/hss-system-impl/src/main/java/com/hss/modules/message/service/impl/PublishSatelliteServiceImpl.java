package com.hss.modules.message.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.message.constant.MessageStateEnum;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.PublishSatellite;
import com.hss.modules.message.entity.PublishSatelliteTerminal;
import com.hss.modules.message.event.SatelliteArrivedEvent;
import com.hss.modules.message.mapper.PublishSatelliteMapper;
import com.hss.modules.message.model.PublishSatelliteVO;
import com.hss.modules.message.service.IPublishSatelliteService;
import com.hss.modules.message.service.IPublishSatelliteTerminalService;
import com.hss.modules.message.util.TimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PublishSatelliteServiceImpl extends ServiceImpl<PublishSatelliteMapper, PublishSatellite> implements IPublishSatelliteService {

    @Autowired
    private IPublishSatelliteTerminalService publishSatelliteTerminalService;
    @Autowired
    private ApplicationContext applicationContext;
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PublishSatellite publishSatellite) {
        check(publishSatellite);
        save(publishSatellite);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<PublishSatellite> entityList) {
        for (PublishSatellite publishSatellite : entityList) {
            check(publishSatellite);
        }
        return super.saveBatch(entityList);
    }

    /**
     * 校验数据
     * @param publishSatellite 信息
     */
    private void check(PublishSatellite publishSatellite) {
        if (StringUtils.isBlank(publishSatellite.getName())) {
            throw new HssBootException("卫星名称不能为空");
        }
        if (StringUtils.isBlank(publishSatellite.getNationality())) {
            throw new HssBootException("卫星国别不能为空");
        }
        if (StringUtils.isBlank(publishSatellite.getSatelliteType())) {
            throw new HssBootException("卫星类型不能为空");
        }
        if (publishSatellite.getEnterTime() == null) {
            throw new HssBootException("进入时间不能为空");
        }
        if (publishSatellite.getLeaveTime() == null) {
            throw new HssBootException("离开时间不能为空");
        }
        if (!publishSatellite.getLeaveTime().isAfter(publishSatellite.getEnterTime())) {
            throw new HssBootException("离开时间必须大于进入时间");
        }
        publishSatellite.setState(MessageStateEnum.NOT_PUBLISH.value);
        publishSatellite.setPeriod(TimeUtil.getDurationStr(publishSatellite.getEnterTime(), publishSatellite.getLeaveTime()));

    }

    @Override
    public void edit(PublishSatellite publishSatellite) {
        check(publishSatellite);
        publishSatelliteTerminalService.delByMessageId(publishSatellite.getId());
        updateById(publishSatellite);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(PublishMessageDTO dto) {
        PublishSatellite byId = getById(dto.getMessageId());
        if (byId == null ){
            return;
        }
        if (!MessageStateEnum.NOT_PUBLISH.value.equals(byId.getState())) {
            throw new HssBootException("不是未发布状态");
        }
        PublishSatellite update = new PublishSatellite();
        update.setId(byId.getId());
        update.setState(MessageStateEnum.PUBLISH.value);
        update.setPublishTime(LocalDateTime.now());
        updateById(update);
        List<PublishSatelliteTerminal> publishDutyTerminals = dto.getTerminalIds().stream().map(id -> {
            PublishSatelliteTerminal publishSatelliteTerminal = new PublishSatelliteTerminal();
            publishSatelliteTerminal.setSatelliteId(dto.getMessageId());
            publishSatelliteTerminal.setTerminalId(id);
            return publishSatelliteTerminal;
        }).collect(Collectors.toList());
        publishSatelliteTerminalService.saveBatch(publishDutyTerminals);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revocation(String id) {
        PublishSatellite byId = getById(id);
        if (byId == null){
            return;
        }
        if (MessageStateEnum.NOT_PUBLISH.value.equals(byId.getState())) {
            return;
        }
        PublishSatellite update = new PublishSatellite();
        update.setId(id);
        update.setState(MessageStateEnum.NOT_PUBLISH.value);
        updateById(update);
        publishSatelliteTerminalService.delByMessageId(id);
    }
    @Override
    public IPage<PublishSatelliteVO> getPage(Page<PublishSatelliteVO> page, List<String> terminalIds) {
        IPage<PublishSatelliteVO> pageResult;
        if (CollectionUtils.isEmpty(terminalIds)){
            pageResult = baseMapper.getPage(page);
        } else {
            pageResult = baseMapper.getPageByTerminal(page, terminalIds);
        }
        for (PublishSatelliteVO vo : pageResult.getRecords()) {
            List<String> terminalId = publishSatelliteTerminalService.listTerminalIdByMessageId(Collections.singletonList(vo.getId()));
            vo.setTerminalIds(terminalId);
        }
        return pageResult;
    }



    @Override
    public void checkState() {
        List<PublishSatellite> list = baseMapper.listNoOver();
        LocalDateTime now = LocalDateTime.now();
        // 非周期
        List<PublishSatellite> updateNoCycle = list.stream()
                .map(e -> {
                    Integer state = e.getState();
                    if (!e.getLeaveTime().isAfter(now)) {
                        state = MessageStateEnum.OVER.value;
                    }else  if (!MessageStateEnum.NOT_PUBLISH.value.equals(e.getState()) && !e.getEnterTime().isAfter(now) ) {
                        state =  MessageStateEnum.ENTER.value;
                    }
                    if (state.equals(e.getState())) {
                        return null;
                    }
                    e.setState(state);
                    return e;
                })
                .filter(Objects::nonNull)
                .map(e -> {
                    PublishSatellite upDate = new PublishSatellite();
                    upDate.setId(e.getId());
                    upDate.setState(e.getState());
                    return upDate;
                })
                .collect(Collectors.toList());
        if (!updateNoCycle.isEmpty()) {
            updateBatchById(updateNoCycle);
            if (updateNoCycle.stream().map(PublishSatellite::getState)
                    .anyMatch(MessageStateEnum.ENTER.value::equals)) {
                SatelliteArrivedEvent arrivedEvent = new SatelliteArrivedEvent("message");
                this.applicationContext.publishEvent(arrivedEvent);
            }
        }

    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        PublishSatellite byId = getById(id);
        if (byId == null){
            return;
        }
        removeById(id);
        publishSatelliteTerminalService.delByMessageId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<String> ids) {
        List<PublishSatellite> publishSatellites = listByIds(ids);
        for (PublishSatellite publishSatellite : publishSatellites) {
            delete(publishSatellite.getId());
        }
    }
    @Override
    public List<PublishSatellite> listPublishByTerminalId(String terminalId) {
        return baseMapper.listPublishByTerminalId(terminalId);
    }

}
