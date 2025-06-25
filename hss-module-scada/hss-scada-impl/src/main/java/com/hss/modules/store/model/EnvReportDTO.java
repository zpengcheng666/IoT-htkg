package com.hss.modules.store.model;

import lombok.Data;

@Data
public class EnvReportDTO {

    private String recordTime;

    private String attrName;

    private double max;

    private double min;

    private double avg;
}
