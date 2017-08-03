package com.fsll.wmes.common;

/**
 * 模块常量参数配置类
 * @author mqzou
 * @date 2016-07-19
 */
public class CommonConstantsWeb {

	public static final int MEMBERBASE_MEMBERTYPE_SYSTEM=0;//平台级用户
	public static final int MEMBERBASE_MEMBERTYPE_INVESTOR=1;//投资人
	public static final int MEMBERBASE_MEMBERTYPE_IFA=2;//IFA
	public static final int MEMBERBASE_MEMBERTYPE_DISTRIBUTOR=3;//代理商
	
	public static final int MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL=11;//独立投资账户
	public static final int MEMBERBASE_SUB_MEMBERTYPE_COPORATE=12;//公司投资账户
	public static final int MEMBERBASE_SUB_MEMBERTYPE_FI=13;//FI账户
	public static final int MEMBERBASE_SUB_MEMBERTYPE_IFA=21;//IFA个人
	public static final int MEMBERBASE_SUB_MEMBERTYPE_IFAFIRM=22;//ifafirm
	public static final int MEMBERBASE_SUB_MEMBERTYPE_DISTRIBUTOR=31;//代理商
	public static final int MEMBERBASE_SUB_MEMBERTYPE_SYSTEM=41;//平台级用户
	
	public static final String MEMBERADMIN_TYPE_SYSTEM="0";//平台级用户
	public static final String MEMBERADMIN_TYPE_IFA="1";//IFA
	public static final String MEMBERADMIN_TYPE_DISTRIBUTOR="2";//代理商
	
	//定义查看权限表的module type
	public static final String WEB_VIEW_MODULE_INSIGHT="insight";//观点
	public static final String WEB_VIEW_MODULE_NEWS="news";//新闻
	public static final String WEB_VIEW_MODULE_STRATEGY="strategy";//策略
	public static final String WEB_VIEW_MODULE_PORTFOLIO_INFO="portfolio_info";//组合
	public static final String WEB_VIEW_MODULE_PORTFOLIO_ARENA="portfolio_arena";//组合
	public static final String WEB_VIEW_MODULE_LIVE_FINO="live_info";//直播
	
	//定义推送权限明细表的type
	public static final String WEB_VIEW_DETAIL_TYPE_CLIENT="Client";
	public static final String WEB_VIEW_DETAIL_TYPE_PROSPECT="Prospect";
	public static final String WEB_VIEW_DETAIL_TYPE_BUDDY="Buddy";
	public static final String WEB_VIEW_DETAIL_TYPE_TEAM="Team";
	public static final String WEB_VIEW_DETAIL_TYPE_COLLEAGUE="Colleague";

	//定义推送权限表的module type
	public static final String WEB_PUSH_MODULE_INSIGHT="insight";//观点
	public static final String WEB_PUSH_MODULE_NEWS="news";//新闻
	public static final String WEB_PUSH_MODULE_STRATEGY="strategy";//策略
	public static final String WEB_PUSH_MODULE_PORTFOLIO_INFO="portfolio_info";//组合
	public static final String WEB_PUSH_MODULE_PORTFOLIO_ARENA="portfolio_arena";//组合
	public static final String WEB_PUSH_MODULE_LIVE_FINO="live_info";//直播
	public static final String WEB_PUSH_MODULE_CRM_SCHEDULE="crm_schedule";//日程
	public static final String WEB_PUSH_MODULE_FUND="fund";//基金
	
	//定义推送权限明细表的type
	public static final String WEB_PUSH_DETAIL_TYPE_CLIENT="Client";
	public static final String WEB_PUSH_DETAIL_TYPE_PROSPECT="Prospect";
	public static final String WEB_PUSH_DETAIL_TYPE_BUDDY="Buddy";
	public static final String WEB_PUSH_DETAIL_TYPE_TEAM="Team";
	public static final String WEB_PUSH_DETAIL_TYPE_COLLEAGUE="Colleague";

	//定义产品信息类型product_info.type ：fund基金,stock股票,bond债券,futures期货,insure保险,cash现金
	public static final String WEB_PRODUCT_TYPE_FUND="fund";
	public static final String WEB_PRODUCT_TYPE_STOCK="stock";
	public static final String WEB_PRODUCT_TYPE_BOND="bond";
	public static final String WEB_PRODUCT_TYPE_FUTURES="futures";
	public static final String WEB_PRODUCT_TYPE_INSURE="insure";
	public static final String WEB_PRODUCT_TYPE_CASH="cash";
	
	//分配方法,G:Geographical,S:Sector,F:Funds Type
	public static final String WEB_ALLOCATION_TYPE_GEOGRAPHICAL="G";
	public static final String WEB_ALLOCATION_TYPE_SECTOR="S";
	public static final String WEB_ALLOCATION_TYPE_FUNDTYPE="F";

	//基础参数类型key
	public static final String SYS_PARM_TYPE_GEOGRAPHICAL="region";//片区划分
	public static final String SYS_PARM_TYPE_SECTOR="fund_sector";//主题分类
	public static final String SYS_PARM_TYPE_FUNDTYPE="fund_type";//基金类型
	public static final String SYS_PARM_TYPE_PRODUCT_ALLOCATION="product_allocation";//产品分配
	public static final String SYS_PARM_TYPE_STRATEGY_RISK_RATING="strategy_risk_rating";//策略风险等级
	public static final String SYS_PARM_TYPE_STRATEGY_SOURCE="strategy_source";//策略来源
	public static final String SYS_PARM_TYPE_SEARCH_PERIOD="search_period";//查询时段
	public static final String SYS_PARM_TYPE_CURRENCY="currency_type";//货币类型
	
	public static final String SYS_PARM_TYPE_TASK="task_type";//任务类型
	public static final String SYS_PARM_TYPE_HOBBY="hobby_type";//兴趣爱好
	public static final String SYS_PARM_TYPE_INVEST_FIELD="invest_field";//投资领域
	public static final String SYS_PARM_TYPE_SECTION="section_type";//版块类型
	public static final String SYS_PARM_TYPE_EXPERTISE="expertise_type";//擅长领域
	
	public static final String SYS_PARM_TYPE_SERVICE_LANGUAGE = "service_language";//服务语言
	public static final String SYS_PARM_TYPE_CURRENCY_TYPE = "currency_type";//货币类型
	public static final String SYS_PARM_TYPE_REMIND_TIME = "remind_time"; // 提醒时间
	public static final String SYS_PARM_TYPE_NOTICE_TYPE = "notice_type"; // 公告类型

	//WEB_READ_TO_DO 消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
	public static final String WEB_READ_MESSAGE_TYPE_EXCHANGE="1";
	public static final String WEB_READ_MESSAGE_TYPE_NORMAL="2";
	public static final String WEB_READ_MESSAGE_TYPE_WARNIGN="3";

	//WEB_READ_TO_DO 发送消息模块：insight观点,news新闻,friend好友相关
	public static final String WEB_READ_MODULE_INSIGHT="insight";
	public static final String WEB_READ_MODULE_NEWS="news";
	public static final String WEB_READ_MODULE_FRIENDS="friend";
	public static final String WEB_READ_MODULE_STRATEGY="strategy";//策略
	public static final String WEB_READ_MODULE_FUND="fund";//基金
	public static final String WEB_READ_MODULE_INVITE_OPEN_ACCOUNT="invite_open_account";//IFA邀请开户
	public static final String WEB_READ_MODULE_SUBMIT_OPEN_ACCOUNT="submit_open_account";//提交开户申请
	public static final String WEB_READ_MODULE_PORTFOLIO_ARENA_PUSH="portfolio_arena_push";// 组合提交提醒
	public static final String WEB_READ_MODULE_KYC_SUMMIT="kyc_summit";// 投资人提交KYC文档审批
	public static final String WEB_READ_MODULE_KYC_COMFIRM="kyc_comfirm";// 代理商提交KYC文档审批结果
	
	// 发送消息模块（new）
	public static final String WEB_READ_MODULE_PROPOSAL = "Proposal-Summit"; // 方案提交
	public static final String WEB_READ_MODULE_PROPOSAL_REJECT = "Proposal-reject"; // 方案确认
	public static final String WEB_READ_MODULE_PROPOSAL_CONFIRMED = "Proposal-confirmed"; // 方案拒绝
	public static final String WEB_READ_MODULE_IFA_PORTFOLIO_PUBLISH = "IFA-Portfolio-Publish"; // IFA组合推送
	public static final String WEB_READ_MODULE_ORDER_SUMMIT_IFA1 = "Order-Summit-IFA1"; // IFA向投资人提交交易计划
	public static final String WEB_READ_MODULE_ORDER_SUMMIT_INVESTOR1 = "Order-Summit-Investor1"; // 投资人提交交易计划
	public static final String WEB_READ_MODULE_ORDER_CONFIRM_INVESTOR = "Order-Confirmed-Investor"; // Investor确认交易计划
	public static final String WEB_READ_MODULE_ORDER_REJECT_INVESTOR = "Order-Reject-Investor"; // Investor退回交易计划
	public static final String WEB_READ_MODULE_ORDER_REJECT_ADMIN = "Order-Reject-Admin"; // Supervisor退回交易计划
	public static final String WEB_READ_MODULE_ORDER_CONFIRM_ADMIN = "Order-Confirmed-IFA-Admin"; // Supervisor确认交易计划
	public static final String WEB_READ_MODULE_ORDER_COMMISSION = "Order-Commission"; // Supervisor确认交易计划
	
	
	//WEB_TASK_LIST 发送任务模块,K:KYC,P:Proposal,A:Appointment,M:Meeting
	public static final String WEB_TASK_MODULE_KYC="K";
	public static final String WEB_TASK_MODULE_PROPOSAL="P";
	public static final String WEB_TASK_MODULE_APPOINTMENT="A";
	public static final String WEB_TASK_MODULE_MEETING="M";
	public static final String WEB_TASK_MODULE_ORDER_PLAN="O"; // 交易
	
	//STRATEGY_ALLOCATION TYPE 分配置类型,fund,stock,bond
	public static final String WEB_ALLOCATION_TYPE_FUND="fund";
	public static final String WEB_ALLOCATION_TYPE_STOCK="stock";
	public static final String WEB_ALLOCATION_TYPE_BOND="bond";

	//STRATEGY_ALLOCATION METHOD_TYPE策略分配方法 G:Geographical,S:Sector,F:Funds Type,P:Product Type
	public static final String WEB_ALLOCATION_METHOD_GEOGRAPHICAL="G";
	public static final String WEB_ALLOCATION_METHOD_SECTOR="S";
	public static final String WEB_ALLOCATION_METHOD_FUNDS="F";
	public static final String WEB_ALLOCATION_METHOD_PRODUCT="P";
	//工作流
	public static final String WORK_FLOW_OPENACCOUNT="Investor_open_account";
	public static final String WORKFLOW_ROLE_IFA_OFFICER = "IO";//投资顾问公司Officer
	public static final String WORKFLOW_ROLE_IFA_RO = "IR";//投资顾问公司RO
	public static final String WORKFLOW_ROLE_DIS_OFFICER = "DO";//代理商Officer
	public static final String WORKFLOW_ROLE_DIS_RO = "DR";//代理商RO
	
	//组件服务器（工作流）
	public static final String COMP_SERVER = "http://192.168.138.70/comp";
	
	//默认头像
	public static final String DEFAULT_HEADER = "/res/images/common/portrait.png";
	
	//基金类型
	public static final String FUND_SECTOR_TYPE = "sectorType";
	//基金地域分布
	public static final String FUND_GEO_ALLOCATION = "geoAllocation";
	
	//基础数据基金类型
	public static final String SYS_PARM_TYPE_SECTOR_TYPE = "sector_type";
	//基础数据片区划分
	public static final String SYS_PARM_TYPE_REGION = "region";
	//基础数据服务区域
	public static final String SYS_PARM_TYPE_SERVICE_REGION = "service_region";
	//基金主题
	public static final String SYS_PARM_TYPE_FUND_SECTOR = "fund_sector";
	
	//IFA需要对客户投资组合进行回顾的临界值
	public static final String IFA_PORTFOLIO_CRITICAL_VALUE="0";
	
	//访问记录类型
	public static final String WEB_VISIT_INSIGHT="insight";//观点
	public static final String WEB_VISIT_FUND="fund";//基金
	public static final String WEB_VISIT_STRATEGY="strategy";//策略
	public static final String WEB_VISIT_PORTFOLIO="portfolio";//组合
	public static final String WEB_VISIT_BBS_TOPIC="bbs_topic";//BBS
	public static final String WEB_VISIT_CORNER_INFO="corner_info";//
	public static final String WEB_VISIT_LIVE="live";//直播
	public static final String WEB_VISIT_NEWS="news";//新闻
	public static final String WEB_VISIT_COMMUNITY_TOPIC="community_topic"; //社区-帖子
	public static final String WEB_VISIT_COMMUNITY_QUESTION="community_question";//社区-问题
	
	public static final String WEB_VISIT_PORTFOLIO_ARENA = "portfolio_arena"; // 组合竞技场访客
	public static final String WEB_VISIT_PORTFOLIO_INFO = "portfolio_info"; // 组合访客
	
	//访问动作类型
	public static final String WEB_VISIT_BUS_VIEW="view";//访问
	public static final String WEB_VISIT_BUS_DOWN="down";//踩
	public static final String WEB_VISIT_BUS_UP="up";//顶
	
	//回报编码
	public static final String WEB_RETURN_PERIOD_CODE_PRE="return_period_code_";
	public static final String WEB_RETURN_YEAR_CODE_PRE="return_";
	
	// 图表的尺寸类型
	public static final String CHART_IMAGE_SIZE_SMALL= "small"; 
	public static final String CHART_IMAGE_SIZE_MIDDL= "middle"; 
	
	//校验账户密码时的账户类型
	public static final String WEB_ACCOUNTCHECK_IFA="0";//ifa
	public static final String WEB_ACCOUNTCHECK_INVESTOR="1";//投资人
	
	
	//开户状态
	public static final String WEB_ACCCOUNT_OPEN_REJECT="-1";//退回
	public static final String WEB_ACCCOUNT_OPEN_DRAFT="0";//草稿
	public static final String WEB_ACCCOUNT_OPEN_TOBECONFIRM="1";//待投资人确认
	public static final String WEB_ACCCOUNT_OPEN_PROCESSING="2";//处理中
	public static final String WEB_ACCCOUNT_OPEN_COMPLETE="3";//开户成功
	public static final String WEB_ACCCOUNT_OPEN_REFUSE="4";//开户失败
	
	// Task List、日程分组
	public static final String GROUP_T="T"; // Task List
	public static final String GROUP_S="S"; // 日程

	//watching list 类型，目前暂定为3种：fund,stock,bond
	public static final String WEB_WATCHLIST_TYPE_FUND="1";//基金
	public static final String WEB_WATCHLIST_TYPE_STOCK="2";//股票
	public static final String WEB_WATCHLIST_TYPE_BOND="3";//债劵
	
	// 销售阶段代码
	public static final String PIPELINE_SALES_PROPOSAL="sales_proposal"; 
	public static final String PIPELINE_SALES_NEGOTIATING="sales_negotiating"; 
	public static final String PIPELINE_SALES_NEW="sales_new";
	public static final String PIPELINE_SALES_CONTACT="sales_contact";
	
	public static final String CREATE_PROPOSAL_CRM_PROPOSAL="crm_proposal"; 
	public static final String CREATE_PROPOSAL_IMAGE_AGGREGATE="create_proposal_aggregate"; 
	public static final String CREATE_PROPOSAL_IMAGE_SEPARATENES="create_proposal_separatenes"; 
	public static final String CREATE_PROPOSAL_IMAGE_GEOALLCATION="create_proposal_geoAllocation"; 
	public static final String CREATE_PROPOSAL_IMAGE_SECTORTYPE="create_proposal_sectorType"; 
	public static final String CREATE_PROPOSAL_IMAGE_RISKANALYSIS="create_proposal_riskAnalysis"; 
	public static final String CREATE_PROPOSAL_PDF="create_proposal_PDF_"; 
	public static final String CREATE_PROPOSAL_PDF_SC="create_proposal_PDF_sc"; 
	public static final String CREATE_PROPOSAL_PDF_TC="create_proposal_PDF_tc"; 
	public static final String CREATE_PROPOSAL_PDF_EN="create_proposal_PDF_en"; 
	
	public static final String ORDER_HISTORY_STATUS_DISPLAY_PROCESSING="Processing"; // 处理中，等待OMS返回结果（-10），PendOMS等待处理（1）	 
	public static final String ORDER_HISTORY_STATUS_DISPLAY_PART="Part"; // 部分成交（2）
	public static final String ORDER_HISTORY_STATUS_DISPLAY_COMP="Comp"; // 全部成交（3）
	public static final String ORDER_HISTORY_STATUS_DISPLAY_CANC="Canc"; // 取消（4）
	public static final String ORDER_HISTORY_STATUS_DISPLAY_INAC="Inac"; // 无效（5）
	public static final String ORDER_HISTORY_STATUS_DISPLAY_CHE="Che"; // 审查通过（6）
	public static final String ORDER_HISTORY_STATUS_DISPLAY_CONF="Conf"; // 已确认（7）
	public static final String ORDER_HISTORY_STATUS_DISPLAY_REJE="Reje"; //审核不通过（-1） 
	
	public static final String ORDER_PLAN_PRODUCT_DIVIDEND_R="R"; // Reinvestment再投资
	public static final String ORDER_PLAN_PRODUCT_DIVIDEND_D="D"; // 分配到现金账号
	public static final String ORDER_PLAN_PRODUCT_DIVIDEND_DISPLAY_R="Relnv."; // Reinvestment再投资
	public static final String ORDER_PLAN_PRODUCT_DIVIDEND_DISPLAY_D="Cash Acc"; // 分配到现金账号
	
	
	//好友类型
	public static final String WEB_FRIEND_BUDDY="Buddy";
	public static final String WEB_FRIEND_CLIENT="Client";
	public static final String WEB_FRIEND_ADVISOR="Advisor";
	public static final String WEB_FRIEND_PROSPECT="Prospect";
	
	//新闻栏目
	public static final String NEWS_CATEGORY_POLICY="policy";//政策
	public static final String NEWS_CATEGORY_ECONOMY="economy";//经济
	public static final String NEWS_CATEGORY_MARKETS="markets";//市场
	public static final String NEWS_CATEGORY_INDUSTRIES="industries";//产业
	public static final String NEWS_CATEGORY_WORLD="world";//国际
	public static final String NEWS_CATEGORY_STOCKS="stocks";//股票
	public static final String NEWS_CATEGORY_BONDS="bonds";//债券
	public static final String NEWS_CATEGORY_INDEPTH="indepth";//深度分析
	public static final String NEWS_CATEGORY_ENERGY="energy";//能源
	public static final String NEWS_CATEGORY_FINANCE="finance";//金融
	public static final String NEWS_CATEGORY_TECHNOLOGY="technology";//科技
	public static final String NEWS_CATEGORY_HEADLINES="headlines";//头条
	public static final String NEWS_CATEGORY_RECOMMEND="recommend";//编辑推荐
	
	//新闻评论类型
	public static final String NEWS_COMMENT_TYPE_FEEDBACK="feedback";//文字评论，回复
	public static final String NEWS_COMMENT_TYPE_GOOD="good";//顶
	public static final String NEWS_COMMENT_TYPE_BAD="bad";//踩
	
	//新闻或评论操作类型
	public static final String NEWS_BEHAVIOR_READ="read";//阅读
	public static final String NEWS_BEHAVIOR_FAVORITE="favorite";//收藏
	public static final String NEWS_BEHAVIOR_LIKE="like";//点赞
	public static final String NEWS_BEHAVIOR_UNLIKE="unlike";//踩
	
	public static final String NEWS_BEHAVIOR_TARGET_COMMENT="comment";
	public static final String NEWS_BEHAVIOR_TARGET_NEWS="news";
	
	//帖子或评论操作类型
	public static final String COMUNITY_BEHAVIOR_READ="read";//阅读
	public static final String COMUNITY_BEHAVIOR_FAVORITE="favorite";//收藏
	public static final String COMUNITY_BEHAVIOR_LIKE="like";//点赞
	public static final String COMUNITY_BEHAVIOR_UNLIKE="unlike";//踩
	
	
	public static final String COMUNITY_BEHAVIOR_TARGET_COMMENT="comment";
	public static final String COMUNITY_BEHAVIOR_TARGET_TOPIC="topic";
	public static final String COMUNITY_BEHAVIOR_TARGET_QUESTION="question";
	
	//帖子的来源类型（转发或者分享的帖子）
	public static final String TOPIC_SOURCE_TYPE_TOPIC="topic";
	public static final String TOPIC_SOURCE_TYPE_NEWS="news";
	public static final String TOPIC_SOURCE_TYPE_PORTFOLIO="portfolio";
	public static final String TOPIC_SOURCE_TYPE_STRATEGY="strategy";
	// 公告来源
	public static final String NOTICE_SOURCE_TYPE_SYS = "sys";
	public static final String NOTICE_SOURCE_TYPE_IFAFIRM = "ifafirm";
	public static final String NOTICE_SOURCE_TYPE_DISTRIBUTOR = "distributor";
	
	public static final String NOTICE_TARGET_ALL = "ALL";
	public static final String NOTICE_TARGET_I1 = "I1";
	public static final String NOTICE_TARGET_I2 = "I2";
	public static final String NOTICE_TARGET_D1 = "D1";
	public static final String NOTICE_TARGET_D2 = "D2";
	
	// 回报的编码
	public static final String RETURN_PERIOD_CODE_LAUNCH = "return_period_code_LAUNCH";
	
	// 推荐对应模块
	public static final String WEB_RECOMMENDED_MODULE_TYPE_FUND = "fund";
	public static final String WEB_RECOMMENDED_MODULE_TYPE_INSIGHT = "insight";
	public static final String WEB_RECOMMENDED_MODULE_TYPE_NEWS = "news";
	public static final String WEB_RECOMMENDED_MODULE_TYPE_STRATEGY = "strategy";
	public static final String WEB_RECOMMENDED_MODULE_TYPE_PORTFOLIO_INFO = "portfolio_info";
	public static final String WEB_RECOMMENDED_MODULE_TYPE_PORTFOLIO_ARENA = "portfolio_arena";
	
	public static final String COMMUNITY_RULE_HOT_TOPIC = "hot_topic";
	public static final String COMMUNITY_RULE_TOTAL_RANK = "total_rank";
	public static final String COMMUNITY_RULE_ACTIVITTY_MIDDLE = "activity_middle";
	public static final String COMMUNITY_RULE_ACTIVITTY_HIGH = "activity_high";
	
	public static final String WEB_FOLLOW_TYPE_FUND = "fund";
	public static final String WEB_FOLLOW_TYPE_IFA = "ifa";
	public static final String WEB_FOLLOW_TYPE_INSIGHT = "insight";
	public static final String WEB_FOLLOW_TYPE_NEWS = "news";
	public static final String WEB_FOLLOW_TYPE_STRATEGY = "strategy";
	public static final String WEB_FOLLOW_TYPE_PORTFOLIO = "portfolio";
	public static final String WEB_FOLLOW_TYPE_MARKET = "market";
	public static final String WEB_FOLLOW_TYPE_TOPIC = "topic";
	
	// 邮件记录模块
	public static final String WEB_EMAIL_LOG_REGISTER = "register"; // 注册和激活
	public static final String WEB_EMAIL_LOG_RESETPASS = "resetPass"; // 取回密码
	public static final String WEB_EMAIL_LOG_CHANGEPASS = "changePass"; // 修改密码
	public static final String WEB_EMAIL_LOG_PROPOSAL = "proposal"; // 方案确认
	
	// 附件模块
	public static final String ACCESSORY_FILE_MODULE_TYPE_NOTICE="notice"; 
	
	//IFA人气值计算
	public static final int IFA_POPULARITY_TOPIC_PUBLIC=1;//发帖
	public static final int IFA_POPULARITY_TOPIC_REPLY_SELF=2;//回复别人（自己回复自己不算）
	public static final int IFA_POPULARITY_TOPIC_REPLY_OTHER=2;//有人回复（不管这个人回复谁）
	public static final int IFA_POPULARITY_TOPIC_NICE_OTHER=2;//有人顶
	public static final int IFA_POPULARITY_TOPIC_FAV_OTHER=2;//有人收藏
	public static final int IFA_POPULARITY_TOPIC_READ_OTHER=1;//有人阅读
	public static final int IFA_POPULARITY_QUESTION_ASK_OTHER=2;//有人问问题
	public static final int IFA_POPULARITY_QUESTION_ANSWER=5;//回答别人问题
	public static final int IFA_POPULARITY_QUESTION_REPLY_SELF=2;//回复别人问题（自己回复自己不算）
	public static final int IFA_POPULARITY_QUESTION_REPLY_OTER=2;//有人回复（不管这个人回复谁）
	public static final int IFA_POPULARITY_FOCUS_ADD=10;//有人关注
	public static final int IFA_POPULARITY_FOCUS_CANCEL=-10;//取消关注
}
	

