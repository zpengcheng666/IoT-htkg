package com.hss.modules.linkage.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Set;

/**
 * @author 26060
 */
@ApiModel("根据报警策略id列表查询")
@Data
public class ListByAlarmStrategyIdsDTO {
    private Set<String> ids;
}
