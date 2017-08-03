/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.exception;

/**
 * 账号过期异常。如：改账号只缴纳了一年的费用，一年后没有续费。
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
@SuppressWarnings("serial")
public class AccountExpiredException extends AccountStatusException {
	public AccountExpiredException() {
	}

	public AccountExpiredException(String msg) {
		super(msg);
	}

	public AccountExpiredException(String msg, Object extraInformation) {
		super(msg, extraInformation);
	}
}
