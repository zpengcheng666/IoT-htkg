package com.hss.modules.store.service.impl;

import cn.hutool.core.date.CalendarUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.util.DateUtils;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.entity.GsChangJing;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.scada.service.IGSChangJingSheBeiService;
import com.hss.modules.scada.service.IGsChangJingService;
import com.hss.modules.store.constant.StoreConstant;
import com.hss.modules.store.entity.StoreHistory;
import com.hss.modules.store.mapper.StoreHistoryMapper;
import com.hss.modules.store.model.*;
import com.hss.modules.store.model.vo.LineStateVO;
import com.hss.modules.store.model.vo.PieStateVO;
import com.hss.modules.store.service.IStoreHistoryService;
import com.hss.modules.system.entity.BaseLocation;
import com.hss.modules.system.entity.StatisticsSection;
import com.hss.modules.system.service.IBaseLocationService;
import com.hss.modules.system.service.IStatisticsSectionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.CalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Description: 设备运行时历史数据
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
@Service
public class StoreHistoryServiceImpl extends ServiceImpl<StoreHistoryMapper, StoreHistory> implements IStoreHistoryService {

    @Autowired
    private IConSheBeiService conSheBeiService;
    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;
    @Autowired
    private IStatisticsSectionService statisticsSectionService;

    @Autowired
    private IGSChangJingSheBeiService gsChangJingSheBeiService;

    @Autowired
    private IGsChangJingService gsChangJingService;

    @Override
    public List<PieStateVO> distributeState(EnvGoodRatioDTO dto) {
        ConDeviceAttribute attr = conDeviceAttributeService.getById(dto.getAttrId());
        if (attr == null){
            return Collections.emptyList();
        }
        //根据设备类型和属性id查询
        List<StatisticsSection> scopeList = statisticsSectionService.listByDeviceTypeIdAndEnName(attr.getDeviceTypeId(),attr.getEnName());
        if (scopeList.isEmpty()){
            return Collections.emptyList();
        }
        BigDecimal b = new BigDecimal(100);
        AtomicInteger sum = new AtomicInteger(0);
        List<PieStateVO> resultList = scopeList.stream()
                .map(StatisticsSection::getSectionDisplay)
                .sorted((x1, x2) -> {
                    String s1 = x1.split(",")[0];
                    String s2 = x2.split(",")[0];
                    if ("-∞".equals(s1)) {
                        return -1;
                    }
                    if ("-∞".equals(s2)) {
                        return 1;
                    }
                    return Float.compare(Float.parseFloat(s1), Float.parseFloat(s2));
                })
                .map(s -> {
                    String[] split = s.split(",");
                    String s1 = split[0];
                    String s2 = split[1];
                    float min = "-∞".equals(s1) ? Integer.MIN_VALUE : Float.parseFloat(s1);
                    float max = "+∞".equals(s2) ? Integer.MAX_VALUE : Float.parseFloat(s2);
                    int coount = baseMapper.countByStartAndEnd(dto, min, max);
                    sum.getAndAdd(coount);
                    PieStateVO pieStateVO = new PieStateVO();
                    pieStateVO.setName(s);
                    pieStateVO.setValue(new BigDecimal(coount));
                    return pieStateVO;
                }).collect(Collectors.toList());
        BigDecimal sumBig = new BigDecimal(sum.get());
        resultList.forEach(vo -> {
            if (sum.get() == 0){
                vo.setRatio(BigDecimal.ZERO);
            }else {
                vo.setRatio(vo.getValue().multiply(b).divide(sumBig, 2, RoundingMode.HALF_UP ));
            }
        });
        return resultList;
    }

    @Override
    public void saveByVariableId(ConDeviceAttribute attr, String value) {
        ConSheBei device = conSheBeiService.getById(attr.getDeviceId());
        if (device == null){
            return;
        }
        // On 2024-06-23 By Chushubin
        // 直接采用locationId保存的位置的信息，不在维护位置信息表了
//        String locationName = null;
//        if (StringUtils.isNotBlank(device.getLocationId())){
//            BaseLocation location = baseLocationService.getById(device.getLocationId());
//            if (location != null){
//                locationName = location.getName();
//            }
//        }
        StoreHistory his = new StoreHistory();
        his.setDeviceId(attr.getDeviceId());
        his.setDeviceType(device.getDeviceTypeId());
        his.setDeviceName(device.getName());
        // On 2024-06-23 By Chushubin
        // 获取subsystem，用于查询的时候使用
        String sceneId = this.gsChangJingSheBeiService.getSceneIdByDeviceId(attr.getDeviceId());
        if (StringUtils.isNotBlank(sceneId)){
            GsChangJing scene = this.gsChangJingService.getById(sceneId);
            his.setSubsystem(scene != null ? scene.getSubSystem(): null);
        }
        his.setLocationId(device.getLocationId());
//        his.setLocationName(locationName);
        his.setAttrName(attr.getName());
        his.setAttrEnName(attr.getEnName());
        his.setVariableId(attr.getVariableId());
        his.setVariableName(attr.getName());
        his.setAttrId(attr.getId());
        his.setUnit(attr.getUnit());
        if (StringUtils.isNotBlank(attr.getMinValue()) && StringUtils.isNotBlank(attr.getMaxValue())){
            his.setRange(attr.getMinValue() + "," + attr.getMaxValue());
        }
        his.setRecordValue(value);
        his.setRecordTime(attr.getUpdatedTime());
        save(his);

    }

    @Override
    public List<StoreHistoryStatWrapper> stat(String deviceType, String deviceId, String attrName, Date startTime, Date endTime, String subSystem) {
        List<StoreHistoryStatWrapper> list = this.getBaseMapper().stat(deviceType, deviceId, attrName, startTime, endTime, subSystem);
        return list;
    }

    @Override
    public LineStateVO historyLineStatistics(StoreHistoryLineStatisticsDTO dto) {
        if (dto.getEndTime() != null) {
            Date endTime = Date.from(dto.getEndTime().toInstant().atZone(ZoneId.systemDefault()).plusDays(1L).toInstant());
            dto.setEndTime(endTime);
        }
        List<StoreHistory> list = baseMapper.historyLineStatistics(dto);
        Map<String,String> titleMap = new HashMap<>(16);
        LineStateVO vo = new LineStateVO();
        List<String > legend = new ArrayList<>();
        List<List<BigDecimal>> data = new ArrayList<>();
        vo.setLegend(legend);
        vo.setSeries(data);
        Set<String> xSet = new HashSet<>();
        Map<String, Map<String, StoreHistory>> collect = list.stream()
                .filter(h -> StringUtils.isNotBlank(h.getRecordValue()))
                .peek(o -> titleMap.put(o.getAttrId(),o.getDeviceName() + "." + o.getAttrName()))
                .collect(
                        Collectors
                                .groupingBy(
                                        StoreHistory::getAttrId,
                                        Collectors.toMap(
                                                o -> {
                                                    String x = DateUtil.formatDateTime(o.getRecordTime());
                                                    xSet.add(x);
                                                    return x;
                                                },
                                                o -> o,
                                                (o1,o2) -> o1
                                        )
                                )
                );
        List<String> xList = xSet.stream().sorted(String::compareTo).collect(Collectors.toList());
        vo.setXAxis(xList);
        Map<String, BigDecimal> upMap = new HashMap<>(16);
        collect.forEach((attrId, timeMap) -> {
            ArrayList<BigDecimal> dataList = new ArrayList<>();
            data.add(dataList);
            legend.add(titleMap.get(attrId));
            BigDecimal dataValue = null;
            for (String x : xList) {
                StoreHistory storeHistory = timeMap.get(x);
                if (storeHistory == null) {
                    BigDecimal up = upMap.get(attrId);
                    if (up != null){
                        dataValue = up;
                    }
                } else {
                    dataValue = new BigDecimal(StringUtils.isBlank(storeHistory.getRecordValue()) ? "0" : storeHistory.getRecordValue());
                }
                dataList.add(dataValue);
                upMap.put(attrId, dataValue);
            }
        });
        return vo;
    }

    @Override
    public IPage<StoreHistory> selectHistoryPage(IPage<StoreHistory> page, String subSystem, String[] deviceIds, String[] deviceTypes, String[] attrIds, Date startTime, Date endTime) {
        return baseMapper.selectHistoryPage(page, subSystem, deviceIds, deviceTypes, attrIds, startTime, endTime);
    }

    @Override
    public List<EnvReportResult> reportEnv(String deviceType, String deviceId, String reportType, Date date) {

        List<EnvReportResult> list = new ArrayList<>();
        Calendar startTime = null;
        Calendar endTime = null;

        // 日报
        if (StoreConstant.REPORT_TYPE_DAY.equals(reportType)){
            startTime = CalendarUtil.beginOfDay(CalendarUtil.calendar(date));
            endTime = CalendarUtil.endOfDay(CalendarUtil.calendar(date));
            List<EnvReportDTO> ll = this.getBaseMapper().envHourReport(deviceType, deviceId, startTime.getTime(), endTime.getTime());

            Map<String, EnvReportDTO> tempMap =  ll.stream().filter(e -> e.getAttrName().equals("温度"))
                    .collect(Collectors.toMap(s -> s.getRecordTime(), s -> s));
            Map<String, EnvReportDTO> humMap =  ll.stream().filter(e -> e.getAttrName().equals("湿度"))
                    .collect(Collectors.toMap(s -> s.getRecordTime(), s -> s));

            for (int i = 0; i < 24; i++) {
                EnvReportResult t = new EnvReportResult();
                t.setDate(String.valueOf(i) + ":00 - " + String.valueOf(i + 1) + ":00");
                String key = String.format("%02d", i);
                EnvReportDTO tmp = tempMap.get(key);
                if (tmp != null){
                    t.setTempAvg(tmp.getAvg());
                    t.setTempMax(tmp.getMax());
                    t.setTempMin(tmp.getMin());
                }
                tmp = humMap.get(key);
                if (tmp != null){
                    t.setHumAvg(tmp.getAvg());
                    t.setHumMax(tmp.getMax());
                    t.setHumMin(tmp.getMin());
                }
                list.add(t);
            }
        } else if (StoreConstant.REPORT_TYPE_WEEK.equals(reportType)){
            startTime = CalendarUtil.beginOfWeek(CalendarUtil.calendar(date));
            endTime = CalendarUtil.endOfWeek(CalendarUtil.calendar(date));

            List<EnvReportDTO> ll = this.getBaseMapper().envDayReport(deviceType, deviceId, startTime.getTime(), endTime.getTime());

            Map<String, EnvReportDTO> tempMap =  ll.stream().filter(e -> e.getAttrName().equals("温度"))
                    .collect(Collectors.toMap(s -> s.getRecordTime(), s -> s));
            Map<String, EnvReportDTO> humMap =  ll.stream().filter(e -> e.getAttrName().equals("湿度"))
                    .collect(Collectors.toMap(s -> s.getRecordTime(), s -> s));

            for (int i = 0; i < 7; i++) {
                EnvReportResult t = new EnvReportResult();
                String key = DateUtil.format(startTime.getTime(), "yyyy-MM-dd");
                t.setDate(key);

                EnvReportDTO tmp = tempMap.get(key);
                if (tmp != null){
                    t.setTempAvg(tmp.getAvg());
                    t.setTempMax(tmp.getMax());
                    t.setTempMin(tmp.getMin());
                }
                tmp = humMap.get(key);
                if (tmp != null){
                    t.setHumAvg(tmp.getAvg());
                    t.setHumMax(tmp.getMax());
                    t.setHumMin(tmp.getMin());
                }
                startTime.add(Calendar.DATE, 1);
                list.add(t);
            }
        } else {
            startTime = CalendarUtil.beginOfMonth(CalendarUtil.calendar(date));
            endTime = CalendarUtil.endOfMonth(CalendarUtil.calendar(date));

            List<EnvReportDTO> ll = this.getBaseMapper().envDayReport(deviceType, deviceId, startTime.getTime(), endTime.getTime());
            Map<String, EnvReportDTO> tempMap =  ll.stream().filter(e -> e.getAttrName().equals("温度"))
                    .collect(Collectors.toMap(s -> s.getRecordTime(), s -> s));
            Map<String, EnvReportDTO> humMap =  ll.stream().filter(e -> e.getAttrName().equals("湿度"))
                    .collect(Collectors.toMap(s -> s.getRecordTime(), s -> s));

            long between = DateUtil.between(startTime.getTime(), endTime.getTime(), DateUnit.DAY);
            for (int i = 0; i <= between; i++) {
                EnvReportResult t = new EnvReportResult();
                String key = DateUtil.format(startTime.getTime(), "yyyy-MM-dd");
                t.setDate(key);

                EnvReportDTO tmp = tempMap.get(key);
                if (tmp != null){
                    t.setTempAvg(tmp.getAvg());
                    t.setTempMax(tmp.getMax());
                    t.setTempMin(tmp.getMin());
                }
                tmp = humMap.get(key);
                if (tmp != null){
                    t.setHumAvg(tmp.getAvg());
                    t.setHumMax(tmp.getMax());
                    t.setHumMin(tmp.getMin());
                }
                startTime.add(Calendar.DATE, 1);
                list.add(t);
            }
        }
        return list;
    }

//    public static void main(String[] args) {
//
//        Date date = DateUtil.parse("2024-06-27 00:00:00", "yyyy-MM-dd HH:mm:ss");
//
//        Calendar startTime = CalendarUtil.beginOfDay(CalendarUtil.calendar(date));
//        Calendar endTime = CalendarUtil.endOfDay(CalendarUtil.calendar(date));
//
//        System.out.println(DateUtil.format(startTime.getTime(), "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(DateUtil.format(endTime.getTime(), "yyyy-MM-dd HH:mm:ss"));
//
//        System.out.println(String.format("%02d", 0));
//        System.out.println(String.format("%02d", 2));
//        System.out.println(String.format("%02d", 5));
//
//        System.out.println(String.format("%02d", 10));
//
//        System.out.println(String.format("%02d", 23));
//
//
//    }
}
