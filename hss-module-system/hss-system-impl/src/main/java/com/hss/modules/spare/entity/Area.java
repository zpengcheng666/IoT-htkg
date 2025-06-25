package com.hss.modules.spare.entity;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 库区
 * @Author: zpc
 * @Date:   2024-04-25
 * @Version: V1.0
 */
@Data
@TableName("BP_AREA")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BP_AREA对象", description="库区")
public class Area {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**库区编号*/
	@Excel(name = "库区编号", width = 15)
    @ApiModelProperty(value = "库区编号")
	private java.lang.String num;
	/**库区名称*/
	@Excel(name = "库区名称", width = 15)
    @ApiModelProperty(value = "库区名称")
	private java.lang.String name;
	/**所属仓库*/
	@Excel(name = "所属仓库", width = 15)
    @ApiModelProperty(value = "所属仓库")
	private java.lang.String warehouseId;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private java.lang.String remark;

	@ApiModelProperty(value = "deleted",hidden = true)
	@TableField()
	@TableLogic
	private java.lang.Integer deleted;
}
