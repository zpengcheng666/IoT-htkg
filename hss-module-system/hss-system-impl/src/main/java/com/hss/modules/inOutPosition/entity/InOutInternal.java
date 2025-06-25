package com.hss.modules.inOutPosition.entity;

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
 * @Description: 内部人员审批表
 * @Author: zpc
 * @Date:   2022-12-13
 * @Version: V1.0
 */
@Data
@TableName("MT_IN_OUT_INTERNAL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MT_IN_OUT_INTERNAL对象", description="内部人员审批表")
public class InOutInternal {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**人员信息ID*/
	@Excel(name = "人员信息ID", width = 15)
    @ApiModelProperty(value = "人员信息ID")
	private java.lang.String personId;
	/**审批单ID*/
	@Excel(name = "审批单ID", width = 15)
    @ApiModelProperty(value = "审批单ID")
	private java.lang.String listId;
	/**出入状态*/
	@Excel(name = "出入状态", width = 15)
    @ApiModelProperty(value = "出入状态")
	private java.lang.Integer inOutStatus;
	/**入库时间*/
    @ApiModelProperty(value = "入库时间")
	private java.util.Date inTime;
	/**出库时间*/
    @ApiModelProperty(value = "出库时间")
	private java.util.Date outTime;
	@ApiModelProperty(value = "性别")
	private Integer sex;
	@ApiModelProperty(value = "工作你单位")
	private String workunit;
	@ApiModelProperty(value = "职位")
	private String position;
}
