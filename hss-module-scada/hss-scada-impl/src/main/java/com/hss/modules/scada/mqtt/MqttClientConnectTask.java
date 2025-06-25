package com.hss.modules.scada.mqtt;

import com.hss.core.task.executor.TaskExecutor;
import com.hss.core.task.impl.BaseTask;
import com.hss.core.task.reload.TaskReloadStrategy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * @author zpc
 * @version 1.0
 * @description: 网关链接任务
 * @date 2024/3/20 9:59
 */
@Slf4j
public class MqttClientConnectTask extends BaseTask implements TaskReloadStrategy {

    /**
     * 错误计数
     */
    private int errCount = 0;

    private long waitTime = 0L;

    /**
     * 客户端
     */
    private final MqttClient client;

    /**
     * 链接参数
     */
    private final MqttConnectOptions options;

    public MqttClientConnectTask(MqttClient client, MqttConnectOptions options, TaskExecutor executor) {
        super(executor);
        this.client = client;
        this.options = options;
        setReloadStrategy(this);
    }

    @Override
    public void run() {
        try {
            client.connect(options);
            waitTime = -1;
        } catch (MqttException e) {
            log.error("链接mqtt服务器失败", e);
            reloadWaitTime();
        }
    }

    @Override
    public void errFun(Throwable e) {
        log.error("链接网关失败", e);
        reloadWaitTime();
    }

    private void reloadWaitTime() {
        errCount++;
        long next = 2L * errCount;
        waitTime = next * 1000;
    }

    @Override
    public long getNextTime(long time) {
        return time + waitTime;
    }

    @Override
    public boolean isHaveNext() {
        return waitTime != -1;
    }

}
