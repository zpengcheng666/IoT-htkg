package com.hss.modules.message.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author hd
 */
@Data
@ApiModel("自动排班请求参数")
public class DutyShiftsAutomaticDTO {
    private String dutyId;

    private Integer days;
}
