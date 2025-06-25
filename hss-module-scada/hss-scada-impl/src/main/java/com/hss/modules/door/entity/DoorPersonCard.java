package com.hss.modules.door.entity;

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
 * @Description: 门禁人员卡
 * @Author: zpc
 * @Date:   2023-03-02
 * @Version: V1.0
 */
@Data
@TableName("DOOR_PERSON_CARD")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DOOR_PERSON_CARD对象", description="门禁人员卡")
public class DoorPersonCard {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**卡号*/
	@Excel(name = "卡号", width = 15)
    @ApiModelProperty(value = "卡号")
	private java.lang.String cardNo;
	/**人员ID*/
	@Excel(name = "人员ID", width = 15)
    @ApiModelProperty(value = "人员ID")
	private java.lang.String personId;
	/**卡类型（0：正常卡，1：胁迫卡，2：附加卡）*/
	@Excel(name = "卡类型（0：正常卡，1：胁迫卡，2：附加卡）", width = 15)
    @ApiModelProperty(value = "卡类型（0：正常卡，1：胁迫卡，2：附加卡）")
	private java.lang.String cardType;
	/**createdTime*/
    @ApiModelProperty(value = "createdTime")
	private java.util.Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime")
	private java.util.Date updatedTime;
	/**deleted*/
	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted")
	private java.lang.Integer deleted;
}
