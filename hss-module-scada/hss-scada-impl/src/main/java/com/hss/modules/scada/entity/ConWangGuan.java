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

import java.util.Date;

/**
 * @Description: 网关
 * @Author: zpc
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Data
@TableName("CON_WANGGUAN")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CON_WANGGUAN对象", description="网关")
public class ConWangGuan {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private String id;
	/**网关名称*/
	@Excel(name = "网关名称", width = 15)
    @ApiModelProperty(value = "网关名称")
	private String name;
	/**网关IP*/
	@Excel(name = "网关IP", width = 15)
    @ApiModelProperty(value = "网关IP")
	private String ip;
	/**网关端口*/
	@Excel(name = "网关端口", width = 15)
    @ApiModelProperty(value = "网关端口")
	private String port;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String note;
	/**接口地址*/
	@Excel(name = "接口地址", width = 15)
    @ApiModelProperty(value = "接口地址")
	private String url;
	/**公钥*/
	@Excel(name = "公钥", width = 15)
    @ApiModelProperty(value = "公钥")
	private String publickey;
	/**私钥*/
	@Excel(name = "私钥", width = 15)
    @ApiModelProperty(value = "私钥")
	private String privatekey;

	@ApiModelProperty(value = "用户名")
	private String userName;

	@ApiModelProperty(value = "密码")
	private String password;

	@ApiModelProperty(value = "最后更新时间")
	private Date lastUpdateTime;

	@ApiModelProperty(value = "站点id")
	private String siteId;

	@ApiModelProperty(value = "站点状态")
	private Integer siteState;
}
