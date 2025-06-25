package com.hss.modules.door.controller;

import com.hss.core.common.system.base.controller.HssController;
import com.hss.modules.door.entity.DoorPersonCard;
import com.hss.modules.door.service.IDoorPersonCardService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 门禁人员卡
 * @Author: zpc
 * @Date:   2023-03-02
 * @Version: V1.0
 */
@Slf4j
@Api(tags="门禁人员卡")
@RestController
@RequestMapping("/door/doorPersonCard")
public class DoorPersonCardController extends HssController<DoorPersonCard, IDoorPersonCardService> {
	@Autowired
	private IDoorPersonCardService doorPersonCardService;
	


	


}
