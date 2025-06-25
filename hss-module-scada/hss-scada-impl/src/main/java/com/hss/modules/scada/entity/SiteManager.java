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

import java.util.List;

/**
 * @Description: 站点管理
 * @Author: zpc
 * @Date:   2023-06-06
 * @Version: V1.0
 */
@Data
@TableName("SITE_MANAGER")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SITE_MANAGER对象", description="站点管理")
public class SiteManager {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**站点名字*/
	@Excel(name = "站点名字", width = 15)
    @ApiModelProperty(value = "站点名字")
	private java.lang.String name;

	@ApiModelProperty(value = "站点编号")
	private String siteCode;
	/**站点ip*/
	@Excel(name = "站点ip", width = 15)
    @ApiModelProperty(value = "站点ip")
	private java.lang.String ip;
	/**端口号*/
	@Excel(name = "端口号", width = 15)
    @ApiModelProperty(value = "端口号")

	private java.lang.String port;
	@ApiModelProperty(value = "站点状态: 0 站控，1中控")
	private Integer siteState;


	@ApiModelProperty(value = "网关列表")
	@TableField(exist = false)
	private List<ConWangGuan> gatewayList;
}
