package com.hss.modules.message.model;

import com.hss.core.common.exception.HssBootException;
import com.hss.modules.message.constant.MessageStateEnum;
import lombok.Data;

import java.util.Date;

/**
 * @ClassDescription: 时间校验
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/2 15:02
 */
@Data
public class MessageStateBO {

    private String id;
    private Integer state;
    private Date enterDate;
    private Date exitDate;

    /**
     * 校验状态
     * @param date 时间
     * @return true 修改，false不需要修改
     */
    public Integer getState(Date date) {
        // 如果过期则过期
        if (exitDate.getTime() <= date.getTime()) {
            return MessageStateEnum.OVER.value;
        }
        // 如果未发布则是未发布
        if (MessageStateEnum.NOT_PUBLISH.value.equals(state)) {
            return MessageStateEnum.NOT_PUBLISH.value;
        }
        // 如果进入则进入
        if (enterDate.getTime() <= date.getTime() ) {
            return MessageStateEnum.ENTER.value;
        }
        // 如果未进入则为未进入
        return MessageStateEnum.PUBLISH.value;
    }

    public boolean checkEnter(Date date) {
        long time = date.getTime();
        return enterDate.getTime() <= time && exitDate.getTime() > time;
    }

    /**
     * 校验发布
     * 如果不对则发布异常
     * @param publishDate 发布时间
     */
    public void checkPublish(Date publishDate) {
        if (!MessageStateEnum.NOT_PUBLISH.value.equals(state)) {
            throw new HssBootException("不是未发布状态不能发布");
        }
        if (publishDate.getTime() > exitDate.getTime()) {
            throw new HssBootException("发布的时间不能大于结束时间");
        }
        if (this.enterDate.getTime() <= publishDate.getTime()) {
            this.state = MessageStateEnum.ENTER.value;
        } else {
            this.state = MessageStateEnum.PUBLISH.value;
        }
    }

    /**
     * 校验是否可撤销
     * @return true 需要更新， false 不需要更新
     */
    public boolean checkRevocation() {
        if (MessageStateEnum.PUBLISH.value.equals(state) || MessageStateEnum.ENTER.value.equals(state)) {
            this.state = MessageStateEnum.NOT_PUBLISH.value;
            return true;
        }
        return false;
    }






}
