package com.hss.modules.spare.model;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassDescription: 备品备件字典类
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/25 16:35
 */
@Data
@ApiOperation("备品备件字典类")
@AllArgsConstructor
@NoArgsConstructor
public class SpareDict {

    /**
     * 值
     */
    @ApiModelProperty("值")
    private Object value;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String label;
}
