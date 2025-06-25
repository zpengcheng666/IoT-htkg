package com.hss.modules.alarm.hander;

import com.hss.modules.alarm.constant.AlarmConstant;
import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.model.AlarmProcessData;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.util.ExpressionUtil;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 检查是否报警
 * 并设置报警标识位
 *
 * @author hd
 */
@Service
@Order(AlarmProcess.CHECK)
public class AlarmProcessCheck implements AlarmProcess {
    @Autowired
    private ExpressionUtil expressionUtil;
    @Autowired
    private RedisUtil redisUtil;


    /**
     * 处理报警逻辑。
     *
     * @param data 包含报警策略和相关数据的对象。
     */
    @Override
    public boolean process(AlarmProcessData data) {
        AlarmStrategy strategy = data.getStrategy();

        boolean alarm = false;
        // 判断是否需要进行报警判断
        if (AlarmConstant.IS_ENABLE.equals(strategy.getIsEnable())) {
            // 根据策略表达式计算是否报警
            Boolean alarmState = expressionUtil.getValueByAttrValue(strategy.getExpression(), Boolean.class);
            if (Boolean.TRUE.equals(alarmState)) {
                alarm = true;
            } else if (!Boolean.FALSE.equals(alarmState)) {
                return false;
            }
        }
        // 设置报警状态
        data.setAlarm(alarm);
        return true;
    }


    /**
     * 变化率报警检查
     *
     * @param strategy 策略
     * @param attr     属性
     * @param value    值
     * @return
     */
    private Boolean checkChangeRate(AlarmStrategy strategy, ConDeviceAttribute attr, String value) {
        String key = AlarmConstant.REDIS_KEY_ALARM_CHANGE_RATE_DATA + strategy.getId();
        // 查询缓存变化率
        AlarmChangeRateData rateData = (AlarmChangeRateData) redisUtil.get(key);
        BigDecimal valueBigDecimal = new BigDecimal(value);
        if (rateData == null) {
            // 没有缓存数据
            rateData = new AlarmChangeRateData();
            rateData.setMaxValue(valueBigDecimal);
            rateData.setMaxMs(System.currentTimeMillis());
            rateData.setMinValue(valueBigDecimal);
            rateData.setMinMs(System.currentTimeMillis());
            redisUtil.set(key, rateData, 1000 * 60 * 60 * 24);
            return false;
        } else {
            // 有缓存数据
            if (valueBigDecimal.compareTo(rateData.getMaxValue()) >= 0) {
                // 校验最大值
                rateData.setMaxValue(valueBigDecimal);
                rateData.setMaxMs(System.currentTimeMillis());
            }
            if (valueBigDecimal.compareTo(rateData.getMinValue()) <= 0) {
                // 校验最小是
                rateData.setMinValue(valueBigDecimal);
                rateData.setMinMs(System.currentTimeMillis());
            }
            redisUtil.set(key, rateData, 1000 * 60 * 60 * 24);

            String changeRateAlarmValue = expressionUtil.getValueByAttrValue(strategy.getValueExpression(), String.class);
            if (changeRateAlarmValue == null) {
                return false;
            }
            // 分母 （max - min) * 100
            BigDecimal fenMu = rateData.getMaxValue().subtract(rateData.getMinValue()).multiply(new BigDecimal("100"));
            // 时间
            long time = rateData.getMaxMs() - rateData.getMinMs();
            // 分子 (max -min) * 检验周期 * 时间
            BigDecimal fenZi = new BigDecimal(attr.getMaxValue())
                    .subtract(new BigDecimal(attr.getMinValue()))
                    .multiply(new BigDecimal(strategy.getPeriod()))
                    .multiply(new BigDecimal(time));
            if (fenZi.equals(BigDecimal.ZERO)) {
                return false;
            } else {
                BigDecimal changeRate = fenMu.divide(fenZi);
                return changeRate.compareTo(new BigDecimal(changeRateAlarmValue)) >= 0;
            }
        }
    }

    /**
     * 校验范围报警
     *
     * @param value
     * @param range
     * @return
     */
    private boolean checkInterval(Object value, String[] range) {
        BigDecimal val = new BigDecimal((String) value);
        String range1 = expressionUtil.getValueByAttrValue(range[0], String.class);
        if (range1 != null && new BigDecimal(range1).compareTo(val) > 0) {
            return true;
        }
        String range2 = expressionUtil.getValueByAttrValue(range[0], String.class);
        if (range2 != null && new BigDecimal(range2).compareTo(val) < 0) {
            return true;
        }
        return false;
    }
}
