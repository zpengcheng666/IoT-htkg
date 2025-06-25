package com.hss.modules.tool.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.hss.modules.system.model.FilesList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 资料表
 * @Author: zpc
 * @Date:   2022-12-26
 * @Version: V1.0
 */
@Data
@TableName(value = "BI_DATUM", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BI_DATUM对象", description="资料表")
public class Datum {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**密级*/
	@Excel(name = "密级", width = 15)
    @ApiModelProperty(value = "密级")
	private java.lang.String secretLevel;
	@TableField(exist = false)
	private java.lang.String secretLevel_disp;
	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
	private java.lang.String code;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
	private java.lang.String unit;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
	private java.lang.Integer quantity;
	/**规格型号*/
	@Excel(name = "类别名称", width = 15)
    @ApiModelProperty(value = "类别名称")
	private java.lang.String model;
	@TableField(exist = false)
	private java.lang.String model_disp;
	/**存放位置*/
	@Excel(name = "存放位置", width = 15)
    @ApiModelProperty(value = "存放位置")
	private java.lang.String site;
	@TableField(exist = false)
	private java.lang.String site_disp;
	/**编制单位*/
	@Excel(name = "编制单位", width = 15)
    @ApiModelProperty(value = "编制单位")
	private java.lang.String producer;
	/**存档时间*/
    @ApiModelProperty(value = "存档时间")
	private java.util.Date pigeonholeTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String remark;
	@TableLogic
	@TableField(select = true)
	@ApiModelProperty(value = "删除标志",hidden = true)
	private String deleted;

	@TableField(exist = false)
	private Integer checked;

	/**路径*/
	@ApiModelProperty(value = "附件路径")
	private String fileUrls;

	/**附件信息*/
	@ApiModelProperty(value = "附件信息")
	@TableField(typeHandler = JacksonTypeHandler.class)
	private FilesList[] filesList;

}
