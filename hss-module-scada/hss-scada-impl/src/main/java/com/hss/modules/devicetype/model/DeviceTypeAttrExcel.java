package com.hss.modules.devicetype.model;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author hd
 * 设备类型属性excel
 */
@Data
public class DeviceTypeAttrExcel {
    @Excel(name = "属性id", width = 20, orderNum = "0")
    private String id;

    @Excel(name = "属性名称", width = 20, orderNum = "1")
    private String name;

    @Excel(name = "属性英文名称", width = 20, orderNum = "1")
    private String category;

    @Excel(name = "数据类型", width = 10, orderNum = "2")
    private String dataType;

    @Excel(name = "初始值", width = 10, orderNum = "3")
    private String initValue;

    @Excel(name = "最小值", width = 10, orderNum = "4")
    private String minValue;

    @Excel(name = "最大值", width = 10, orderNum = "5")
    private String maxValue;

    @Excel(name = "单位", width = 10, orderNum = "6")
    private String unit;

    @Excel(name = "排序", width = 10, orderNum = "7")
    private Integer sortNumber;

    @Excel(name = "展示区域", width = 30, orderNum = "8")
    private String displayAreas;

    @Excel(name = "是否可控", width = 5, orderNum = "9")
    private Integer isAct;

    @Excel(name = "是否联动", width = 5, orderNum = "10")
    private Integer isAssociate;

    @Excel(name = "是否关联变量", width = 5, orderNum = "11")
    private Integer isAssociateVar;

    @Excel(name = "是否配置", width = 5, orderNum = "12")
    private Integer isConfigurable;

    @Excel(name = "是否存储", width = 5, orderNum = "13")
    private Integer isSave;

    @Excel(name = "类型id", width = 20, orderNum = "14")
    private String typeId;

    @Excel(name = "控制信息json", width = 15, orderNum = "15")
    private String actOrders;

    @Excel(name = "配置信息json", width = 15, orderNum = "16")
    private String configOptions;

    @Excel(name = "值映射json", width = 15, orderNum = "17")
    private String valueMap;
}
