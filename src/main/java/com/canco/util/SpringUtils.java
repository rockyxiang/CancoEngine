package com.canco.util;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class SpringUtils {
	
	/**
	 * 获取spring容器中的用户对象
	 * 
	 * @param beanId
	 * @return
	 */
	public static Object getBean(String beanId) {
		WebApplicationContext act = ContextLoader.getCurrentWebApplicationContext();
		return act.getBean(beanId);
	}
}
