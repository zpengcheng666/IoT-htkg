package com.hss.modules.tool.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 资料类别表
 * @Author: zpc
 * @Date:   2022-12-26
 * @Version: V1.0
 */
@Data
@TableName("MT_DATUM_CLASS")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MT_DATUM_CLASS对象", description="资料类别表")
public class DatumClass {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
	private java.lang.String code;
	/**PID*/
	@Excel(name = "PID", width = 15)
    @ApiModelProperty(value = "PID")
	private java.lang.String pid;

	@TableLogic
	@TableField(select = true)
	@ApiModelProperty(value = "删除标志",hidden = true)
	private String deleted;

	/**文件路径*/
	@Excel(name = "文件路径", width = 15)
	@ApiModelProperty(value = "文件路径")
	private java.lang.String imgUrls;
}
