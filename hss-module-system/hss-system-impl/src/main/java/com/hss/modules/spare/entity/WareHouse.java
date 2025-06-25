package com.hss.modules.spare.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.ArrayList;
import java.util.List;

/**
* @description: 仓库
* @author zpc
* @date 2024/4/25 11:00
* @version 1.0
*/
@Data
@TableName("BP_WAREHOUSE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BP_WAREHOUSE对象", description="仓库")
public class WareHouse {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**仓库名称*/
	@Excel(name = "仓库名称", width = 15)
    @ApiModelProperty(value = "仓库名称")
	private java.lang.String name;
	/**仓库编号*/
	@Excel(name = "仓库编号", width = 15)
    @ApiModelProperty(value = "仓库编号")
	private java.lang.String num;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private java.lang.String remark;

	/**deleted*/
	@ApiModelProperty(value = "deleted",hidden = true)
	@TableField()
	@TableLogic
	private java.lang.Integer deleted;

	@TableField(exist = false)
	private List<Area> children = new ArrayList<>();
}
