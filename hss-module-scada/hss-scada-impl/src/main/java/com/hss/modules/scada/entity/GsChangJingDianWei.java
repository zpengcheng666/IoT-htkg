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
 * @Description: 场景 点位
 * @Author: zpc
 * @Date:   2022-11-17
 * @Version: V1.0
 */
@Data
@TableName("GS_CHANGJING_DIANWEI")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GS_CHANGJING_DIANWEI对象", description="场景 点位")
public class GsChangJingDianWei {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**网关id*/
	@Excel(name = "网关id", width = 15)
    @ApiModelProperty(value = "网关id")
	private java.lang.String wgid;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
	private java.lang.String sbid;
	/**点位id*/
	@Excel(name = "点位id", width = 15)
    @ApiModelProperty(value = "点位id")
	private java.lang.String dwid;
	/**场景id*/
	@Excel(name = "场景id", width = 15)
    @ApiModelProperty(value = "场景id")
	private java.lang.String cjid;
}
