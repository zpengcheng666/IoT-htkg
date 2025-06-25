package com.hss.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 附件及图片上传表
 * @Author: zpc
 * @Date:   2023-02-01
 * @Version: V1.0
 */
@Data
@TableName("T_FILE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_FILE对象", description="附件及图片上传表")
public class FilesUtil {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**附件ID*/
	@Excel(name = "附件ID", width = 15)
    @ApiModelProperty(value = "附件ID")
	private java.lang.String fileId;
	/**记录ID*/
	@Excel(name = "记录ID", width = 15)
    @ApiModelProperty(value = "记录ID")
	private java.lang.String recordId;
	/**上传者*/
	@Excel(name = "上传者", width = 15)
    @ApiModelProperty(value = "上传者")
	private java.lang.String uploader;
	/**上传时间*/
    @ApiModelProperty(value = "上传时间")
	private java.util.Date uploadTime;
	/**附件名称*/
	@Excel(name = "附件名称", width = 15)
    @ApiModelProperty(value = "附件名称")
	private java.lang.String name;
	/**附件类型*/
	@Excel(name = "附件类型", width = 15)
    @ApiModelProperty(value = "附件类型")
	private java.lang.String type;
	/**静态文件IDHash路径*/
	@Excel(name = "静态文件IDHash路径", width = 15)
    @ApiModelProperty(value = "静态文件IDHash路径")
	private java.lang.String hashPath;
	/**createdTime*/
    @ApiModelProperty(value = "createdTime")
	private java.util.Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime")
	private java.util.Date updatedTime;
	/**deleted*/
    @ApiModelProperty(value = "deleted")
	@TableField(select = true)
	@TableLogic
	private java.lang.Integer deleted;
}
