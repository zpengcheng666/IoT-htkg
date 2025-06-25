package com.hss.modules.scada.model;

import com.hss.modules.scada.entity.ConSheBei;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceRunRecordVo extends ConSheBei {

    private Date lastStartTime;

    private Date lastStopTime;

    private Long lastSumTime;

    private Long totalSumTime;

}
