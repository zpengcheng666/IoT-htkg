package com.hss.modules.scada.process;

import cn.hutool.core.util.ObjectUtil;
import com.hss.modules.scada.additional.DeviceMqttReadEventListenerAdditional;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataAttrValueUpdateEvent;
import com.hss.modules.scada.model.DeviceAttrData;
import com.hss.modules.scada.service.DeviceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/3/6 9:41
 */
@Slf4j
@Service
public class ProcessExecutorImpl implements ProcessExecutor{
    private final List<ThreadPoolTaskExecutor> executorList;
    private static final int COUNT = 64;

    @Autowired
    private DeviceDataService deviceDataService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired(required = false)
    private List<DeviceMqttReadEventListenerAdditional> additionalList;

    public ProcessExecutorImpl() {
        this.executorList = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            executorList.add(getThreadPool("scadaProcess_", i));
        }

    }

    /**
     * 处理给定设备ID的属性列表。
     * 该方法通过计算设备ID的哈希值，将其映射到一个线程池执行器上，然后异步执行属性处理任务。
     *
     * @param deviceId 设备的唯一标识符，用于计算哈希值并选择对应的线程池。
     * @param attrList 要处理的设备属性列表。
     */
    @Override
    public void process(String deviceId, List<ConDeviceAttribute> attrList) {
        // 计算设备ID的哈希值，并通过取模运算选择一个线程池执行器
        int hashCode = Math.abs(deviceId.hashCode());
        int index = hashCode % COUNT;
        // 从执行器列表获取选中的线程池执行器
        ThreadPoolTaskExecutor executor = executorList.get(index);
        // 异步执行属性处理任务
        executor.execute(() -> {
            try {
                // 尝试处理设备属性列表
                processItem(deviceId, attrList);
            } catch (Exception e) {
                // 如果处理过程中发生异常，记录错误日志
                log.error("处理scada数据失败deviceId={}", deviceId, e);
            }
        });
    }

    public static ThreadPoolTaskExecutor getThreadPool(String pre, int i) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数目
        executor.setCorePoolSize(1);
        //指定最大线程数
        executor.setMaxPoolSize(1);
        //队列中最大的数目
        executor.setQueueCapacity(128);
        //线程名称前缀
        executor.setThreadNamePrefix(pre + i + "_");
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(0);
        //加载
        executor.initialize();
        return executor;
    }

    /**
     * 处理设备项及其属性列表。
     * 对于给定的设备ID和属性列表，首先执行额外的操作（如果有），然后筛选出与初始值不相等的属性，并发布其更新事件。
     *
     * @param deviceId 设备ID，用于标识特定的设备。
     * @param attrList 属性列表，包含需要处理的设备属性。
     */
    private void processItem(String deviceId, List<ConDeviceAttribute> attrList) {
        // 如果存在额外的操作列表，则对每个额外操作执行处理
        if (additionalList != null){
            for (DeviceMqttReadEventListenerAdditional additional : additionalList) {
                try {
                    additional.additionalOperate(deviceId, attrList);
                } catch (Exception e) {
                    // 记录处理附加操作时出现的异常
                    log.error("处理接受mqtt消息附加操作异常", e);
                }
            }
        }

        // 筛选属性列表中与初始值不相等的属性，并发布其更新事件
        attrList.stream().filter(attr -> {
            DeviceAttrData data = deviceDataService.getAttrValueByAttrId(attr.getId());
            // 仅选择当前值与初始值不相等的属性
            return data == null || ObjectUtil.notEqual(attr.getInitValue(), data.getValue());
        }).forEach(attr -> publisher.publishEvent(new ScadaDataAttrValueUpdateEvent(attr)));
    }
}
