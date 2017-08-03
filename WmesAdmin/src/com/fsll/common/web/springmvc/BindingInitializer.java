/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web.springmvc;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * 数据绑定初始化类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0
 * @date 2016年2月15日
 */
public class BindingInitializer implements WebBindingInitializer {
	/**
	 * 初始化数据绑定
	 */
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(Date.class, new DateTypeEditor());
	}
}
