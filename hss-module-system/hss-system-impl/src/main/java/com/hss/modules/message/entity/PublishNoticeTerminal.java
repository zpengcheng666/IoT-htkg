package com.hss.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 通知消息和终端关系表
 * @Author: zpc
 * @Date:   2022-12-23
 * @Version: V1.0
 */
@Data
@TableName("T_NOTICE_R_TERMINAL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_NOTICE_R_TERMINAL对象", description="通知消息和终端关系表")
public class PublishNoticeTerminal {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**通知公告ID*/
	@Excel(name = "通知公告ID", width = 15)
    @ApiModelProperty(value = "通知公告ID")
	private java.lang.String noticeId;
	/**终端ID*/
	@Excel(name = "终端ID", width = 15)
    @ApiModelProperty(value = "终端ID")
	private java.lang.String terminalId;
}
