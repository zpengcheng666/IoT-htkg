package com.hss.modules.store.entity;

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
 * @Description: 设备运行时数据
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
@Data
@TableName("STORE_DATA")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="STORE_DATA对象", description="设备运行时数据")
public class StoreData {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**变量ID*/
	@Excel(name = "变量ID", width = 15)
    @ApiModelProperty(value = "变量ID")
	private java.lang.String variableId;
	/**记录值*/
	@Excel(name = "记录值", width = 15)
    @ApiModelProperty(value = "记录值")
	private java.lang.String recordValue;
	/**记录时间*/
    @ApiModelProperty(value = "记录时间")
	private java.util.Date recordTime;
	/**标定值范围*/
	@Excel(name = "标定值范围", width = 15)
    @ApiModelProperty(value = "标定值范围")
	private java.lang.String range;
}
