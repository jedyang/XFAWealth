/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.exception;
/**
 * 通用异常
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public class commonException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public commonException() {
		super();
	}

	public commonException(String message) {
		super(message);
	}

	public commonException(String message, Throwable cause) {
		super(message, cause);
	}

	public commonException(Throwable cause) {
		super(cause);
	}
}
