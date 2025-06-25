package com.hss.modules.alarm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.alarm.entity.AlarmHistory;
import com.hss.modules.alarm.mapper.AlarmHistoryMapper;
import com.hss.modules.alarm.model.*;
import com.hss.modules.alarm.service.IAlarmHistoryService;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.store.model.EnvGoodRatioDTO;
import com.hss.modules.store.model.vo.LineStateVO;
import com.hss.modules.store.model.vo.PieStateVO;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.util.StatisticsUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 报警历史数据
 * @Author: jeecg-boot
 * @Date: 2022-12-01
 * @Version: V1.0
 */
@Service
public class AlarmHistoryServiceImpl extends ServiceImpl<AlarmHistoryMapper, AlarmHistory> implements IAlarmHistoryService {

    @Autowired
    private IBaseDictDataService baseDictDataService;
    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;
    @Autowired
    private IConSheBeiService conSheBeiService;

    @Override
    public LineStateVO stat(AlarmHistoryStatSearchModel model) {
        if (StringUtils.isNotEmpty(model.getSubsystem())
                && !"alarmCount".equals(model.getSubsystem())
                && CollectionUtils.isEmpty(model.getDeviceId())
        ) {
            List<String> deviceIds = conSheBeiService.listIdBySubsystem(model.getSubsystem());
            if (!deviceIds.isEmpty()) {
                model.setDeviceId(deviceIds);
            }
        }
        model.setSubsystem(null);
        if (CollectionUtils.isNotEmpty(model.getDeviceId())) {
            model.setDeviceType(null);
        }
        if (CollectionUtils.isNotEmpty(model.getAttrId())) {
            model.setDeviceType(null);
            model.setDeviceId(null);
        }
        Date endTime = model.getEndTime();
        if (endTime != null) {
            Date endTime1 = Date.from(endTime.toInstant().atZone(ZoneId.systemDefault()).plusDays(1L).toInstant());
            model.setEndTime(endTime1);
        }
        //处理报警统计
        List<AlarmStatisticsBO> list = baseMapper.statistics(model);
        //处理报警等级
        final Map<String, String> titleMap = baseDictDataService.queryStatisticsWayMap(model.getStatisticsWay());

        LineStateVO vo = new LineStateVO();
        List<String> legend = new ArrayList<>();
        List<List<BigDecimal>> data = new ArrayList<>();
        vo.setLegend(legend);
        vo.setSeries(data);
        Set<String> xSet = new HashSet<>();
        //图表数据处理
        Map<String, Map<String, AlarmStatisticsBO>> collect = list.stream()
                .collect(
                        Collectors
                                .groupingBy(
                                        AlarmStatisticsBO::getGroupName,
                                        Collectors.toMap(
                                                o -> {
                                                    String x = o.getValueName();
                                                    xSet.add(x);
                                                    return x;
                                                },
                                                o -> o,
                                                (o1, o2) -> o1
                                        )
                                )
                );


        List<String> xList = StatisticsUtil.generateXAxis(model.getStartTime(),endTime,
                model.getStatisticsMethod());
        vo.setXAxis(xList);

        titleMap.forEach((key, value) -> {
            legend.add(value);
            Map<String, AlarmStatisticsBO> timeMap = collect.get(key);
            if (timeMap == null) {
                ArrayList<BigDecimal> dataList = new ArrayList<>();
                data.add(dataList);
                for (String x : xList) {
                    dataList.add(BigDecimal.ZERO);
                }
            } else {
                ArrayList<BigDecimal> dataList = new ArrayList<>();
                data.add(dataList);

                for (String x : xList) {
                    AlarmStatisticsBO bo = timeMap.get(x);
                    if (bo == null) {
                        dataList.add(BigDecimal.ZERO);
                    } else {
                        dataList.add(new BigDecimal(bo.getValue()));
                    }
                }
            }
        });

        return vo;
    }

    @Override
    public void falseAlarm(String alarmDataId) {
        AlarmHistory alarmHistory = baseMapper.getByAlarmDataId(alarmDataId);
        AlarmHistory update = new AlarmHistory();
        update.setId(alarmHistory.getId());
        update.setAlarmEndTime(new Date());
        long ms = System.currentTimeMillis() - alarmHistory.getAlarmStartTime().getTime();
        update.setAlarmDuration(String.valueOf(ms / 1000));
        updateById(update);

    }

    @Override
    public void ack(AlarmAckDTO dto) {
        AlarmHistory alarmHistory = baseMapper.getByAlarmDataId(dto.getId());
        if (alarmHistory == null) {
            return;
        }
        AlarmHistory update = new AlarmHistory();
        update.setId(alarmHistory.getId());
        update.setConfirmor(dto.getActOperateName());
        update.setConfirmTime(new Date());
        update.setHandler(dto.getHandler());
        update.setHandleTime(new Date());
        update.setHandleMethod(dto.getHandlerContents());
        update.setAlarmTypeId(dto.getType());
        update.setAlarmDuration(String.valueOf(System.currentTimeMillis() - alarmHistory.getAlarmStartTime().getTime()));
        updateById(update);
    }

    @Override
    public List<PieStateVO> goodRatio(EnvGoodRatioDTO dto) {
        if (dto.getEndTime().getTime() <= dto.getStartTime().getTime()) {
            throw new HssBootException("开始时间必须大于结束时间");
        }
        long sum = dto.getEndTime().getTime() - dto.getStartTime().getTime();
        Long badValue = baseMapper.getBadSumTime(dto);
        if (badValue == null) {
            badValue = 0L;
        }
        badValue = badValue / 1000;
        List<PieStateVO> result = new ArrayList<>(2);
        long goodValue = sum - badValue;
        PieStateVO good = new PieStateVO();
        good.setName("合格");
        //good.setValue(new BigDecimal(goodValue));
        good.setValue(new BigDecimal(goodValue * 100).divide(new BigDecimal(sum), 2, RoundingMode.HALF_UP));
        good.setRatio(new BigDecimal(goodValue * 100).divide(new BigDecimal(sum), 2, RoundingMode.HALF_UP));
        result.add(good);

        PieStateVO bad = new PieStateVO();
        bad.setName("不合格");
        //bad.setValue(new BigDecimal(badValue));
        bad.setValue(new BigDecimal(100).subtract(good.getRatio()));
        bad.setRatio(new BigDecimal(100).subtract(good.getRatio()));
        result.add(bad);
        return result;
    }

    @Override
    public IPage<AlarmHistory> queryPageList(Page<AlarmHistory> page, LambdaQueryWrapper<AlarmHistory> queryWrapper) {
        Page<AlarmHistory> page1 = page(page, queryWrapper);
        // 报警级别字典
        Map<String, String> alarmLeveMap = baseDictDataService.mapByTypeId("2EF9FDF0DD954D77819872FD75136AD9");
        // 报警类型字典
        Map<String, String> alarmTypeMap = baseDictDataService.mapByTypeId("2743FE253A974CB8B345A59A963D7EEF");
        for (AlarmHistory e : page1.getRecords()) {
            if (StringUtils.equals(AlarmHistory.STATUS_HANDLED, e.getIsHandle())) {
                e.setIsHandleTxt("已处理");
            } else {
                e.setIsHandleTxt("未处理");
            }
            if (StringUtils.isNotBlank(e.getLevelId())) {
                e.setLevelName(alarmLeveMap.get(e.getLevelId()));
            }
            if (StringUtils.isNotBlank(e.getDeviceTypeId())) {
                e.setAlarmTypeName(alarmTypeMap.get(e.getAlarmTypeId()));
            }
            String originVarId = e.getAttrId();
            if (StringUtils.isEmpty(originVarId)) {
                continue;
            }
            ConDeviceAttribute byId = conDeviceAttributeService.getById(originVarId);
            if (byId == null) {
                continue;
            }
            String valueMap = byId.getValueMap();
            if (StringUtils.isEmpty(valueMap) || "{}".equals(valueMap)) {
                continue;
            }
            JSONObject jsonObject = JSONObject.parseObject(valueMap);
            e.setAlarmValue(jsonObject.getString(e.getAlarmValue()));
            e.setRange(jsonObject.getString(e.getRange()));

        }
        return page1;
    }

    @Override
    public void batchAck(AlarmBatchAckDTO dto) {
        List<AlarmHistory> list = baseMapper.listByAlarmDataIds(dto.getIds());
        if (list.size() == 0) {
            return;
        }

        String actOperateName = dto.getActOperateName();
        Date confirmTime = new Date();
        String type = dto.getType();
        String handler = dto.getHandler();
        String handlerContents = dto.getHandlerContents();
        boolean isHandler = StringUtils.isBlank(handler);

        List<AlarmHistory> updateList = list.stream().map(h -> {
            AlarmHistory update = new AlarmHistory();
            update.setId(h.getId());
            update.setConfirmor(actOperateName);
            update.setConfirmTime(confirmTime);
            update.setAlarmTypeId(type);
            if (isHandler) {
                update.setHandler(handler);
                update.setHandleMethod(handlerContents);
                update.setHandleTime(confirmTime);
            }
            return update;
        }).collect(Collectors.toList());
        updateBatchById(updateList);
/*        for (String id : dto.getIds()) {
            AlarmHistory alarmHistory = baseMapper.getByAlarmDataId(id);
            if (alarmHistory == null) {
                continue;
            }
            AlarmHistory update = new AlarmHistory();
            update.setId(alarmHistory.getId());
            update.setConfirmor(dto.getActOperateName());
            update.setConfirmTime(new Date());
            update.setHandler(dto.getHandler());
            update.setHandleTime(new Date());
            update.setHandleMethod(dto.getHandlerContents());
            update.setAlarmDuration(String.valueOf(System.currentTimeMillis() - alarmHistory.getAlarmStartTime().getTime()));
            update.setAlarmTypeId(dto.getType());
            updateById(update);
        }*/
    }

    @Override
    public LineStateVO stateCountLastMonth() {
        Date upMonthDate = Date.from(LocalDateTime.now().plusMonths(-1).atZone(ZoneId.systemDefault()).toInstant());

        List<StateCountLastMonthBO> list = baseMapper.stateCountLastMonth(upMonthDate);
        List<String> names = new ArrayList<>(list.size());
        List<BigDecimal> datas = new ArrayList<>(list.size());
        for (StateCountLastMonthBO bo : list) {
            names.add(bo.getDay());
            datas.add(new BigDecimal(bo.getCount()));
        }
        LineStateVO vo = new LineStateVO();
        vo.setLegend(Collections.singletonList("报警数量"));
        vo.setXAxis(names);
        vo.setSeries(Collections.singletonList(datas));
        return vo;
    }

    @Override
    public LineStateVO stateCountAlarm(String subSystems) {
        Date upMonthDate = Date.from(LocalDateTime.now().plusMonths(-1).atZone(ZoneId.systemDefault()).toInstant());

        List<StateCountLastMonthBO> list = baseMapper.stateCountAlarm(upMonthDate,subSystems);
        List<String> names = new ArrayList<>(list.size());
        List<BigDecimal> datas = new ArrayList<>(list.size());
        for (StateCountLastMonthBO bo : list) {
            names.add(bo.getDay());
            datas.add(new BigDecimal(bo.getCount()));
        }
        LineStateVO vo = new LineStateVO();
        vo.setLegend(Collections.singletonList("报警数量"));
        vo.setXAxis(names);
        vo.setSeries(Collections.singletonList(datas));
        return vo;
    }

    @Override
    public void handler(AlarmHistoryHandlerDTO dto) {
        if (dto.getIds() == null || dto.getIds().size() == 0) {
            return;
        }
        if (StringUtils.isBlank(dto.getHandler())) {
            throw new JeecgBootException("处理人不能为空");
        }
        if (StringUtils.isBlank(dto.getHandleMethod())) {
            throw new JeecgBootException("处理意见不能为空");
        }
        baseMapper.handler(dto, new Date());
    }
}
