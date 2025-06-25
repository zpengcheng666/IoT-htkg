package com.hss.modules.scada.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 场景 场景自定义图片上传
 * @Author: zpc
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Data
@TableName("GS_CHANGJING_ZDYTP")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GS_CHANGJING_ZDYTP对象", description="场景 场景自定义图片上传")
public class GsChangJingZDYTP {
    
	/**图片id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "图片id")
	private java.lang.String id;
	/**图片访问路径*/
	@Excel(name = "图片访问路径", width = 15)
    @ApiModelProperty(value = "图片访问路径")
	private java.lang.String picurl;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createdTime;
}
