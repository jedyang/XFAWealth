/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web.freemarker;

import freemarker.template.TemplateModelException;

/**
 * 非数字参数异常
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0
 * @date   2016年2月15日
 */
@SuppressWarnings("serial")
public class MustSplitNumberException extends TemplateModelException {
	public MustSplitNumberException(String paramName) {
		super("The \"" + paramName
				+ "\" parameter must be a number split by ','");
	}

	public MustSplitNumberException(String paramName, Exception cause) {
		super("The \"" + paramName
				+ "\" parameter must be a number split by ','", cause);
	}
}
