package com.hss.modules.scada.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.entity.ConWangGuan;
import com.hss.modules.scada.mapper.ConWangGuanMapper;
import com.hss.modules.scada.model.GateWayVariableBO;
import com.hss.modules.scada.model.GatewayExecuteVO;
import com.hss.modules.scada.model.SiteRequestParam;
import com.hss.modules.scada.mqtt.MqttFactory;
import com.hss.modules.scada.service.IConWangGuanService;
import com.hss.modules.system.service.IBaseParamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
* @description: 网关执行命令、获取摄像机信息、获取点位、获取门禁人员、发送数据相关操作
* @author zpc
* @date 2024/3/19 13:51
* @version 1.0
*/
@Service
@Slf4j
public class ConWangGuanServiceImpl extends ServiceImpl<ConWangGuanMapper, ConWangGuan> implements IConWangGuanService {
    @Autowired
    private MqttFactory mqttFactory;
    @Autowired
    private IBaseParamService baseParamService;

    private static final String SITE_MAIN = "scada_gateway_site_cs";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ConWangGuan conWangGuan) {
        save(conWangGuan);
        mqttFactory.gatewayRegister(conWangGuan);
    }

    @Override
    public void edit(ConWangGuan conWangGuan) {
        updateById(conWangGuan);
        mqttFactory.update(conWangGuan);
    }

    @Override
    public boolean removeById(Serializable id) {
        boolean b = super.removeById(id);
        mqttFactory.remove((String) id);
        return b;
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        boolean b = super.removeByIds(list);
        list.stream().map(id -> (String) id).forEach(id -> mqttFactory.remove(id));
        return b;
    }


    @Override
    public void executeCommand(String wgId, String variableId, String value) {
        ConWangGuan wangGuan = getById(wgId);
        if (wangGuan == null) {
            throw new HssBootException("设备网关不存在");
        }
        String valueStr = baseParamService.getParamByCode(SITE_MAIN);
        if (StringUtils.isNotBlank(valueStr)) {
            int siteMain = Integer.parseInt(valueStr);
            if (wangGuan.getSiteState() != null && wangGuan.getSiteState() != siteMain) {
                return;
            }
        }

        HashMap<String, Object> paramMap = new HashMap<>(0);
        paramMap.put("ID", variableId);
        paramMap.put("Value", value);
        GatewayExecuteVO gatewayExecuteVO = null;
        String resultJson;
        String url = "http://" + wangGuan.getIp() + ":" + wangGuan.getPort() + "/DeviceApi/setDeviceValue";
        try {
            // 删除超时设置，使用系统默认超时时间
            resultJson = HttpUtil.post(url, JSONObject.toJSONString(paramMap));
            log.info("请求网关param={},result={}, url={}", JSONObject.toJSONString(paramMap), resultJson, url);
        } catch (Exception e) {
            log.error("请求网关失败param={},e={}, url={}", JSONObject.toJSONString(paramMap), e, url);
            throw new HssBootException("请求网关异常");
        }
        try {
            gatewayExecuteVO = JSONObject.parseObject(resultJson, GatewayExecuteVO.class);
        } catch (Exception e) {
            log.error("请求网关解析返回值失败result={},e={}", resultJson, e);
        }
        if (gatewayExecuteVO != null) {
            if (!ScadaConstant.CODE.equals(gatewayExecuteVO.getCode())) {
                throw new HssBootException(gatewayExecuteVO.getMsg());
            }
        }
    }

    @Override
    public String getCameraByDeviceId(String wgid, String deviceId) {
        ConWangGuan wangGuan = getById(wgid);
        if (wangGuan == null) {
            throw new HssBootException("网关不存在");
        }
        HashMap<String, Object> paramMap = new HashMap<>(0);
        paramMap.put("ID", deviceId.toUpperCase(Locale.ROOT));
        try {
            String resultJson = HttpUtil.post("http://" + wangGuan.getIp() + ":" + wangGuan.getPort() + "/DeviceApi/getCameraInfo", JSONObject.toJSONString(paramMap));
            log.info("请求网关param={},result={}", JSONObject.toJSONString(paramMap), resultJson);
            return resultJson;

        } catch (Exception e) {
            log.error("请求网关失败param={},e={}", JSONObject.toJSONString(paramMap), e);
            throw new HssBootException("请求网关异常");
        }
    }

    @Override
    public List<GateWayVariableBO> listPoint(String gatewayId) {
        // 1. 获取网关配置信息
        ConWangGuan wangGuan = getById(gatewayId);
        if (wangGuan == null) {
            throw new HssBootException("网关不存在");
        }
        // 2. 更新网关的最后同步时间
        ConWangGuan update = new ConWangGuan();
        update.setId(gatewayId);
        update.setLastUpdateTime(new Date());
        updateById(update);

        String resultJson = HttpUtil.post("http://" + wangGuan.getIp() + ":" + wangGuan.getPort() + "/DeviceApi/devicelist", new HashMap<>(0));
        log.info("请求网关result={}", resultJson);
        if (StringUtils.isEmpty(resultJson)) {
            throw new HssBootException("请求网关失败");
        }
        return JSONObject.parseObject(resultJson, new TypeReference<List<GateWayVariableBO>>() {
        });
    }

    @Override
    public String getDoorPersonListJson(ConWangGuan wangGuan) {
        HashMap<String, Object> paramMap = new HashMap<>(0);
        paramMap.put("ID", "");
        String resultJson;
        try {
            resultJson = HttpUtil.post("http://" + wangGuan.getIp() + ":" + wangGuan.getPort() + "/DeviceApi/GetPersonList", JSONObject.toJSONString(paramMap));
            log.info("请求网关获取门禁信息result={}", resultJson);
        } catch (Exception e) {
            log.error("请求网关失败param={},e={}", JSONObject.toJSONString(paramMap), e);
            throw new HssBootException("请求网关数据失败");
        }
        try {
            if (StringUtils.isNotBlank(resultJson)) {
                JSONObject jsonObject = JSONObject.parseObject(resultJson);
                String code = jsonObject.getString(ScadaConstant.KEY);
                // 如果没有找到数据，直接返回null
                if (ScadaConstant.NOT_FOUND.equals(code)){
                    return null;
                }
                if (!ScadaConstant.CODE.equals(code)) {
                    throw new HssBootException("请求网关数据失败");
                }
                return jsonObject.getString("data");
            }

        } catch (Exception e) {
            log.error("请求网关解析返回值失败result={}", resultJson, e);
            throw new HssBootException("解析网关数据失败");
        }
        return null;
    }

    @Override
    public Object toSite(ConWangGuan wangGuan, SiteRequestParam param) {
        String resultJson;
        String paramJson = JSONObject.toJSONString(param);
        try {
            resultJson = HttpUtil.post("http://" + wangGuan.getIp() + ":" + wangGuan.getPort() + "/DeviceApi/transMsg", paramJson);
            log.info("请求网关获发送子站命令param={},result={}", paramJson, resultJson);
        } catch (Exception e) {
            log.error("请求网关失败param={},e={}", paramJson, e);
            throw new HssBootException("请求网关数据失败");
        }

        if (StringUtils.isNotBlank(resultJson)) {
            JSONObject jsonObject;
            try {
                jsonObject = JSONObject.parseObject(resultJson);
            } catch (Exception e) {
                log.error("解析网关json数据失败json={}", resultJson);
                throw new HssBootException("解析网关数据失败");
            }
            if (!ScadaConstant.CODE.equals(jsonObject.getString(ScadaConstant.KEY))) {
                throw new HssBootException(jsonObject.getString("msg"));
            }
            String data = jsonObject.getString("data");
            if (StringUtils.isBlank(data)) {
                throw new HssBootException("发送命令到站点失败");
            }
            JSONObject dataObject = JSONObject.parseObject(data);
            Boolean success = dataObject.getBoolean("success");
            if (!Boolean.TRUE.equals(success)) {
                throw new HssBootException(dataObject.getString("message"));
            }
            return dataObject.get("result");
        }
        return null;
    }

    @Override
    public List<ConWangGuan> listBySiteId(String siteId) {
        return baseMapper.listBySiteId(siteId);
    }
}
