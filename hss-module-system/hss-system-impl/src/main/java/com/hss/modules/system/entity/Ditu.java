package com.hss.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 场景底图
 * @Author: zpc
 * @Date:   2024-02-29
 * @Version: V1.0
 */
@Data
@TableName("DITU")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DITU对象", description="场景底图")
public class Ditu {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**底图名称*/
	@Excel(name = "底图名称", width = 15)
    @ApiModelProperty(value = "底图名称")
	private java.lang.String name;
	/**底图路径*/
	@Excel(name = "底图路径", width = 15)
    @ApiModelProperty(value = "底图路径")
	private java.lang.String imgUrl;
	/**底图文件格式*/
	@Excel(name = "底图文件格式", width = 15)
    @ApiModelProperty(value = "底图文件格式")
	private java.lang.String imgType;
	/**新名称*/
	@Excel(name = "新名称", width = 15)
    @ApiModelProperty(value = "新名称")
	private java.lang.String newName;
	/**存储路径*/
	@Excel(name = "存储路径", width = 15)
	@ApiModelProperty(value = "存储路径")
	private java.lang.String saveUrl;
	/**上传时间*/
	@Excel(name = "上传时间", width = 15)
	@ApiModelProperty(value = "上传时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date upTime;
}
