/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.exception;

/**
 * 用户名没有找到异常
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
@SuppressWarnings("serial")
public class UsernameNotFoundException extends AuthenticationException {
	public UsernameNotFoundException() {
	}

	public UsernameNotFoundException(String msg) {
		super(msg);
	}
}