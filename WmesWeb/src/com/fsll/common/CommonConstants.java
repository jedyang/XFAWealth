/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common;

import org.springframework.context.support.StaticApplicationContext;

/**
 * 通用模块常量参数配置类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public class CommonConstants {
	public static final String MAIN_SITE_ID = "1";//主站点标识 sys_site主键
	public static final String RND_SECRET_KEY = "o1f4Jpmgi6wyt4U7rXSm0AVioNWhMs7Q";//数据安全传输时用的混淆码
	public static final String SECRET_KEY = "a!b#1$2%3^h&7*8*&ffgg&hh";//网站des加解密算法用的安全码,必须24位
	public static final String DB_DATA_SECRET_KEY = "xfawealth@0990@htlaewafa";//数据库中数据加解密的密钥,必须24位
	
	public static final String SYS_DATE = "NOW()";//当前系统使用的数据库时间
	public static final String FORMAT_DATE = "yyyy-MM-dd";//时间格式
	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";//时间格式
	public static final String FORMAT_TIME = "HH:mm:ss";//时间格式
	
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
    
	public static final String DEF_CURRENCY = "HKD";//系统的默认货币
	public static final String DEF_CURRENCY_HKD = "HKD";//默认货币,港币
	public static final String DEF_DISPLAY_COLOR="1";//默认绿涨红跌
	public static final String DISPLAY_COLOR_GR="1";// 绿涨红跌
	public static final String DISPLAY_COLOR_RG="2";// 红涨绿跌
	
	public static final String DATE_TYPE_HOUR = "H"; //小时
	public static final String DATE_TYPE_DAY = "D"; // 天
	public static final String DATE_TYPE_MONTH = "M"; // 月
	public static final String DATE_TYPE_YEAR = "Y"; // 年
	
	public static final String TRAN_TYPE_SELL = "S"; // 卖
	public static final String TRAN_TYPE_BUY = "B"; // 买
	
	public static final String ORDER_TYPE_BUY = "Buy"; // 买
	public static final String ORDER_TYPE_SELL = "Sell"; // 卖
	public static final String ORDER_TYPE_REVOKE = "Revoke"; // 撤单
	
	public static final String AIP_EXEC_CYCLE_W = "w"; // 定投周期,周
	public static final String AIP_EXEC_CYCLE_B  = "b"; // 定投周期，双周
	public static final String AIP_EXEC_CYCLE_M = "m"; // 定投周期，月
	
	public static final Long ONE_DAY  = (long) (24*60*60*1000); // 一天
	public static final Long ONE_HOUR = (long) (60*60*1000); // 一小时
	
	public static final String NUMBER_STING_FORMAT = "#,##0.00"; // 数字格式
	

	public static final String RETURN_PERIOD_CODE_1W = "return_period_code_1W"; //回报周期编码
	public static final String RETURN_PERIOD_CODE_1M = "return_period_code_1M"; //回报周期编码
	public static final String RETURN_PERIOD_CODE_3M = "return_period_code_3M"; //回报周期编码
	public static final String RETURN_PERIOD_CODE_6M = "return_period_code_6M"; //回报周期编码
	public static final String RETURN_PERIOD_CODE_1Y = "return_period_code_1Y"; //回报周期编码
	public static final String RETURN_PERIOD_CODE_3Y = "return_period_code_3Y"; //回报周期编码
	public static final String RETURN_PERIOD_CODE_5Y = "return_period_code_5Y"; //回报周期编码
	public static final String RETURN_PERIOD_CODE_LAUNCH = "return_period_code_LAUNCH"; //回报周期编码
	public static final String RETURN_PERIOD_CODE_YTD = "return_period_code_YTD"; //回报周期编码
	

	public static final String MEMBER_NAME_NICKNAME = "1";//昵称（显示用户名称）
	public static final String MEMBER_NAME_REAL_NAME = "2";//真名（显示用户名称）
	
	public static final String SYS_PARAM_TYPE_CURRENCY = "currency_type";//基础数据——货币类型编码
	
	public static final String DEVICE_TYPE_WEB = "web";
	public static final String DEVICE_TYPE_ANDROID = "android";
	public static final String DEVICE_TYPE_IOS = "ios";

    /**
     * 构造方法
     */
    private CommonConstants() {
    	
    }
    
}
