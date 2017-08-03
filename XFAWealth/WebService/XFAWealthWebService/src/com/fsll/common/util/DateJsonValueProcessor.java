/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * Json对接日期格式化
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-10-21
 */
public class DateJsonValueProcessor implements JsonValueProcessor {
	
	public static final String Default_DATE_PATTERN = "yyyy-MM-dd";
	private DateFormat dateFormat;

	public DateJsonValueProcessor(String datePattern) {
		try {
			dateFormat = new SimpleDateFormat(datePattern);
		} catch (Exception e) {
			dateFormat = new SimpleDateFormat(Default_DATE_PATTERN);
		}
	}

    public Object processArrayValue(Object value, JsonConfig jsonConfig) {   
        return process(value);   
    }   
  
    public Object processObjectValue(String key, Object value,   
            JsonConfig jsonConfig) {   
        return process(value);   
    }   
  
    private Object process(Object value) {  
        try {   
            return dateFormat.format((Date) value);   
        } catch (Exception e) {   
            return "";   
        }   
    }
}
