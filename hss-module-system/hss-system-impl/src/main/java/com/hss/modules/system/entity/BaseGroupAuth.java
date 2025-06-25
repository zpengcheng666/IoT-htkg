package com.hss.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 机构、按钮关系表
 * @Author: zpc
 * @Date:   2023-05-22
 * @Version: V1.0
 */
@Data
@TableName("BASE_GROUP_AUTH")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_GROUP_AUTH对象", description="机构、按钮关系表")
public class BaseGroupAuth {
    
	/**机构id*/
	@Excel(name = "机构id", width = 15)
    @ApiModelProperty(value = "机构id")
	private java.lang.String orgId;
	/**按钮id*/
	@Excel(name = "按钮id", width = 15)
    @ApiModelProperty(value = "按钮id")
	private java.lang.String authId;
}
