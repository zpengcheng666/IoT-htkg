package org.jeecg.common.util.minio;

/**
 * OptionalUtil 提供用于处理可选值的实用程序方法。
 */
public class OptionalUtil {

    /**
     *如果该值不为 null，则返回该值，否则返回默认值。@param value 要检查的值
     * @param defaultValue 要返回的默认值（如果值为 null）
     * @param <T> 值的类型@return值或默认值（如果值为 null）
     */
    public static <T> T getValueOrDef(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     将空白字符串或 null 字符串转换为指定的默认值。
     @param value 要检查的字符串
     @param defaultValue 返回默认值，如果字符串为空，则返回 null，如果原始字符串不为空，则为
     @return null，否则为默认值
     */
    public static String convertBlankToDef(String value, String defaultValue) {
        return (value == null || value.trim().isEmpty()) ? defaultValue : value;
    }
}
