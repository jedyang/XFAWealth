/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web.springmvc;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * 日期编辑器
 * 
 * 根据日期字符串长度判断是长日期还是短日期。只支持yyyy-MM-dd，yyyy-MM-dd HH:mm:ss两种格式。
 * 扩展支持yyyy,yyyy-MM日期格式
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0
 * @date 2016年2月15日
 */
public class DateTypeEditor extends PropertyEditorSupport {
	public static final DateFormat DF_LONG = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat DF_SHORT = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat DF_YEAR = new SimpleDateFormat("yyyy");
	public static final DateFormat DF_MONTH = new SimpleDateFormat("yyyy-MM");
	/**
	 * 短类型日期长度
	 */
	public static final int SHORT_DATE = 10;
	
	public static final int YEAR_DATE = 4;
	
	public static final int MONTH_DATE = 7;

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String textTmp = text;
		textTmp = textTmp.trim();
		if (!StringUtils.hasText(textTmp)) {
			setValue(null);
			return;
		}
		try {
			if (textTmp.length() <= YEAR_DATE) {
				setValue(new java.sql.Date(DF_YEAR.parse(textTmp).getTime()));
			}else  if (textTmp.length() <= MONTH_DATE) {
				setValue(new java.sql.Date(DF_MONTH.parse(textTmp).getTime()));
			}else if (textTmp.length() <= SHORT_DATE) {
				setValue(new java.sql.Date(DF_SHORT.parse(textTmp).getTime()));
			} else {
				setValue(new java.sql.Timestamp(DF_LONG.parse(textTmp).getTime()));
			}
		} catch (ParseException ex) {
			IllegalArgumentException iae = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
			iae.initCause(ex);
			throw iae;
		}
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? DF_LONG.format(value) : "");
	}
}
