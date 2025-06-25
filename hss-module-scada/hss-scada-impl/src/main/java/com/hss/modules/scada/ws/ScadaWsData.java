package com.hss.modules.scada.ws;

import lombok.Data;

/**
 * @author hd
 */
@Data
public class ScadaWsData {

    private String messageType;

    private Object messageContent;
}
