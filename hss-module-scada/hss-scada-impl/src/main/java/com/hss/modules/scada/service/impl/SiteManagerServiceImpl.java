package com.hss.modules.scada.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.scada.entity.ConWangGuan;
import com.hss.modules.scada.entity.SiteManager;
import com.hss.modules.scada.mapper.SiteManagerMapper;
import com.hss.modules.scada.model.SetSiteStateDTO;
import com.hss.modules.scada.model.SiteRequestParam;
import com.hss.modules.scada.model.SiteStateUpdateDTO;
import com.hss.modules.scada.service.IConWangGuanService;
import com.hss.modules.scada.service.ISiteManagerService;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 站点管理的增删改查，保存站点、站点状态切换、系统启动获取远程站点装填、获取本地站点状态、保存网关列表
 * @Author: zpc
 * @Date:   2024/3/19 13:44
 * @Version: V1.0
 */
@Service
public class SiteManagerServiceImpl extends ServiceImpl<SiteManagerMapper, SiteManager> implements ISiteManagerService {

    @Autowired
    private IConWangGuanService conWangGuanService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SiteManager siteManager) {
        siteManager.setSiteState(0);
        save(siteManager);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SiteManager siteManager) {
        siteManager.setSiteState(null);
        updateById(siteManager);
    }

    @Override
    public IPage<SiteManager> pageList(Page<SiteManager> page, String name) {
        Page<SiteManager> pageList = baseMapper.pageList(page, name);
        for (SiteManager record : pageList.getRecords()) {
            List<ConWangGuan> list = conWangGuanService.listBySiteId(record.getId());
            record.setGatewayList(list);
        }
        return pageList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGatewayList(SiteManager siteManager) {
        SiteManager byId = getById(siteManager.getId());
        if (byId == null) {
            return;
        }
        List<ConWangGuan> gatewayList = conWangGuanService.listBySiteId(siteManager.getId());
        Set<String> gateWayIds = gatewayList.stream().map(ConWangGuan::getId).collect(Collectors.toSet());
        List<ConWangGuan> updateList = new ArrayList<>();
        for (ConWangGuan conWangGuan : siteManager.getGatewayList()) {
            gateWayIds.remove(conWangGuan.getId());

            ConWangGuan updateGateway = new ConWangGuan();
            updateGateway.setId(conWangGuan.getId());
            updateGateway.setSiteState(byId.getSiteState());
            updateGateway.setSiteId(byId.getId());
            updateList.add(updateGateway);
        }
        for (String gateWayId : gateWayIds) {
            ConWangGuan updateGateway = new ConWangGuan();
            updateGateway.setId(gateWayId);
            updateGateway.setSiteId("");
            updateList.add(updateGateway);
        }
        if (!updateList.isEmpty()) {
            conWangGuanService.updateBatchById(updateList);
        }
        if (gatewayList.isEmpty() && !updateList.isEmpty()) {
            List<ConWangGuan> gatewayList1 = conWangGuanService.listBySiteId(siteManager.getId());
            Integer otherSite = getOtherSite(byId, gatewayList1);
            if (!byId.getSiteState().equals(otherSite)) {
                updateOtherSite(byId, gatewayList1);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState(SiteStateUpdateDTO dto) {
        SiteManager byId = getById(dto.getSiteId());
        if (byId == null) {
            return;
        }
        if (dto.getSiteState().equals(byId.getSiteState())) {
            return;
        }
        byId.setSiteState(dto.getSiteState());
        List<ConWangGuan> gatewayList = updateSiteState(dto.getSiteId(), dto.getSiteState());
        updateOtherSite(byId, gatewayList);
    }

    @NotNull
    private List<ConWangGuan> updateSiteState(String id, Integer state) {
        SiteManager update = new SiteManager();
        update.setId(id);
        update.setSiteState(state);
        updateById(update);
        List<ConWangGuan> gatewayList = conWangGuanService.listBySiteId(id);
        List<ConWangGuan> updateGatewayList = new ArrayList<>();
        for (ConWangGuan conWangGuan : gatewayList) {
            ConWangGuan updateGateway = new ConWangGuan();
            updateGateway.setId(conWangGuan.getId());
            updateGateway.setSiteState(state);
            updateGatewayList.add(updateGateway);
        }
        if (!updateGatewayList.isEmpty()) {
            conWangGuanService.updateBatchById(updateGatewayList);
        }
        return gatewayList;
    }

    private void updateOtherSite(SiteManager site, List<ConWangGuan> gatewayList) {
        if (CollectionUtils.isEmpty(gatewayList)) {
            return;
        }
        String url = "http://" + site.getIp() + ":" + site.getPort() + "/hss-boot/scada/siteManager/setState";
        SetSiteStateDTO data = new SetSiteStateDTO();
        data.setSiteCode(site.getSiteCode());
        data.setSiteState(site.getSiteState());
        SiteRequestParam param = new SiteRequestParam();
        param.setUrl(url);
        param.setJsonData(JSONObject.toJSONString(data));
        conWangGuanService.toSite(gatewayList.get(0), param);
    }
    private Integer getOtherSite(SiteManager site, List<ConWangGuan> gatewayList) {
        if (gatewayList.isEmpty()) {
            return null;
        }
        String url = "http://" + site.getIp() + ":" + site.getPort() + "/hss-boot/scada/siteManager/getState";
        SetSiteStateDTO data = new SetSiteStateDTO();
        data.setSiteCode(site.getSiteCode());
        SiteRequestParam param = new SiteRequestParam();
        param.setUrl(url);
        param.setJsonData(JSONObject.toJSONString(data));
        return  (Integer) conWangGuanService.toSite(gatewayList.get(0), param);
    }

    @Override
    public void setLocalState(SetSiteStateDTO dto) {
        if (dto.getSiteState() == null) {
            return;
        }
        SiteManager site = baseMapper.getByCode(dto.getSiteCode());
        if (site == null) {
            return;
        }
        if (ObjectUtil.equal(dto.getSiteState(), site.getSiteState())) {
            return;
        }
        updateSiteState(site.getId(), dto.getSiteState());

    }

    @Override
    public Integer getLocalState(SetSiteStateDTO dto) {
        SiteManager site = baseMapper.getByCode(dto.getSiteCode());
        if (site == null) {
            return null;
        }
        return site.getSiteState();
    }

    @Override
    public void sysStartInit() {
        List<SiteManager> list = list();
        for (SiteManager site : list) {
            List<ConWangGuan> gatewayList = conWangGuanService.listBySiteId(site.getId());
            Integer otherSite = getOtherSite(site, gatewayList);
            if (otherSite != null && !otherSite.equals(site.getSiteState())) {
                updateSiteState(site.getId(), otherSite);
            }

        }
    }
}
