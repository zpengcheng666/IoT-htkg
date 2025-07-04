package com.hss.core.common.aspect.annotation;

import com.hss.core.common.constant.enums.LowAppAopEnum;

import java.lang.annotation.*;

/**
 * 自动注入low_app_id
 * 
 * @Author scott
 * @email jeecgos@163.com
 * @Date 2022年01月05日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLowApp {

	/**
	 * 切面类型（add、delete、db_import等其他操作）
	 *
	 * @return
	 */
	LowAppAopEnum action();

	/**
	 * 业务类型（cgform等）
	 *
	 * @return
	 */
	String bizType();

}
