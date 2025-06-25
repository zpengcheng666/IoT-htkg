package com.hss.modules.system.monitorThing.server;


/**
 * 网速相关信息
 *
 * @author huasheng
 */
public class NetWork {

    /**
     * 上行速度
     */
    private String txPercent ;
    /**
     * 下行速度
     */
    private String rxPercent ;

    public String getTxPercent() {
        return txPercent;
    }

    public void setTxPercent(String txPercent) {
        this.txPercent = txPercent;
    }

    public String getRxPercent() {
        return rxPercent;
    }

    public void setRxPercent(String rxPercent) {
        this.rxPercent = rxPercent;
    }
}

