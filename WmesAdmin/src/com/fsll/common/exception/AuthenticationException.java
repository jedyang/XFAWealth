/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.exception;

import org.apache.shiro.authc.AccountException;

/**
 * 登录异常
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
@SuppressWarnings("serial")
public class AuthenticationException  extends AccountException {
	
	private Object extraInformation;

	public AuthenticationException() {

	}

	public AuthenticationException(String msg) {
		super(msg);
	}

	public AuthenticationException(String msg, Object extraInformation) {
		super(msg);
		this.extraInformation = extraInformation;
	}

	public Object getExtraInformation() {
		return extraInformation;
	}

	public void clearExtraInformation() {
		this.extraInformation = null;
	}
	
}
