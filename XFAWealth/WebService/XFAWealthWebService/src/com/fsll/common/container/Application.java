/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.container;


/**
 * 通用模块常量参数配置类
 * @author 黄模建
 * @version 1.0.0
 * Created On: 2016-7-4
 */
public class Application{
	
	private static final Application instance = new Application();
	
	private Container container; 
	
	private Application() {
	}
	
	/**
	 * 获取Application实例。
	 * @return Application实例
	 */
	public static Application getInstance() {
		return instance;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}
}
