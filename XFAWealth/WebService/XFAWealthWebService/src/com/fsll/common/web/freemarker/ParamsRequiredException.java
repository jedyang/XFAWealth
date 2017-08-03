/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web.freemarker;

import freemarker.template.TemplateModelException;

/**
 * 缺少必须参数异常
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0
 * @date   2016年2月15日
 */
@SuppressWarnings("serial")
public class ParamsRequiredException extends TemplateModelException {
	public ParamsRequiredException(String paramName) {
		super("The required \"" + paramName + "\" paramter is missing.");
	}
}
