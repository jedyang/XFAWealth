/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.oms;

/**
 * 与itrader相关的事务常量
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public class OmsConstants {
	
	public static final String PLAT_TYPE = "WEBPLATFORM";//平台类型
	public static final int TRADE_TIME_OUT = 14;//交易系统会话过期时间,单位分钟
	public static final int TRADE_SLEEP_SEC = 2000;//延迟2秒数
	
	//service实例名称
	public static final String ITS_ERROR = "iTraderReceiveError";//统一出错
	public static final String ITS_SESSION = "iTraderReceiveSession";//登录/Pin码/会话校验
	public static final String ITS_LOGOUT = "iTraderReceiveLogout";//登出
	public static final String ITS_POSITION = "iTraderReceivePosition";//持仓
	public static final String ITS_DOD_DATA = "iTraderReceiveBodData";//持仓
	public static final String ITS_ACCOUNT = "iTraderReceiveAccount";//查询帐户资产
	public static final String ITS_ORDER_HIS = "iTraderReceiveOrderHistory";//订单历史
	public static final String ITS_ORDER = "iTraderReceiveOrder";//订单管理
	public static final String ITS_CURRENCY_ACCOUNT = "iTraderReceiveCurrencyAccount";//查询多币种帐户资产
	
	
	//接口日志模块类型
	public static final String ITS_LOG = "its_log";//its日志模块
	public static final String AIMA_LOG = "aima_log";//aima日志模块
	public static final String AC_LOG = "ac_log";//开户日志模块
	
}
