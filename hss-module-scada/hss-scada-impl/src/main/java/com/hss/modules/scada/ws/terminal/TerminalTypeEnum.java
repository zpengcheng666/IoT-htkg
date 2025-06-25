package com.hss.modules.scada.ws.terminal;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/2 9:22
 */
public enum TerminalTypeEnum {
    DUTY("duty", "1"),
    DO_WORK("handDuty", "12"),
    NOTICE("notice", "2"),
    /**
     * 全部卫星信息
     */
    SATELLITE_ALL("satelliteAll", "3"),

    /**
     * 卫星临空
     */
    SATELLITE("satellite", "31"),
    /**
     * 即将临空
     */
    SATELLITE_NOT("satelliteNot", "32"),
    SATELLITE_SECURE("satelliteSecure", "33"),
    WEATHER("weather", "4"),
    IN_OUT("door", "5"),
    CARD("card", "51"),
    /**
     * 报警信息
     */
    ALARM("alarm", "6"),
    CHECK_DOOR("ajm", "7"),
    YJCZ("yjcz", "8");
    private final String type;
    private final String typeCode;

    TerminalTypeEnum(String type, String typeCode) {
        this.type = type;
        this.typeCode = typeCode;
    }

    public String getType() {
        return type;
    }

    public String getTypeCode() {
        return typeCode;
    }
}
