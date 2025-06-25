package com.hss.modules.alarm.model;

import com.hss.modules.alarm.entity.AlarmData;
import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

/**
 * alarmProcessData
 * @author hd
 */
@Data
public class AlarmProcessData {
    /**
     * 策略
     * 第0步，添加
     */
    private AlarmStrategy strategy;

    /**
     * 是否是原来的报警
     * 第1步添加
     * true 已经存在报警
     * false 新的报警
     */
    private Boolean cached;

    /**
     * 报警缓存数据
     * 第1步添加， 原来就有的报警
     * 第4步添加，新的报警，更新缓存
     * 如果是新的报警则在第4不添加 报警跟新缓存数据和报警缓存
     */
    private AlarmData alarmData;

    /**
     * 是否报警
     * 第2步添加， 检测出报警
     * true 报警
     * false 解除报警
     */
    private Boolean alarm;

    /**
     * 报警时机
     * 1:新报警,2:报警结束，0:原来报警状态
     * 第2步添加，报警状态
     */
    private Integer alarmStatus;


    /**
     * 状态变量
     * 第4步添加,更新状态变量
     */
    @Nullable
    private ConDeviceAttribute statusAttr;

    /**
     * 设备信息
     * 第5步添加，更新缓存
     *
     */
    @Nullable
    private ConSheBei device;


    /**
     * 原始变量
     * 第5步添加，更新缓存
     */
    @Nullable
    private ConDeviceAttribute originAttr;







}
