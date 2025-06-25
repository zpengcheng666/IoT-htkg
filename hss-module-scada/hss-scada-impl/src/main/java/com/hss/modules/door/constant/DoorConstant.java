package com.hss.modules.door.constant;


import java.util.HashMap;
import java.util.Map;

/**
 * 门常量列
 * @author hd
 */
public class DoorConstant {

    /**
     * 远程开门属性
     */
    public static final String EN_REMOTE_OPEN_CTRL = "remoteOpenCtrl";
    /**
     * 进门卡号
     */
    public static final String EN_ENTRY_CARD = "entryCard";
    /**
     * 出门卡号
     */
    public static final String EN_EXIT_CARD = "exitCard";
    /**
     * 进门结果
     */
    public static final String EN_ENTRY_RESULT = "entryResult";
    /**
     * 出门结果
     */
    public static final String EN_EXIT_RESULT = "exitResult";

    /**
     * 刷卡结果
     */
    public static final Map<Integer, String> CARD_RESULT = new HashMap<>();
    static {
        /*
         * 其他原因刷卡失败
         */
        CARD_RESULT.put(0, "0");

        /*
         * 刷卡成功
         */
        CARD_RESULT.put(1, "1");

        /*
         * 非法卡
         */
        CARD_RESULT.put(2, "2");

        /*
         * 卡没到齐
         */
        CARD_RESULT.put(3, "3");

        /*
         * 等待其他设备输入
         */
        CARD_RESULT.put(4, "4");
    }

    public static final Map<String, String> CARD_RESULT_STR = new HashMap<>();
    static {
        /*
         * 其他原因刷卡失败
         */
        CARD_RESULT_STR.put("0", "刷卡失败");

        /*
         * 刷卡成功
         */
        CARD_RESULT_STR.put("1", "开门成功");

        /*
         * 非法卡
         */
        CARD_RESULT_STR.put("2", "非法卡");

        /*
         * 卡没到齐
         */
        CARD_RESULT_STR.put("3", "卡没到齐");

        /*
         * 等待其他设备输入
         */
        CARD_RESULT_STR.put("4", "等待其他设备输入");
    }

    /**
     * 开门类型
     */
    public static final Map<Integer, String> OPEN_TYPE = new HashMap<>();
    static {
        // 刷卡开门
        OPEN_TYPE.put(1, "CardSingleOpen");
        // 密码开门
        OPEN_TYPE.put(2, "Password");
        // 指纹开门
        OPEN_TYPE.put(3, "Fingerprint");
        // 人脸开门
        OPEN_TYPE.put(4, "Face");
        // 按钮开门
        OPEN_TYPE.put(5, "Button");
        // 远程开门
        OPEN_TYPE.put(6, "RemoteOpen");
        OPEN_TYPE.put(7, "RemoteOpen");
    }



    /**
     * 单卡开门
     */
    public static final String CARD_SINGLE_OPEN = "CardSingleOpen";
    /**
     * 双卡开门
     */
    public static final String CARD_DOUBLE_OPEN = "CardDoubleOpen";
    /**
     * 三卡开门
     */
    public static final String CARD_THREE_OPEN = "CardThreeOpen";
    /**
     * 卡+密码开门
     */
    public static final String CARD_PASSWORD_OPEN = "CardPasswordOpen";
    /**
     * 卡或密码开门
     */
    public static final String CARD_OR_PASSWORD = "CardOrPassword";
    /**
     * 卡+指纹开门
     */
    public static final String CARD_FINGERPRINT_OPEN = "CardFingerprintOpen";
    /**
     * 卡+指纹+密码开门
     */
    public static final String CARD_FINGERPRINT_PASSWORD_OPEN = "CardFingerprintPasswordOpen";
    /**
     * 密码开门
     */
    public static final String PASSWORD = "Password";
    /**
     * 指纹开门
     */
    public static final String FINGERPRINT = "Fingerprint";
    /**
     * 人脸开门
     */
    public static final String FACE = "Face";
    /**
     * 按钮开门
     */
    public static final String BUTTON = "Button";
    /**
     * 按钮开门
     */
    public static final String REMOTE_OPEN = "RemoteOpen";


    /**
     * 开门方式对应关系
     */
    public static final Map<String, String> OPEN_TYPE_STR = new HashMap<>();
    static {
        OPEN_TYPE_STR.put(CARD_SINGLE_OPEN, "单卡开门");
        OPEN_TYPE_STR.put(CARD_DOUBLE_OPEN, "双卡开门");
        OPEN_TYPE_STR.put(CARD_THREE_OPEN, "三卡开门");
        OPEN_TYPE_STR.put(CARD_PASSWORD_OPEN, "卡+密码开门");
        OPEN_TYPE_STR.put(CARD_OR_PASSWORD, "卡或密码开门");
        OPEN_TYPE_STR.put(CARD_FINGERPRINT_OPEN, "卡+指纹开门");
        OPEN_TYPE_STR.put(CARD_FINGERPRINT_PASSWORD_OPEN, "卡+指纹+密码开门");
        OPEN_TYPE_STR.put(PASSWORD, "密码开门");
        OPEN_TYPE_STR.put(FINGERPRINT, "指纹开门");
        OPEN_TYPE_STR.put(FACE, "人脸开门");
        OPEN_TYPE_STR.put(BUTTON, "按钮开门");
        OPEN_TYPE_STR.put(REMOTE_OPEN, "远程开门");
    }

}
