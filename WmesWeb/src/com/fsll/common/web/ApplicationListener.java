/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fsll.common.container.Application;
import com.fsll.common.container.SpringContainer;

/**
 * 框架容器类
 * @author 黄模建
 * @version 1.0.0
 * Created On: 2016-7-4
 */
public final class ApplicationListener implements ServletContextListener {
	
	//private Log log = LogFactory.getLog(this.getClass());

	public void contextDestroyed(ServletContextEvent event) {
		
	} 

	public void contextInitialized(ServletContextEvent event) {
        SpringContainer container = new SpringContainer(event.getServletContext());//注入容器实例
        Application.getInstance().setContainer(container);//注入容器实例
	}
}
