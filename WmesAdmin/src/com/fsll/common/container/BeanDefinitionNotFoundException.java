/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.container;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * 
 * 按照名称或者类型查找bean，按照类型查找一个bean时发现有多个bean，这个时候抛出BeanDefinitionNotFoundException。
 * BeanDefinitionNotFoundException是Container的一部分，按照Container的抽象思想，BeanDefinitionNotFoundException
 * 不能依赖于任何容器，因此不能直接使用Spring的NoSuchBeanDefinitionException。继承NoSuchBeanDefinitionException，间接依赖
 * Spring，是一个图省事的方案。如果不使用SpringContainer，则需要重写编写BeanDefinitionNotFoundException。
 * 
 * @author 黄模建
 * @version 1.0.0
 * Created On: 2016-7-4
 */
public class BeanDefinitionNotFoundException extends NoSuchBeanDefinitionException {

	public BeanDefinitionNotFoundException(String name, String message) {
		super(name, message);
	}

	public BeanDefinitionNotFoundException(Class type, String message) {
		super(type, message);
	}

}
