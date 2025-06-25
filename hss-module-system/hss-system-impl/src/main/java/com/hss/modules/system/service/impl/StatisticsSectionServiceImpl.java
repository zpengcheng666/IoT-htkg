package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.StatisticsSection;
import com.hss.modules.system.mapper.StatisticsSectionMapper;
import com.hss.modules.system.model.StatisticsSectionModel;
import com.hss.modules.system.service.IStatisticsSectionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Description: 分布区间统计设置
 * @Author: zpc
 * @Date: 2022-12-05
 * @Version: V1.0
 */
@Service
public class StatisticsSectionServiceImpl extends ServiceImpl<StatisticsSectionMapper, StatisticsSection> implements IStatisticsSectionService {

    @Override
    public List<StatisticsSection> queryStatistics(String devAttrId) {
        return this.getBaseMapper().queryDevAttrId(devAttrId);
    }

    @Override
    public void addAll(StatisticsSectionModel model) {
        List<String> modelList = model.getIntervalValueList();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        List<StatisticsSection> params = modelList.stream().map(e -> {
            StatisticsSection entity = new StatisticsSection();
            entity.setAttrEnName(model.getAttrEnName());
            entity.setAttrName(model.getAttrName());
            entity.setDevClassid(model.getDevClass());
            entity.setDevAttrid(model.getAttrId());
            entity.setOrderNum(atomicInteger.get());
            atomicInteger.getAndAdd(1);

            String[] tmp = StringUtils.split(e, ",");
            String min = tmp[0];
            String max = tmp[1];
            String calc = "";
            if ("-∞".equals(min)) {
                calc = "x<" + max;
            } else {
                if ("+∞".equals(max)) {
                    calc = "x>=" + min;
                } else {
                    calc = min + "<=x&& x<" + max;
                }
            }
            entity.setSectionCalculation(calc);
            entity.setSectionDisplay(e);

            return entity;
        }).collect(Collectors.toList());

        this.getBaseMapper().insertAll(params);
    }

    @Override
    public void deleteAndInsert(StatisticsSectionModel model) {
        //1.根据id将列表中相关的属性删除
        List<String> idList = model.getIdList();

        //2.根据idList获取属性名称、属性英文名称、设备类型id
        String ids = idList.get(0);
        StatisticsSection byId = this.getById(ids);
        String attrName = byId.getAttrName();
        String attrEnName = byId.getAttrEnName();
        String devClassid = byId.getDevClassid();
        String attrId = byId.getDevAttrid();

        //3.根据id删除列表
        for (String s : idList) {
            this.removeById(s);
        }

        //4. 重新写入属性区间值
        List<String> modelList = model.getIntervalValueList();
        List<StatisticsSection> params = modelList.stream().map(e -> {
            StatisticsSection entity = new StatisticsSection();
            String uuid = UUID.randomUUID().toString().replace("-", "");//获取UUID并转化为String对象
            entity.setId(uuid);
            entity.setAttrEnName(attrEnName);
            entity.setAttrName(attrName);
            entity.setDevClassid(devClassid);
            entity.setDevAttrid(attrId);

            String[] tmp = StringUtils.split(e, ",");
            String min = tmp[0];
            String max = tmp[1];
            String calc = "";
            if ("-∞".equals(min)) {
                calc = "x<" + max;
            } else {
                if ("+∞".equals(max)) {
                    calc = "x>=" + min;
                } else {
                    calc = min + "<=x&& x<" + max;
                }
            }
            entity.setSectionCalculation(calc);
            entity.setSectionDisplay(e);
            return entity;
        }).collect(Collectors.toList());

        this.getBaseMapper().insertAll(params);
    }

    @Override
    public List<StatisticsSection> listByDeviceTypeIdAndEnName(String deviceTypeId, String enName) {
        return baseMapper.listByDeviceTypeIdAndEnName(deviceTypeId, enName);
    }
}
