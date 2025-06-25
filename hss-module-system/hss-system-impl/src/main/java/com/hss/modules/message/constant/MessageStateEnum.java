package com.hss.modules.message.constant;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/2 14:41
 */
public enum MessageStateEnum {
    /**
     * 未发布
     */
    NOT_PUBLISH(0),
    /**
     * 已发布
     */
    PUBLISH(1),
    /**
     * 已生效
     */
    ENTER(2),
    /**
     * 已过期
     */
    OVER(3);

    public final Integer value;

    MessageStateEnum(Integer value) {
        this.value = value;
    }
}
