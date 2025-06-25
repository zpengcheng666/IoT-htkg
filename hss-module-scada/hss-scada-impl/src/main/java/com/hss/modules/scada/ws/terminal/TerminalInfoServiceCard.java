package com.hss.modules.scada.ws.terminal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hss.modules.door.dto.CardDoorDTO;
import com.hss.modules.door.service.IDoorDataService;
import com.hss.modules.door.vo.CardDoorVO;
import com.hss.modules.system.entity.BaseTerminal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/2 9:36
 */
@Service
public class TerminalInfoServiceCard implements TerminalInfoService{

    @Autowired
    private IDoorDataService doorDataService;


    @Override
    public boolean processHave(Set<String> types) {
        return types.contains(TerminalTypeEnum.CARD.getType());
    }

    @Override
    public Collection<TerminalMsg> list(BaseTerminal terminal, Set<String> types) {
        return Collections.singleton(new TerminalMsg(TerminalTypeEnum.CARD.getTypeCode(), list(terminal.getDoorId())));
    }
    private List<CardDoorVO> list(String doorId) {
        if (StringUtils.isBlank(doorId)) {
            return Collections.emptyList();
        }
        CardDoorDTO dto = new CardDoorDTO();
        dto.setPageNo(1);
        dto.setPageSize(20);
        dto.setDoorIds(Collections.singletonList(doorId));
        IPage<CardDoorVO> resultPage = doorDataService.pageByCard(dto);
        return resultPage.getRecords();
    }
}
