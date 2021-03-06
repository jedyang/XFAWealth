/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common;
/**
 * 通用模块常量参数配置类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public class CommonConstants {
	
	public static final int TOKEN_TIME_OUT = 30;//token有效期,单位分钟
	public static final String RND_SECRET_KEY = "o1f4Jpmgi6wyt4U7rXSm0AVioNWhMs7Q";//数据安全传输时用的混淆码
	public static final String SECRET_KEY = "a!b#1$2%3^h&7*8*&ffgg&hh";//网站des加解密算法用的安全码,必须24位
	public static final String DB_DATA_SECRET_KEY = "xfawealth@0990@htlaewafa";//数据库中数据加解密的密钥,必须24位
	
	public static final String SYS_DATE = "NOW()";//当前系统使用的数据库时间
	public static final String FORMAT_DATE = "yyyy-MM-dd";//时间格式
	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";//时间格式
	public static final String FORMAT_TIME = "HH:mm:ss";//时间格式
	public static final String DEF_CURRENCY = "HKD";//系统的默认货币
	
	public static final String CONFIG_PATH = "config/properties/config.properties";
	public static final String MONGO_CONFIG_PATH = "config/spring/jdbc.properties";
	
	//语言文件
	public static final String DEF_LANG_CODE = "sc";//默认的语言编码
	public static final String LANG_CODE_SC = "sc";
	public static final String LANG_CODE_TC = "tc";
	public static final String LANG_CODE_EN = "en";
	
	public static final String LANG_PATH_SC = "lang/lang_sc.properties";
	public static final String LANG_PATH_TC = "lang/lang_tc.properties";
	public static final String LANG_PATH_EN = "lang/lang_en.properties";
	
	public static final String JSESSION_COOKIE = "JSESSIONID";//cookie中的JSESSIONID名称
	public static final String JSESSION_URL = "jsessionid";//url中的jsessionid名称
    public static final String UTF8 = "utf-8";
    public static final String MOBILE_PATTERN = "^(13|14|15|17|18|)\\d{9}$";
    public static final String NUMBER_PATTERN = "^[0-9]*$";
    private static final String SPECIAL_CHARS = "\\(\\)<>@,;:\\\\\\\"\\.\\[\\]+";
    private static final String VALID_CHARS = "[^\\s" + SPECIAL_CHARS + "]+";
    public static final String EMAIL_PATTERN = "(" + VALID_CHARS + "(\\." + VALID_CHARS + ")*@" + VALID_CHARS + "(\\." + VALID_CHARS + ")*)";
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    
	public static final String DEF_DISPLAY_COLOR="1";//默认绿涨红跌

    /**
     * 构造方法
     */
    private CommonConstants() {
    	
    }
    
}
