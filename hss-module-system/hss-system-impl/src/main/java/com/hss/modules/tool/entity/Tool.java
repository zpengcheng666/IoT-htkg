package com.hss.modules.tool.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 工具管理
 * @Author: zpc
 * @Date:   2022-12-23
 * @Version: V1.0
 */
@Data
@TableName("MT_TOOL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MT_TOOL对象", description="工具管理")
public class Tool {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**工具类别名称*/
	@Excel(name = "工具类别名称", width = 15)
    @ApiModelProperty(value = "工具类别名称")
	private java.lang.String goodsSortName;
	/**工具类别*/
	@Excel(name = "工具类别", width = 15)
    @ApiModelProperty(value = "工具类别")
	private java.lang.String goodsSort;
	/**工具名称*/
	@Excel(name = "工具名称", width = 15)
    @ApiModelProperty(value = "工具名称")
	private java.lang.String name;
	/**工具型号*/
	@Excel(name = "工具型号", width = 15)
    @ApiModelProperty(value = "工具型号")
	private java.lang.String goodsModel;
	/**工具编号*/
	@Excel(name = "工具编号", width = 15)
    @ApiModelProperty(value = "工具编号")
	private java.lang.String goodsNum;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
	private java.lang.String unit;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
	private java.lang.Integer count;
	/**出厂时间*/
    @ApiModelProperty(value = "出厂时间")
	private java.util.Date productTime;
	/**存放位置名称*/
	@Excel(name = "存放位置名称", width = 15)
    @ApiModelProperty(value = "存放位置名称")
	private java.lang.String positionName;
	/**存放位置ID*/
	@Excel(name = "存放位置ID", width = 15)
    @ApiModelProperty(value = "存放位置ID")
	private java.lang.String position;

	@TableField(exist = false)
	private java.lang.String position_disp;
	/**配套时间*/
    @ApiModelProperty(value = "配套时间")
	private java.util.Date inDate;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String note;
	/**备件工具(0:备件,1:工具)*/
	@Excel(name = "备件工具(0:备件,1:工具)", width = 15)
    @ApiModelProperty(value = "备件工具(0:备件,1:工具)")
	private java.lang.Integer goodsOrTool;
	/**标准数量*/
	@Excel(name = "标准数量", width = 15)
    @ApiModelProperty(value = "标准数量")
	private java.lang.Integer standard;
	/**责任人ID*/
	@Excel(name = "责任人ID", width = 15)
    @ApiModelProperty(value = "责任人ID")
	private java.lang.String creatorId;
	/**是否可批量*/
	@Excel(name = "是否可批量", width = 15)
    @ApiModelProperty(value = "是否可批量")
	private java.lang.Integer isMultiple;
	/**可用数量*/
	@Excel(name = "可用数量", width = 15)
    @ApiModelProperty(value = "可用数量")
	private java.lang.Integer inUseCount;

	@TableLogic
	@TableField(select = true)
	@ApiModelProperty(value = "删除标志",hidden = true)
	private String deleted;
}
