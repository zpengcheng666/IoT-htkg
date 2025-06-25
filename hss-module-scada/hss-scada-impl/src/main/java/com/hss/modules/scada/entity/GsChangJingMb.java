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

/**
 * @Description: 场景 场景模板
 * @Author: zpc
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Data
@TableName("GS_CHANGJING_MB")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GS_CHANGJING_MB对象", description="场景 场景模板")
public class GsChangJingMb {
    
	/**场景模板id*/
	@TableId(type = IdType.ASSIGN_UUID)
	@Excel(name = "场景模板id", width = 15)
    @ApiModelProperty(value = "场景模板id")
	private java.lang.String id;
	/**场景模板名称*/
	@Excel(name = "场景模板名称", width = 15)
    @ApiModelProperty(value = "场景模板名称")
	private java.lang.String name;
	/**画面缩略图(base64)*/
	@Excel(name = "画面缩略图(base64)", width = 15)
    @ApiModelProperty(value = "画面缩略图(base64)")
	private java.lang.Object stagebase64;
	/**场景模板画面JSON*/
	@Excel(name = "场景模板画面JSON", width = 15)
    @ApiModelProperty(value = "场景模板画面JSON")
	private java.lang.Object stagedatajson;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createdTime;
	/**修改时间*/
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updatedTime;
}
