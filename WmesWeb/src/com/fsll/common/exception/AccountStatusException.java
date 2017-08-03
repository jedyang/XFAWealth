/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.exception;

/**
 * 账号状态异常
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
@SuppressWarnings("serial")
public class AccountStatusException extends AuthenticationException {
	public AccountStatusException() {
	}

	public AccountStatusException(String msg) {
		super(msg);
	}

	public AccountStatusException(String msg, Object extraInformation) {
		super(msg, extraInformation);
	}
}
