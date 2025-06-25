package com.hss.modules.store.model;

import lombok.Data;

@Data
public class StoreHistoryStatWrapper implements java.io.Serializable{

    private String attrName;

    private String attrEnName;

    private String recordTime;

    private Long cnt;

}
