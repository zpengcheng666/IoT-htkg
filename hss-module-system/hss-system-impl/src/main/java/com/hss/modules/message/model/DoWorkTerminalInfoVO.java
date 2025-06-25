package com.hss.modules.message.model;
import lombok.Data;

/**
 * @ClassDescription: 手动值班大屏显示信息
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/3 11:56
 */
@Data
public class DoWorkTerminalInfoVO {

    /**
     * 工作名
     */
    private String workName;
    /**
     * 人员
     */
    private String persons;

    /**
     * 值班日期
     * 如果开始人气和结束日期不是1天则用-拼接
     * 如果是1天则显示1天
     */
    private String day;

}
