package com.hss.modules.door.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.door.entity.DoorPersonCard;
import com.hss.modules.door.entity.DoorPersonSys;
import com.hss.modules.door.mapper.DoorPersonMapper;
import com.hss.modules.door.service.IDoorPersonCardService;
import com.hss.modules.door.service.IDoorPersonService;
import com.hss.modules.scada.entity.ConWangGuan;
import com.hss.modules.scada.service.IConWangGuanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 门禁人员
 * @Author: zpc
 * @Date:   2023-03-02
 * @Version: V1.0
 */
@Service
@Slf4j
public class DoorPersonServiceImpl extends ServiceImpl<DoorPersonMapper, DoorPersonSys> implements IDoorPersonService {
    @Autowired
    private IConWangGuanService wangGuanService;
    @Autowired
    private IDoorPersonCardService doorPersonCardService;



    @Override
    public void sync() {
        HashMap<String, DoorPersonSys> personMap = new HashMap<>();
        ArrayList<DoorPersonCard> cardList = new ArrayList<>();
        for (ConWangGuan wangGuan : wangGuanService.list()) {
            //获取门禁人员json
            String dataJson = wangGuanService.getDoorPersonListJson(wangGuan);
            if (dataJson != null){
                JSONArray jsonArray = JSONObject.parseArray(dataJson);
                for (Object o : jsonArray) {
                    JSONObject jsonObject = (JSONObject) o;
                    String personId = jsonObject.getString("id");
                    DoorPersonSys doorPersonSys = personMap.get(personId);
                    if (doorPersonSys == null){
                        doorPersonSys = createPersion(jsonObject, personId);
                        personMap.put(personId,doorPersonSys);
                    }
                    String card1 = jsonObject.getString("cardNo1");
                    if (StringUtils.isNotBlank(card1)) {
                        DoorPersonCard doorPersonCard = new DoorPersonCard();
                        doorPersonCard.setCardNo(card1);
                        doorPersonCard.setPersonId(personId);
                        doorPersonCard.setCardType("0");
                        cardList.add(doorPersonCard);
                    }
                    String card2 = jsonObject.getString("cardNo2");
                    if (StringUtils.isNotBlank(card2)) {
                        DoorPersonCard doorPersonCard = new DoorPersonCard();
                        doorPersonCard.setCardNo(card2);
                        doorPersonCard.setPersonId(personId);
                        doorPersonCard.setCardType("2");
                        cardList.add(doorPersonCard);
                    }

                }
            }
        }
        delAll();
        if (!personMap.isEmpty()){
            saveBatch(personMap.values());
        }
        if (!cardList.isEmpty()){
            doorPersonCardService.saveBatch(cardList);
        }



    }

    @NotNull
    private DoorPersonSys createPersion(JSONObject jsonObject, String personId) {
        DoorPersonSys doorPersonSys;
        doorPersonSys = new DoorPersonSys();
        doorPersonSys.setPersonId(personId);
        doorPersonSys.setName(jsonObject.getString("name"));
        JSONArray fields = jsonObject.getJSONArray("fields");
        for (Object field : fields) {
            JSONObject fieldObject = (JSONObject) field;
            if ("单位".equals(fieldObject.get("name"))){
                doorPersonSys.setDepartment(fieldObject.getString("value"));
                continue;
            }
            if ("职务".equals(fieldObject.get("name"))){
                doorPersonSys.setTitle(fieldObject.getString("value"));
                continue;
            }
            if ("性别".equals(fieldObject.get("name"))){
                doorPersonSys.setSex(fieldObject.getInteger("value"));
            }

        }
        return doorPersonSys;
    }

    @Override
    public DoorPersonSys getByCardNumber(String cardNumber) {
        //根据卡号查询人员信息
        return baseMapper.getByCardNumber(cardNumber);
    }

    @Override
    public IPage<DoorPersonSys> queryPageList(Page<DoorPersonSys> page, QueryWrapper<DoorPersonSys> queryWrapper) {
        Page<DoorPersonSys> page1 = page(page, queryWrapper);
        for (DoorPersonSys record : page1.getRecords()) {
            //获取卡列表
            List<DoorPersonCard> cardList = doorPersonCardService.listCardByPersonId(record.getPersonId());
            for (DoorPersonCard doorPersonCard : cardList) {
                if ("0".equals(doorPersonCard.getCardType())){
                    record.setMainCard(doorPersonCard.getCardNo());
                    continue;
                }
                if ("2".equals(doorPersonCard.getCardType())){
                    record.setAttacheCard(doorPersonCard.getCardNo());
                }
            }
        }
        return page1;
    }

    private void delAll(){
        //查询全部id
        List<String> ids = baseMapper.listAllId();
        if (!ids.isEmpty()){
            removeByIds(ids);
        }
        //删除全部
        doorPersonCardService.delAll();

    }


}
