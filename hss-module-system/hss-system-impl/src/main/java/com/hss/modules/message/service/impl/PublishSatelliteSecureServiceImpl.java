package com.hss.modules.message.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.message.constant.MessageStateEnum;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.PublishSatelliteSecure;
import com.hss.modules.message.mapper.PublishSatelliteSecureMapper;
import com.hss.modules.message.model.PublishSatelliteSecureVO;
import com.hss.modules.message.service.IPublishSatelliteSecureService;
import com.hss.modules.message.service.IPublishSatelliteSecureTerminalService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 26060
 */
@Service
public class PublishSatelliteSecureServiceImpl extends ServiceImpl<PublishSatelliteSecureMapper, PublishSatelliteSecure> implements IPublishSatelliteSecureService {

    @Autowired
    private IPublishSatelliteSecureTerminalService publishSatelliteSecureTerminalService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PublishSatelliteSecure entity) {
        check(entity);
        save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<PublishSatelliteSecure> entityList) {
        for (PublishSatelliteSecure publishSatellite : entityList) {
            check(publishSatellite);
        }
        return super.saveBatch(entityList);
    }

    /**
     * 校验数据
     * @param entity 信息
     */
    private void check(PublishSatelliteSecure entity) {
        if (entity.getStartTime() == null) {
            throw new HssBootException("开始时间不能为空");
        }
        if (entity.getEndTime() == null) {
            throw new HssBootException("结束时间不能为空");
        }
        if (!entity.getEndTime().isAfter(entity.getStartTime())) {
            throw new HssBootException("结束时间必须大于开始时间");
        }
        entity.setState(MessageStateEnum.NOT_PUBLISH.value);
    }

    @Override
    public void edit(PublishSatelliteSecure entity) {
        check(entity);
        updateById(entity);
        publishSatelliteSecureTerminalService.removeByMsgId(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(PublishMessageDTO dto) {
        PublishSatelliteSecure byId = getById(dto.getMessageId());
        if (byId == null ){
            return;
        }
        if (!MessageStateEnum.NOT_PUBLISH.value.equals(byId.getState())) {
            throw new HssBootException("不是未发布状态");
        }
        PublishSatelliteSecure update = new PublishSatelliteSecure();
        update.setId(byId.getId());
        update.setState(MessageStateEnum.PUBLISH.value);
        update.setPublishTime(LocalDateTime.now());
        updateById(update);
        publishSatelliteSecureTerminalService.add(byId.getId(), dto.getTerminalIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revocation(String id) {
        PublishSatelliteSecure byId = getById(id);
        if (byId == null){
            return;
        }
        if (MessageStateEnum.NOT_PUBLISH.value.equals(byId.getState())) {
            return;
        }
        PublishSatelliteSecure update = new PublishSatelliteSecure();
        update.setId(id);
        update.setState(MessageStateEnum.NOT_PUBLISH.value);
        updateById(update);
        publishSatelliteSecureTerminalService.removeByMsgId(id);
    }
    @Override
    public IPage<PublishSatelliteSecureVO> getPage(Page<PublishSatelliteSecureVO> page, List<String> terminalIds) {
        IPage<PublishSatelliteSecureVO> pageResult;
        if (CollectionUtils.isEmpty(terminalIds)){
            pageResult = baseMapper.getPage(page);
        } else {
            pageResult = baseMapper.getPageByTerminal(page, terminalIds);
        }
        for (PublishSatelliteSecureVO vo : pageResult.getRecords()) {
            List<String> terminalId = publishSatelliteSecureTerminalService.listTerminalIdByMessageId(Collections.singletonList(vo.getId()));
            vo.setTerminalIds(terminalId);
        }
        return pageResult;
    }



    @Override
    public void checkState() {
        List<PublishSatelliteSecure> list = baseMapper.listNoOver();
        LocalDateTime now = LocalDateTime.now();
        // 非周期
        List<PublishSatelliteSecure> updateNoCycle = list.stream()
                .map(e -> {
                    Integer state = e.getState();
                    if (!e.getEndTime().isAfter(now)) {
                        state = MessageStateEnum.OVER.value;
                    }else  if (!MessageStateEnum.NOT_PUBLISH.value.equals(e.getState()) && !e.getStartTime().isBefore(now) ) {
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
                    PublishSatelliteSecure upDate = new PublishSatelliteSecure();
                    upDate.setId(e.getId());
                    upDate.setState(e.getState());
                    return upDate;
                })
                .collect(Collectors.toList());
        if (!updateNoCycle.isEmpty()) {
            updateBatchById(updateNoCycle);
        }

    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        PublishSatelliteSecure byId = getById(id);
        if (byId == null){
            return;
        }
        removeById(id);
        publishSatelliteSecureTerminalService.removeByMsgId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<String> ids) {
        List<PublishSatelliteSecure> publishSatellites = listByIds(ids);
        for (PublishSatelliteSecure publishSatellite : publishSatellites) {
            delete(publishSatellite.getId());
        }
    }
    @Override
    public List<PublishSatelliteSecure> listPublishByTerminalId(String terminalId) {
        return baseMapper.listPublishByTerminalId(terminalId);
    }

}
