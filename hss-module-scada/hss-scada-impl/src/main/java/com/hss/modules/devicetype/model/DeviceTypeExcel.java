package com.hss.modules.devicetype.model;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.util.List;

/**
 * @author hd
 * 设备类型excel
 */
@Data
public class DeviceTypeExcel {
    @Excel(name = "id", width = 20, needMerge = true, orderNum = "0")
    private String id;

    @Excel(name = "设备名称", width = 20, needMerge = true, orderNum = "1")
    private String name;

    @Excel(name = "设备英文名称", width = 20, needMerge = true, orderNum = "2")
    private String type;

    @ExcelCollection(name = "设备属性", orderNum = "3")
    private List<DeviceTypeAttrExcel> attrList;




}
