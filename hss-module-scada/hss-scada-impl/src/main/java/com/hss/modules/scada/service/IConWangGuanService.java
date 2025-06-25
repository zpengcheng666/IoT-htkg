package com.hss.modules.scada.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.scada.entity.ConWangGuan;
import com.hss.modules.scada.model.GateWayVariableBO;
import com.hss.modules.scada.model.SiteRequestParam;

import java.util.List;

/**
* @description: 网关
* @author zpc
* @date 2024/3/19 13:39
* @version 1.0
*/
public interface IConWangGuanService extends IService<ConWangGuan> {

    /**
     * 添加网关
     * @param conWangGuan
     */
    void add(ConWangGuan conWangGuan);

    /**
     * 编辑网关
     * @param conWangGuan
     */
    void edit(ConWangGuan conWangGuan);

    /**
     * 执行命令
     * @param wgId
     * @param variableId
     * @param value
     */
    void executeCommand(String wgId, String variableId, String value);

    /**
     * 获取摄像机详细信息
     * @param wgid
     * @param deviceId
     * @return
     */
    String getCameraByDeviceId(String wgid, String deviceId);

    /**
     * 获取点位
     * @param gatewayId
     * @return
     */
    List<GateWayVariableBO> listPoint(String gatewayId);

    /**
     * 获取门禁人员json
     * @param wangGuan
     * @return
     */
    String getDoorPersonListJson(ConWangGuan wangGuan);

    /**
     * 根据站点id查询
     * @param siteId
     * @return
     */
    List<ConWangGuan> listBySiteId(String siteId);

    /**
     * 通过网关发送数据
     * @param conWangGuan
     * @param param
     * @return data
     */
    Object toSite(ConWangGuan conWangGuan, SiteRequestParam param);
}
