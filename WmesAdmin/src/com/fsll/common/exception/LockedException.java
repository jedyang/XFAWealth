/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.exception;

/**
 * 账号被锁定异常
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
@SuppressWarnings("serial")
public class LockedException extends AccountStatusException {
	public LockedException() {
	}

	public LockedException(String msg) {
		super(msg);
	}

	public LockedException(String msg, Object extraInformation) {
		super(msg, extraInformation);
	}
}
