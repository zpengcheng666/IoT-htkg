package com.hss.modules.maintain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

/**
 * @Description: 保养方案表
 * @Author: zpc
 * @Date:   2022-12-15
 * @Version: V1.0
 */
@Data
@TableName("MT_MAINTAIN_SCHEMAS")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MT_MAINTAIN_SCHEMAS对象", description="保养方案表")
public class MaintainSchemas {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "方案名称", width = 15)
    @ApiModelProperty(value = "方案名称")
	private java.lang.String schemasName;
	/**保养类别*/
	@Excel(name = "保养类别", width = 15)
    @ApiModelProperty(value = "保养类别")
	private java.lang.String itemClassId;
	@TableField(exist = false)
	private java.lang.String itemClass_disp;
	/**保养周期*/
	@Excel(name = "保养周期,运行时长", width = 15)
    @ApiModelProperty(value = "保养周期,运行时长")
	private java.lang.Integer period;
	@TableField
	@TableLogic
	@ApiModelProperty(value = "删除标志")
	private Integer deleted;

	@ApiModelProperty(value = "设备类别数组")
	@TableField(exist = false)
	private List<String> deviceClassIds;
}
