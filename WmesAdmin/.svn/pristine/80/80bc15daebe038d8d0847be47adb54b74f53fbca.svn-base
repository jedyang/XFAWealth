/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.util;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;

/**
 * 连接属性加解密信息
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0 Created On: 2016-12-6
 */
public class ProEncryptBean implements FactoryBean{
	
	private Properties properties;

	public Object getObject() throws Exception {
		return getProperties();
	}

	public Class getObjectType() {
		return java.util.Properties.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties inProperties) {
		this.properties = inProperties;
		String originalUsername = properties.getProperty("user");
		String originalPassword = properties.getProperty("password");
		if (originalUsername != null) {
			String newUsername = deEncryptUsername(originalUsername);
			properties.put("user", newUsername);
		}
		if (originalPassword != null) {
			String newPassword = deEncryptPassword(originalPassword);
			properties.put("password", newPassword);
		}
	}

	private String deEncryptUsername(String originalUsername) {
		return deEncryptString(originalUsername);
	}

	private String deEncryptPassword(String originalPassword) {
		return deEncryptString(originalPassword);
	}

	private String deEncryptString(String originalString) {
		String secretKey = "xfawealth@0990@htlaewafa";//必须24位
		String oriStr = DESUtils.decrypt(originalString, secretKey);
		return oriStr;// 把String 类型的密文解密
	}
	
}
