/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core;
/**
 * 核对模块常量参数配置类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public class CoreConstants {
	public static final String FRONT_USER_LOGIN = "_FRONT_USER_LOGIN_";//前台保存登录用户信息
	public static final String FRONT_USER_LOGIN_MEMBER = "_FRONT_USER_LOGIN_MEMBER_";//前台保存登录用户memberId
	public static final String FRONT_USER_SSO = "_FRONT_USER_SSO_";//sso的信息
	public static final String FRONT_USER_NAME = "_FRONT_USER_NAME_";//前台保存登录用户名
	public static final String FRONT_USER_ICONURL = "_FRONT_USER_ICONURL_";//前台登录用户头像路径
    public static final String FRONT_USER_TYPE = "_FRONT_USER_TYPE_";//前台保存登录用户类型
    public static final String FRONT_USER_LAST_LOGIN = "_FRONT_USER_LAST_LOGIN_";//最近一次登录时间
	public static final String FRONT_USER_ROLE = "_FRONT_USER_ROLE_";//前台保存登录用户角色
	public static final String FRONT_USER_CURRENCY = "_FRONT_USER_CURRENCY_";//前台保存登录用户的货币
	public static final String FRONT_USER_DATE_FORMAT = "_FRONT_USER_DATE_FMT_";//前台保存登录用户的日期格式
	public static final String FRONT_USER_UP_DOWN_COLOR = "_FRONT_USER_UPDOWN_CLR_";//前台保存登录用户的涨跌配色
	public static final String FRONT_USER_MENU = "_FRONT_USER_MENU_";//前台保存登录用户的菜单

	public static final String FRONT_USER_ROLE_IFA = "IFA";//IFA角色
	public static final String FRONT_USER_ROLE_INVESTOR = "investor";//个人投资者角色
	public static final String FRONT_USER_ROLE_IFA_FIRM = "IFA_FIRM";//IFA Firm角色
	public static final String FRONT_USER_ROLE_DISTRIBUTOR = "distributor";//代理商角色
	public static final String FRONT_USER_ROLE_GUEST = "guest";//游客
	public static final String FRONT_USER_SYS_ADMIN = "sys";

    public static final String CONSOLE_USER_LOGIN = "_CONSOLE_USER_LOGIN_";//工作台保存登录用户信息
	public static final String CONSOLE_USER_NAME = "_CONSOLE_USER_NAME_";//工作台保存登录用户名
	public static final String CONSOLE_USER_ADMIN = "_CONSOLE_USER_ADMIN_";//工作台保存登录用户管理信息
	
	//当前运营公司信息
	public static final String FRONT_COMPANY = "_FRONT_COMPANY_";//company的信息
	public static final String FRONT_COMPANY_ID = "_FRONT_COMPANY_ID_";//公司ID
	public static final String FRONT_COMPANY_CODE = "_FRONT_COMPANY_CODE_";//公司编码
	public static final String FRONT_COMPANY_LOGO = "_FRONT_COMPANY_LOGO_";//logo图url
	public static final String FRONT_COMPANY_LOGO_TAB = "_FRONT_COMPANY_LOGO_TAB_";//logo图url用于tab页面快捷
	public static final String FRONT_COMPANY_BACKGROUND = "_FRONT_COMPANY_BACKGROUND_";//背景图url
	public static final String FRONT_COMPANY_CSS = "_FRONT_COMPANY_CSS_";//主css的url
	public static final String FRONT_COMPANY_NAME = "_FRONT_COMPANY_NAME_";//公司名
	public static final String FRONT_COMPANY_SYSNAME = "_FRONT_COMPANY_SYSNAME_";//系统名
	public static final String FRONT_COMPANY_SYSNAME_EN = "_FRONT_COMPANY_SYSNAME_EN_";//系统名
	public static final String FRONT_COMPANY_SYSNAME_SC = "_FRONT_COMPANY_SYSNAME_SC_";//系统名
	public static final String FRONT_COMPANY_SYSNAME_TC = "_FRONT_COMPANY_SYSNAME_TC_";//系统名
	public static final String FRONT_COMPANY_COPYRIGHT = "_FRONT_COMPANY_COPYRIGHT_";//版权声明
	public static final String FRONT_COMPANY_LOGIN_PAGE = "_FRONT_COMPANY_LOGIN_";//登录页面url
	public static final String DEFAULT_COMPANY = "default_company";//默认运营公司编码参数properties
	
	//基础数据格式
    public static final String DATE_FORMAT          = "yyyy-MM-dd"; // 日期格式
    public static final String TIME_FORMAT          = "HH:mm:ss"; // 时间格式
    public static final String DATE_TIME_FORMAT     = "yyyy-MM-dd HH:mm:ss"; // 日期时间格式
	public static final String NUMBER_PERCENT 		= "#0.00%";//百分数字格式
	public static final String NUMBER_PRICE 		= "#,##0.00";//金额数字格式
	
	public static final String LANG_CODE 			= "_LANG_CODE_";//当前语言
    
    //前台操作日志模块   以1开头
    public static final String FRONT_LOG_LOGIN = "1001";//前台登录模块
    public static final String FRONT_LOG_LOGOUT = "1002";//前台登出模块
    public static final String FRONT_LOG_PERSONAL_INFO = "1003";//前台个人信息模块
    public static final String FRONT_LOG_PERSONAL_RESET_PASS = "1004";//前台重置密码模块
    public static final String FRONT_LOG_FUND_INFO = "1005";//前台基金信息模块
    public static final String FRONT_LOG_INVESTOR_ACCOUNT = "1006";//前台开户信息模块
    public static final String FRONT_LOG_STRATEGY_INFO = "1007";//前台投资决策信息模块
    public static final String FRONT_LOG_PORTFOLIO_INFO = "1008";//前台投资组合信息模块
    public static final String FRONT_LOG_PORTFOLIO_ARENA = "1009";//前台投资组合信息(模拟)模块
    
    //后台操作日志模块  以2开头
    public static final String CONSOLE_LOG_LOGIN = "2001";//后台登录模块
    public static final String CONSOLE_LOG_LOGOUT = "2002";//后台登出模块
    
    //组件服务定义
	public static final String COMP_WS_SERVER = "http://localhost/comp";
	public static final String COMP_WS_EMAIL = "/service/component/email/send.r";
	public static final String COMP_WS_WORKFLOW_CREATE = "/service/component/workflow/create.r";//创建工作流
	public static final String COMP_WS_WORKFLOW_QUERY = "/service/component/workflow/get.r";//查询工作流状态
	public static final String COMP_WS_WORKFLOW_INSERT = "/service/component/workflow/insert.r";//插入工作流待办
	public static final String COMP_WS_WORKFLOW_UPDATE = "/service/component/workflow/update.r";//更新工作流状态

    //消息服务定义
	public static final String SMS_WS_SERVER = "http://localhost/smsServer";
	public static final String SMS_WS_SEND = "/service/sms/send.r";
	public static final String SMS_WS_PUSH = "/service/push/sendMsg.r";

	//邮件服务定义
	public static final String EMAIL_SERVER = "mail.fsll.cn";
	public static final String EMAIL_LOG_MODULE_REGISTER = "register";
	public static final String EMAIL_LOG_MODULE_RESET_PASSWORD = "resetPass";
	
	//itrader监听socket服务名
	public static final String ITRADER_SSM_SERVER = "itrader_ssm_server";
	public static final String ITRADER_ITS_SERVER = "itrader_its_server";

    /**
     * 构造方法
     */
    private CoreConstants() {
    	
    }
    
}
