package com.hss.modules.maintain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 保养要求表
 * @Author: zpc
 * @Date:   2022-12-27
 * @Version: V1.0
 */
@Data
@TableName("MT_MAINTAIN_OPERATE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MT_MAINTAIN_OPERATE对象", description="保养要求表")
public class MaintainOperate {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**技术要求*/
	@Excel(name = "技术要求", width = 15)
    @ApiModelProperty(value = "技术要求")
	private java.lang.String thchRequire;
	/**排序号*/
	@Excel(name = "排序号", width = 15)
    @ApiModelProperty(value = "排序号")
	private java.lang.Integer index1;
	/**项目ID*/
//	@Excel(name = "项目ID", width = 15)
    @ApiModelProperty(value = "项目ID")
	private java.lang.String itemId;
	/**保养类别ID*/
//	@Excel(name = "保养类别ID", width = 15)
    @ApiModelProperty(value = "保养类别ID")
	private java.lang.String itemClass;

	@Excel(name = "保养类别", width = 15)
	@TableField(exist = false)
	private String itemClass_disp;
	/**设备类别ID*/
//	@Excel(name = "设备类别ID", width = 15)
    @ApiModelProperty(value = "设备类别ID")
	private java.lang.String deviceClassId;
	@TableField(select = true)
	@TableLogic
	@ApiModelProperty(value = "删除标志")
	private String deleted;
}
