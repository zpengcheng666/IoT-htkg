package com.hss.core.config;

import com.hss.core.common.api.CommonAPI;
import com.hss.core.common.system.vo.DictModel;
import com.hss.core.common.util.OConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecgframework.dict.service.AutoPoiDictServiceI;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：AutoPoi Excel注解支持字典参数设置
 *  举例： @Excel(name = "性别", width = 15, dicCode = "sex")
 * 1、导出的时候会根据字典配置，把值1,2翻译成：男、女;
 * 2、导入的时候，会把男、女翻译成1,2存进数据库;
 * 
 * @Author:scott 
 * @since：2019-04-09 
 * @Version:1.0
 */
@Slf4j
@Service
public class AutoPoiDictConfig implements AutoPoiDictServiceI {
	final static String EXCEL_SPLIT_TAG = "_";
	final static String TEMP_EXCEL_SPLIT_TAG = "---";

	@Lazy
	@Resource
	private CommonAPI commonApi;

	/**
	 * 通过字典查询easypoi，所需字典文本
	 * 
	 * @Author:scott 
	 * @since：2019-04-09
	 * @return
	 */
	@Override
	public String[] queryDict(String dicTable, String dicCode, String dicText) {
		List<String> dictReplaces = new ArrayList<String>();
		List<DictModel> dictList = null;
		// step.1 如果没有字典表则使用系统字典表
		if (OConvertUtils.isEmpty(dicTable)) {
			dictList = commonApi.queryDictItemsByCode(dicCode);
		} else {
			try {
				dicText = OConvertUtils.getString(dicText, dicCode);
				dictList = commonApi.queryTableDictItemsByCode(dicTable, dicText, dicCode);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}


		for (DictModel t : dictList) {
			if(t!=null){
				//update-begin---author:scott   Date:20211220  for：[issues/I4MBB3]@Excel dicText字段的值有下划线时，导入功能不能正确解析---
				if(t.getValue().contains(EXCEL_SPLIT_TAG)){
					String val = t.getValue().replace(EXCEL_SPLIT_TAG,TEMP_EXCEL_SPLIT_TAG);
					dictReplaces.add(t.getText() + EXCEL_SPLIT_TAG + val);
				}else{
					dictReplaces.add(t.getText() + EXCEL_SPLIT_TAG + t.getValue());
				}
				//update-end---author:20211220     Date:20211220  for：[issues/I4MBB3]@Excel dicText字段的值有下划线时，导入功能不能正确解析---
			}
		}
		if (dictReplaces != null && dictReplaces.size() != 0) {
			log.info("---AutoPoi--Get_DB_Dict------"+ dictReplaces.toString());
			return dictReplaces.toArray(new String[dictReplaces.size()]);
		}
		return null;
	}
}
