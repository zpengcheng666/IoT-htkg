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
 * @Description: 特情处置工作
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Data
@TableName("T_CONTINGENCY_RECORD_WORKITEM")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_CONTINGENCY_RECORD_WORKITEM对象", description="特情处置工作")
public class ContingencyRecordWorkitem {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**工作项名称*/
	@Excel(name = "工作项名称", width = 15)
    @ApiModelProperty(value = "工作项名称")
	private java.lang.String name;
	/**工作内容*/
	@Excel(name = "工作内容", width = 15)
    @ApiModelProperty(value = "工作内容")
	private java.lang.String content;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
	private java.lang.Integer index1;
	/**工作情况*/
	@Excel(name = "工作情况", width = 15)
    @ApiModelProperty(value = "工作情况")
	private java.lang.String workProcess;
	/**完成状态*/
	@Excel(name = "完成状态", width = 15)
    @ApiModelProperty(value = "完成状态")
	private java.lang.Integer isCompleted;
	/**提交人*/
	@Excel(name = "提交人", width = 15)
    @ApiModelProperty(value = "提交人")
	private java.lang.String submitter;
	/**提交时间*/
    @ApiModelProperty(value = "提交时间")
	private java.util.Date submitTime;
	/**阶段名称*/
	@Excel(name = "阶段名称", width = 15)
    @ApiModelProperty(value = "阶段名称")
	private java.lang.String stageId;


}
