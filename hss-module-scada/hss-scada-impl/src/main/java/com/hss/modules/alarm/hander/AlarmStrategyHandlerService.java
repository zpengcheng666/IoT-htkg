package com.hss.modules.alarm.hander;

import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.model.AlarmProcessData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @description: alarm消息处理
* @author zpc
* @date 2024/3/20 15:03
* @version 1.0
*/
@Slf4j
@Service
public class AlarmStrategyHandlerService {

    @Autowired
    private List<AlarmProcess> alarmProcessList;

    /**
     * 根据给定的报警策略处理报警逻辑。
     *
     * @param strategy 报警策略对象，定义了报警的处理方式。
     */
    public void process(AlarmStrategy strategy) {
        // 创建报警处理数据对象，并设置报警策略
        AlarmProcessData alarmProcessData = new AlarmProcessData();
        alarmProcessData.setStrategy(strategy);
        // 调用过程处理报警数据
        process(alarmProcessData);
    }

    /**
     * 处理报警数据。
     * 遍历报警处理列表，对每个报警处理对象调用其处理方法，直到有一个处理方法返回false或遍历完成。
     * @param alarmProcessData 报警处理数据，包含需要处理的报警信息。
     */
    private void process(AlarmProcessData alarmProcessData) {
        // 遍历报警处理列表，依次调用每个报警处理对象的处理方法
        for (AlarmProcess alarmProcess : alarmProcessList) {
            boolean process = alarmProcess.process(alarmProcessData);
            // 如果某个报警处理对象处理失败（返回false），则终止遍历
            if (!process){
                break;
            }
        }
    }

}
