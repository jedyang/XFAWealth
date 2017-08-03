package com.fsll.wmes.portfolio.action.front;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.buscore.fund.service.CoreFundService;
import com.fsll.buscore.fund.service.CorePortfolioService;
import com.fsll.buscore.fund.vo.CoreFundsReturnForChartsVO;
import com.fsll.buscore.fund.vo.CorePortfolioVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaAip;
import com.fsll.wmes.entity.PortfolioArenaCumperf;
import com.fsll.wmes.entity.PortfolioArenaProduct;
import com.fsll.wmes.entity.StrategyAllocation;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.ifa.service.InsightInfoService;
import com.fsll.wmes.ifa.vo.IfaClientVO;
import com.fsll.wmes.ifa.vo.StrategyInfoVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.portfolio.service.PortfolioArenaService;
import com.fsll.wmes.portfolio.service.PortfolioInfoService;
import com.fsll.wmes.portfolio.vo.CriteriaVO;
import com.fsll.wmes.portfolio.vo.PortfolioArenaVO;
import com.fsll.wmes.portfolio.vo.PortfolioFundListVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebPushVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebViewVO;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.web.service.WebFollowStatusService;
import com.fsll.wmes.web.service.WebFriendService;
import com.fsll.wmes.web.service.WebInvestorVisitService;
import com.fsll.wmes.web.service.WebReadToDoService;

/**
 * 控制器:基金组合管理（主网站）
 * 
 * @author michael
 * @version 1.0.0 Created On: 2016-8-17
 */

@Controller
@RequestMapping("/front/portfolio/arena")
public class FrontPortfolioArenaAct extends WmesBaseAct {
	
	@Autowired
	private PortfolioArenaService portfolioArenaService;

	@Autowired
	private StrategyInfoService strategyInfoService;
	
    @Autowired
    private FundInfoService fundInfoService;
    
	@Autowired
	private InvestorService investorService;
	
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private WebFollowStatusService webFollowStatusService;
	
	@Autowired
    private WebFollowStatusService webFollowService;
	
	@Autowired
	private PortfolioInfoService portfolioInfoService;
	
	@Autowired
	private WebFriendService webFriendService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private InsightInfoService insightInfoService;
	@Autowired
	private IfaInfoService ifaInfoService;
	@Autowired
	private WebReadToDoService webReadToDoService;
	@Autowired
	private WebInvestorVisitService webInvestorVisitService;
	
	@Autowired
	private CorePortfolioService corePortfolioService;
	@Autowired
	private CoreFundService coreFundService;
    /**
     * 投资组合分页列表（公共） - 不用登录
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	this.saveLastVisitUrl(request, response);
    	String langCode=getLoginLangFlag(request);
    	//地区列表
        List<GeneralDataVO> itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, langCode);
        model.put("regionList", itemList);
        //主题分类
        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR,langCode);
        model.put("sectorList", itemList);
        
        //风险等级
        itemList=findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_STRATEGY_RISK_RATING, langCode);
        model.put("riskList", itemList);
        
        //策略来源
        itemList=findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_STRATEGY_SOURCE, langCode);
        model.put("sourceList", itemList);
        
        //搜索时段
        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SEARCH_PERIOD, this.getLoginLangFlag(request));
        model.put("periodList", itemList);
        
        MemberBase loginUser=getLoginUser(request);
        
        String defDisplayColor=loginUser.getDefDisplayColor();
        if(null==defDisplayColor || "".equals(defDisplayColor)){
        	defDisplayColor=CommonConstants.DEF_DISPLAY_COLOR;
        }
        model.put("defDisplayColor", defDisplayColor);
        
        String dateFormat=loginUser.getDateFormat();
        if(null==dateFormat || "".equals(dateFormat))
        	dateFormat=CommonConstants.FORMAT_DATE;
        
        model.put("dateFormat", dateFormat);
        
        return "portfolio/arena/list";//页面摆放路径
    }
    
    /**
     * 我的投资组合分页列表
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/myList", method = RequestMethod.GET)
    public String myList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
	    	this.isMobileDevice(request, model);
	
	        //地区列表
	        List<GeneralDataVO> itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, this.getLoginLangFlag(request));
	        model.put("regionList", itemList);
	
	        //主题分类
	        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR, this.getLoginLangFlag(request));
	        model.put("sectorList", itemList);
	        
//	        jsonPaging = findList(request, response, model);
//	        model.put("dataList", jsonPaging.getList());
	        //风险等级
	        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_STRATEGY_RISK_RATING, this.getLoginLangFlag(request));
	        model.put("riskList", itemList);

	        //搜索时段
	        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SEARCH_PERIOD, this.getLoginLangFlag(request));
	        model.put("periodList", itemList);
	        model.put("defDisplayColor", loginUser.getDefDisplayColor());
	        String dateFormat=loginUser.getDateFormat();
	        if(null==dateFormat || "".equals(dateFormat))
	        	dateFormat=CommonConstants.FORMAT_DATE;
	        
	        model.put("dateTimeFormat", dateFormat+" "+CommonConstants.FORMAT_TIME);
	        model.put("dateFormat", dateFormat);
	        return "portfolio/arena/myList";//页面摆放路径
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
    }
    
    /**
     * 获取投资策略列表（公开）
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    public JsonPaging findList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        jsonPaging = findPortfolioList(request, model, "list");
		return jsonPaging;
    }

    /**
     * 获取我的投资策略列表
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    public JsonPaging findMyList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		if (loginUser!=null){
	        jsonPaging = findPortfolioList(request, model, "mylist");
		}
		return jsonPaging;
    }
    
    /**
     * 获取投资组合列表（公共方法）
     * @author michael
     * @param request
     * @param model
     * @param module 调用模块： list，mylist
     * @return
     */
    private JsonPaging findPortfolioList(HttpServletRequest request, ModelMap model, String module) {
		MemberBase loginUser = getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		CriteriaVO criteria = new CriteriaVO();
		if (loginUser!=null){
	        
	        //获取发布日期条件
	        String period = StrUtils.getString(request.getParameter("period"));
	        String dataTo=StrUtils.getString(request.getParameter("dataTo"));
	        String dataFrom=StrUtils.getString(request.getParameter("dataFrom"));
	        
	        String pref=StrUtils.getString(request.getParameter("pref"));
	        if(null!=pref && !"".equals(pref)){
	        	pref=StringEscapeUtils.unescapeHtml(pref);
	        }
			/*String startDate = "";//DateUtil.getCurDateStr();
			String endDate = DateUtil.getCurDateStr(Calendar.DATE, 1);//DateUtil.getCurDateStr();
			if ("1D".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -1);
			if ("1W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
			if ("1M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
			if ("3M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
			if ("6M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
			if ("1Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
			if ("2Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -2);
			if ("3Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
			if ("5Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
			if ("10Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -10);
			if ("YTD".equals(period)) startDate = DateUtil.formatDate(DateUtil.getCurrYearFirst());//今年以来
	        String fromDate = request.getParameter("fromDate");
	        if (null!=fromDate && fromDate.length()==10)
	        	startDate = fromDate;
	        String toDate = request.getParameter("toDate");
	        if (null!=toDate && toDate.length()==10)
	        	endDate = toDate;
			//开始日期统一提前一天
	        if (!"".equals(startDate))
	        	startDate = DateUtil.addDateToString(startDate,Calendar.DATE, -1);*/
	        
	        String keyword = toUTF8String(request.getParameter("keyWord"));
	        String sector = StrUtils.getString(request.getParameter("sectors"));
	        
	       
	        
	        String geoAllocation = StrUtils.getString(request.getParameter("regions"));
	       // String riskLevel=StrUtils.getString(request.getParameter("riskLevel"));
	        
	     /* String viewSort = request.getParameter("viewSort");
	        String issuedDateSort = request.getParameter("issuedDateSort");
	        String totalReturnSort = request.getParameter("totalReturnSort");
	        String riskLevelSort=request.getParameter("riskLevelSort");*/
	        
	        String totalReturn = request.getParameter("totalReturn");
	        String riskLevel = StrUtils.getString(request.getParameter("riskLevel"));
	        String source = StrUtils.getString(request.getParameter("source"));
	        String status = StrUtils.getString(request.getParameter("status"));
	        String checkIsMyFollow = request.getParameter("checkIsMyFollow");
	        
	        if ("".equals(status)) status = "1";//默认查询已发布的记录
	        
			criteria.setModule(module);
	        criteria.setUserId(loginUser.getId());
	        criteria.setKeyword(keyword);
	        criteria.setSector(sector);
	        criteria.setGeoAllocation(geoAllocation);
	        criteria.setRiskLevel(riskLevel);
	        criteria.setTotalReturn(totalReturn);
	        criteria.setStatus(status);
	        criteria.setSource(source);
	        criteria.setCheckIsMyFollow("1".equals(checkIsMyFollow)?true:false);
	        criteria.setPeriod(period);
	        criteria.setDataFrom(dataFrom);
	        criteria.setDataTo(dataTo);
	        criteria.setPrefData(pref);
	        criteria.setCheckCount(true);
	        
	        jsonPaging = this.getJsonPaging(request);
	        
	       /* if(null!=issuedDateSort&& !"".equals(issuedDateSort)){//发布日期排序
	        	orderBy = " overhead desc,l.lastUpdate "+issuedDateSort;
			}else if(null!=viewSort && !"".equals(viewSort)){//点击次数排序
				orderBy = " overhead desc,click "+ viewSort;
			}else if(null!=totalReturnSort && !"".equals(totalReturnSort)){//点击回报率排序
				orderBy = " overhead desc,totalReturn "+ totalReturnSort;
			}else if(null!=riskLevelSort && !"".equals(riskLevelSort)){//点击回报率排序
				orderBy = " overhead desc,l.riskLevel "+ riskLevelSort;
			}
	        criteria.setOrderBy(orderBy);*/
	        
	        jsonPaging = portfolioArenaService.findByUser(jsonPaging, criteria,langCode);
	        List<PortfolioArenaVO> list=new ArrayList<PortfolioArenaVO>();
	        if(null!=jsonPaging && null!=jsonPaging.getList()){
	        	Iterator it=jsonPaging.getList().iterator();
	        	while (it.hasNext()) {
					PortfolioArenaVO vo = (PortfolioArenaVO) it.next();
					String geo=vo.getGeoAllocation();
					if(null!=geo && !"".equals(geo)){
						String[] strings=geo.split(",");
						if(strings.length>2){
							vo.setGeoAllocation(strings[0]+","+strings[1]);
						}
					}
					String sec=vo.getSector();
					if(null!=sec && !"".equals(sec)){
						String[] strings=sec.split(",");
						if(strings.length>2){
							vo.setSector(strings[0]+","+strings[1]);
						}
					}
					list.add(vo);
				}
	        }
	        jsonPaging.setList(list);
		}
		return jsonPaging;
    }
    
    /**
     * 分页获得我的策略Json记录
     * @author michael
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping(value = "/myListJson", method = RequestMethod.POST)
    public void myListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	jsonPaging = findMyList(request, response, model);
    	ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
    }
    
    /**
     * 分页获得Json记录
     * @author michael
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public void listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	jsonPaging = findList(request, response, model);
    	ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));

    }
    
    /**
     * 新增投资组合
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
	    	PortfolioArena obj = new PortfolioArena();
	    	model.put("portfolioVO", obj);
	    	return "portfolio/arena/add";
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
    }
    
    /**
     * 修改投资组合
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
	    	String id = request.getParameter("id");
	    	PortfolioArena obj = portfolioArenaService.findById(id);
	    	model.put("portfolioVO", obj);
	    	
	    	//基金列表
	    	List<FundInfo> fundList = portfolioArenaService.findFundListByPortfolio(id);
	    	String fundIds = "";
	    	if (!fundList.isEmpty())
	    		for (FundInfo x : fundList){
	    			if (null!=x && null!=x.getId() && !"".equals(x.getId()))
	    				fundIds+=x.getId()+",";
	    		}
	    	model.put("fundIds", fundIds);
	    	
	    	
	    	//查看权限
	    	PortfolioWebViewVO webViewVO = portfolioArenaService.findWebViewByPortfolio(id);
	    	if (null==webViewVO) webViewVO = new PortfolioWebViewVO();
	    	model.put("webViewVO", webViewVO);
	    	//推送权限
	    	PortfolioWebPushVO webPushVO = portfolioArenaService.findWebPushByPortfolio(id);
	    	if (null==webPushVO) webPushVO = new PortfolioWebPushVO();
	    	model.put("webPushVO", webPushVO);
	    	
			//客户类型：Buddy， Client， Advisor， Prospect
			List<MemberIndividual> clients = investorService.findClientByIFA(loginUser.getId(),"Client");
			model.put("cnt_clients", null==clients?0:clients.size());

			List<MemberIndividual> prospects = investorService.findClientByIFA(loginUser.getId(),"Prospect");
			model.put("cnt_prospects", null==prospects?0:prospects.size());

			List<MemberIndividual> buddies = investorService.findClientByIFA(loginUser.getId(),"Buddy");
			model.put("cnt_buddies", null==buddies?0:buddies.size());

			MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
			List<MemberIfa> colleagues = investorService.findIfaColleagues(ifa.getMember().getId(), ifa.getIfafirm().getId());
			model.put("cnt_colleagues", null==colleagues?0:colleagues.size());
			
	    	return "portfolio/arena/edit";
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
    
    /**
     * 修改投资组合
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
			String id = request.getParameter("id");
	    	PortfolioArena obj = portfolioArenaService.findById(id);
	    	model.put("portfolioVO", obj);
	    	
	    	//基金列表
	    	List<FundInfo> fundList = portfolioArenaService.findFundListByPortfolio(id);
	    	String fundIds = "";
	    	if (!fundList.isEmpty())
	    		for (FundInfo x : fundList){
	    			if (null!=x && null!=x.getId() && !"".equals(x.getId()))
	    				fundIds+=x.getId()+",";
	    		}
	    	model.put("fundIds", fundIds);
	    	
	    	
	    	//查看权限
	    	PortfolioWebViewVO webViewVO = portfolioArenaService.findWebViewByPortfolio(id);
	    	if (null==webViewVO) webViewVO = new PortfolioWebViewVO();
	    	model.put("webViewVO", webViewVO);
	    	//推送权限
	    	PortfolioWebPushVO webPushVO = portfolioArenaService.findWebPushByPortfolio(id);
	    	if (null==webPushVO) webPushVO = new PortfolioWebPushVO();
	    	model.put("webPushVO", webPushVO);
	    	
			//客户类型：Buddy， Client， Advisor， Prospect
			List<MemberIndividual> clients = investorService.findClientByIFA(loginUser.getId(),"Client");
			model.put("cnt_clients", null==clients?0:clients.size());

			List<MemberIndividual> prospects = investorService.findClientByIFA(loginUser.getId(),"Prospect");
			model.put("cnt_prospects", null==prospects?0:prospects.size());

			List<MemberIndividual> buddies = investorService.findClientByIFA(loginUser.getId(),"Buddy");
			model.put("cnt_buddies", null==buddies?0:buddies.size());

			MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
			List<MemberIfa> colleagues = investorService.findIfaColleagues(ifa.getMember().getId(), ifa.getIfafirm().getId());
			model.put("cnt_colleagues", null==colleagues?0:colleagues.size());
			
	    	return "portfolio/arena/view";
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
    
    /**
     * 保存投资组合
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/savePortfolio", method = RequestMethod.POST)
    public String savePortfolio(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser!=null){
			try {
				//编辑状态时使用
				String id = request.getParameter("id");//portfolio id
				
		    	String fundIds = request.getParameter("ids");
		    	
		    	String portfolioName = toUTF8String(request.getParameter("portfolioName"));
		    	String investmentGoal = toUTF8String(request.getParameter("investmentGoal"));
		    	String suitability = toUTF8String(request.getParameter("suitability"));
		    	String riskLevel = toUTF8String(request.getParameter("riskLevel"));
		    	String reason = toUTF8String(request.getParameter("reason"));
		    	String aipState = request.getParameter("aipState");
		    	
		    	String permit = StrUtils.getString(request.getParameter("permit"));
		    	String permitClients = StrUtils.getString(request.getParameter("permit_clients"));
		    	String permitProspects = StrUtils.getString(request.getParameter("permit_prospects"));
		    	String permitBuddies = StrUtils.getString(request.getParameter("permit_buddies"));
		    	String permitColleagues = StrUtils.getString(request.getParameter("permit_colleagues"));
		    	String permitClient = StrUtils.getString(request.getParameter("permit_client"));
		    	String permitProspect = StrUtils.getString(request.getParameter("permit_prospect"));
		    	String permitBuddy = StrUtils.getString(request.getParameter("permit_buddy"));
		    	String permitColleague = StrUtils.getString(request.getParameter("permit_colleague"));
		    	
		    	String push = StrUtils.getString(request.getParameter("push"));
		    	String pushClients = StrUtils.getString(request.getParameter("push_clients"));
		    	String pushProspects = StrUtils.getString(request.getParameter("push_prospects"));
		    	String pushBuddies = StrUtils.getString(request.getParameter("push_buddies"));
		    	String pushColleagues = StrUtils.getString(request.getParameter("push_colleagues"));
		    	String pushClient = StrUtils.getString(request.getParameter("push_client"));
		    	String pushProspect = StrUtils.getString(request.getParameter("push_prospect"));
		    	String pushBuddy = StrUtils.getString(request.getParameter("push_buddy"));
		    	String pushColleague = StrUtils.getString(request.getParameter("push_colleague"));
		    	
		    	PortfolioArena info = new PortfolioArena();
		    	if (null!=id && !"".equals(id)){
		    		info = portfolioArenaService.findById(id);
		    	}
	    		if (null==info || null==info.getId()){
	    			info = new PortfolioArena();
			    	info.setCreator(loginUser);
			    	info.setCreateTime(new Date());
			    	info.setIsValid("1");
			    	info.setOverhead("0");//默认不置顶
			    	info.setStatus("0");//草稿
	    		}
		    	info.setPortfolioName(portfolioName);
		    	info.setInvestmentGoal(investmentGoal);
		    	info.setSuitability(suitability);
		    	info.setReason(reason);
		    	if (StringUtils.isNotBlank(riskLevel)) {
		    		info.setRiskLevel(Integer.parseInt(riskLevel));
				}
		    	info.setLastUpdate(new Date());
		    	
//		    	info.setIsPublic(isPublic);
//		    	info.setOverheadTime(overheadTime);
		    	info.setDescription(portfolioName);
		    	info.setAipFlag(aipState);
		    	
		    	//保存投资组合信息
		    	info = portfolioArenaService.saveOrUpdate(info);
		    	
		    	//保存投资组合的产品信息    method 1:不带分配比例
		    	try {
		    		List regionList = new ArrayList();
		    		List sectorList = new ArrayList();
			    	if (null!=fundIds && !"".equals(fundIds)){
			            String[] idArrStrings = fundIds.split(",");
			            if (idArrStrings!=null && idArrStrings.length>0){
			            	for(int k=0;k<idArrStrings.length;k++){
			            		String fundId = idArrStrings[k].toString();
			            		if(fundId!=null && fundId.length()>0){
			            			FundInfoEn fundInfo = fundInfoService.findFundInfoEnById(fundId);
			            			
			            			if (!regionList.contains(fundInfo.getGeoAllocationCode()))
			            				regionList.add(fundInfo.getGeoAllocationCode());
			            			if (!sectorList.contains(fundInfo.getSectorTypeCode()))
			            				sectorList.add(fundInfo.getSectorTypeCode());
		
			            			//保存组合与产品信息
//			            			portfolioArenaService.saveOrUpdate(info, fundId);
			            		}
			            	}
			            }
			    	}

			    	//更新补充信息
			    	info.setGeoAllocation(StrUtils.arrayListToString(regionList, ","));
			    	info.setSector(StrUtils.arrayListToString(sectorList, ","));
			    	info = portfolioArenaService.saveOrUpdate(info);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				//保存投资组合的产品信息    method 2:带分配比例                 add wwluo 20160926
				//portfolioProduct值      例：[{"fundid":"e4da3b7fbbce2345d7772b0674a318d5","fundweight":0.2}]
				String portfolioProduct = request.getParameter("portfolioProduct");
				try {
					List regionList = new ArrayList();
					List sectorList = new ArrayList();
					if (StringUtils.isNotBlank(portfolioProduct)){
						//双引号 替换 单引号
						portfolioProduct = portfolioProduct.replaceAll("\'","\"");
						List<Map> products = JsonUtil.toListMap(portfolioProduct);
						for (Map map : products) {
							String fundId = (String) map.get("fundId");
							Integer weight = (Integer) map.get("fundWeight");
							FundInfo fund = fundInfoService.findFundInfoById(fundId);
							FundInfoEn fundInfo = fundInfoService.findFundInfoEnById(fundId);
							if (!regionList.contains(fundInfo.getGeoAllocationCode()))
								regionList.add(fundInfo.getGeoAllocationCode());
							if (!sectorList.contains(fundInfo.getSectorTypeCode()))
								sectorList.add(fundInfo.getSectorTypeCode());
							//保存组合与产品信息
							PortfolioArenaProduct product = new PortfolioArenaProduct();
							product.setPortfolio(info);
							product.setAllocationRate(Double.parseDouble(weight.toString()));
							product.setProduct(fund.getProduct());
							product = portfolioArenaService.saveOrUpdate(product);
						}
					}
						//更新补充信息
						info.setGeoAllocation(StrUtils.arrayListToString(regionList, ","));
						info.setSector(StrUtils.arrayListToString(sectorList, ","));
						info = portfolioArenaService.saveOrUpdate(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//add end
				
				//保存定投信息  add wwluo 20160929
				try {
					if("1".equals(info.getAipFlag())){
						String aipExecCycle = request.getParameter("aipExecCycle");
						String aipTimeDistance = request.getParameter("aipTimeDistance");
						String aipAmount = request.getParameter("aipAmount");
						String aipEndDate = request.getParameter("aipEndDate");
						String aipEndCount = request.getParameter("aipEndCount");
						String aipEndTotalAmount = request.getParameter("aipEndTotalAmount");
						
						PortfolioArenaAip arenaAip = new PortfolioArenaAip();
						
						//arenaAip.setAipState(aipState);
						arenaAip.setAipExecCycle(aipExecCycle);
						if (StringUtils.isNotBlank(aipTimeDistance)) {
							arenaAip.setAipTimeDistance(Integer.parseInt(aipTimeDistance));
						}
						if (StringUtils.isNotBlank(aipAmount)) {
							arenaAip.setAipAmount(Double.parseDouble(aipAmount));
						}
						if (StringUtils.isNotBlank(aipEndDate)) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							arenaAip.setAipEndDate(sdf.parse(aipEndDate));
						}
						if (StringUtils.isNotBlank(aipEndCount)) {
							arenaAip.setAipEndCount(Integer.parseInt(aipEndCount));
						}
						if (StringUtils.isNotBlank(aipEndTotalAmount)) {
							arenaAip.setAipEndTotalAmount(Double.parseDouble(aipEndTotalAmount));
						}
						arenaAip.setPortfolio(info);
						//arenaAip.setIsValid("1");
						arenaAip.setCreateTime(new Date());
						arenaAip.setLastUpdate(new Date());
						portfolioArenaService.saveOrUpdate(arenaAip);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//add end
				
		       	//保存推送权限信息
				try {
		       		WebPush webPush = new WebPush();
		       		PortfolioWebPushVO webPushVO = portfolioArenaService.findWebPushByPortfolio(id);
		       		if (null!=webPushVO && null!=webPushVO.getId() && !"".equals(webPushVO.getId())){
		       			webPush = portfolioArenaService.findWebPushById(webPushVO.getId());
//		       			BeanUtils.copyProperties(webPushVO,webPush);//拷贝信息
		       		}else{//新建
			       		webPush.setModuleType(CommonConstantsWeb.WEB_PUSH_MODULE_PORTFOLIO_ARENA);
			       		webPush.setRelateId(info.getId());
			       		webPush.setCreateTime(new Date());
			       		webPush.setFromMember(loginUser);
		       		}
		       		webPush.setScopeFlag(push);
		       		webPush.setLastUpdate(new Date());
			       	if("2".equals(push)){//setting
			       		//1 - 全选，0 - 无， -1 - 选部分
			       		webPush.setBuddyFlag(pushBuddy);
			       		webPush.setClientFlag(pushClient);
			       		webPush.setColleagueFlag(pushColleague);
			       		webPush.setProspectFlag(pushProspect);
			       	}else if("1".equals(push)){//all
			       		webPush.setBuddyFlag("1");
			       		webPush.setClientFlag("1");
			       		webPush.setColleagueFlag("1");
			       		webPush.setProspectFlag("1");
			       	}else{//only me
			       		webPush.setBuddyFlag("0");
			       		webPush.setClientFlag("0");
			       		webPush.setColleagueFlag("0");
			       		webPush.setProspectFlag("0");
			       	}
			       	webPush = strategyInfoService.saveWebPush(webPush,pushClients,pushProspects,pushBuddies,pushColleagues);
				
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
		       	
		       	//保存查看权限信息
				try{
		       		WebView webView = new WebView();
		       		PortfolioWebViewVO webViewVO = portfolioArenaService.findWebViewByPortfolio(id);
		       		if (null!=webViewVO && null!=webViewVO.getId() && !"".equals(webViewVO.getId())){
		       			webView = portfolioArenaService.findWebViewById(webViewVO.getId());
		       		}else{//新建
			       		//对应模块,insight观点,news新闻,portfolio组合,portfolio_info组合,portfolio_arena组合,live_info直播
			       		webView.setModuleType(CommonConstantsWeb.WEB_VIEW_MODULE_PORTFOLIO_ARENA);
			       		webView.setRelateId(info.getId());
			       		webView.setCreateTime(new Date());
			       		webView.setFromMember(loginUser);
		       		}
		       		webView.setLastUpdate(new Date());
		       		webView.setScopeFlag(permit);
			       	if("2".equals(permit)){//setting
			       		//1 - 全选，0 - 无， -1 - 选部分
			       		webView.setBuddyFlag(permitBuddy);
			       		webView.setClientFlag(permitClient);
			       		webView.setColleagueFlag(permitColleague);
			       		webView.setProspectFlag(permitProspect);
			       	}else if("1".equals(permit)){//all
			       		webView.setBuddyFlag("1");
			       		webView.setClientFlag("1");
			       		webView.setColleagueFlag("1");
			       		webView.setProspectFlag("1");
			       	}else{
			       		webView.setBuddyFlag("0");
			       		webView.setClientFlag("0");
			       		webView.setColleagueFlag("0");
			       		webView.setProspectFlag("0");
			       	}
			       	webView = strategyInfoService.saveWebView(webView,permitClients,permitProspects,permitBuddies,permitColleagues);
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
		    	
				//发布
				try {
			    	String ifPublish = StrUtils.getString(request.getParameter("ifPublish"));
			    	if ("true".equals(ifPublish)){
			    		info.setStatus("1");
			    		portfolioArenaService.saveOrUpdate(info);
			    	}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
		    	result.put("result",true);
		    	result.put("info",info);
		       	result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		       	this.saveOperLog(request,loginUser.getLoginCode(),"",CoreConstants.FRONT_LOG_PORTFOLIO_ARENA, "保存密码成功");
			} catch (Exception e) {
				// TODO: handle exception
				result.put("result",false);
	        	result.put("msg",e.getMessage());
				this.saveOperLog(request,loginUser.getLoginCode(),"",CoreConstants.FRONT_LOG_PORTFOLIO_ARENA, "保存密码失败");
			}
	        
		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("msg","User is not login.");
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(result));

    	return null;
    }
    
    /**
     * 发布投资组合
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/publishPortfolio", method = RequestMethod.POST)
    public String publishPortfolio(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	
    	savePortfolio(request, response, model);
    	
    	return null;
    }
    
    
	/**
	 * 设置/取消【置顶】
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/setOverhead", method = RequestMethod.POST)
	public void setOverhead(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser!=null){
			String id = StrUtils.getString(request.getParameter("itemId"));
			String type = StrUtils.getString(request.getParameter("type"));
			
			//如果是设置【置顶】，那么先清除之前的所有置顶。
			if (null!=type && "1".equals(type))
				portfolioArenaService.clearOverhead(loginUser.getId());
			
			PortfolioArena info = portfolioArenaService.findById(id);
			if (null!=info){
				info.setOverhead(type);
				info.setOverheadTime(new Date());
				portfolioArenaService.saveOrUpdate(info);
			}
			result.put("result",true);
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));

		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		JsonUtil.toWriter(result, response);
    }	
	
	/**
	 * 关注/取消关注组合
	 * @author qgfeng
	 * @param relateId 对应类型id
	 * @param memberId 网站会员ID
	 * @parame opType	Follow-设置关注;Delete-取消关注
	 * @param moduleType 对应模块,fund基金关注,ifa关注,article文章关注,strategy策略
	 * */
	@RequestMapping(value = "/setPortfolioFollow", method = RequestMethod.GET)
	public void  setPortfolioFollow(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberBase loginUser = this.getLoginUser(request);//获取当前登录用户
		if (null!=loginUser){
			String relateId = request.getParameter("relateid");
			String opType = request.getParameter("opType");
			String memberId = loginUser.getId();
			WebFollow follow = webFollowService.saveWebFollowStatus(relateId, memberId, opType, "portfolio_arena");
			
			obj.put("followId", follow==null?"":follow.getId());
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}else{
			obj.put("followId", "");
			obj.put("result",false);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.fail",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 删除组合
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/delPortfolio", method = RequestMethod.POST)
	public void delPortfolio(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser!=null){
			String id = StrUtils.getString(request.getParameter("itemId"));
			
			PortfolioArena info = portfolioArenaService.findById(id);
			
			//modify by wwluo 2016/11/21
			//portfolioArenaService.delete(info);
			portfolioArenaService.delPortfolio(info);
			//modify end
			
			result.put("result",true);
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));

		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		JsonUtil.toWriter(result, response);
    }	
	
//    /**
//     * 选人页面
//     * @author michael
//     * @param request
//     * @param response
//     * @param model
//     * @return
//     */
//    @RequestMapping(value = "/userSelector", method = RequestMethod.GET)
//    public String userSelector(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//		MemberBase loginUser = getLoginUser(request);
////		String name = StrUtils.getString(request.getParameter("name"));
////		model.put("win_name", name);
//		
//		if (loginUser!=null){
//			//客户类型：Buddy， Client， Advisor， Prospect
//			List<MemberIndividual> clients = investorService.findClientByIFA(loginUser.getId(),"Client");
//			model.put("clients", clients);
//
//			List<MemberIndividual> prospects = investorService.findClientByIFA(loginUser.getId(),"Prospect");
//			model.put("prospects", prospects);
//
//			List<MemberIndividual> buddies = investorService.findClientByIFA(loginUser.getId(),"Buddy");
//			model.put("buddies", buddies);
//
//			MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
//			List<MemberIfa> colleagues = investorService.findIfaColleagues(ifa.getMember().getId(), ifa.getIfafirm().getId());
////			List<MemberIfa> colleagues = investorService.findIfaTeamColleagues(ifa.getMember().getId(), ifa.getIfafirm().getId(), );
//			model.put("colleagues", colleagues);
//			
//	        return "portfolio/arena/userSelector";//页面摆放路径
//		}else{//未登录，跳转到登录页面
//			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
//		}
//    }
	
	
	
	/**
	 * 查看投资组合
	 * 
	 * @author mqzou
	 * @data 2016-09-13
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);

		String langCode=getLoginLangFlag(request);
		
		String type=request.getParameter("type");
		if("read".equals(type)){
			model.put("allowFav", false);
		}else {
			model.put("allowFav", true);
		}
		if (loginUser != null) {
			
			String dateFormat=loginUser.getDateFormat();
			if(null==dateFormat || "".equals(dateFormat))
				dateFormat=CommonConstants.FORMAT_DATE;
			model.put("dateFormat", dateFormat);
			
			String id = request.getParameter("id");
			PortfolioArena obj=portfolioArenaService.findById(id);
			PortfolioArenaVO vo=new PortfolioArenaVO(obj);
			vo.setCreatorName(getCommonMemberName(obj.getCreator().getId(), langCode, "2"));
		    String isFollow=webFollowStatusService.getWebFollowStatus(id, loginUser.getId() , "portfolio_arena");
		    String geoAllocation=sysParamService.findNameByCode(obj.getGeoAllocation(), langCode);
		    vo.setGeoAllocation(geoAllocation);
			String sector=sysParamService.findNameByCode(obj.getSector(), langCode);
			vo.setSector(sector);
			
		    model.put("isFollow", isFollow);
		    
			if (loginUser.getId().equals(obj.getCreator().getId())) {
				model.put("myself", "1");
				WebView webView = insightInfoService.getViewByRelateAndModule(id, CommonConstantsWeb.WEB_VIEW_MODULE_PORTFOLIO_ARENA);
				if (webView != null && "3".equals(webView.getScopeFlag())) {
					List<IfaClientVO> prospects = new ArrayList<IfaClientVO>();
					List<IfaClientVO> existings = new ArrayList<IfaClientVO>();
					List<IfaClientVO> buddies = new ArrayList<IfaClientVO>();
					List<IfaClientVO> colleagues = new ArrayList<IfaClientVO>();
					String prospectIds = "";
					String existingIds = "";
					String buddyIds = "";
					String colleagueIds = "";
					List<IfaClientVO> prospectsAll = ifaInfoService.findMyClientList(loginUser.getId(), "0",langCode);
					List<IfaClientVO> existingAll = ifaInfoService.findMyClientList(loginUser.getId(), "1",langCode);
					List<IfaClientVO> buddiesAll = webFriendService.findMyFriendList(loginUser.getId());
					List<IfaClientVO> colleaguesAll = ifaInfoService.findMyColleagueList(loginUser.getId(), langCode);
					List<WebViewDetail> details = insightInfoService.getViewDetailByView(webView.getId());
					if (details != null && !details.isEmpty()) {
						if (prospectsAll != null && !prospectsAll.isEmpty()) {
							for (IfaClientVO prospect : prospectsAll) {
								boolean flag = false;
								String memberId = prospect.getId();
								for (WebViewDetail webViewDetail : details) {
									if (memberId.equals(webViewDetail.getToMember().getId())) {
										flag = true;
										break;
									}
								}
								if (flag && !prospects.contains(prospect)) {
									prospects.add(prospect);
									prospectIds += prospect.getId() + ",";
								}
							}
							if (StringUtils.isNotBlank(prospectIds) && ',' == prospectIds.charAt(prospectIds.length() - 1)) {
								prospectIds = prospectIds.substring(0, prospectIds.length() - 1);
							}
						}
						if (existingAll != null && !existingAll.isEmpty()) {
							for (IfaClientVO existing : existingAll) {
								boolean flag = false;
								String memberId = existing.getId();
								for (WebViewDetail webViewDetail : details) {
									if (memberId.equals(webViewDetail.getToMember().getId())) {
										flag = true;
										break;
									}
								}
								if (flag && !existings.contains(existing)) {
									existings.add(existing);
									existingIds += existing.getId() + ",";
								}
							}
							if (StringUtils.isNotBlank(existingIds) && ',' == existingIds.charAt(existingIds.length() - 1)) {
								existingIds = existingIds.substring(0, existingIds.length() - 1);
							}
						}
						if (buddiesAll != null && !buddiesAll.isEmpty()) {
							for (IfaClientVO buddy : buddiesAll) {
								boolean flag = false;
								String memberId = buddy.getId();
								for (WebViewDetail webViewDetail : details) {
									if (memberId.equals(webViewDetail.getToMember().getId())) {
										flag = true;
										break;
									}
								}
								if (flag && !buddies.contains(buddy)) {
									buddies.add(buddy);
									buddyIds += buddy.getId() + ",";
								}
							}
							if (StringUtils.isNotBlank(buddyIds) && ',' == buddyIds.charAt(buddyIds.length() - 1)) {
								buddyIds = buddyIds.substring(0, buddyIds.length() - 1);
							}
						}
						if (colleaguesAll != null && !colleaguesAll.isEmpty()) {
							for (IfaClientVO colleague : colleaguesAll) {
								boolean flag = false;
								String memberId = colleague.getId();
								for (WebViewDetail webViewDetail : details) {
									if (memberId.equals(webViewDetail.getToMember().getId())) {
										flag = true;
										break;
									}
								}
								if (flag && !colleagues.contains(colleague)) {
									colleagues.add(colleague);
									colleagueIds += colleague.getId() + ",";
								}
							}
							if (StringUtils.isNotBlank(colleagueIds) && ',' == colleagueIds.charAt(colleagueIds.length() - 1)) {
								colleagueIds = colleagueIds.substring(0, colleagueIds.length() - 1);
							}
						}
					}
					model.put("prospectIds", prospectIds);
					model.put("existingIds", existingIds);
					model.put("buddyIds", buddyIds);
					model.put("colleagueIds", colleagueIds);

					model.put("prospects", prospects);
					model.put("existings", existings);
					model.put("buddies", buddies);
					model.put("colleagues", colleagues);
				}
				model.put("webView", webView);

			} else {
				model.put("myself", "0");
			}
		    
		    
		    MemberIfa ifa=memberBaseService.findIfaMember(obj.getCreator().getId());
		   // ifa.getMember().setIconUrl(getUserHeadUrl(ifa.getMember().getIconUrl(), ifa.getGender()));
		    vo.setIfa(ifa);
		    
		    vo.setCreateTime(DateUtil.getDateStr(vo.getCreateTime(), DateUtil.DEFAULT_DATE_TIME_FORMAT, dateFormat));
			model.put("portfolioVO", vo);
			
			//收益盈亏
			/*PortfolioArenaCumperf cumperf=portfolioArenaService.findLastPortfolioArenaCumperf(id);
			if(null!=cumperf ){
				if(null==cumperf.getCumprefRate())
				cumperf.setCumprefRate(0.00);
				else {
					cumperf.setCumprefRate(cumperf.getCumprefRate()*100);
				}
			}
			model.put("cumperf", cumperf);*/

			// 基金列表
			/*List<FundInfo> fundList = portfolioArenaService.findFundListByPortfolio(id);
			String fundIds = "";
			if (!fundList.isEmpty())
				for (FundInfo x : fundList) {
					if (null!=x && null!=x.getId() && !"".equals(x.getId()))
	    				fundIds+=x.getId()+",";
				}*/
			List<PortfolioFundListVO> fundList=portfolioArenaService.findPortfolioFundList(id);
			String fundIds = "";
			if(null!=fundList && !fundList.isEmpty()){
				for (PortfolioFundListVO x : fundList) {
					if (null!=x && null!=x.getFundId() && !"".equals(x.getFundId()))
	    				fundIds+=x.getFundId()+",";
				}
			}
			model.put("fundIds", fundIds);
			model.put("fundList", fundList);
			model.put("fundListJson", JsonUtil.toJson(fundList));

			//model.put("fundListJson", JsonUtil.toJson(fundInfoList));
			
			List sectorList=portfolioArenaService.findPortfolioProductRate("sector_type", id, langCode);
			model.put("sectorList", JsonUtil.toJson(sectorList));
			
			List geoList=portfolioArenaService.findPortfolioProductRate("geo_allocation", id, langCode);
			model.put("geoList", JsonUtil.toJson(geoList));
			
			List typeList=portfolioArenaService.findPortfolioProductRate("fund_type", id, langCode);
			model.put("typeList", JsonUtil.toJson(typeList));
			
			List<PortfolioArenaCumperf> cumperfList=portfolioArenaService.findPortfolioArenaCumperfById(id);
			model.put("cumperfList", JsonUtil.toJson(cumperfList));
			saveVisit(loginUser, "", id, CommonConstantsWeb.WEB_VISIT_BUS_VIEW, CommonConstantsWeb.WEB_VISIT_PORTFOLIO, getRemoteHost(request));
			webInvestorVisitService.checkAndSaveVisit(CommonConstantsWeb.WEB_VISIT_PORTFOLIO, id, loginUser);
			
			
			return "portfolio/info/detail";
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}
	
	/**
	 * 获取组合的收益情况
	 * @author mqzou 2016-11-15
	 */
	@RequestMapping(value = "/getPortfolioCumperf", method = RequestMethod.POST)
	public void getPortfolioCumperf(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String id=request.getParameter("id");
		//获取发布日期条件
        String period = StrUtils.getString(request.getParameter("period"));
        
		/*String startDate = "";//DateUtil.getCurDateStr();
		String endDate = DateUtil.getCurDateStr(Calendar.DATE, 1);//DateUtil.getCurDateStr();
		if(null!=period && !"".equals(period)){
			if ("1W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
			if ("2W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -14);
			if ("1M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
			if ("3M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
			if ("6M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
			if ("1Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
			if ("2Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -2);
			if ("3Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
			if ("5Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
		}else {
			String fromDate = request.getParameter("fromDate");
			if (null != fromDate && fromDate.length() == 10)
				startDate = fromDate;
			String toDate = request.getParameter("toDate");
			if (null != toDate && toDate.length() == 10)
				endDate = toDate;
		}*/
		List<CorePortfolioVO> list=corePortfolioService.getPortfolioReturnRate(id, period, "HKD");
		
		String[] products = portfolioArenaService.getProductWeight(id);
		String fundIds = products[0];
		String weights = products[1];
		CoreFundsReturnForChartsVO chartsData = null;
		if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(weights) && StringUtils.isNotBlank(period)) {
			chartsData = coreFundService.getMoreFundReturnRate(fundIds, weights, period, getLoginLangFlag(request));
		}
		/*//开始日期统一提前一天
        if (!"".equals(startDate))
        	startDate = DateUtil.addDateToString(startDate,Calendar.DATE, -1);
        List<PortfolioArenaCumperf> cumperfList=portfolioArenaService.findPortfolioArenaCumperf(id, startDate, endDate);*/
        JsonUtil.toWriter(chartsData,response);
	}
	
	/**
	 * 收藏/取消收藏投资组合
	 * @author mqzou 2016-11-02
	 */
	@RequestMapping(value = "/pofolioFollow", method = RequestMethod.POST)
	public void pofolioFollow(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser!=null){
			String id = StrUtils.getString(request.getParameter("id"));
			String type = StrUtils.getString(request.getParameter("type"));
			WebFollow follow = webFollowStatusService.saveWebFollowStatus(id, loginUser.getId(), type, "portfolio_arena");
			result.put("result",true);
			result.put("code","global.success.save");
			result.put("msg", follow==null?"":follow.getId());
		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		JsonUtil.toWriter(result, response);
    }		
	
	/**
	 * 创建投资组合step1--选择创建模式页面
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/createPortfolioOne")
	public String createPortfolioOne(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		model.put("member", loginUser);
		return "portfolio/createPortfolio/createPortfolioOne";
	}
	
	/**
	 * 创建投资组合step1--获取我的投资策略
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getMyStrategy")
	public void getMyStrategy(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser != null) {
			String memberId = loginUser.getId();
			List<StrategyInfoVO> strategyInfoVOs = new ArrayList<StrategyInfoVO>();
			List<StrategyInfo> strategyInfos = portfolioArenaService.getMyStrategy(memberId);
			if(null != strategyInfos && !strategyInfos.isEmpty()){
				for (StrategyInfo strategyInfo : strategyInfos) {
					if(!"1".equals(strategyInfo.getStatus())){
						continue;
					}
					StrategyInfoVO strategyInfoVO = new StrategyInfoVO();
					BeanUtils.copyProperties(strategyInfo, strategyInfoVO);
					List<StrategyAllocation> strategyAllocations = portfolioArenaService.getStrategyAllocation(strategyInfo.getId());
					strategyInfoVO.setStrategyAllocations(strategyAllocations);
					strategyInfoVOs.add(strategyInfoVO);
				}
			}
			result.put("flag",true);
			result.put("strategyInfoVOs",strategyInfoVOs);
		} else {// 未登录，跳转到登录页面
			result.put("flag",false);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 创建投资组合step2--组合概览页面
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/createPortfolioTwo")
	public String portfolioOverview(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		String strategyId = request.getParameter("strategyId");
		String portfolioId = request.getParameter("portfolioId");
		String newStrategy = request.getParameter("newStrategy");
		PortfolioArena arena = null;
		StrategyInfo strategy = new StrategyInfo();
		if (StringUtils.isNotBlank(newStrategy) && "1".equals(newStrategy)) {
			arena = portfolioArenaService.getPortfolioArenaById(portfolioId);
			if (StringUtils.isNotBlank(portfolioId)) {
				arena = portfolioArenaService.getPortfolioArenaById(portfolioId);
			}
			if (StringUtils.isNotBlank(strategyId)) {
				strategy = portfolioArenaService.getStrategyInfoById(strategyId);
				if(null != strategy){
					if(arena == null){
						arena = new PortfolioArena();
					}
					arena.setPortfolioName(strategy.getStrategyName());
					arena.setInvestmentGoal(strategy.getInvestmentGoal());
					arena.setSuitability(strategy.getSuitability());
					arena.setReason(strategy.getReason());
					arena.setSector(strategy.getSector());
					arena.setGeoAllocation(strategy.getGeoAllocation());
					arena.setLastUpdate(new Date());
					portfolioArenaService.saveOrUpdate(arena);
				}
			}
		}else if (StringUtils.isNotBlank(portfolioId)) {
			arena = portfolioArenaService.getPortfolioArenaById(portfolioId);
		}else{
			arena = new PortfolioArena();
		}
		//Sector选项
		List<GeneralDataVO> sectorList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_FUND_SECTOR,langCode);
		if(arena!=null){
			model.put("mySector", getMyGeneralList(sectorList, arena.getSector()));
			model.put("notMySector", getNonMyGeneralList(sectorList, arena.getSector()));
		}else{
			model.put("notMySector", getNonMyGeneralList(sectorList, null));
		}
		//Allocation选项
		List<GeneralDataVO> locationList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_REGION, langCode);
		if(arena!=null){
			model.put("myAllocation", getMyGeneralList(locationList,arena.getGeoAllocation()));
			model.put("notMyAllocation", getNonMyGeneralList(locationList, arena.getGeoAllocation()));
		}else{
			model.put("notMyAllocation", getNonMyGeneralList(locationList, null));
		}
		model.put("portfolioArena", arena);
		model.put("strategyId", strategyId);
		model.put("edit", request.getParameter("edit"));
		return "portfolio/createPortfolio/createPortfolioTwo";
	}
	
	/**
	 * 基础数据列表处理
	 *@author wwluo
	 *@date 20161209
	 *@param list 基础数据list
	 *@param codes	实体已选类型code
	 *
	 */	
	private List<GeneralDataVO> getMyGeneralList(List<GeneralDataVO> list, String codes){
		List<GeneralDataVO> result = new ArrayList<GeneralDataVO>();
		if (list!=null && !list.isEmpty() && codes!=null && codes.length()>0){
			List<String> codeArr = Arrays.asList(StrUtils.splitAndTrim(codes,",",""));
			for (GeneralDataVO v: list){
				if (codeArr.contains(v.getItemCode())){
					result.add(v);
				}
			}
		}
		return result;
	}
	
	/**
	 * 基础数据列表处理
	 *@author wwluo
	 *@date 20161209
	 *@param list 基础数据list
	 *@param codes	实体已选类型code
	 *
	 */	
	private List<GeneralDataVO> getNonMyGeneralList(List<GeneralDataVO> list, String codes){
		List<GeneralDataVO> result = new ArrayList<GeneralDataVO>();
		if (list!=null && !list.isEmpty()){
			if (codes!=null && codes.length()>0){
				List<String> codeArr = Arrays.asList(StrUtils.splitAndTrim(codes,",",""));
				for (GeneralDataVO v: list){
					if (!codeArr.contains(v.getItemCode()))
						result.add(v);
				}
			}else{
				result = list;
			}
		}
		return result;
	}
	
	
	/**
	 * 创建投资组合step3--设置组合页面
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/createPortfolioThree")
	public String AllocationMethod(PortfolioArena arena,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			String strategyId = request.getParameter("strategyId");
			String portfolioId = request.getParameter("portfolioId");
			PortfolioArenaAip arenaAip = new PortfolioArenaAip();
			String fundIds = null;
			if (StringUtils.isNotBlank(portfolioId)) {
				arena = portfolioArenaService.getPortfolioArenaById(portfolioId);
				if(arena != null){
					arenaAip = portfolioArenaService.getAipByPortfolioId(arena.getId());
					List<FundInfo> fundInfos = portfolioArenaService.findFundListByPortfolio(portfolioId);
					if(fundInfos != null && !fundInfos.isEmpty()){
						for (FundInfo fundInfo : fundInfos) {
							if(fundInfo!=null && StringUtils.isNotBlank(fundInfo.getId())){
								fundIds = fundIds + fundInfo.getId() + ',';
							}
						}
						if(fundIds.charAt(fundIds.length()-1)==','){
							fundIds = fundIds.substring(0, fundIds.length()-1);
						}
					}
				}
			}
			String defCurrency = loginUser.getDefCurrency();
			if (StringUtils.isBlank(defCurrency)) {
				defCurrency = CommonConstants.DEF_CURRENCY;
			}
			String fundType = "";
			if (StringUtils.isNotBlank(strategyId)) {
				List<StrategyAllocation> strategyAllocations = strategyInfoService.getStrategyAllocation(strategyId, "F");
				if (strategyAllocations != null && !strategyAllocations.isEmpty()) {
					for (StrategyAllocation allocation : strategyAllocations) {
						if (StringUtils.isNotBlank(allocation.getItemCode())) {
							fundType += allocation.getItemCode() + ",";
						}
					}
					if(fundType != ""){
						fundType = fundType.substring(0, fundType.length() - 1);
					}
				}
			}
			model.put("defCurrency",defCurrency );
			model.put("fundIds",fundIds );
			model.put("aip", arenaAip);
			model.put("portfolioArena", arena);
			model.put("strategyId", strategyId);
			model.put("portfolioId", portfolioId);
			model.put("fundType", fundType);
			model.put("edit", request.getParameter("edit"));
			return "portfolio/createPortfolio/createPortfolioThree";
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request)
			+ "front/viewLogin.do";
		}
	}
	
	/**
	 * 创建投资组合step3--获取基金集合
	 * @author wwluo
	 * @data 2016-09-21
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value = "/getFunds")
	public void getFunds(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String langCode = getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		String fundIds = this.toUTF8String(request.getParameter("fundIds"));
        if(StringUtils.isNotBlank(fundIds)){
        	List<Map> fundMaps = JsonUtil.toListMap(fundIds);
        	String toCurrency = request.getParameter("toCurrency");
        	if (StringUtils.isBlank(toCurrency)) {
        		toCurrency = loginUser.getDefCurrency();
        		if(StringUtils.isBlank(toCurrency)){
        			toCurrency = CommonConstants.DEF_CURRENCY;
        		}
			}
        	List<FundInfoDataVO> fundInfoList = fundInfoService.getFundDataByFunds(loginUser, langCode, fundMaps, toCurrency);
	        result.put("flag",true);
			result.put("fundIds",fundIds);
			result.put("fundInfoList",fundInfoList);
        }
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取组合基金
	 * @author wwluo
	 * @data 2016-09-21
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value = "/getFundsByPortfolio")
	public void getFundsByPortfolio(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String langCode = getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser != null) {
			List<Map> fundMaps = null;
			String portfolioId = request.getParameter("portfolioId");
			List<FundInfoDataVO> fundInfoList = null;
			if (StringUtils.isNotBlank(portfolioId)) {
				List<FundInfo> fundInfos = portfolioArenaService.findFundListByPortfolio(portfolioId);
				if(fundInfos != null && !fundInfos.isEmpty()){
					fundMaps = new ArrayList<Map>();
					for (FundInfo fundInfo : fundInfos) {
						Map<String,String> map = new HashMap<String, String>();
						map.put("fund", fundInfo.getId());
						fundMaps.add(map);
					}
				}
				String toCurrency = request.getParameter("toCurrency");
				if (StringUtils.isBlank(toCurrency)) {
					toCurrency = loginUser.getDefCurrency();
					if(StringUtils.isBlank(toCurrency)){
						toCurrency = CommonConstants.DEF_CURRENCY;
					}
				}
				fundInfoList = fundInfoService.getFundDataByFunds(loginUser, langCode, fundMaps, toCurrency);
				fundInfoList = portfolioArenaService.getProductWeight(portfolioId,fundInfoList);
			}
			result.put("flag",true);
			result.put("fundInfoList",fundInfoList);
		} else {// 未登录，跳转到登录页面
			result.put("flag",false);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		JsonUtil.toWriter(result, response);
	}

	/**
	 * 创建投资组合step4--发布组合页面
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/createPortfolioFour")
	public String portfolioRelease(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String langCode = getLoginLangFlag(request);
		if (loginUser != null) {
			String strategyId = request.getParameter("strategyId");
			String portfolioId = request.getParameter("portfolioId");
			PortfolioArena arena = portfolioArenaService.getPortfolioArenaById(portfolioId);
			WebView webView = null;
			if(arena != null){
				webView = insightInfoService.getViewByRelateAndModule(arena.getId(),CommonConstantsWeb.WEB_VIEW_MODULE_PORTFOLIO_ARENA);
				if (webView != null && "3".equals(webView.getScopeFlag())) {
					List<IfaClientVO> prospects = new ArrayList<IfaClientVO>();
					List<IfaClientVO> existings = new ArrayList<IfaClientVO>();
					List<IfaClientVO> buddies = new ArrayList<IfaClientVO>();
					List<IfaClientVO> colleagues = new ArrayList<IfaClientVO>();
					String prospectIds = "";
					String existingIds = "";
					String buddyIds = "";
					String colleagueIds = "";
					List<IfaClientVO> prospectsAll = ifaInfoService.findMyClientList(loginUser.getId(), "0",langCode);
					List<IfaClientVO> existingAll = ifaInfoService.findMyClientList(loginUser.getId(), "1",langCode);
					List<IfaClientVO> buddiesAll = webFriendService.findMyFriendList(loginUser.getId());
					List<IfaClientVO> colleaguesAll = ifaInfoService.findMyColleagueList(loginUser.getId(), langCode);
					List<WebViewDetail> details = insightInfoService.getViewDetailByView(webView.getId());
					if(details != null && !details.isEmpty()){
						if(prospectsAll != null && !prospectsAll.isEmpty()){
							for (IfaClientVO prospect : prospectsAll) {
								boolean flag = false;
								String id = prospect.getId();
								for (WebViewDetail webViewDetail : details) {
									if(id.equals(webViewDetail.getToMember().getId())){
										flag = true;
										break;
									}
								}
								if(flag && !prospects.contains(prospect)){
									prospects.add(prospect);
									prospectIds += prospect.getId() + ",";
								}
							}
							if(StringUtils.isNotBlank(prospectIds) && ',' == prospectIds.charAt(prospectIds.length()-1)){
								prospectIds = prospectIds.substring(0, prospectIds.length()-1);
							}
						}
						if(existingAll != null && !existingAll.isEmpty()){
							for (IfaClientVO existing : existingAll) {
								boolean flag = false;
								String id = existing.getId();
								for (WebViewDetail webViewDetail : details) {
									if(id.equals(webViewDetail.getToMember().getId())){
										flag = true;
										break;
									}
								}
								if(flag && !existings.contains(existing)){
									existings.add(existing);
									existingIds += existing.getId() + ",";
								}
							}
							if(StringUtils.isNotBlank(existingIds) && ',' == existingIds.charAt(existingIds.length()-1)){
								existingIds = existingIds.substring(0, existingIds.length()-1);
							}
						}
						if(buddiesAll != null && !buddiesAll.isEmpty()){
							for (IfaClientVO buddy : buddiesAll) {
								boolean flag = false;
								String id = buddy.getId();
								for (WebViewDetail webViewDetail : details) {
									if(id.equals(webViewDetail.getToMember().getId())){
										flag = true;
										break;
									}
								}
								if(flag && !buddies.contains(buddy)){
									buddies.add(buddy);
									buddyIds += buddy.getId() + ",";
								}
							}
							if(StringUtils.isNotBlank(buddyIds) && ',' == buddyIds.charAt(buddyIds.length()-1)){
								buddyIds = buddyIds.substring(0, buddyIds.length()-1);
							}
						}
						if(colleaguesAll != null && !colleaguesAll.isEmpty()){
							for (IfaClientVO colleague : colleaguesAll) {
								boolean flag = false;
								String id = colleague.getId();
								for (WebViewDetail webViewDetail : details) {
									if(id.equals(webViewDetail.getToMember().getId())){
										flag = true;
										break;
									}
								}
								if(flag && !colleagues.contains(colleague)){
									colleagues.add(colleague);
									colleagueIds += colleague.getId() + ",";
								}
							}
							if(StringUtils.isNotBlank(colleagueIds) && ',' == colleagueIds.charAt(colleagueIds.length()-1)){
								colleagueIds = colleagueIds.substring(0, colleagueIds.length()-1);
							}
						}
					}
					model.put("prospectIds", prospectIds);
					model.put("existingIds", existingIds);
					model.put("buddyIds", buddyIds);
					model.put("colleagueIds", colleagueIds);
					
					model.put("prospects", prospects);
					model.put("existings", existings);
					model.put("buddies", buddies);
					model.put("colleagues", colleagues);
				}
			}
			model.put("strategyId", strategyId);
			model.put("portfolioId", portfolioId);
			model.put("webView", webView);
			model.put("edit", request.getParameter("edit"));
			return "portfolio/createPortfolio/createPortfolioFour";
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request)
			+ "front/viewLogin.do";
		}
	}

	/**
	 * 保存PortfolioArena
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/savePortfolioArena")
	public void savePortfolioArena(PortfolioArena arena,PortfolioArenaAip  portfolioArenaAip,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		boolean flag = false;
		try {
			String portfolioId = request.getParameter("portfolioId");
			String step = request.getParameter("step");
			if ("2".equals(step)){
				String portfolioName = this.toUTF8String(request.getParameter("portfolioName"));
				String investmentGoal = this.toUTF8String(request.getParameter("investmentGoal"));
				String suitability = this.toUTF8String(request.getParameter("suitability"));
				String reason = this.toUTF8String(request.getParameter("reason"));
				String geoAllocation = request.getParameter("geoAllocation");
				String sector = request.getParameter("sector");
				if(StringUtils.isNotBlank(portfolioId)){
					arena = portfolioArenaService.getPortfolioArenaById(portfolioId);
					if(arena == null){
						arena = new PortfolioArena();
					}
					arena.setStatus("0");
					arena.setIsValid("1");
					arena = portfolioArenaService.saveOrUpdate(arena);
				}else{
					arena.setStatus("0");
					arena.setIsValid("1");
					arena.setCreateTime(new Date());
					arena = portfolioArenaService.saveOrUpdate(arena);
				}
				arena.setPortfolioName(portfolioName);
				arena.setInvestmentGoal(investmentGoal);
				arena.setSuitability(suitability);
				arena.setReason(reason);
				arena.setSector(sector);
				arena.setGeoAllocation(geoAllocation);
				arena.setStatus("0");
				arena.setIsValid("1");
				arena.setCreator(loginUser);
				arena.setLastUpdate(new Date());
				arena = portfolioArenaService.saveOrUpdate(arena);
				flag = true;
			}else if ("3".equals(step)){
				if(StringUtils.isNotBlank(portfolioId)){
					arena = portfolioArenaService.getPortfolioArenaById(portfolioId);
					String portfolioName = request.getParameter("portfolioName");
					String riskLevel = request.getParameter("riskLevel");
					arena.setPortfolioName(portfolioName);
					if (StringUtils.isNotBlank(riskLevel)) {
						arena.setRiskLevel(Integer.parseInt(riskLevel));
					}
					arena.setLastUpdate(new Date());
					arena = portfolioArenaService.saveOrUpdate(arena);
					// 删除原产品数据
					portfolioArenaService.delArenaProduct(portfolioId);
					String portfolioProduct = request.getParameter("portfolioProduct");
					portfolioProduct = portfolioProduct.replaceAll("&quot;", "\"");
					if (StringUtils.isNotBlank(portfolioProduct)){
						List<Map> products = JsonUtil.toListMap(portfolioProduct);
						for (Map map : products) {
							String fundId = (String) map.get("fundId");
							String weight = (String) map.get("fundWeight");
							FundInfo fund = fundInfoService.findFundInfoById(fundId);
							//保存组合与产品信息
							PortfolioArenaProduct product = new PortfolioArenaProduct();
							product.setPortfolio(arena);
							if (StringUtils.isNotBlank(weight)) {
								product.setAllocationRate(Double.parseDouble(weight)/100);
							}
							product.setProduct(fund.getProduct());
							product = portfolioArenaService.saveOrUpdate(product);
						}
						arena.setLastUpdate(new Date());
						arena = portfolioArenaService.saveOrUpdate(arena);
					}
					flag = true;
				}
			}else if("4".equals(step) && StringUtils.isNotBlank(portfolioId)){
					arena = portfolioArenaService.getPortfolioArenaById(portfolioId);
					if(arena != null){
						String scopeFlag = request.getParameter("scopeFlag");
						String clientFlag = request.getParameter("clientFlag");
						String prospectFlag = request.getParameter("prospectFlag");
						String buddyFlag = request.getParameter("buddyFlag");
						String colleagueFlag = request.getParameter("colleagueFlag");
						String prospectsDetail = request.getParameter("prospectsDetail");
						String clientsDetail = request.getParameter("clientsDetail");
						String buddiesDetail = request.getParameter("buddiesDetail");
						String colleaguesDetail = request.getParameter("colleaguesDetail");
				       	// 保存查看权限信息
				   		WebView webView = null;
				   		webView = insightInfoService.getViewByRelateAndModule(arena.getId(),CommonConstantsWeb.WEB_VIEW_MODULE_PORTFOLIO_ARENA);
				   		if (null == webView){//新建
				   			webView = new WebView();
				       		webView.setModuleType(CommonConstantsWeb.WEB_VIEW_MODULE_PORTFOLIO_ARENA);
				       		webView.setRelateId(arena.getId());
				       		webView.setCreateTime(new Date());
				       		webView.setFromMember(loginUser);
				   		}
				   		webView.setLastUpdate(new Date());
				   		webView.setScopeFlag(scopeFlag);
				   		webView.setBuddyFlag(buddyFlag);
				   		webView.setClientFlag(clientFlag);
				   		webView.setColleagueFlag(colleagueFlag);
				   		webView.setProspectFlag(prospectFlag);
				   		strategyInfoService.delWebViewDetail(webView.getId());
				       	webView = strategyInfoService.saveWebView(webView,clientsDetail,prospectsDetail,buddiesDetail,colleaguesDetail);
				    	// 发送消息提醒
				       	portfolioArenaTodoRead(arena, loginUser, clientsDetail, langCode);
				       	portfolioArenaTodoRead(arena, loginUser, prospectsDetail, langCode);
				       	portfolioArenaTodoRead(arena, loginUser, buddiesDetail, langCode);
				       	portfolioArenaTodoRead(arena, loginUser, colleaguesDetail, langCode);
				        if("1".equals(scopeFlag)){
				        	arena.setIsPublic("0");
				        }else{
				        	arena.setIsPublic("1");
				        }
				    	arena.setIsValid("1");
				    	arena.setStatus("1");
				    	arena = portfolioArenaService.saveOrUpdate(arena);
				    	flag = true;
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("flag", flag);
		result.put("portfolioId", arena.getId());
		result.put("arena", arena);
		JsonUtil.toWriter(result, response);
	}

	/**
	 * 发送消息提醒
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	private void portfolioArenaTodoRead(PortfolioArena arena,
			MemberBase loginUser, String clientsDetail, String langCode) {
		if (StringUtils.isNotBlank(clientsDetail)) {
			Object[] idArrStrings = null;
			if(clientsDetail.indexOf(",") > -1){
				idArrStrings = clientsDetail.split(",");
			}else{
				idArrStrings = new Object[1];
				idArrStrings[0] = clientsDetail;
			}
			if(idArrStrings != null && idArrStrings.length > 1){
				Set<Object> idSet = new TreeSet<Object>();
				for (int i = 0; i < idArrStrings.length; i++) {
					idSet.add(idArrStrings[i]);
				}
				idArrStrings = idSet.toArray();
			}
		    if (idArrStrings!=null && idArrStrings.length>0){
		    	for(int k=0;k<idArrStrings.length;k++){
		    		String id = idArrStrings[k].toString();
		    		if(id!=null && id.length()>0){
		    			List<Object> params = new ArrayList<Object>();
					 	MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
					 	MemberBase memberBase = memberBaseService.findById(id);
					 	params.add(this.getIfaName(ifa, langCode));
					 	params.add(arena.getPortfolioName());
					 	String titleSc = PropertyUtils.getSmsPropertyValue("portfolio.arena.push",CommonConstants.LANG_CODE_SC,params.toArray());
					 	String titleTc = PropertyUtils.getSmsPropertyValue("portfolio.arena.push",CommonConstants.LANG_CODE_TC,params.toArray());
					 	String titleEn = PropertyUtils.getSmsPropertyValue("portfolio.arena.push",CommonConstants.LANG_CODE_EN,params.toArray());
					 	// 插入 web
		    			WebReadToDo webReadToDo = new WebReadToDo();
					 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_NORMAL);
					 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_IFA_PORTFOLIO_PUBLISH);
					 	webReadToDo.setRelateId(arena.getId());
					 	webReadToDo.setFromMember(loginUser);
					 	webReadToDo.setMsgUrl("/front/portfolio/arena/detail.do");
					 	webReadToDo.setMsgParam("id="+arena.getId());
					 	webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.index.activity.PortfolioDetailActivity");
					 	webReadToDo.setAppParam("portfolioId="+arena.getId());
					 	webReadToDo.setIsApp("1");
					 	webReadToDo.setIsRead("0");
					 	webReadToDo.setOwner(memberBase);
					 	webReadToDo.setCreateTime(new Date());
					 	webReadToDo.setIsValid("1");
					 	webReadToDo.setTitle(titleEn);
					 	webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);
		    		}
		    	}
		    }
		}
	}	
	
	/**
	 * 获取我的投资组合（arena）
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getMyArenas")
	public void getMyArenas(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		jsonPaging = this.getJsonPaging(request);
		String isPublic = request.getParameter("isPublic");
		String status = request.getParameter("status");
		String keyWord = request.getParameter("keyWord");
		jsonPaging = portfolioArenaService.getMyArenas(jsonPaging,loginUser,keyWord,isPublic,status);
		if(!jsonPaging.getList().isEmpty()){
			List<PortfolioArenaVO> portfolioArenaVOs = new ArrayList<PortfolioArenaVO>();
			for (Object object : jsonPaging.getList()) {
				
				PortfolioArena portfolioArena = (PortfolioArena) object;
				PortfolioArenaVO portfolioArenaVO = new PortfolioArenaVO(portfolioArena);
				//BeanUtils.copyProperties(portfolioArena, portfolioArenaVO);
				List<WebInvestorVisit> wedInvestorVisits = strategyInfoService.getWebInvestorVisitByRelateId(portfolioArena.getId());
				if(wedInvestorVisits != null){
					portfolioArenaVO.setVisitCount(wedInvestorVisits.size());
				}
				JsonPaging paging = new JsonPaging();
				paging = strategyInfoService.getCustomerVisitStrategy(paging,portfolioArena.getId(),CommonConstantsWeb.WEB_VISIT_PORTFOLIO,loginUser);
				portfolioArenaVO.setCustomerVisitCount(paging.getTotal());
				portfolioArenaVOs.add(portfolioArenaVO);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(portfolioArenaVOs);
		}
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	/**
	 * 保存定投信息
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveArenaAip")
	public void saveArenaAip(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		boolean flag = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String portfolioId = request.getParameter("portfolioId");
		String currencyCode = request.getParameter("currencyCode");
		String aipExecCycle = request.getParameter("aipExecCycle");
		String aipInitTime = request.getParameter("aipInitTime");
		String chargeDay = request.getParameter("chargeDay");
		String aipAmount = request.getParameter("aipAmount");
		String aipEndType = request.getParameter("aipEndType");
		String aipEndDate = request.getParameter("aipEndDate");
		String aipEndCount = request.getParameter("aipEndCount");
		String aipEndTotalAmount = request.getParameter("aipEndTotalAmount");
		PortfolioArena portfolioArena = null;
		if (StringUtils.isNotBlank(portfolioId)) {
			portfolioArena = portfolioArenaService.findById(portfolioId);
		}
		if(portfolioArena != null){
			Double exChangeRate = null;
			if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(loginUser.getDefCurrency())) {
				WebExchangeRate webExchangeRate = fundInfoService.findExchangeRate(currencyCode, loginUser.getDefCurrency());
				if(webExchangeRate != null){
					exChangeRate = webExchangeRate.getRate();
				}
			}
			if(exChangeRate == null){
				exChangeRate = 1.00;
			}
			PortfolioArenaAip aip = portfolioArenaService.getAipByPortfolioId(portfolioArena.getId());
			if(aip == null){
				aip = new PortfolioArenaAip();
				aip.setPortfolio(portfolioArena);
				aip.setCreateTime(new Date());
				aip.setLastUpdate(new Date());
			}
			try {
				if (StringUtils.isNotBlank(aipInitTime)) {
					aip.setAipInitTime(dateFormat.parse(aipInitTime));
				}else{
					aip.setAipInitTime(null);
				}
				if (StringUtils.isNotBlank(chargeDay)) {
					aip.setAipTimeDistance(Integer.parseInt(chargeDay));
				}else{
					aip.setAipTimeDistance(null);
				}
				if (StringUtils.isNotBlank(aipAmount)) {
					aip.setAipAmount(Double.parseDouble(aipAmount) * exChangeRate);
				}else{
					aip.setAipAmount(null);
				}
				if (StringUtils.isNotBlank(aipEndDate)) {
					aip.setAipEndDate(dateFormat.parse(aipEndDate));
				}else{
					aip.setAipEndDate(null);
				}
				if (StringUtils.isNotBlank(aipEndCount)) {
					aip.setAipEndCount(Integer.parseInt(aipEndCount));
				}else{
					aip.setAipEndCount(null);
				}
				if (StringUtils.isNotBlank(aipEndTotalAmount)) {
					aip.setAipEndTotalAmount(Double.parseDouble(aipEndTotalAmount) * exChangeRate);
				}else{
					aip.setAipEndTotalAmount(null);
				}
				aip.setAipEndType(aipEndType);
				aip.setAipExecCycle(aipExecCycle);
				aip.setLastUpdate(new Date());
			} catch (Exception e) {
				e.printStackTrace();
			}
			aip = portfolioArenaService.saveOrUpdate(aip);
			portfolioArena.setAipFlag("1");
			portfolioArena.setLastUpdate(new Date());
			portfolioArena = portfolioArenaService.saveOrUpdate(portfolioArena);
			flag = true;
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 定投状态控制
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateArenaAipState")
	public void updateArenaAipState(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String portfolioId = request.getParameter("portfolioId");
		String state = request.getParameter("state");
		boolean flag = false;
		if (StringUtils.isNotBlank(portfolioId) && StringUtils.isNotBlank(state)) {
			PortfolioArena arena = portfolioArenaService.getPortfolioArenaById(portfolioId);
			if(arena != null){
				arena.setAipFlag(state);
				arena.setLastUpdate(new Date());
				portfolioArenaService.saveOrUpdate(arena);
				flag = true;
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取Memberbase信息
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getMemberBase")
	public void getMemberBase(PortfolioArenaAip aip,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String ids = request.getParameter("ids");
		boolean flag = false;
		List<MemberBase> members = null;
		if (StringUtils.isNotBlank(ids)) {
			members = portfolioArenaService.getMemberBaseByIds(ids);
			flag = true;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("members", members);
		JsonUtil.toWriter(result, response);
	}
	
	
	
	
}


