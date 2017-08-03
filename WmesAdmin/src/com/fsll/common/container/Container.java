/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.container;

/**
 * 
 * @author 黄模建
 * @version 1.0.0
 * Created On: 2016-7-4
 */
public interface Container {
	/**
	 * 按照名称从容器中获取组件对象。
	 * 
	 * @param name of bean
	 * @return bean
	 * @throws BeanDefinitionNotFoundException
	 */
	public Object getBean(String name) throws BeanDefinitionNotFoundException;

	/**
	 * 按照类型从容器中获取组件对象。在容器中只能有一个该类型的组件，否则抛出BeanDefinitionNotFoundException。
	 * @param type of bean
	 * @return bean
	 * @throws BeanDefinitionNotFoundException
	 */
	public Object getBean(Class type) throws BeanDefinitionNotFoundException;

	/**
	 * 重新装载容器定义。
	 */
	public void refresh();

	/**
	 * 关闭容器
	 */
	public void close();

	/**
	 * 按照名称自动装配组件对象的协作对象。
	 * @param bean  被装配的组件对象
	 */
	public void autowireBeanPropertiesByName(Object bean);

}
