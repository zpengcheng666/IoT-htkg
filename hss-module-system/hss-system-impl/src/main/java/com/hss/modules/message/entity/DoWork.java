package com.hss.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 手动值班
 * @Author: zpc
 * @Date:   2023-12-06
 * @Version: V1.0
 */
@Data
@TableName("T_DO_WORK")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_DO_WORK对象", description="手动值班")
public class DoWork {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**工作名称*/
	@Excel(name = "工作名称", width = 15)
    @ApiModelProperty(value = "工作名称")
	private java.lang.String workName;
	/**值班日期*/
	@Excel(name = "值班开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "值班开始日期")
	private java.util.Date workDay;

	@Excel(name = "值班结束日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "值班结束日期")
	private java.util.Date workEndDay;

	/**消息状态(0：未发布，1：已发布，2：发布中，3：已过期)*/
	@Excel(name = "消息状态(0：未发布，1：已发布，2：发布中，3：已过期)", width = 15)
    @ApiModelProperty(value = "消息状态(0：未发布，1：已发布，2：发布中，3：已过期)")
	private java.lang.Integer state;
	/**值班人员列表(通过,分割)*/
	@Excel(name = "值班人员列表", width = 15)
    @ApiModelProperty(value = "值班人员列表")
	private java.lang.String persons;
}
