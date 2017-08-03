package com.fsll.common.util;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StringType;
/***
 * 重写了mysql5Dialect的类
 * @author simon
 * @date 2016-4-26
 */
public class MyISAMDialect extends MySQL5Dialect {
	 public MyISAMDialect(){  
	        super();  
	        registerFunction("convert_mine", new SQLFunctionTemplate(StringType.INSTANCE, "convert(?1 using ?2)"));  
	 } 
}
