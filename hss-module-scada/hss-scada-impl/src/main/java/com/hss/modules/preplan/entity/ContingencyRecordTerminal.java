package com.hss.modules.preplan.entity;

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
 * @Description: 特情处置终端关系
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Data
@TableName("T_CONTINGENCY_RECORD_R_TERMINA")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_CONTINGENCY_RECORD_R_TERMINA对象", description="特情处置终端关系")
public class ContingencyRecordTerminal {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**处置ID*/
	@Excel(name = "处置ID", width = 15)
    @ApiModelProperty(value = "处置ID")
	private java.lang.String recordId;
	/**终端ID*/
	@Excel(name = "终端ID", width = 15)
    @ApiModelProperty(value = "终端ID")
	private java.lang.String terminalId;
}
