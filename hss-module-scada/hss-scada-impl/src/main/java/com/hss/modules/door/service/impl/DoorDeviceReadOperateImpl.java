package com.hss.modules.door.service.impl;

import com.hss.modules.door.constant.DoorConstant;
import com.hss.modules.door.entity.DoorData;
import com.hss.modules.door.entity.DoorHistory;
import com.hss.modules.door.entity.DoorPersonSys;
import com.hss.modules.door.service.IDoorDataService;
import com.hss.modules.door.service.IDoorHistoryService;
import com.hss.modules.door.service.IDoorPersonService;
import com.hss.modules.scada.additional.DeviceMqttReadEventListenerAdditional;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.service.DeviceDataService;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.scada.service.IConSheBeiService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zpc
 * @version 1.0
 * @description: 开门附加操作
 * @date 2024/3/20 9:42
 */
@Service
public class DoorDeviceReadOperateImpl implements DeviceMqttReadEventListenerAdditional {

    @Autowired
    private IDoorDataService doorDataService;
    @Autowired
    private IConSheBeiService conSheBeiService;
    @Autowired
    private IDoorHistoryService doorHistoryService;
    @Autowired
    private IDoorPersonService doorPersonService;
    @Autowired
    private DeviceDataService deviceDataService;
    @Autowired
    private IConDeviceAttributeService deviceAttributeService;

    /**
     * 通行方向（1：进，2：出）
     * 通行方式（1：刷卡，2：密码，3：指纹，4：人脸，5：按钮开门，6：平台开门，7：北向接口开门）
     */

    @Override
    public void additionalOperate(String deviceId, List<ConDeviceAttribute> attributeList) {
        Map<String, ConDeviceAttribute> attrMap = attributeList.stream().collect(Collectors.toMap(ConDeviceAttribute::getEnName, o -> o));
        DoorProcess doorProcess = new DoorProcess();
        inCard(attrMap, deviceId, doorProcess);
        outCard(attrMap, deviceId, doorProcess);
        inOpen(attrMap, deviceId, doorProcess);
        outOpen(attrMap, deviceId, doorProcess);
    }

    /**
     * 进门刷卡
     */
    private void inCard(Map<String, ConDeviceAttribute> attrMap, String deviceId, DoorProcess doorProcess) {

        ConDeviceAttribute inCardAttr = attrMap.get(DoorConstant.EN_ENTRY_CARD);
        if (inCardAttr == null) {
            return;
        }
        String result = getResult(attrMap, DoorConstant.EN_ENTRY_RESULT);
        saveCard(deviceId, inCardAttr, "entryCard", result, doorProcess, "entryOpenType");
    }


    /**
     * 出门刷卡
     */
    private void outCard(Map<String, ConDeviceAttribute> attrMap, String deviceId, DoorProcess doorProcess) {
        ConDeviceAttribute outCardAttr = attrMap.get(DoorConstant.EN_EXIT_CARD);
        if (outCardAttr == null) {
            return;
        }
        String result = getResult(attrMap, DoorConstant.EN_EXIT_RESULT);
        saveCard(deviceId, outCardAttr, "exitCard", result, doorProcess, "exitOpenType");
    }

    /**
     * 刷卡结果
     */
    @Nullable
    private String getResult(Map<String, ConDeviceAttribute> attrMap, String resultEnName) {
        String result = null;
        if (StringUtils.isNotEmpty(resultEnName)) {
            ConDeviceAttribute attribute = attrMap.get(resultEnName);
            if (attribute != null && StringUtils.isNotEmpty(attribute.getInitValue())) {
                int resultValueInt = Integer.parseInt(attribute.getInitValue());
                // 刷卡结果
                int i = resultValueInt % 10;
                result = DoorConstant.CARD_RESULT.get(i);
            }
        }
        return result;
    }

    /**
     * 保存刷卡记录
     */
    private void saveCard(String deviceId, ConDeviceAttribute attr, String inOut, String result, DoorProcess doorProcess, String openTypeEnName) {
        ConSheBei device = conSheBeiService.getById(deviceId);
        if (device == null) {
            return;
        }
        ConDeviceAttribute doorOpenTypeAttr = deviceAttributeService.getByDeviceIdAndAttrEnName(deviceId, openTypeEnName);
        if (doorOpenTypeAttr == null) {
            return;
        }
        doorProcess.device = device;
        String cardNumber = attr.getInitValue();
        if (StringUtils.isEmpty(cardNumber)) {
            return;
        }

        DoorData doorData = new DoorData();
        doorData.setDoorId(device.getId());
        doorData.setDoorCode(device.getCode());
        doorData.setDoorName(device.getName());
        doorData.setDoorType(device.getType());
        doorData.setDoorLocation(device.getLocationId());
        DoorPersonSys cardPerson = doorPersonService.getByCardNumber(cardNumber);
        doorProcess.cardNumber = cardNumber;
        doorProcess.doorPersonSys = cardPerson;
        doorProcess.cardTime = attr.getUpdatedTime();
        if (cardPerson != null) {
            doorData.setPerName(cardPerson.getName());
            doorData.setPerDept(cardPerson.getDepartment());
            doorData.setPerTitle(cardPerson.getTitle());
        }
        doorData.setAccessType(inOut);
        doorData.setCardResult(result);
        doorData.setOpenType(deviceDataService.getAttrValueByAttrId(doorOpenTypeAttr.getId()).getValue());
        doorData.setSwipeTime(attr.getUpdatedTime());
        doorData.setCardCode(cardNumber);
        doorDataService.save(doorData);
    }

    private void inOpen(Map<String, ConDeviceAttribute> attrMap, String deviceId, DoorProcess doorProcess) {
        ConDeviceAttribute attr = attrMap.get(DoorConstant.EN_ENTRY_RESULT);
        if (attr == null) {
            return;
        }
        saveOpen(deviceId, doorProcess, attr, "entryCard", "entryOpenType");
    }

    private void outOpen(Map<String, ConDeviceAttribute> attrMap, String deviceId, DoorProcess doorProcess) {
        ConDeviceAttribute attr = attrMap.get(DoorConstant.EN_EXIT_RESULT);
        if (attr == null) {
            return;
        }
        saveOpen(deviceId, doorProcess, attr, "exitCard", "exitOpenType");
    }

    private void saveOpen(String deviceId, DoorProcess doorProcess, ConDeviceAttribute attr, String inOut, String openTypeEnName) {
        String openResult = attr.getInitValue();
        if (StringUtils.isEmpty(openResult)) {
            return;
        }
        int resultValueInt = Integer.parseInt(openResult);
        // 开门结果
        int i = resultValueInt % 10;
        if (i != 1) {
            return;
        }
        ConSheBei device;
        if (doorProcess.device == null) {
            device = conSheBeiService.getById(deviceId);
        } else {
            device = doorProcess.device;
        }
        if (device == null) {
            return;
        }
        ConDeviceAttribute doorOpenTypeAttr = deviceAttributeService.getByDeviceIdAndAttrEnName(deviceId, openTypeEnName);
        if (doorOpenTypeAttr == null) {
            return;
        }
        DoorHistory doorHistory = new DoorHistory();
        doorHistory.setDoorId(device.getId());
        doorHistory.setDoorCode(device.getCode());
        doorHistory.setDoorName(device.getName());
        doorHistory.setDoorType(device.getType());
        doorHistory.setDoorLocation(device.getLocationId());
        doorHistory.setOpenTime(attr.getUpdatedTime());
        doorHistory.setAccessType(inOut);
        doorHistory.setRecordType(0);
        String openType = deviceDataService.getAttrValueByAttrId(doorOpenTypeAttr.getId()).getValue();
        int readOpenType = resultValueInt / 10;
        String opentypeStr = getOpentype(openType, readOpenType);
        doorHistory.setOpenType(opentypeStr);
        //readOpenType  的值为（1：刷卡，2：密码，3：指纹，4：人脸，5：按钮开门，6：平台开门，7：北向接口开门）
        // 密码开门 没有卡号，直接存储
        int cardCount = getCardCount(openType, readOpenType);
        if (cardCount == 0) {
            doorHistoryService.save(doorHistory);
            return;
        }
        List<DoorData> cardList = doorDataService.listCard3ByDoorId(inOut, device.getId(), cardCount);
        if (cardList.size() > 0) {
            DoorData doorData = cardList.get(0);
            setCard1Info(doorHistory, doorData);
        }
        if (cardList.size() > 1) {
            DoorData doorData = cardList.get(1);
            setCard2Info(doorHistory, doorData);
        }
        if (cardList.size() > 2) {
            DoorData doorData = cardList.get(2);
            setCard3Info(doorHistory, doorData);
        }
        doorHistoryService.save(doorHistory);
    }

    private String getOpentype(String openType, int readOpenType) {
        // 按钮，远程，远程
        if (readOpenType > 5) {
            return DoorConstant.OPEN_TYPE.get(readOpenType);
        }
        // 密码开门
        if (DoorConstant.PASSWORD.equals(openType) && readOpenType == 2) {
            return DoorConstant.OPEN_TYPE.get(readOpenType);
        }
        if (DoorConstant.CARD_OR_PASSWORD.equals(openType) && readOpenType == 2) {
            return DoorConstant.OPEN_TYPE.get(readOpenType);
        }
        return openType;
    }

    /**
     * 获取卡号需要查询卡号的数量
     *
     * @param openType
     * @param readOpenType 1：刷卡，2：密码，3：指纹，4：人脸，5：按钮开门，6：平台开门，7：北向接口开门
     * @return 获取卡号的数量
     */
    private int getCardCount(String openType, int readOpenType) {
        if (readOpenType > 5) {
            return 0;
        }
        // 按钮开门
        if (DoorConstant.BUTTON.equals(openType)) {
            return 0;
        }
        // 远程开门
        if (DoorConstant.REMOTE_OPEN.equals(openType)) {
            return 0;
        }
        // 密码开门
        if (DoorConstant.PASSWORD.equals(openType) && readOpenType == 2) {
            return 0;
        }
        // 指纹开门
        if (DoorConstant.FINGERPRINT.equals(openType) && readOpenType == 3) {
            return 1;
        }
        // 人脸开门
        if (DoorConstant.FACE.equals(openType) && readOpenType == 4) {
            return 1;
        }
        // 单卡开门
        if (DoorConstant.CARD_SINGLE_OPEN.equals(openType)) {
            return 1;
        }
        // 双开开门
        if (DoorConstant.CARD_DOUBLE_OPEN.equals(openType)) {
            return 2;
        }
        // 3卡开门
        if (DoorConstant.CARD_THREE_OPEN.equals(openType)) {
            return 3;
        }
        // 卡+密码
        if (DoorConstant.CARD_PASSWORD_OPEN.equals(openType)) {
            return 1;
        }
        // 卡或密码
        if (DoorConstant.CARD_OR_PASSWORD.equals(openType)) {
            if (readOpenType == 1) {
                return 1;
            } else {
                return 0;
            }
        }
        // 卡+指纹开门
        if (DoorConstant.CARD_FINGERPRINT_OPEN.equals(openType)) {
            return 2;
        }
        // 卡+指纹+密码开门
        if (DoorConstant.CARD_FINGERPRINT_PASSWORD_OPEN.equals(openType)) {
            return 2;
        }
        return 0;
    }

    /**
     * 设置卡1信息
     */
    private void setCard1Info(DoorHistory doorHistory, DoorData doorData) {
        doorHistory.setCardCode1(doorData.getCardCode());
        doorHistory.setSwipeTime1(doorData.getSwipeTime());
        if (StringUtils.isNotEmpty(doorData.getCardCode())) {
            DoorPersonSys cardPerson = doorPersonService.getByCardNumber(doorData.getCardCode());
            if (cardPerson != null) {
                doorHistory.setPerName1(cardPerson.getName());
                doorHistory.setPerDept1(cardPerson.getDepartment());
                doorHistory.setPerTitle1(cardPerson.getTitle());
            }
        }
    }

    /**
     * 设置卡2信息
     */
    private void setCard2Info(DoorHistory doorHistory, DoorData doorData) {
        doorHistory.setCardCode2(doorData.getCardCode());
        doorHistory.setSwipeTime2(doorData.getSwipeTime());
        if (StringUtils.isNotEmpty(doorData.getCardCode())) {
            DoorPersonSys cardPerson = doorPersonService.getByCardNumber(doorData.getCardCode());
            if (cardPerson != null) {
                doorHistory.setPerName2(cardPerson.getName());
                doorHistory.setPerDept2(cardPerson.getDepartment());
                doorHistory.setPerTitle2(cardPerson.getTitle());
            }
        }
    }

    /**
     * 设置卡3信息
     */
    private void setCard3Info(DoorHistory doorHistory, DoorData doorData) {
        doorHistory.setCardCode3(doorData.getCardCode());
        doorHistory.setSwipeTime3(doorData.getSwipeTime());
        if (StringUtils.isNotEmpty(doorData.getCardCode())) {
            DoorPersonSys cardPerson = doorPersonService.getByCardNumber(doorData.getCardCode());
            if (cardPerson != null) {
                doorHistory.setPerName3(cardPerson.getName());
                doorHistory.setPerDept3(cardPerson.getDepartment());
                doorHistory.setPerTitle3(cardPerson.getTitle());
            }
        }
    }

    static class DoorProcess {

        /**
         * 卡号
         */
        String cardNumber;


        /**
         * 人员
         */
        DoorPersonSys doorPersonSys;

        /**
         * 设备信息
         */
        ConSheBei device;

        /**
         * 刷卡时间
         */
        Date cardTime;
    }
}
