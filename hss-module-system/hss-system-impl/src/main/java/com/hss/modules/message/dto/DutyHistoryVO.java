package com.hss.modules.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hss.modules.message.model.DutyPersonModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.jeecgframework.poi.excel.annotation.ExcelIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author hd
 * 值班记录
 */
@ApiModel("值班记录")
@Data
public class DutyHistoryVO {

    @Excel(name = "值班安排", width = 20)
    @ApiModelProperty("值班安排")
    private String dutyName;

    @Excel(name = "值班日期", width = 20, format = "yyyy-MM-dd")
    @ApiModelProperty("值班日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date dutyDate;

    @Excel(name = "值班小组", width = 20)
    @ApiModelProperty("值班小组")
    private String groupName;

    @ApiModelProperty("排班信息")
    @ExcelCollection(name="排班信息")
    private List<DutyPersonModel> shifts;

    @ExcelIgnore
    @ApiModelProperty(value = "排班json", hidden = true)
    private String shiftsJson;
}
