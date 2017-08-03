/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.container;

import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Spring的Container
 * 
 * @author 黄模建
 * @version 1.0.0
 * Created On: 2016-7-4
 */
public class SpringContainer implements Container{
	
	private ApplicationContext applicationContext;

	/**
	 * SpringContainer的构造方法。
	 * 
	 * @param servletContext
	 */
	public SpringContainer(ServletContext servletContext) {
		this.applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		//registCustomEditors();
	}

	/**
	 * SpringContainer的构造方法。通常用于测试用例。
	 */
	public SpringContainer(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		//registCustomEditors();
	}

	private void registCustomEditors() {
		try {
			Map customEditors = (Map) this.getBean("customEditorsForSpringContainer");
			CustomEditorConfigurer cec = new CustomEditorConfigurer();
			cec.setCustomEditors(customEditors);
			cec.postProcessBeanFactory(((AbstractApplicationContext) applicationContext).getBeanFactory());
		} catch (BeanDefinitionNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Object getBean(String name) throws BeanDefinitionNotFoundException {
		if (applicationContext == null)
			throw new IllegalStateException("Spring Application context has not been set");
		try {
			return applicationContext.getBean(name);
		} catch (NoSuchBeanDefinitionException e) {
			throw new BeanDefinitionNotFoundException(e.getBeanName(), e.getMessage());
		}
	}

	public Object getBean(Class type) throws BeanDefinitionNotFoundException {
		Map beans = applicationContext.getBeansOfType(type);
		if (beans == null || beans.size() == 0) {
			throw new BeanDefinitionNotFoundException(type,"The container is unable to resolve single instance of " + type.getName() + ", none instances found");
		}
		if (beans.size() > 1) {
			throw new BeanDefinitionNotFoundException(type,"The container is unable to resolve single instance of " + type.getName() + ", number of instances found was: " + beans.size());
		}
		return beans.entrySet().iterator().next();
	}

	public void refresh() {
		((AbstractApplicationContext) applicationContext).refresh();
	}

	public void close() {
		((AbstractApplicationContext) applicationContext).close();
	}

	public void autowireBeanPropertiesByName(Object bean) {
		((AbstractApplicationContext) applicationContext).getBeanFactory().autowireBeanProperties(bean,AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
	}
	
}
