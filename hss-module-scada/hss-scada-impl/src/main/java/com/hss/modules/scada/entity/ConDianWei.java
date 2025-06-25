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
 * @Description: 网关点位
 * @Author: zpc
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Data
@TableName("CON_DIANWEI")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ConDianWei", description="网关点位")
public class ConDianWei {
    
	/**点位id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "点位id")
	private java.lang.String id;
	/**点位名称*/
	@Excel(name = "点位名称", width = 15)
    @ApiModelProperty(value = "点位名称")
	private java.lang.String name;
	/**点位数据类型*/
	@Excel(name = "点位数据类型", width = 15)
    @ApiModelProperty(value = "点位数据类型")
	private java.lang.String dataType;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
	private java.lang.String deviceId;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
	private java.lang.String deviceName;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String note;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createdTime;
	/**修改时间*/
    @ApiModelProperty(value = "修改时间")

	private java.util.Date updatedTime;

	/**网关id**/
	@Excel(name = "网关id", width = 15)
	@ApiModelProperty(value = "网关id")
	private String wgid;
}
