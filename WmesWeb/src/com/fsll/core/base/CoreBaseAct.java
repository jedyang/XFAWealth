/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.base;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.fsll.buscore.trade.service.TradeCoreService;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.action.BaseAct;
import com.fsll.common.util.CSRFTokenUtil;
import com.fsll.common.util.ChineseToEnglish;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.common.util.WebServiceUtil;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.entity.SysSite;
import com.fsll.core.service.CoreBaseService;
import com.fsll.core.service.SysParamService;
import com.fsll.core.service.SysSiteService;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.entity.WebOperLog;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebVisitLog;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.web.service.WebOperLogService;
import com.fsll.wmes.web.service.WebReadToDoService;
import com.fsll.wmes.web.service.WebVisitLogService;

/**
 * 控制器基类：核对模块
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public abstract class CoreBaseAct extends BaseAct{
	protected JsonPaging jsonPaging = new JsonPaging();
	
	@Autowired
	protected CoreBaseService coreBaseService;
	
	@Autowired
	protected WebOperLogService webOperLogService;
	
    @Autowired
    private SysParamService sysParamService;
    
    @Autowired
    private MemberBaseService memberBaseService;
    
	@Autowired
	private WebReadToDoService webReadToDoService;
	@Autowired
	private WebVisitLogService webVisitLogService;
	@Autowired
	private SysSiteService sysSiteService;
	
	@Autowired
	protected TradeCoreService tradeCoreService;
	
	/**
	 * 验证CSRFToken的值
	 * 
	 * @param request
	 * @return true合法,false表示不合法
	 */
    protected boolean verifyCSRFToken(HttpServletRequest request) {
    	try {
	    	HttpSession session = request.getSession();
	    	String csrfToken = request.getParameter("CSRFToken") == null ? null:request.getParameter("CSRFToken");
	        if(session == null || csrfToken == null || !csrfToken.equals(StrUtils.getString(session.getAttribute(CSRFTokenUtil.CSRF_TOKEN_FOR_SESSION_ATTR_NAME)))){
	        	return false;
	        }else{
	        	return true;
	        }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
    
	/**
	 * 获得当前的登陆用户语言
	 * @param request
	 * @return
	 */
	protected String getLoginLangFlag(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(CoreConstants.LANG_CODE);
		if(obj != null){
			return (String)obj;
		}
		return CommonConstants.DEF_LANG_CODE;
	}
	
	/**
	 * 获得当前的登陆用户
	 * @param request
	 * @return
	 */
	protected MemberSsoVO getLoginUserSSO(HttpServletRequest request) {
		Object memberSsoVO = request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
		MemberSsoVO loginMemberSsoVO = null;
		if(memberSsoVO != null){
			loginMemberSsoVO = (MemberSsoVO)memberSsoVO;
		}
		return loginMemberSsoVO;
	}
	
	/**
	 * 获得当前的登陆用户
	 * @param request
	 * @return
	 */
	protected MemberBase getLoginUser(HttpServletRequest request) {
		Object memberBase = request.getSession().getAttribute(CoreConstants.FRONT_USER_LOGIN);
		MemberBase loginMemberBase = null;
		if(memberBase != null){
			loginMemberBase = (MemberBase)memberBase;
		}
		return loginMemberBase;
	}
	
	/**
	 * 获得当前的登陆工作台管理员信息
	 * @param request
	 * @return
	 */
	protected MemberAdmin getLoginMemberAdmin(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(CoreConstants.CONSOLE_USER_ADMIN);
		MemberAdmin memberAdmin = null;
		if(obj != null){
			memberAdmin = (MemberAdmin)obj;
		}
		return memberAdmin;
	}
	
	/**
	 * 返回分页json给页面
	 * @param response
	 * @param jsonPaging
	 */
	public void toJsonString(HttpServletResponse response,JsonPaging jsonPaging) {
		Integer record = jsonPaging.getTotal();
		//int _totalPages = (int) (record % jsonPaging.getRows());
		//Integer total = (int) (record / jsonPaging.getRows());
		//if (_totalPages > 0)total++;
		//if (jsonPaging.getPage() > total)jsonPaging.setPage(total);
		Map<String, Object> statuts = new HashMap<String, Object>();
		//statuts.put("page",jsonPaging.getPage());
		statuts.put("total",record);
		//statuts.put("records",record);
		statuts.put("rows",jsonPaging.getList());
		JsonUtil.toWriter(statuts, response);
	}
	
	
	/**
	 * 初始化分页参数
	 * @param response
	 * @param jsonPaging
	 */
	public JsonPaging getJsonPaging(HttpServletRequest request) {
		Integer page = 1;
		try {
			page = request.getParameter("page") == null ? 1:Integer.parseInt(request.getParameter("page"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Integer rows = 10;
		try {
			rows = request.getParameter("rows") == null ? 10:Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//String sord = request.getParameter("sord") == null ? "":request.getParameter("sord");
		//String sidx = request.getParameter("sidx") == null ? "":request.getParameter("sidx");
		String sort = request.getParameter("sort") == null ? "":request.getParameter("sort");
		String order = request.getParameter("order") == null ? "":request.getParameter("order");
	    //Integer rows = 0;  //指示每页显示的记录条数。
	    //Integer page = 0;  //当前页         返回中要包含：当前是第几页数据
	    //Integer total = 0;  //总页数        返回中要包含：总共有多少页数据
//	    Integer record = 0;  ////总记录数   返回中要包含：总的记录条数
	    //String sord;  //指示查询排序的方式，可能的值是ASC和DESC
	    //String sidx; //指示查询排序的条件,这是一个字符串，可能是数据库表字段或者是POJO对象的属性名.这需要程序来处理
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		jsonPaging.setOrder(order);
		jsonPaging.setSort(sort);
		jsonPaging.setTotal(null);
		jsonPaging.setList(null);
		return jsonPaging;
	}

	public void setJsonPaging(JsonPaging jsonPaging) {
		this.jsonPaging = jsonPaging;
	}
	
	/**
	 * web操作日志添加
	 * @param request 请求 
     * @param loginCode 登陆ID  可以为空
     * @param nickName 名称   可以为空
	 * @param moduleType  登陆 退出 故障 其他为模块的具体名称
	 * @param remark 备注
	 */
	public void saveOperLog(HttpServletRequest request,String loginCode,String nickName,String moduleType, String remark){
		WebOperLog operLog = new WebOperLog();
		if(this.getLoginUser(request) != null){
			operLog.setMember(this.getLoginUser(request));
		}
		operLog.setLoginCode(loginCode);
		operLog.setNickName(nickName);
		operLog.setIp(this.getRemoteHost(request));
		operLog.setModuleType(moduleType);
		operLog.setRemark(remark);
		operLog.setCreateTime(new Date());
		webOperLogService.saveOrUpdate(operLog);
	}
	
    /**
     * 获取基础参数表
     * （基金类型fund_type、教育程度education、就业情况employment、货币类型currency_type、主题分类fund_sector、
     * 		职业分类occupation、年龄段age_scope、费用类型fees_item）
     * 
     * @return List<GeneralDataVO>
     */
	public List<GeneralDataVO>  findSysParameters(String type, String lang){
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		List<SysParamConfig> configs = sysParamService.findParamConfigByType(type);
		lang = (lang==null)?"":lang;
		if (configs!=null && !configs.isEmpty()){
			for(SysParamConfig c: configs){
				GeneralDataVO vo = new GeneralDataVO();
				vo.setId(c.getId());
				vo.setItemCode(c.getConfigCode());
				if ("en".equals(lang)){
					vo.setName(c.getNameEn());
					vo.setValue(c.getConfValueEn());
					vo.setKey((null!=c.getNameEn() && c.getNameEn().length()>0)?c.getNameEn().substring(0,1).toUpperCase():"");
				}else if ("tc".equals(lang)){
					vo.setName(c.getNameTc());
					vo.setValue(c.getConfValueTc());
					//设定拼音为关键词
					try {
						vo.setKey(ChineseToEnglish.getPinYinHeadChar(c.getNameTc()));
					} catch (Exception e) {
						// TODO: handle exception
						vo.setKey(c.getNameEn());
					}
				}else {
					vo.setName(c.getNameSc());
					vo.setValue(c.getConfValueSc());
					//设定拼音为关键词
					try {
						vo.setKey(ChineseToEnglish.getPinYinHeadChar(c.getNameSc()).substring(0,1).toUpperCase());
					} catch (Exception e) {
						// TODO: handle exception
						vo.setKey(c.getNameEn());
					}
				}
				
				list.add(vo);
			}
		}
		return list;
	}
	
    /**
     * 把字符串的编码转换为UTF8
     * @author michael
     * @param val
     * @return
     */
	public String toUTF8String(String val){
    	try {
        	if (null!=val && !"".equals(val)){
        		val = URLDecoder.decode(val,"UTF-8");
        		//val = new String(val.getBytes("ISO-8859-1"),"UTF-8");
        		return val;
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
    }
    
	/**
	 * 调用ws接口，获取返回结果(适用与RestFul的url参数方式的调用)
	 * @author michael
	 * @param urlString  http://www.xx.com/service/servicename?a=a&b=b...
	 * @return
	 */
	protected String sendWebServiceByUrl(String urlString){
		return WebServiceUtil.sendWebServiceByUrl(urlString);
	}
	
	/**
	 * 调用ws接口，获取返回结果(适用与RestFul的body参数方式的调用)
	 * @author michael
	 * @param urlString http://www.xx.com/service/servicename
	 * @param bodyString 参数json字符串
	 */
	public String sendWebServiceByBody(String urlString, String bodyString) {
        return WebServiceUtil.sendWebServiceByBody(urlString, bodyString);
	}
	
	/**
	 * 获取环境变量（通过请求获取语言编码）
	 * @param request 请求
	 * @param key 环境变量编码
	 * @return
	 */
	public String getProperty(HttpServletRequest request, String key){
		return PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),key,null);
	}
	
	/**
	 * 根据语言编码获取环境变量值
	 * @param langCode 语言编码
	 * @param key 环境变量编码
	 * @return
	 */
	public String getProperty(String langCode, String key){
		return PropertyUtils.getPropertyValue(langCode,key,null);
	}
	
	/**
	 * 通过组件接口发送邮件
	 * @author michael
	 * @param sender  发送人
	 * @param jsonObj 发送邮件的json对象（body）
	 * @return
	 */
	public String sendEmailByWS(MemberBase sender, JSONObject jsonObj){
		String wsServer = PropertyUtils.getConfPropertyValue("comp_ws_server");//获取配置文件的配置
		if (null==wsServer || "".equals(wsServer)) wsServer = CoreConstants.COMP_WS_SERVER;
		
		if (sender!=null){
			String urlString = wsServer+"/service/component/email/send.r";
//			JSONObject jsonObj = new JSONObject();
//			jsonObj.put("to_member_id", "40280ad455de27960155de42f8990001");
//			jsonObj.put("to_email_addr", "mic@mic.com");
//			jsonObj.put("to_email_title", "emailTest");
//			jsonObj.put("to_email_content", "test...");
//			jsonObj.put("module_type", "fund");
//			jsonObj.put("send_by", sender.getId());
			String jsonString = jsonObj.toString();
			String result = sendWebServiceByBody(urlString, jsonString);
			return result;
		}
		return null;
	}
	
	/**
	 * 获取组件接口的服务器
	 * @return
	 */
	public String getCompWsServer(){
		return WebServiceUtil.getCompWsServer();
	}
	
	/**
	 * 发送邮件(调用邮件服务器发送邮件，并写邮件发送日志）
	 * @author michael
	 * @param moduleType 调用模块
	 * @param sender 发送人
	 * @param receiver 接收人
	 * @param receiverEmail 接收人邮箱
	 * @param subject 邮件标题
	 * @param message 邮件内容
	 * @return emailLog 邮件发送日志，返回null，发送失败
	 */
	public WebEmailLog sendEmail(String moduleType, MemberBase sender, MemberBase receiver,String receiverEmail, String subject, String message, String relateId){
		return memberBaseService.saveAndSendEmail(moduleType, sender, receiver, receiverEmail, subject, message, relateId);
	}
	
	/**
	 * 发送待办待阅消息
	 * @param fromUser 发送人
	 * @param toUser 接收人
	 * @param msgType 消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
	 * @param moduleType 消息关联的模块类型，如 insight关点,news新闻,invite_open_account要请开户
	 * @param relateId 消息关联的页面ID
	 * @param title 消息通用标题
	 * @param titleKey 多语言配置中的消息标题关键字
	 * @param url
	 * @return
	 */
	public WebReadToDo sendWebReadToDo(MemberBase fromUser, MemberBase toUser, String msgType, String moduleType, String relateId, String title, String url){
		// 发送待办
		return webReadToDoService.sendToRead(msgType, moduleType, relateId, title, url, "", "", fromUser, toUser);
	}
	
	/**
	 * 模块访问统计
	 * @author mqzou 2016-12-22
	 * @param member 当前登录用户（已注册用户，非游客时不为空）
	 * @param browser 当前用户为游客时不为空
	 * @param relateId 关联的Id
	 * @param busType 访问类型    CommonConstantsWeb.WEB_VISIT_BUS_VIEW  WEB_VISIT_BUS_DOWN WEB_VISIT_BUS_UP
	 * @param moduleType 访问模块 CommonConstantsWeb.WEB_VISIT_INSIGHT 等
	 * @param ip 用户ip地址
	 * @return
	 */
	public WebVisitLog saveVisit(MemberBase member,String browser,String relateId,String busType,String moduleType,String ip){
		WebVisitLog log=new WebVisitLog();
		if(null!=member)
			log.setMember(member);
		log.setBrowser(browser);
		log.setBusType(busType);
		log.setModuleType(moduleType);
		log.setRelateId(relateId);
		log.setVistiTime(DateUtil.getCurDate());
		log.setVistiIp(ip);
		
		
		WebVisitLog webVisitLog=webVisitLogService.findLastLog(log);
		if(null!=webVisitLog){
			SysSite site=sysSiteService.findBydomain(CommonConstants.MAIN_SITE_ID);
			int hours=1;
			if(null!=site){
				hours=site.getVisitCountDiv();
			}
	
		 long time=0;
		 if(null!=webVisitLog.getVistiTime())
			 time=DateUtil.compareDateStr(DateUtil.getDateStr(webVisitLog.getVistiTime(),"yyyy-MM-dd HH:mm:ss"), DateUtil.getDateStr(DateUtil.getCurDate(),"yyyy-MM-dd HH:mm:ss"));
		 if(time/3600000>hours ||time==0){
			 webVisitLogService.addLog(log);
		 }
		}else {
			 webVisitLogService.addLog(log);
		}
		
		return log;
	}
	/**
	 * 根据语言获取国家名称
	 * @author mqzou	
	 * @date 2016-07-07
	 * @param sysCountry
	 * @param langCode
	 */
	public String getCountryString(SysCountry sysCountry,String langCode){
		if(null!=sysCountry){
			if("sc".equalsIgnoreCase(langCode)){
				return sysCountry.getNameSc();
			}else if ("tc".equalsIgnoreCase(langCode)) {
				return sysCountry.getNameTc();
			}else if ("en".equalsIgnoreCase(langCode)) {
				return sysCountry.getNameEn();
			}else {
				return "";
			}
		}else {
			return "";
		}
		
	}
	/**
	 * 获取用户头像
	 * @author mqzou
	 * @date 2017-01-07
	 * @param url
	 * @param gender
	 * @return
	 *//*
	public String getUserHeadUrl(String url,String gender){
		if(null!=url && !"".equals(url)){
			if(url.startsWith("/res")){
				return url;
			}else if (url.startsWith("/u")) {
				return "loadImgSrcByPath.do?filePath="+url;
			}else {
				if("F".endsWith(gender)){
					return "/res/images/hyzx_touxiang_08.png";
				}else {
					return "/res/images/hyzx_touxiang_07.png";
				}
			}
		}else {
			if("F".endsWith(gender)){
				return "/res/images/hyzx_touxiang_08.png";
			}else {
				return "/res/images/hyzx_touxiang_07.png";
			}
		}
	}*/
}
