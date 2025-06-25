package com.hss.modules.door.dto;

import com.hss.modules.door.entity.DoorPerson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author hd
 */
@Data
@ApiModel("门历史数据新增参数")
public class DoorHistoryAddDTO {

    @ApiModelProperty("门id")
    private String doorId;

    @ApiModelProperty(value = "出入类型（'entryCard', '进门', 'exitCard', '出门'）")
    private java.lang.String accessType;

    @ApiModelProperty(value = "开门时间")
    private java.util.Date openTime;

    @ApiModelProperty(value = "进出人数")
    private java.lang.Integer perNumRecord;

    @ApiModelProperty(value = "进入原因")
    private java.lang.String enterReason;

    @ApiModelProperty(value = "进出人员信息")
    private List<DoorPerson> perList;
}
