package com.hss.modules.util;

import com.hss.modules.scada.model.DeviceAttrData;
import com.hss.modules.scada.service.DeviceDataService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 条件表达式
 */
@Component
public class ExpressionUtil {
    @Autowired
    private DeviceDataService deviceDataService;

    private static final Pattern PATTERN = Pattern.compile("\\[.*?\\]");
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();


    /**
     * 根据表达式获取值
     * @param expression 表达式
     * @param tClass 返回值类型
     * @param <T> 返回值类型
     * @return value
     */
    @Nullable
    public  <T> T getValue(String expression, Class<T> tClass){
        Expression expression1 = PARSER.parseExpression(expression);
        return expression1.getValue(tClass);
    }


    /**
     * 获取属性值表达式
     * @param expression 属性表达式
     * @param cls 返回值类型
     * @param <T> 返回值类型
     * @return value
     */
    @Nullable
    public <T> T getValueByAttrValue(String expression, Class <T> cls){
        Expression spelExpression = getExpression(null,expression);
        if (spelExpression == null){
            return null;
        }
        return spelExpression.getValue(cls);
    }

    /**
     * 获取表达式的值
     * @param expression 表达式
     * @param valueMap 表达式valuemap
     * @param cls 返回值类型
     * @param <T> 返回值类型
     * @return value
     */
    @Nullable
    public <T> T getValueByValueMap(String expression, Map<String,String> valueMap, Class <T> cls){
        Expression spelExpression = getExpression(valueMap,expression);
        if (spelExpression == null){
            return null;
        }
        return spelExpression.getValue(cls);
    }



    /**
     * 获取表达式的值
     * @param expression  表达式
     * @param valueFun value函数
     * @param cls 返回值类型
     * @param <T> 返回值类型
     * @return value
     */
    @Nullable
    public <T> T getValueByValueFun(String expression, Function<String, String> valueFun, Class <T> cls){
        Expression spelExpression = getExpression(expression, valueFun);
        if (spelExpression == null){
            return null;
        }
        return spelExpression.getValue(cls);
    }





    /**
     * 获取表达式对象
     * @param expression 表达式
     * @param valueFun 获取value函数
     * @return 表达式对象
     */
    @Nullable
    private Expression getExpression(String expression,  Function<String, String> valueFun) {
        String expressionStr = getExpressionStr(expression, valueFun);
        if (expressionStr == null){
            return null;
        }
        return PARSER.parseExpression(expressionStr);
    }

    /**
     * 获取表达式对象
     * @param kvMap 值map 为空代表从attr缓存中获取
     * @param expression 表达式
     * @return 表达式对象
     */
    @Nullable
    private Expression getExpression(@Nullable Map<String,String> kvMap, String expression) {
        String expressionStr = getExpressionStr(kvMap,expression);
        if (expressionStr == null){
            return null;
        }
        return PARSER.parseExpression(expressionStr);
    }







    /**
     * 获取表达式字符串
     * @param kvMap 值得映射关系
     * @param expression 达式字符串动态属性用[]包裹
     * @return 表达式字符串
     */
    @Nullable
    public String getExpressionStr(@Nullable Map<String,String> kvMap, String expression) {
        return getExpressionStr(expression, value -> this.getValue(kvMap, value));
    }



    /**
     * 获取变量的值
     * @param attrId 属性id
     * @return value
     */
    @Nullable
    private String getValue(String attrId){
        DeviceAttrData attrValueByAttrId = deviceDataService.getAttrValueByAttrId(attrId);
        if (attrValueByAttrId == null){
            return null;
        }
        return attrValueByAttrId.getValue();
    }

    /**
     * 获取值
     * @param kvMap 值映射，如果为null 为属性id和属性值
     * @param variableName valueName
     * @return value
     */
    @Nullable
    private String getValue(@Nullable Map<String,String> kvMap, String variableName){
        if (kvMap != null){
            return kvMap.get(variableName);
        }
        return getValue(variableName);
    }



    /**
     * 获取表达式字符串
     * @param expression 表达式字符串动态属性用[]包裹
     * @param valueFun 替换值的函数
     * @return 表达式字符串
     */
    @Nullable
    public String getExpressionStr(String expression, Function<String, String> valueFun) {
        if (expression.contains("[")){
            Matcher matcher = PATTERN.matcher(expression);
            StringBuffer spelBuffer = new StringBuffer();
            while (matcher.find()) {
                String substring = expression.substring(matcher.start(), matcher.end());
                if (StringUtils.isBlank(substring) || substring.length() <= 2){
                    return null;
                }
                String attrId = substring.substring(1, substring.length() - 1);
                String value = valueFun.apply(attrId);
                if (value == null){
                    return null;
                }
                // 将匹配到的字符串替换为当前字符串
                matcher.appendReplacement(spelBuffer, matcher.group().replace(substring, value));
            }
            // 将剩余字符串添加到buffer中
            matcher.appendTail(spelBuffer);
            return spelBuffer.toString();
        }else {
            return expression;
        }
    }

    /**
     * 解析原始字符串
     * @param expression
     * @return
     */
    @Nullable
    public String getExpressionOriginalStr(Map<String,String> kvMap, String expression) {
        if (expression.contains("[")){
            Matcher matcher = PATTERN.matcher(expression);
            StringBuffer spelBuffer = new StringBuffer();
            while (matcher.find()) {
                String substring = expression.substring(matcher.start(), matcher.end());
                if (StringUtils.isBlank(substring) || substring.length() <= 2){
                    return null;
                }
                String enName = substring.substring(1, substring.length() - 1);
                String value = getValue(kvMap, enName);
                if (value == null){
                    return null;
                }
                // 将匹配到的字符串替换为当前字符串
                matcher.appendReplacement(spelBuffer, matcher.group().replace(substring, "[" + value + "]"));
            }
            // 将剩余字符串添加到buffer中
            matcher.appendTail(spelBuffer);
            return spelBuffer.toString();
        }else {
            return expression;
        }
    }


    /**
     * 获取值的Id列表
     * @param expression 表达式value用[]标记
     * @return valueId列表
     */
    @NotNull
    public Set<String> listValueId(String expression) {
        Set<String> ids = new HashSet<>();
        if (expression.contains("[")){
            Matcher matcher = PATTERN.matcher(expression);
            while (matcher.find()){
                String substring = expression.substring(matcher.start(), matcher.end());
                if (StringUtils.isBlank(substring) || substring.length() <= 2){
                    break;
                }
                String id = substring.substring(1, substring.length() - 1);
                ids.add(id);
            }
        }

        return ids;
    }


}
