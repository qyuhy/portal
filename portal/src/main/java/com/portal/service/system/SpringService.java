package com.portal.service.system;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
@Component
public class SpringService implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext ac)
			throws BeansException {
		this.applicationContext = ac;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBeanByName(String beanName) {
		return (T) applicationContext.getBean(beanName);
	}

	public static <T> T getBeanByType(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	/**
	 * 得到spring上下文对象
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
