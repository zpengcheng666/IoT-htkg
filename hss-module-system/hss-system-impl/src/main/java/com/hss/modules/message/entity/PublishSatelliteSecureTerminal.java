package com.hss.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author 26060
 */
@Data
@TableName("T_SECURE_TERMINAL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PublishSatelliteSecureTerminal {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String msgId;
    private String terminalId;


}
