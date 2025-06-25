package com.hss.modules.scada.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @Description: 设备表
 * @Author: zpc
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Data
@TableName("GS_CHANGJING")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GS_CHANGJING对象", description="场景")
public class GsChangJing {
    
	/**场景id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "场景id")
	private String id;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private String description;
	/**是否发布*/
	@Excel(name = "是否发布", width = 15)
    @ApiModelProperty(value = "是否发布")
	private String isPublished;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createdTime;
	/**修改时间*/
    @ApiModelProperty(value = "修改时间")
	private Date updatedTime;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
	private Integer deleted;
	/**场景名称*/
	@Excel(name = "场景名称", width = 15)
    @ApiModelProperty(value = "场景名称")
	private String name;
	/**场景画面JSON*/
	@Excel(name = "场景画面JSON", width = 15)
    @ApiModelProperty(value = "场景画面JSON")
	private String datajson;
	/**场景绑定的所有设备*/
	@Excel(name = "场景绑定的所有设备", width = 15)
    @ApiModelProperty(value = "场景绑定的所有设备")
	private String datakeyarray;
	/**场景缩略图(base64)*/
	@Excel(name = "场景缩略图(base64)", width = 15)
	@ApiModelProperty(value = "场景缩略图(base64)")
	private String base64;

	/**场景所属子系统*/
	@Excel(name = "所属子系统", width = 15)
	@ApiModelProperty(value = "所属子系统")
	private String subSystem;

	@TableField(exist = false)
	@ApiModelProperty(value = "所属子系统名字")
	private String subSystemName;

	/**场景所属子模块*/
	@Excel(name = "场景所属子模块Id", width = 15)
	@ApiModelProperty(value = "场景所属子模块Id")
	@TableField("SUB_MODULE")
	private String moduleId;

	@TableField(exist = false)
	@ApiModelProperty(value = "场景所属子模块名字")
	private String moduleName;
}
