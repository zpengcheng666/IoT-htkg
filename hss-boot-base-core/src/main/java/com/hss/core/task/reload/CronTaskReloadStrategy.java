package com.hss.core.task.reload;

import com.hss.core.common.exception.HssBootException;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;


/**
 * @author 26060
 */
public class CronTaskReloadStrategy implements TaskReloadStrategy {
    private final CronExpression expression;

    public CronTaskReloadStrategy(String cronExpression) {
        try {
            expression = new CronExpression(cronExpression);
        } catch (ParseException e) {
            throw new HssBootException("cron表达式错误");
        }
    }

    @Override
    public long getNextTime(long con) {
        Date date = new Date();
        Date nextInvalidTimeAfter = expression.getNextValidTimeAfter(date);
        return nextInvalidTimeAfter.getTime();
    }

    @Override
    public boolean isHaveNext() {
        return true;
    }
}
