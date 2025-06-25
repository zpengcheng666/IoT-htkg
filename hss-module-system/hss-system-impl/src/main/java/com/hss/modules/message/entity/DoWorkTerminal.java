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
 * @Description: 手动值班终端中间表
 * @Author: zpc
 * @Date:   2023-12-06
 * @Version: V1.0
 */
@Data
@TableName("T_DO_WORK_TERMINAL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_DO_WORK_TERMINAL对象", description="手动值班终端中间表")
public class DoWorkTerminal {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**doWorkId*/
	@Excel(name = "doWorkId", width = 15)
    @ApiModelProperty(value = "doWorkId")
	private java.lang.String doWorkId;
	/**terminalId*/
	@Excel(name = "terminalId", width = 15)
    @ApiModelProperty(value = "terminalId")
	private java.lang.String terminalId;
}
