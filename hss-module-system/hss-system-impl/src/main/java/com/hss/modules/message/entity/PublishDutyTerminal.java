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
 * @Description: 值班消息和终端关系表
 * @Author: zpc
 * @Date:   2022-12-23
 * @Version: V1.0
 */
@Data
@TableName("T_DUTY_R_TERMINAL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_DUTY_R_TERMINAL对象", description="值班消息和终端关系表")
public class PublishDutyTerminal {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**值班信息ID*/
	@Excel(name = "值班信息ID", width = 15)
    @ApiModelProperty(value = "值班信息ID")
	private java.lang.String dutyId;
	/**终端ID*/
	@Excel(name = "终端ID", width = 15)
    @ApiModelProperty(value = "终端ID")
	private java.lang.String terminalId;
}
