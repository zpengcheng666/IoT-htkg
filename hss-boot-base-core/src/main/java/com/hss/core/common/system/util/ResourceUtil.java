package com.hss.core.common.system.util;

import com.hss.core.common.system.annotation.EnumDict;
import com.hss.core.common.system.vo.DictModel;
import com.hss.core.common.util.OConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Descrition: 资源加载工具类
 * @Author: chushubin
 * @Date 2022/7/8 10:40
 **/
@Slf4j
public class ResourceUtil {

    /**
     * 枚举字典数据
     */
    private final static Map<String, List<DictModel>> enumDictData = new HashMap<>(5);

    /**
     * 所有java类
     */
    private final static String CLASS_PATTERN="/**/*.class";

    /**
     * 所有枚举java类
     */
    private final static String CLASS_ENMU_PATTERN="/**/*Enum.class";

    /**
     * 包路径 com.hss
     */
    private final static String BASE_PACKAGE = "com.hss";

    /**
     * 枚举类中获取字典数据的方法名
     */
    private final static String METHOD_NAME = "getDictList";

    /**
     * 获取枚举类对应的字典数据 SysDictServiceImpl#queryAllDictItems()
     * @return
     */
    public static Map<String, List<DictModel>> getEnumDictData(){
        if(enumDictData.keySet().size()>0){
            return enumDictData;
        }
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(BASE_PACKAGE) + CLASS_ENMU_PATTERN;
        try {
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                String classname = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(classname);
                EnumDict enumDict = clazz.getAnnotation(EnumDict.class);
                if (enumDict != null) {
                    EnumDict annotation = clazz.getAnnotation(EnumDict.class);
                    String key = annotation.value();
                    if(OConvertUtils.isNotEmpty(key)){
                        List<DictModel> list = (List<DictModel>) clazz.getDeclaredMethod(METHOD_NAME).invoke(null);
                        enumDictData.put(key, list);
                    }
                }
            }
        }catch (Exception e){
            log.error("获取枚举类字典数据异常", e.getMessage());
            // e.printStackTrace();
        }
        return enumDictData;
    }

    /**
     * 用于后端字典翻译 SysDictServiceImpl#queryManyDictByKeys(java.util.List, java.util.List)
     * @param dictCodeList
     * @param keys
     * @return
     */
    public static Map<String, List<DictModel>> queryManyDictByKeys(List<String> dictCodeList, List<String> keys){
        if(enumDictData.keySet().size()==0){
            getEnumDictData();
        }
        Map<String, List<DictModel>> map = new HashMap<>();
        for (String code : enumDictData.keySet()) {
            if(dictCodeList.indexOf(code)>=0){
                List<DictModel> dictItemList = enumDictData.get(code);
                for(DictModel dm: dictItemList){
                    String value = dm.getValue();
                    if(keys.indexOf(value)>=0){
                        List<DictModel> list = new ArrayList<>();
                        list.add(new DictModel(value, dm.getText()));
                        map.put(code,list);
                        break;
                    }
                }
            }
        }
        return map;
    }

}
