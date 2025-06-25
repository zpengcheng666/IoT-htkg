package com.hss.modules.alarm.hander;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hss.modules.alarm.constant.AlarmConstant;
import com.hss.modules.alarm.entity.AlarmData;
import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.model.AlarmProcessData;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.entity.GsChangJing;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.scada.service.IGSChangJingSheBeiService;
import com.hss.modules.scada.service.IGsChangJingService;
import com.hss.modules.util.ExpressionUtil;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 更新缓存
 * 如果是新的报警则添加到缓存中
 * 如果报警解除 则删除缓存
 * @author hd
 */
@Service
@Order(AlarmProcess.CACHE_UPDATE)
public class AlarmProcessCacheUpdate implements AlarmProcess {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ExpressionUtil expressionUtil;
    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;
    @Autowired
    private IConSheBeiService conSheBeiService;

    @Autowired
    private IGSChangJingSheBeiService gsChangJingSheBeiService;

    @Autowired
    private IGsChangJingService gsChangJingService;
    /**
     * @description: 处理报警数据。
     * 当报警状态为新报警或报警解除时，发布关联变量变更消息。
     * @author zpc
     * @date 2024/3/26 10:05
     * @param data 包含报警策略和状态等信息的数据对象。
     * @return 返回处理是否成功的布尔值。成功为true，失败为false。
     */
    @Override
    public boolean process(AlarmProcessData data) {
        Integer alarmStatus = data.getAlarmStatus();
        AlarmStrategy strategy = data.getStrategy();

        // 当报警状态为新报警时，处理并发布消息
        if (AlarmConstant.ALARM_STATUS_NEW.equals(alarmStatus)) {
            // 获取并设置设备信息
            ConSheBei device = conSheBeiService.getById(strategy.getDeviceId());
            if (device == null){
                return false;
            }
            data.setDevice(device);

            // 获取并设置原始变量信息
            String originVarId = strategy.getOriginVarId();
            if (StringUtils.isEmpty(originVarId)){
                return false;
            }
            ConDeviceAttribute originAttr = conDeviceAttributeService.getById(originVarId);
            if (originAttr == null) {
                return false;
            }
            data.setOriginAttr(originAttr);

            // 根据deviceId查询对应的场景、和subsystem
            String sceneId = this.gsChangJingSheBeiService.getSceneIdByDeviceId(strategy.getDeviceId());
            GsChangJing stage = null;
            if (StringUtils.isNotEmpty(sceneId)){
                stage = this.gsChangJingService.getById(sceneId);
            }

            // 创建并设置报警数据对象
            AlarmData alarmData = new AlarmData();
            alarmData.setId(IdWorker.getIdStr());
            alarmData.setDeviceId(device.getId());
            alarmData.setDeviceName(device.getName());
            // 增加设备别名设置
            alarmData.setOtherName(device.getOtherName());
            alarmData.setDeviceType(device.getType());
            alarmData.setOriginVarId(originAttr.getId());
            alarmData.setOriginVarName(originAttr.getName());
            alarmData.setRecordValue(expressionUtil.getValueByAttrValue(strategy.getValueExpression(), String.class));
            alarmData.setRecordTime(new Date());
            alarmData.setStatusVarId(strategy.getStatusVarId());
            alarmData.setRange(expressionUtil.getExpressionStr(null, strategy.getRange()));
            alarmData.setAlarmType(strategy.getType());
            alarmData.setAlarmLevel(strategy.getLevelId());
            alarmData.setStatus(AlarmStatus.RUN);
            alarmData.setStrategyId(strategy.getId());
            //START On 2024-06-23 By chushubin
            alarmData.setSceneId(sceneId);
            alarmData.setSubSystem(stage != null ? stage.getSubSystem(): null);
            //END

            redisUtil.set(AlarmConstant.REDIS_KEY_ALARM + strategy.getId(), alarmData);
            data.setAlarmData(alarmData);

        // 当报警状态为解除时，从Redis中删除对应的报警数据
        }else if (AlarmConstant.ALARM_STATUS_FALSE.equals(alarmStatus)){
            redisUtil.del(AlarmConstant.REDIS_KEY_ALARM + strategy.getId());
        }
        return true;
    }
}
