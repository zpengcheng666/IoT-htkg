package com.hss.modules.store.model;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 历史数据报表返回数据结构
 */
@Data
public class EnvReportResult {

        /** 报表中的日期 */
        @Excel(name = "日期/时间", needMerge = true, width = 15)
        private String date;

        /** 温度 均值 */
        @Excel(name = "均值", groupName = "温度", width = 15, numFormat = "0.00")
        private Double tempAvg = 0.00;

        /** 温度 高值 */
        @Excel(name = "高值", groupName = "温度", width = 15, numFormat = "0.00")
        private Double tempMax = 0.00;

        /** 温度 低值 */
        @Excel(name = "低值", groupName = "温度", width = 15, numFormat = "0.00")
        private Double tempMin = 0.00;

        /** 湿度 均值 */
        @Excel(name = "均值", groupName = "湿度", width = 15, numFormat = "0.00")
        private Double humAvg = 0.00;

        /** 湿度 高值 */
        @Excel(name = "高值", groupName = "湿度", width = 15, numFormat = "0.00")
        private Double humMax = 0.00;

        /** 湿度 高值 */
        @Excel(name = "低值", groupName = "湿度", width = 15, numFormat = "0.00")
        private Double humMin = 0.00;
}
