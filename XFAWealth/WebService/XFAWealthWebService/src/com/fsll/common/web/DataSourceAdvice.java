/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

/**
 * 
 * 数据源切换的AOP拦截器
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0 Created On: 2016-11-23
 */
public class DataSourceAdvice implements MethodBeforeAdvice,AfterReturningAdvice, ThrowsAdvice {
	
	//service方法执行之前被调用
	public void before(Method method, Object[] args, Object target)throws Throwable {
		String mName = method.getName();
		String[] strArray={"save","add","create","insert","update",
				"edit","merge","del","remove","put",
				"use","clear","copy","migrate"};
		boolean isR = false;
		for(int i=0;i<strArray.length;i++)
		{
		    if(mName.startsWith(strArray[i])){
		    	isR = true;
		    	break;
		    }
		}
		if(isR){
			DataSourceHolder.setDataSource(DataSourceHolder.DB_TYPE_W);
		}else{
			DataSourceHolder.setDataSource(DataSourceHolder.DB_TYPE_R);
		}
	}

	//service方法执行完之后切换回主库
	public void afterReturning(Object arg0, Method method, Object[] args,Object target) throws Throwable {
		DataSourceHolder.setDataSource(DataSourceHolder.DB_TYPE_W);
	}
	
	//抛出Exception之后切换回从库
	public void afterThrowing(Method method, Object[] args, Object target,Exception ex) throws Throwable {
		DataSourceHolder.setDataSource(DataSourceHolder.DB_TYPE_R);
	}
}
