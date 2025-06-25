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
 * @Description: 卫星消息和终端关系表
 * @Author: zpc
 * @Date:   2022-12-23
 * @Version: V1.0
 */
@Data
@TableName("T_SATELLITE_R_TERMINAL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_SATELLITE_R_TERMINAL对象", description="卫星消息和终端关系表")
public class PublishSatelliteTerminal {

	public static final String TERMINAL_TYPE_3SG = "3SG";

	public static final String TERMINAL_TYPE_TV = "TV";

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**卫星临空消息ID*/
	@Excel(name = "卫星临空消息ID", width = 15)
    @ApiModelProperty(value = "卫星临空消息ID")
	private java.lang.String satelliteId;

	/**终端ID*/
	@Excel(name = "终端ID", width = 15)
    @ApiModelProperty(value = "终端ID")
	private java.lang.String terminalId;

	/**终端ID*/
	@Excel(name = "终端类型", width = 15)
	@ApiModelProperty(value = "终端类型")
	private java.lang.String terminalType;
}
