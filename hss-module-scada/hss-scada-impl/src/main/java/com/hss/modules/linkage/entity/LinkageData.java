package com.hss.modules.linkage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @Description: 联动数据记录
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Data
@TableName("T_LINKAGE_DATA")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_LINKAGE_DATA对象", description="联动数据记录")
public class LinkageData {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**联动策略ID*/
	@Excel(name = "联动策略ID", width = 15)
    @ApiModelProperty(value = "联动策略ID")
	private java.lang.String linkageStrategyId;
	/**动作ID*/
	@Excel(name = "动作ID", width = 15)
    @ApiModelProperty(value = "动作ID")
	private java.lang.String actionId;
	/**动作执行结果*/
	@Excel(name = "动作执行结果", width = 15)
    @ApiModelProperty(value = "动作执行结果")
	private java.lang.String result;
	/**动作执行时间*/
	@Excel(name = "动作执行时间", width = 15)
    @ApiModelProperty(value = "动作执行时间")
	private Date actionTime;
}
