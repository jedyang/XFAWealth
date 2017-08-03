package com.fsll.wmes.strategy.action.front;

import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioHoldProductCumperf;
import com.fsll.wmes.entity.ProductType;
import com.fsll.wmes.entity.StrategyAllocation;
import com.fsll.wmes.entity.StrategyCount;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundIncomDetailVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.ifa.vo.IfaClientVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.portfolio.service.PortfolioInfoService;
import com.fsll.wmes.portfolio.vo.AssetsTotalVo;
import com.fsll.wmes.portfolio.vo.AssetsVO;
import com.fsll.wmes.portfolio.vo.HoldChartVO;
import com.fsll.wmes.portfolio.vo.HoldProductVO;
import com.fsll.wmes.portfolio.vo.PortfolioHoldProductCumperfVO;
import com.fsll.wmes.portfolio.vo.PortfolioProductPLVO;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.strategy.vo.CharDataVO;
import com.fsll.wmes.strategy.vo.CriteriaVO;
import com.fsll.wmes.strategy.vo.StrategyAllocMethodVO;
import com.fsll.wmes.strategy.vo.StrategyAllocationTypeVO;
import com.fsll.wmes.strategy.vo.StrategyAllocationVO;
import com.fsll.wmes.strategy.vo.StrategyInfoWebVO;
import com.fsll.wmes.strategy.vo.StrategyWebPushVO;
import com.fsll.wmes.strategy.vo.StrategyWebViewVO;
import com.fsll.wmes.web.service.WebFollowStatusService;
import com.fsll.wmes.web.service.WebFriendService;
import com.fsll.wmes.web.service.WebInvestorVisitService;
import com.fsll.wmes.web.service.WebReadToDoService;

/**
 * 控制器:基金策略管理（主网站）
 * 
 * @author michael
 * @version 1.0.0 Created On: 2016-8-17
 */

@Controller
@RequestMapping("/front/strategy/info")
public class FrontStrategyInfoAct extends WmesBaseAct {
	
	@Autowired
	private StrategyInfoService strategyInfoService;

    @Autowired
    private FundInfoService fundInfoService;
    
    @Autowired
    private WebFollowStatusService webFollowService;
    
	@Autowired
	private InvestorService investorService;
	
	@Autowired
	private MemberBaseService memberBaseService;
	
	/*@Autowired
	private PortfolioInfoService portfolioInfoService;*/
	
	@Autowired
	private PortfolioHoldService portfolioHoldService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private CrmCustomerService crmCustomerService;
	
	@Autowired
	private WebInvestorVisitService webInvestorVisitService;

	@Autowired
	private WebReadToDoService webReadToDoService;
	
	@Autowired
	private IfaInfoService ifaInfoService;
	
	@Autowired
	private WebFriendService webFriendService;
    /**
     * 投资策略分页列表（公共） - 不用登录
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	MemberBase loginUser=getLoginUser(request);
    	String langCode=this.getLoginLangFlag(request);
    	//地区列表
        List<GeneralDataVO> itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, langCode);
        model.put("regionList", itemList);
        //主题分类
        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR,langCode);
        model.put("sectorList", itemList);
        
        //风险等级
        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_STRATEGY_RISK_RATING, this.getLoginLangFlag(request));
        model.put("riskList", itemList);
        
        //搜索时段
        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SEARCH_PERIOD, this.getLoginLangFlag(request));
        model.put("periodList", itemList);
        
        String dateFormat=loginUser.getDateFormat();
        if(null==dateFormat || "".equals(dateFormat))
        	dateFormat=CommonConstants.FORMAT_DATE;
        
        model.put("dateFormat", dateFormat);
        
        return "strategy/info/list";//页面摆放路径
    }
    
    /**
     * 我的投资策略分页列表
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
	        
	        //搜索时段
	        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SEARCH_PERIOD, this.getLoginLangFlag(request));
	        model.put("periodList", itemList);
//	        jsonPaging = findList(request, response, model);
//	        model.put("dataList", jsonPaging.getList());
	        
	        
	        String dateFormat=loginUser.getDateFormat();
	        if(null==dateFormat || "".equals(dateFormat))
	        	dateFormat=CommonConstants.FORMAT_DATE;
	        
	        model.put("dateTimeFormat", dateFormat+" "+CommonConstants.FORMAT_TIME);
	        model.put("dateFormat", dateFormat);
	        
	        return "strategy/info/myList";//页面摆放路径
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
        jsonPaging = findStrategyList(request, model, "list");
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
	        jsonPaging = findStrategyList(request, model, "mylist");
		}
		return jsonPaging;
    }

    /**
     * 获取投资策略列表（公共方法）
     * @author michael
     * modify by mqzou 2016-11-08
     * @param request
     * @param model
     * @param module 调用模块： list，mylist
     * @return
     */
    private JsonPaging findStrategyList(HttpServletRequest request, ModelMap model, String module) {
		MemberBase loginUser = getLoginUser(request);
		CriteriaVO criteria = new CriteriaVO();
		String langCode=getLoginLangFlag(request);
        //获取发布日期条件
        String period = StrUtils.getString(request.getParameter("period"));
		String startDate = "";//DateUtil.getCurDateStr();
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
        	startDate = DateUtil.addDateToString(startDate,Calendar.DATE, -1);
		
        String keyWord = toUTF8String(request.getParameter("keyWord"));
        String sectors = StrUtils.getString(request.getParameter("sectors"));
        String regions = StrUtils.getString(request.getParameter("regions"));
        String source = StrUtils.getString(request.getParameter("source"));
        String riskLevel = StrUtils.getString(request.getParameter("riskLevel"));
        String viewSort = request.getParameter("viewSort");
        String issuedDateSort = request.getParameter("issuedDateSort");
        String checkIsMyFollow = request.getParameter("checkIsMyFollow");
        String status = StrUtils.getString(request.getParameter("status"));
       // if ("".equals(status)) 
        	status = "1";//默认查询已发布的记录
        
        String orderBy = "";
		
        //设置筛选条件
		criteria.setCheckCount(true);
		if ("list".equals(StrUtils.getString(module))){
			criteria.setCheckProduct(true);
		}
        criteria.setModule(module);
    	criteria.setUserId(null==loginUser?"":loginUser.getId());//设置用户ID
        criteria.setPeriod(period);
        criteria.setStartDate(startDate);
        criteria.setEndDate(endDate);
        criteria.setKeyword(keyWord);
        criteria.setSector(sectors);
        criteria.setGeoAllocation(regions);
        criteria.setRiskLevel(riskLevel);
        criteria.setSource(source);
        criteria.setStatus(status);
        criteria.setCheckIsMyFollow("1".equals(checkIsMyFollow)?true:false);
        jsonPaging = this.getJsonPaging(request);
        
        if(null!=issuedDateSort&& !"".equals(issuedDateSort)){//发布日期排序
        	orderBy = " lastUpdate "+issuedDateSort;
			
		}else if(null!=viewSort && !"".equals(viewSort)){//点击次数排序
			orderBy = " click "+ viewSort;
		}
        criteria.setOrderBy(orderBy);
        
        jsonPaging = strategyInfoService.findByUser(jsonPaging, criteria,langCode);

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
     * 分页获得策略Json记录
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
     * 新增投资策略
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
	    	StrategyInfo obj = new StrategyInfo();
	    	model.put("strategyVO", obj);
	    	return "strategy/info/add";
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
    }
    
    /**
	 * 关注/取消关注策略
	 * @author qgfeng
	 * @param relateId 对应类型id
	 * @param memberId 网站会员ID
	 * @parame opType	Follow-设置关注;Delete-取消关注
	 * @param moduleType 对应模块,fund基金关注,ifa关注,article文章关注,strategy策略
	 * */
	@RequestMapping(value = "/setStrategyFollow", method = RequestMethod.GET)
	public void  setStrategyFollow(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberBase loginUser = this.getLoginUser(request);//获取当前登录用户
		if (null!=loginUser){
			String relateId = request.getParameter("relateid");
			String opType = request.getParameter("opType");
			String memberId = loginUser.getId();
			WebFollow follow = webFollowService.saveWebFollowStatus(relateId, memberId, opType, "strategy");
			
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
     * 修改投资策略
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
	    	StrategyInfo obj = strategyInfoService.findById(id);
	    	model.put("strategyVO", obj);
	    	
	    	//基金列表
	    	List<FundInfo> fundList = strategyInfoService.findFundListByStrategy(id);
	    	String fundIds = "";
	    	if (!fundList.isEmpty())
	    		for (FundInfo x : fundList){
	    			if (null!=x && null!=x.getId() && !"".equals(x.getId()))
	    				fundIds+=x.getId()+",";
	    		}
	    	model.put("fundIds", fundIds);
	    	
	    	
	    	//查看权限
	    	StrategyWebViewVO webViewVO = strategyInfoService.findWebViewByStrategy(id);
	    	if (null==webViewVO) webViewVO = new StrategyWebViewVO();
	    	model.put("webViewVO", webViewVO);
	    	//推送权限
	    	StrategyWebPushVO webPushVO = strategyInfoService.findWebPushByStrategy(id);
	    	if (null==webPushVO) webPushVO = new StrategyWebPushVO();
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
			
	    	return "strategy/info/edit";
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
    
    /**
     * 修改投资策略
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
	    	StrategyInfo obj = strategyInfoService.findById(id);
	    	model.put("strategyVO", obj);
	    	
	    	//基金列表
	    	List<FundInfo> fundList = strategyInfoService.findFundListByStrategy(id);
	    	String fundIds = "";
	    	if (!fundList.isEmpty())
	    		for (FundInfo x : fundList){
	    			if (null!=x && null!=x.getId() && !"".equals(x.getId()))
	    				fundIds+=x.getId()+",";
	    		}
	    	model.put("fundIds", fundIds);
	    	
	    	
	    	//查看权限
	    	StrategyWebViewVO webViewVO = strategyInfoService.findWebViewByStrategy(id);
	    	if (null==webViewVO) webViewVO = new StrategyWebViewVO();
	    	model.put("webViewVO", webViewVO);
	    	//推送权限
	    	StrategyWebPushVO webPushVO = strategyInfoService.findWebPushByStrategy(id);
	    	if (null==webPushVO) webPushVO = new StrategyWebPushVO();
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
			
	    	return "strategy/info/view";
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
    
    /**
     * 保存投资策略
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/saveStrategy", method = RequestMethod.POST)
    public String saveStrategy(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser!=null){
			try {
				//编辑状态时使用
				String id = request.getParameter("id");//strategy id
				
		    	String fundIds = request.getParameter("ids");
		    	
		    	String strategyName = toUTF8String(request.getParameter("strategyName"));
		    	String investmentGoal = toUTF8String(request.getParameter("investmentGoal"));
		    	String suitability = toUTF8String(request.getParameter("suitability"));
		    	
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
		    	
		    	StrategyInfo info = new StrategyInfo();
		    	if (null!=id && !"".equals(id)){
		    		info = strategyInfoService.findById(id);
		    	}
	    		if (null==info || null==info.getId()){
	    			info = new StrategyInfo();
			    	info.setCreator(loginUser);
			    	info.setCreateTime(new Date());
			    	info.setIsValid("1");
			    	info.setOverhead("0");//默认不置顶
			    	info.setStatus("0");//草稿
			    	info.setClick(0);
	    		}
		    	info.setStrategyName(strategyName);
		    	info.setInvestmentGoal(investmentGoal);
		    	info.setSuitability(suitability);
		    	info.setLastUpdate(new Date());
		    	
	//	    	info.setIsPublic(isPublic);
	//	    	info.setOverheadTime(overheadTime);
		    	info.setDescription(strategyName);
		    	
		    	//保存投资策略信息
		    	info = strategyInfoService.saveOrUpdate(info);
		    	
		    	//保存投资策略的产品信息
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
		
			            			//保存策略与产品信息
			            			strategyInfoService.saveOrUpdateStrategyProduct(info, fundId);
			            		}
			            	}
			            }
			    	}

			    	//更新补充信息
			    	info.setGeoAllocation(StrUtils.arrayListToString(regionList, ","));
			    	info.setSector(StrUtils.arrayListToString(sectorList, ","));
			    	info = strategyInfoService.saveOrUpdate(info);
				} catch (Exception e) {
					// TODO: handle exception
				}
		       	
		       	//保存推送权限信息
				try {
		       		WebPush webPush = new WebPush();
		       		StrategyWebPushVO webPushVO = strategyInfoService.findWebPushByStrategy(id);
		       		if (null!=webPushVO && null!=webPushVO.getId() && !"".equals(webPushVO.getId())){
		       			webPush = strategyInfoService.findWebPushById(webPushVO.getId());
//		       			BeanUtils.copyProperties(webPushVO,webPush);//拷贝信息
		       		}else{//新建
			       		webPush.setModuleType(CommonConstantsWeb.WEB_PUSH_MODULE_STRATEGY);
			       		webPush.setRelateId(info.getId());
			       		webPush.setCreateTime(new Date());
			       		webPush.setFromMember(loginUser);
		       		}
		       		webPush.setScopeFlag(push);
		       		webPush.setLastUpdate(new Date());
			       	if(null!=push && "2".equals(push)){//setting
			       		//1 - 全选，0 - 无， -1 - 选部分
			       		webPush.setBuddyFlag(pushBuddy);
			       		webPush.setClientFlag(pushClient);
			       		webPush.setColleagueFlag(pushColleague);
			       		webPush.setProspectFlag(pushProspect);
			       	}else if("1".equals(permit)){//all
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
		       		StrategyWebViewVO webViewVO = strategyInfoService.findWebViewByStrategy(id);
		       		if (null!=webViewVO && null!=webViewVO.getId() && !"".equals(webViewVO.getId())){
		       			webView = strategyInfoService.findWebViewById(webViewVO.getId());
//		       			BeanUtils.copyProperties(webViewVO,webView);//拷贝信息
		       		}else{//新建
			       		//对应模块,insight观点,news新闻,strategy策略,portfolio_info组合,portfolio_arena组合,live_info直播
			       		webView.setModuleType(CommonConstantsWeb.WEB_VIEW_MODULE_STRATEGY);
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
			    		strategyInfoService.saveOrUpdate(info);
			    	}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
		    	result.put("result",true);
		       	result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		       	this.saveOperLog(request,loginUser.getLoginCode(),"",CoreConstants.FRONT_LOG_STRATEGY_INFO, "保存策略成功");
			} catch (Exception e) {
				// TODO: handle exception
				result.put("result",false);
	        	result.put("msg",e.getMessage());
				this.saveOperLog(request,loginUser.getLoginCode(),"",CoreConstants.FRONT_LOG_STRATEGY_INFO, "保存策略失败");
			}
	        
		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("msg","User is not login.");
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(result));

    	return null;
    }
    
    /**
     * 发布投资策略
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/publishStrategy", method = RequestMethod.POST)
    public String publishStrategy(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	
    	saveStrategy(request, response, model);
    	
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
				strategyInfoService.clearOverhead(loginUser.getId());
			
			StrategyInfo info = strategyInfoService.findById(id);
			if (null!=info){
				info.setOverhead(type);
				info.setOverheadTime(new Date());
				strategyInfoService.saveOrUpdate(info);
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
	 * 删除策略
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/delStrategy", method = RequestMethod.POST)
	public void delStrategy(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser!=null){
			String id = StrUtils.getString(request.getParameter("itemId"));
			
			StrategyInfo info = strategyInfoService.findById(id);
			
			// modify by wwluo 2016/11/21
			//strategyInfoService.delete(info);
			strategyInfoService.delStrategy(info);
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
	
    /**
     * @author tejay zhu
     * 投资策略列表页面
     */
    @RequestMapping(value = "/publicInvestmentStrategy", method = RequestMethod.GET)
    public String publicInvestmentStrategy(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
			this.isMobileDevice(request, model);
			
			try {
		        //地区列表
		        List<GeneralDataVO> itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, this.getLoginLangFlag(request));
		        model.put("regionList", itemList);
		        
		        //主题分类
		        List<GeneralDataVO> sectorList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR, this.getLoginLangFlag(request));
		        model.put("sectorList", sectorList);
		        
		        //获取发布日期条件
		        String period = StrUtils.getString(request.getParameter("period"));
				String startDate = "";//DateUtil.getCurDateStr();
				String endDate = DateUtil.getCurDateStr();
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
		        	startDate = DateUtil.addDateToString(startDate,Calendar.DATE, -1);
				
		        String keyWord = toUTF8String(request.getParameter("keyWord"));
		        String sectors = toUTF8String(request.getParameter("sectors"));
		        String regions = toUTF8String(request.getParameter("regions"));
		
		        jsonPaging = this.getJsonPaging(request);
		        jsonPaging = strategyInfoService.findAll(jsonPaging, keyWord, sectors, regions, startDate, endDate);
		        List<StrategyInfo> strategyInfoList = jsonPaging.getList();
		        List<StrategyInfoWebVO> list = new ArrayList<StrategyInfoWebVO>();
		        for (int i = 0; i < strategyInfoList.size(); i++) {
		        	StrategyInfoWebVO vo = new StrategyInfoWebVO( strategyInfoList.get(i) );
		        	vo.setIsFollow( webFollowService.getWebFollowStatus(vo.getId(), loginUser.getId() , "strategy") );
		        	list.add(vo);
				}
		        jsonPaging.setList(list);
		        model.put("dataList", jsonPaging.getList());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return "strategy/public/strategies";//页面摆放路径
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
    }
    
    
	/**
	 * @author tejay zhu
	 * @tips：投资策略详细页面
	 */
	@RequestMapping(value = "/strategiesdetail", method = RequestMethod.GET)
	public String strategiesDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		String langCode=this.getLoginLangFlag(request);
		
		String type=request.getParameter("type");
		if("read".equals(type)){
			model.put("allowFav", true);
		}else{
			model.put("allowFav", false);
		}
		if (loginUser!=null){
			String dateFormat=loginUser.getDateFormat();
			if(null==dateFormat || "".equals(dateFormat))
				dateFormat=CommonConstants.FORMAT_DATE;
			model.put("dateFormat", dateFormat);
			
			String id = StrUtils.getString(request.getParameter("id"));
			StrategyInfoWebVO strategyInfoWebVO=strategyInfoService.findStrategyDetail(id, langCode, loginUser.getId(), dateFormat);
			/*StrategyInfo strategyInfo = strategyInfoService.findById(id);
			StrategyInfoWebVO strategyInfoWebVO = new StrategyInfoWebVO(strategyInfo);
			strategyInfoWebVO.setLastUpdate(DateUtil.dateToDateString(DateUtil.StringToDate(strategyInfoWebVO.getLastUpdate(), DateUtil.DEFAULT_DATE_TIME_FORMAT), dateFormat));
			strategyInfoWebVO.setIsFollow( webFollowService.getWebFollowStatus(strategyInfoWebVO.getId(), loginUser.getId() , "strategy") );
			String geo=strategyInfoService.getStrategyAllocationItems(id, CommonConstantsWeb.WEB_ALLOCATION_METHOD_GEOGRAPHICAL, 2);
			String geoAllocation=sysParamService.findNameByCode(geo, langCode);
			strategyInfoWebVO.setGeoAllocation(geoAllocation);
			String set=strategyInfoService.getStrategyAllocationItems(id, CommonConstantsWeb.WEB_ALLOCATION_METHOD_SECTOR, 2);
			String sector=sysParamService.findNameByCode(set, langCode);
			strategyInfoWebVO.setSector(sector);
			
            
            MemberIfa ifa=memberBaseService.findIfaMember(strategyInfoWebVO.getCreator().getId());
            strategyInfoWebVO.setIfa(ifa);*/
			model.put("strategyInfo", strategyInfoWebVO);
			
			//如果当前登录人是策略的创建人
			if(loginUser.getId().equals(strategyInfoWebVO.getCreator().getId())){
				//查看权限
		    	StrategyWebViewVO webViewVO = strategyInfoService.findWebViewByStrategy(id);
		    	if (null==webViewVO) webViewVO = new StrategyWebViewVO();
		    	model.put("webViewVO", webViewVO);
		    	model.put("myself", "1");
			}
			
	    	
			
			List<StrategyAllocationTypeVO> typeList=strategyInfoService.findStrategyAllocationType(id);
			model.put("typeList", typeList);
			
			if(null!=loginUser.getDateFormat() && !"".equals(loginUser.getDateFormat()))
			model.put("df", loginUser.getDateFormat());
			List<StrategyAllocationVO> allocationList=strategyInfoService.findStrategyAllocationData(id, langCode,CommonConstantsWeb.WEB_ALLOCATION_METHOD_GEOGRAPHICAL);
			double total=0;
			if(null!=allocationList){
				Iterator<StrategyAllocationVO> it=allocationList.iterator();
				while (it.hasNext()) {
					StrategyAllocationVO vo = (StrategyAllocationVO) it.next();
					double weight=null!=vo.getItemWeight()?Double.valueOf(vo.getItemWeight()):0;
					total+=weight;
				}
			}
			model.put("allocationTotal", total);
			model.put("allocationList", allocationList);
			model.put("allocationJson", JsonUtil.toJson(allocationList));
			

			List<StrategyAllocationVO> sectorList=strategyInfoService.findStrategyAllocationData(id, langCode,CommonConstantsWeb.WEB_ALLOCATION_METHOD_SECTOR);
			 total=0;
				if(null!=allocationList){
					Iterator<StrategyAllocationVO> it=sectorList.iterator();
					while (it.hasNext()) {
						StrategyAllocationVO vo = (StrategyAllocationVO) it.next();
						double weight=null!=vo.getItemWeight()?Double.valueOf(vo.getItemWeight()):0;
						total+=weight;
					}
				}
		    model.put("sectorTotal", total);
			model.put("sectorList", sectorList);
			model.put("sectorListJson", JsonUtil.toJson(sectorList));
			
		/*	List<StrategyProduct> strategyProductList = strategyInfoService.findStrategyProductByStrategyId(id);
			List<ProductInfo> productInfoList = strategyInfoService.findProductInfoList(strategyProductList);*/
			//基金列表
	    	List<String> productInfoList = strategyInfoService.findFundIdByStrategy(id);
	    	String fundIds = "";
	    	if (!productInfoList.isEmpty())
	    		for (String x : productInfoList){
	    			if (null!=x && !"".equals(x))
	    				fundIds+=x+",";
	    		}
	    	model.put("fundIds", fundIds);
			model.put("productInfoList", productInfoList);
			saveVisit(loginUser, "", id, CommonConstantsWeb.WEB_VISIT_BUS_VIEW, CommonConstantsWeb.WEB_VISIT_STRATEGY,getRemoteHost(request));
			if(!strategyInfoWebVO.getCreator().getId().equals(loginUser.getId())){
				webInvestorVisitService.checkAndSaveVisit(CommonConstantsWeb.WEB_VISIT_STRATEGY, id, loginUser);	
			}
			
			
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		
		return "strategy/public/strategiesdetail";
	}
	
	/**
	 * @author mqzou
	 * @date 2016-11-08
	 * @tips：投资策略分配类型
	 */
	@RequestMapping(value="/strategyAllocationMethodType")
	public void strategyAllocationMethodType(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String id=request.getParameter("id");
		String type=request.getParameter("type");
		List<StrategyAllocMethodVO> list=strategyInfoService.findAllocMethod(id, type);
		//ResponseUtils.renderHtml(response,JsonUtil.toJson(list));
		JsonUtil.toWriter(list, response);
	}
	
	/**
	 * @author mqzou
	 * @date 2016-09-08
	 * @tips：投资策略分配比例
	 */
	@RequestMapping(value = "/strategyAllocation", method = RequestMethod.POST)
	public void strategyAllocation(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String id=request.getParameter("id");
		String type=request.getParameter("type");
		String methodType=request.getParameter("methodType");
		String langCode=this.getLoginLangFlag(request);
		List<StrategyAllocationVO> list=strategyInfoService.findStrategyAllocation(id, langCode, methodType, type);
		//ResponseUtils.renderHtml(response,JsonUtil.toJson(list));
		JsonUtil.toWriter(list, response);
	}
	
	/**
	 * @author mqzou
	 * @date 2016-09-12
	 * @tips：投资策略的基金列表
	 */
	@RequestMapping(value = "/strategyFound", method = RequestMethod.POST)
	public void strategyFound(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
	}
	
	/**
	 * @author tejay zhu
	 */
	@RequestMapping(value = "/strategyFollow", method = RequestMethod.POST)
	public void strategyFollow(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser!=null){
			String id = StrUtils.getString(request.getParameter("id"));
			String type = StrUtils.getString(request.getParameter("type"));
			WebFollow follow = webFollowService.saveWebFollowStatus(id, loginUser.getId(), type, "strategy");
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
     * 选人页面
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/userSelector", method = RequestMethod.GET)
    public String userSelector(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
//		String name = StrUtils.getString(request.getParameter("name"));
//		model.put("win_name", name);
		
		if (loginUser!=null){
			//客户类型：Buddy， Client， Advisor， Prospect
			List<MemberIndividual> clients = investorService.findClientByIFA(loginUser.getId(),"Client");
			List<IfaClientVO> clientList=getClients(clients, loginUser.getId());
			model.put("clients", clientList);

			List<MemberIndividual> prospects = investorService.findClientByIFA(loginUser.getId(),"Prospect");
			clientList=getClients(prospects, loginUser.getId());
			model.put("prospects", clientList);

			List<MemberIndividual> buddies = investorService.findClientByIFA(loginUser.getId(),"Buddy");
			clientList=getClients(buddies, loginUser.getId());
			model.put("buddies", clientList);

			MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
			List<MemberIfa> colleagues = investorService.findIfaColleagues(ifa.getMember().getId(), ifa.getIfafirm().getId());
			clientList=new ArrayList<IfaClientVO>();
			if(null!=colleagues){
	    		Iterator<MemberIfa> it=colleagues.iterator();
	    		while (it.hasNext()) {
	    			MemberIfa memberIndividual = (MemberIfa) it.next();
					IfaClientVO vo=new IfaClientVO();
					vo.setFirstName(memberIndividual.getFirstName());
					vo.setIconUrl(memberIndividual.getMember().getIconUrl());
					vo.setId(memberIndividual.getMember().getId());
					vo.setLastName(memberIndividual.getLastName());
					boolean isImportant=crmCustomerService.checkIfImportantCrm(loginUser.getId(), memberIndividual.getMember().getId());
					if(isImportant){
						vo.setIsImportant(1);
					}else {
						vo.setIsImportant(0);
					}
					clientList.add(vo);
				}
	    	}
//			List<MemberIfa> colleagues = investorService.findIfaTeamColleagues(ifa.getMember().getId(), ifa.getIfafirm().getId(), );
			model.put("colleagues", clientList);
			return "strategy/info/selectNameList";
	        //return "strategy/info/userSelector";//页面摆放路径
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
    }
    
    /**
     * 获取客户列表
     * @author mqzou
     * @date 2016-11-21
     * @param memberList
     * @return
     */
    private List<IfaClientVO> getClients(List<MemberIndividual> memberList,String curMemberId){
    	List<IfaClientVO> list=new ArrayList<IfaClientVO>();
    	if(null!=memberList){
    		Iterator<MemberIndividual> it=memberList.iterator();
    		while (it.hasNext()) {
				MemberIndividual memberIndividual = (MemberIndividual) it.next();
				IfaClientVO vo=new IfaClientVO();
				vo.setFirstName(memberIndividual.getFirstName());
				vo.setIconUrl(memberIndividual.getMember().getIconUrl());
				vo.setId(memberIndividual.getMember().getId());
				vo.setLastName(memberIndividual.getLastName());
				boolean isImportant=crmCustomerService.checkIfImportantCrm(curMemberId, memberIndividual.getMember().getId());
				if(isImportant){
					vo.setIsImportant(1);
				}else {
					vo.setIsImportant(0);
				}
				list.add(vo);
			}
    	}
    	return list;
    }
    
    
    /**
     * 新增投资策略 - Step 1
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/addStart", method = RequestMethod.GET)
    public String addStart(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		if (loginUser!=null){
			String id = request.getParameter("id");
			StrategyInfo obj =new StrategyInfo();
			if(null!=id && !"".equals(id)){
				String isNew=request.getParameter("new");
				 model.put("isNew", isNew);
				 obj = strategyInfoService.findById(id);
				 List<StrategyAllocationVO> allocList=strategyInfoService.findStrategyAllocation(id,langCode, CommonConstantsWeb.WEB_ALLOCATION_METHOD_PRODUCT,null);
				 
				/* if(null!=allocList && !allocList.isEmpty()){
					 Iterator it=allocList.iterator();
					 while (it.hasNext()) {
						StrategyAllocationVO vo = (StrategyAllocationVO) it.next();
						if("fund".equals(vo.getItemCode())){
							vo.setItemName("Funds");
						}else if ("stock".equals(vo.getItemCode())) {
							vo.setItemName("Stocks");
						}else if ("bond".equals(vo.getItemCode())) {
							vo.setItemName("Bonds");
						}
					}
				 }*/
				 model.put("allocation", allocList);
				 
			}else{
				 model.put("isNew", "1");
			}
	    	if (null==obj || null==obj.getId()) obj=new StrategyInfo();
	    	model.put("strategyVO", obj);
	    	
	    	List<GeneralDataVO> riskRatingTypes = findSysParameters("strategy_risk_rating",this.getLoginLangFlag(request));
	    	model.put("riskRatingTypes", riskRatingTypes);
	    	
	    	List<GeneralDataVO> productAllocation = findSysParameters("product_allocation",this.getLoginLangFlag(request));
	    //	model.put("productTypes", productAllocation);
	    	
	    	List<ProductType> productTypeList=portfolioHoldService.findProductType();
	    	model.put("productTypes", productTypeList);
	    	

	        //获取分配设置
	        List<StrategyAllocation> allocations = strategyInfoService.findStrategyAllocation(obj.getId(), null, CommonConstantsWeb.WEB_ALLOCATION_METHOD_PRODUCT);
	        setAllocationNamesByValue(allocations,productAllocation);
	        model.put("productAllocation", allocations);
	        
	    	return "strategy/info/addStart";
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
    }
    
    @RequestMapping(value = "/memberComment", method = RequestMethod.GET)
    public String memberComment(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		//String langCode=getLoginLangFlag(request);
		if (loginUser!=null){
	    	return "strategy/info/memberComment";
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
    }
    /**
     * 保存投资策略 - Step 1
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/saveStart", method = RequestMethod.POST)
    public String saveStart(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser!=null){
			try {
				//编辑状态时使用
				String id = request.getParameter("id");//strategy id
				
		    	String strategyName = toUTF8String(StringEscapeUtils.unescapeHtml(request.getParameter("strategyName").replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
		    	String investmentGoal = toUTF8String(StringEscapeUtils.unescapeHtml(request.getParameter("investmentGoal").replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
		    	String suitability = toUTF8String(StringEscapeUtils.unescapeHtml(request.getParameter("suitability").replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
		    	String reason = toUTF8String(StringEscapeUtils.unescapeHtml(request.getParameter("reason").replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
		    	String riskLevel = StrUtils.getString(request.getParameter("riskLevel"));
		    	
		    	String prdAllocations = StrUtils.getString(request.getParameter("prdAllocations"));
		    	String prdAllocationsWeight = StrUtils.getString(request.getParameter("prdAllocationsWeight"));
		    	
		    	
		    	StrategyInfo info = new StrategyInfo();
		    	if (null!=id && !"".equals(id)){
		    		info = strategyInfoService.findById(id);
		    	}
	    		if (null==info || null==info.getId()){
	    			info = new StrategyInfo();
			    	info.setCreator(loginUser);
			    	info.setCreateTime(new Date());
			    	info.setIsValid("1");
			    	info.setOverhead("0");//默认不置顶
			    	info.setStatus("0");//草稿
			    	info.setClick(0);
	    		}
		    	info.setStrategyName(strategyName);
		    	info.setInvestmentGoal(investmentGoal);
		    	info.setSuitability(suitability);
		    	info.setReason(reason);
		    	info.setRiskLevel(riskLevel);
		    	info.setLastUpdate(new Date());
		    	
	//	    	info.setIsPublic(isPublic);
	//	    	info.setOverheadTime(overheadTime);
		    	info.setDescription(strategyName);
		    	
		    	//保存投资策略信息
		    	info = strategyInfoService.saveOrUpdate(info);
		    	
		    	//保存策略分配信息(分配置类型,fund,stock,bond)
		    	strategyInfoService.deleteStrategyAllocationByType(info.getId(), null);//methodType='P'
		    	strategyInfoService.saveStrategyAllocation(info, CommonConstantsWeb.WEB_ALLOCATION_METHOD_PRODUCT, "1", prdAllocations, prdAllocationsWeight);
		    	
		    	result.put("strategyId",info.getId());
		    	result.put("result",true);
		       	result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		       	this.saveOperLog(request,loginUser.getLoginCode(),"",CoreConstants.FRONT_LOG_STRATEGY_INFO, "保存策略成功");
			} catch (Exception e) {
				// TODO: handle exception
				result.put("result",false);
	        	result.put("msg",e.getMessage());
				this.saveOperLog(request,loginUser.getLoginCode(),"",CoreConstants.FRONT_LOG_STRATEGY_INFO, "保存策略失败");
			}
	        
		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("msg","User is not login.");
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(result));

    	return null;
    }
    
    /**
     * 新增投资策略 - Step 2
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/addAllocation", method = RequestMethod.GET)
    public String addAllocation(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
			String isNew=request.getParameter("new");
			model.put("isNew", isNew);
	    	String id = request.getParameter("id");
	    	StrategyInfo obj = strategyInfoService.findById(id);
	    	if (null==obj || null==obj.getId() || "".equals(obj.getId()))
	    		obj = new StrategyInfo();
	    	model.put("strategyVO", obj);
	    	
	    	//地区列表
	        List<GeneralDataVO> itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, this.getLoginLangFlag(request));
	        model.put("regionList", itemList);
	        
	        //主题分类
	        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR, this.getLoginLangFlag(request));
	        model.put("sectorList", itemList);

	        //基金类型
	        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_FUNDTYPE, this.getLoginLangFlag(request));
	        model.put("fundTypeList", itemList);

	        //获取分配设置
	        List<StrategyAllocation> distinctAllocation = new ArrayList<StrategyAllocation>();
	        List<StrategyAllocation> allocations = strategyInfoService.findStrategyAllocation(obj.getId(), null, CommonConstantsWeb.WEB_ALLOCATION_TYPE_GEOGRAPHICAL);
	        allocations = setAllocationNames(allocations,(List<GeneralDataVO>)model.get("regionList"));
	        model.put("regionAllocation", allocations);
	        if (!allocations.isEmpty()) distinctAllocation.add(allocations.get(0));
	        
	        allocations = strategyInfoService.findStrategyAllocation(obj.getId(), null, CommonConstantsWeb.WEB_ALLOCATION_TYPE_SECTOR);
	        allocations = setAllocationNames(allocations,(List<GeneralDataVO>)model.get("sectorList"));
	        model.put("sectorAllocation", allocations);
	        if (!allocations.isEmpty()) distinctAllocation.add(allocations.get(0));
	        
	        allocations = strategyInfoService.findStrategyAllocation(obj.getId(), null, CommonConstantsWeb.WEB_ALLOCATION_TYPE_FUNDTYPE);
	        allocations = setAllocationNames(allocations,(List<GeneralDataVO>)model.get("fundTypeList"));
	        model.put("typeAllocation", allocations);
	        if (!allocations.isEmpty()) distinctAllocation.add(allocations.get(0));
	        
	        model.put("distinctAllocation", distinctAllocation);
	        
	        //获取产品
//	        List<ProductInfo> prods = strategyInfoService.findProductInfoList(id, "fund");
	        List<FundInfo> prods = strategyInfoService.findFundListByStrategy(id);
	        String fundIds = "";
	        if (null!=prods && !prods.isEmpty()){
	        	for (FundInfo x: prods){
	        		fundIds += x.getId()+",";
	        	}
	        }
	        model.put("fundIds", fundIds);
	        
	    	return "strategy/info/addAllocation";
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
    }
    
    /**
     * 获取选项列表数据 - Step 2
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getSelection")
    public void getSelection(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String method = StrUtils.getString(request.getParameter("method"));
//    	String selectionName = StrUtils.getString(request.getParameter("selectionName"));
    	
    	//地区列表
        List<GeneralDataVO> itemList = new ArrayList<GeneralDataVO>();
        if (CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL.equals(method))
        	itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, this.getLoginLangFlag(request));
        
        //主题分类
        if (CommonConstantsWeb.SYS_PARM_TYPE_SECTOR.equals(method))
        	itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR, this.getLoginLangFlag(request));

        //基金类型
        if (CommonConstantsWeb.SYS_PARM_TYPE_FUNDTYPE.equals(method))
        	itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_FUNDTYPE, this.getLoginLangFlag(request));

//    	StringBuffer selectObj = new StringBuffer();
//    	selectObj.append("<select id=\""+selectionName+"\" name=\""+selectionName+"\" class=\"insight-input-select\" style=\"float: left;\">");
//    	selectObj.append("\n    <option value=\"\">=== Select Geographic Item ===</option>");
//    	
//    	if (null!=itemList && !itemList.isEmpty())
//    		for (GeneralDataVO x:itemList){
//    			selectObj.append("\n    <option value=\""+x.getItemCode()+"\">"+x.getName()+"</option>");
//    		}
//    	selectObj.append("\n</select>");
//    	
//    	ResponseUtils.renderHtml(response,JsonUtil.toJson(selectObj.toString()));
        ResponseUtils.renderHtml(response,JsonUtil.toJson(itemList));
    }
    
    /**
     * 获取已选数据 - Step 2
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getSelectedData")
    public void getSelectedData(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String method = StrUtils.getString(request.getParameter("method"));
    	String strategyId = StrUtils.getString(request.getParameter("strategyId"));
    	
    	//地区列表
        List<StrategyAllocation> allocations = new ArrayList<StrategyAllocation>();
        if (CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL.equals(method)){
	        allocations = strategyInfoService.findStrategyAllocation(strategyId, null, CommonConstantsWeb.WEB_ALLOCATION_TYPE_GEOGRAPHICAL);
	        allocations = setAllocationNames(allocations,findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, this.getLoginLangFlag(request)));
        }
        
        //主题分类
        if (CommonConstantsWeb.SYS_PARM_TYPE_SECTOR.equals(method)){
	        allocations = strategyInfoService.findStrategyAllocation(strategyId, null, CommonConstantsWeb.WEB_ALLOCATION_TYPE_SECTOR);
	        allocations = setAllocationNames(allocations,findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR, this.getLoginLangFlag(request)));
        }
        
        //基金类型
        if (CommonConstantsWeb.SYS_PARM_TYPE_FUNDTYPE.equals(method)){
	        allocations = strategyInfoService.findStrategyAllocation(strategyId, null, CommonConstantsWeb.WEB_ALLOCATION_TYPE_FUNDTYPE);
	        allocations = setAllocationNames(allocations,findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_FUNDTYPE, this.getLoginLangFlag(request)));
        }
        ResponseUtils.renderHtml(response,JsonUtil.toJson(allocations));
    }
    
    /**
     * 通过比较code设置分配设置的名称
     * @param allocations
     * @param itemList
     * @return
     */
    private List<StrategyAllocation> setAllocationNames(List<StrategyAllocation> allocations, List<GeneralDataVO> itemList){
    	List<StrategyAllocation> rlt = new ArrayList<StrategyAllocation>();
    	if (!allocations.isEmpty() && !itemList.isEmpty()){
    		for (StrategyAllocation x: allocations){
    			for (GeneralDataVO vo: itemList){
    				if (x.getItemCode().equals(vo.getItemCode())){
    					x.setName(vo.getName());
    					break;
    				}
    			}
    			rlt.add(x);
    		}
    	}
    	return rlt;
    }
    
    /**
     * 通过比较value设置分配设置的名称
     * @param allocations
     * @param itemList
     * @return
     */
    private List<StrategyAllocation> setAllocationNamesByValue(List<StrategyAllocation> allocations, List<GeneralDataVO> itemList){
    	List<StrategyAllocation> rlt = new ArrayList<StrategyAllocation>();
    	if (!allocations.isEmpty() && !itemList.isEmpty()){
    		for (StrategyAllocation x: allocations){
    			for (GeneralDataVO vo: itemList){
    				if (x.getItemCode().equals(vo.getValue())){
    					x.setName(vo.getName());
    					break;
    				}
    			}
    			rlt.add(x);
    		}
    	}
    	return rlt;
    }
    
    /**
     * 保存投资策略 - Step 2
     * @author michael
     * @param request
     * @param response
     * @param model
     * @param id 策略id
     * @param fundIds 基金ids
     * @param regions 所选区域列表
     * @param sectors 所选主题列表
     * @param types 所选基金类型列表
     * @param regionsWeight 所选区域列表对应的权重
     * @param sectorsWeight 所选主题列表对应的权重
     * @param typesWeight 所选基金类型列表对应的权重
     * @return
     */
    @RequestMapping(value = "/saveAllocation", method = RequestMethod.POST)
    public String saveAllocation(HttpServletRequest request, HttpServletResponse response, ModelMap model,
    		String id,String fundIds,String regions,String sectors,String types,String regionsWeight,String sectorsWeight,String typesWeight){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser!=null){
			try {
				//编辑状态时使用
		    	
		    	regions = StrUtils.getString(regions);
		    	sectors = StrUtils.getString(sectors);
		    	types = StrUtils.getString(types);
		    	regionsWeight = StrUtils.getString(regionsWeight);
		    	sectorsWeight = StrUtils.getString(sectorsWeight);
		    	typesWeight = StrUtils.getString(typesWeight);
		    	String typesLevel=StrUtils.getString(request.getParameter("typesLevel"));
		    	String regionsLevel=StrUtils.getString(request.getParameter("regionsLevel"));
		    	String sectorsLevel=StrUtils.getString(request.getParameter("sectorsLevel"));
		    	
		    	if(null!=typesLevel && !"".equals(typesLevel))
		    		typesLevel=String.valueOf(Integer.valueOf(typesLevel)+1);
		    	if(null!=regionsLevel && !"".equals(regionsLevel))
		    		regionsLevel=String.valueOf(Integer.valueOf(regionsLevel)+1);
		    	if(null!=sectorsLevel && !"".equals(sectorsLevel))
		    		sectorsLevel=String.valueOf(Integer.valueOf(sectorsLevel)+1);
		    	
		    	StrategyInfo info = new StrategyInfo();
		    	if (null!=id && !"".equals(id)){
		    		info = strategyInfoService.findById(id);
		    		if (null==info || null==info.getId()){
		    			info = new StrategyInfo();
				    	info.setCreator(loginUser);
				    	info.setCreateTime(new Date());
				    	info.setIsValid("1");
				    	info.setOverhead("0");//默认不置顶
				    	info.setStatus("0");//草稿
				    	info.setClick(0);
		    		}
			    	info.setSector(sectors);
			    	info.setGeoAllocation(regions);
			    	info.setLastUpdate(new Date());
			    	
			    	//保存投资策略信息
			    	info = strategyInfoService.saveOrUpdate(info);
			    	
			    	//保存策略分配信息(分配方法,G:Geographical,S:Sector,F:Funds Type,P:Product Type)
			    	strategyInfoService.deleteStrategyAllocationByMethod(info.getId(), CommonConstantsWeb.WEB_ALLOCATION_TYPE_GEOGRAPHICAL);
			    	strategyInfoService.saveStrategyAllocation(info, CommonConstantsWeb.WEB_ALLOCATION_TYPE_GEOGRAPHICAL, regionsLevel, regions, regionsWeight);
			    	strategyInfoService.deleteStrategyAllocationByMethod(info.getId(), CommonConstantsWeb.WEB_ALLOCATION_TYPE_SECTOR);
			    	strategyInfoService.saveStrategyAllocation(info, CommonConstantsWeb.WEB_ALLOCATION_TYPE_SECTOR, sectorsLevel, sectors, sectorsWeight);
			    	strategyInfoService.deleteStrategyAllocationByMethod(info.getId(), CommonConstantsWeb.WEB_ALLOCATION_TYPE_FUNDTYPE);
			    	strategyInfoService.saveStrategyAllocation(info, CommonConstantsWeb.WEB_ALLOCATION_TYPE_FUNDTYPE, typesLevel, types, typesWeight);
			    	
			    	//保存投资策略的产品信息
			    	try {
			    		List regionList = new ArrayList();
			    		List sectorList = new ArrayList();
			    		//删除就的产品记录
			    		strategyInfoService.deleteStrategyProduct(info.getId());
			    		
//			    		String fundIds = request.getParameter("fundIds");
			    		saveStrategyProduct(info, fundIds, regionList, sectorList);


				    	/*//更新补充信息
				    	info.setGeoAllocation(StrUtils.arrayListToString(regionList, ","));
				    	info.setSector(StrUtils.arrayListToString(sectorList, ","));
				    	info = strategyInfoService.saveOrUpdate(info);*/
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
			    	result.put("strategId",info.getId());
			    	result.put("result",true);
			       	result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
			       	this.saveOperLog(request,loginUser.getLoginCode(),"",CoreConstants.FRONT_LOG_STRATEGY_INFO, "保存策略成功");
		    	}else{
			    	result.put("result",false);
			       	result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.record_not_found",null));
			       	this.saveOperLog(request,loginUser.getLoginCode(),"",CoreConstants.FRONT_LOG_STRATEGY_INFO, "保存策略成功");
		    	}
			} catch (Exception e) {
				// TODO: handle exception
				result.put("result",false);
	        	result.put("msg",e.getMessage());
				this.saveOperLog(request,loginUser.getLoginCode(),"",CoreConstants.FRONT_LOG_STRATEGY_INFO, "保存策略失败");
			}
	        
		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("msg","User is not login.");
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(result));

    	return null;
    }
    /**
     * 保存策略产品
     * @param info
     * @param fundIds
     */
    private void saveStrategyProduct(StrategyInfo info, String fundIds,List regionList,List sectorList){
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

            			//保存策略与产品信息
            			strategyInfoService.saveOrUpdateStrategyProduct(info, fundId);
            		}
            	}
            }
    	}
    }
    
    /**
     * 新增投资策略 - Step 3
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/addRelease", method = RequestMethod.GET)
    public String addRelease(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
			
			String isNew=request.getParameter("new");
			model.put("isNew", isNew);
	    	String id = request.getParameter("id");
	    	StrategyInfo obj = strategyInfoService.findById(id);
	    	model.put("strategyVO", obj);
	    	
	    	//查看权限
	    	StrategyWebViewVO webViewVO = strategyInfoService.findWebViewByStrategy(id);
	    	if (null==webViewVO) webViewVO = new StrategyWebViewVO();
	    	model.put("webViewVO", webViewVO);
	    	//推送权限
	    	StrategyWebPushVO webPushVO = strategyInfoService.findWebPushByStrategy(id);
	    	if (null==webPushVO) webPushVO = new StrategyWebPushVO();
	    	model.put("webPushVO", webPushVO);
	    	
			/*//客户类型：Buddy， Client， Advisor， Prospect
			List<MemberIndividual> clients = investorService.findClientByIFA(loginUser.getId(),"Client");
			model.put("cnt_clients", null==clients?0:clients.size());

			List<MemberIndividual> prospects = investorService.findClientByIFA(loginUser.getId(),"Prospect");
			model.put("cnt_prospects", null==prospects?0:prospects.size());

			List<MemberIndividual> buddies = investorService.findClientByIFA(loginUser.getId(),"Buddy");
			model.put("cnt_buddies", null==buddies?0:buddies.size());

			//当前为IFA
			if ("2".equals(StrUtils.getString(loginUser.getMemberType())) && "21".equals(StrUtils.getString(loginUser.getSubMemberType()))){
				MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
				if (null!=ifa){
					List<MemberIfa> colleagues = investorService.findIfaColleagues(loginUser.getId(), ifa.getIfafirm().getId());
					model.put("cnt_colleagues", null==colleagues?0:colleagues.size());
				}
			}//当前为IFA Firm
			else if ("2".equals(StrUtils.getString(loginUser.getMemberType())) && "22".equals(StrUtils.getString(loginUser.getSubMemberType()))){
				MemberAdmin curAdmin = getLoginMemberAdmin(request);
				if (null!=curAdmin && null!=curAdmin.getIfafirm()){
					List<MemberIfa> colleagues = investorService.findIfaColleagues(loginUser.getId(), curAdmin.getIfafirm().getId());
					model.put("cnt_colleagues", null==colleagues?0:colleagues.size());
				}
			}*/
			
	    	return "strategy/info/addRelease";
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
    }
    
    /**
     * 保存投资策略 - Step 3
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/saveRelease", method = RequestMethod.POST)
    public String saveRelease(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		String langCode = getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser!=null){
			try {
				//编辑状态时使用
				String id = request.getParameter("id");//strategy id
				

		    	String permit = StrUtils.getString(request.getParameter("permit"));
		    	String permitClients = StrUtils.getString(request.getParameter("permit_clients"));
		    	String permitProspects = StrUtils.getString(request.getParameter("permit_prospects"));
		    	String permitBuddies = StrUtils.getString(request.getParameter("permit_buddies"));
		    	String permitColleagues = StrUtils.getString(request.getParameter("permit_colleagues"));
		    	String permitClient = StrUtils.getString(request.getParameter("permit_client"));
		    	String permitProspect = StrUtils.getString(request.getParameter("permit_prospect"));
		    	String permitBuddy = StrUtils.getString(request.getParameter("permit_buddy"));
		    	String permitColleague = StrUtils.getString(request.getParameter("permit_colleague"));
		    	
		    	 if("2".equals(permit)){//all
		    		 permitBuddy="1";
		    		 permitClient="1";
		    		 permitProspect="1";
		    		 permitColleague="1";
		       	}else if("1".equals(permit)){//only me
			       	 permitBuddy="0";
		    		 permitClient="0";
		    		 permitProspect="0";
		    		 permitColleague="0";
		       	}

				if ("0".equals(permitBuddy)) {
					permitBuddies = "";
				} else if ("1".equals(permitBuddy) && "".equals(permitBuddies)) {
					List<IfaClientVO> userList = webFriendService.findMyFriendList(loginUser.getId());
					permitBuddies = getIds(userList);
				}

				if ("0".equals(permitClient)) {
					permitClients = "";
				} else if (permitClient.equals("1") && "".equals(permitClients)) {
					List<IfaClientVO> userList = ifaInfoService.findMyClientList(loginUser.getId(), "1",langCode);
					permitClients = getIds(userList);
				}
				if ("0".equals(permitProspect)) {
					permitProspects = "";
				} else if (permitProspect.equals("1") && "".equals(permitProspects)) {
					List<IfaClientVO> userList = ifaInfoService.findMyClientList(loginUser.getId(), "0",langCode);
					permitProspects = getIds(userList);
				}
				if ("0".equals(permitColleague)) {
					permitColleagues = "";
				} else if ("1".equals(permitColleague) && "".equals(permitColleagues)) {
					List<IfaClientVO> userList = ifaInfoService.findMyColleagueList(loginUser.getId(), CommonConstants.DEF_LANG_CODE);
					permitColleagues = getIds(userList);
				}
		    	
		    	
		    	StrategyInfo info = new StrategyInfo();
		    	if (null!=id && !"".equals(id)){
		    		info = strategyInfoService.findById(id);
		    		
			       	//保存推送权限信息
					try {
			       		WebPush webPush = new WebPush();
			       		StrategyWebPushVO webPushVO = strategyInfoService.findWebPushByStrategy(id);
			       		if (null!=webPushVO && null!=webPushVO.getId() && !"".equals(webPushVO.getId())){
			       			webPush = strategyInfoService.findWebPushById(webPushVO.getId());
//			       			BeanUtils.copyProperties(webPushVO,webPush);//拷贝信息
			       		}else{//新建
				       		webPush.setModuleType(CommonConstantsWeb.WEB_PUSH_MODULE_STRATEGY);
				       		webPush.setRelateId(info.getId());
				       		webPush.setCreateTime(new Date());
				       		webPush.setFromMember(loginUser);
			       		}
			       		webPush.setScopeFlag(permit);
			       		webPush.setLastUpdate(new Date());
			       		
			       		webPush.setBuddyFlag(permitBuddy);
			       		webPush.setClientFlag(permitClient);
			       		webPush.setColleagueFlag(permitColleague);
			       		webPush.setProspectFlag(permitProspect);
			       		
				       	/*if(null!=permit && "3".equals(permit)){//范围标记,1:Only Me,2:Public,3:Let me specify
				       		//1 - 全选，0 - 无， -1 - 选部分
				       		webPush.setBuddyFlag(permitBuddy);
				       		webPush.setClientFlag(permitClient);
				       		webPush.setColleagueFlag(permitColleague);
				       		webPush.setProspectFlag(permitProspect);
				       	}else if("2".equals(permit)){//all
				       		webPush.setBuddyFlag("1");
				       		webPush.setClientFlag("1");
				       		webPush.setColleagueFlag("1");
				       		webPush.setProspectFlag("1");
				       	}else{//only me
				       		webPush.setBuddyFlag("0");
				       		webPush.setClientFlag("0");
				       		webPush.setColleagueFlag("0");
				       		webPush.setProspectFlag("0");
				       	}*/
				       	webPush = strategyInfoService.saveWebPush(webPush,permitClients,permitProspects,permitBuddies,permitColleagues);
					
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
			       	
			       	//保存查看权限信息
					try{
			       		WebView webView = new WebView();
			       		StrategyWebViewVO webViewVO = strategyInfoService.findWebViewByStrategy(id);
			       		if (null!=webViewVO && null!=webViewVO.getId() && !"".equals(webViewVO.getId())){
			       			webView = strategyInfoService.findWebViewById(webViewVO.getId());
//			       			BeanUtils.copyProperties(webViewVO,webView);//拷贝信息
			       		}else{//新建
				       		//对应模块,insight观点,news新闻,strategy策略,portfolio_info组合,portfolio_arena组合,live_info直播
				       		webView.setModuleType(CommonConstantsWeb.WEB_VIEW_MODULE_STRATEGY);
				       		webView.setRelateId(info.getId());
				       		webView.setCreateTime(new Date());
				       		webView.setFromMember(loginUser);
			       		}
			       		webView.setLastUpdate(new Date());
			       		webView.setScopeFlag(permit);
			       		
			       		webView.setBuddyFlag(permitBuddy);
			       		webView.setClientFlag(permitClient);
			       		webView.setColleagueFlag(permitColleague);
			       		webView.setProspectFlag(permitProspect);
				       	/*if("3".equals(permit)){//范围标记,1:Only Me,2:Public,3:Let me specify
				       		//1 - 全选，0 - 无， -1 - 选部分
				       		webView.setBuddyFlag(permitBuddy);
				       		webView.setClientFlag(permitClient);
				       		webView.setColleagueFlag(permitColleague);
				       		webView.setProspectFlag(permitProspect);
				       	}else if("2".equals(permit)){//all
				       		webView.setBuddyFlag("1");
				       		webView.setClientFlag("1");
				       		webView.setColleagueFlag("1");
				       		webView.setProspectFlag("1");
				       	}else{
				       		webView.setBuddyFlag("0");
				       		webView.setClientFlag("0");
				       		webView.setColleagueFlag("0");
				       		webView.setProspectFlag("0");
				       	}*/
				       	webView = strategyInfoService.saveWebView(webView,permitClients,permitProspects,permitBuddies,permitColleagues);
				     // 发送消息提醒
				       	strategyTodoRead(info, loginUser, permitClients);
				       	strategyTodoRead(info, loginUser, permitProspects);
				       	strategyTodoRead(info, loginUser, permitBuddies);
				       	strategyTodoRead(info, loginUser, permitColleagues);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
			    	
					//发布
					try {
				    	String ifPublish = StrUtils.getString(request.getParameter("ifPublish"));
				    	if ("true".equals(ifPublish)){
				    		info.setStatus("1");
				    		info.setIsPublic("1");//modify by mqzou 2016-11-23
				    		strategyInfoService.saveOrUpdate(info);
				    	}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
			    	
			    	result.put("strategId",info.getId());
			    	result.put("result",true);
			       	result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
			       	this.saveOperLog(request,loginUser.getLoginCode(),"",CoreConstants.FRONT_LOG_STRATEGY_INFO, "保存策略成功");
		    	}else{
			    	result.put("result",false);
			       	result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.record_not_found",null));
			       	this.saveOperLog(request,loginUser.getLoginCode(),"",CoreConstants.FRONT_LOG_STRATEGY_INFO, "保存策略成功");
		    	}
			} catch (Exception e) {
				// TODO: handle exception
				result.put("result",false);
	        	result.put("msg",e.getMessage());
				this.saveOperLog(request,loginUser.getLoginCode(),"",CoreConstants.FRONT_LOG_STRATEGY_INFO, "保存策略失败");
			}
	        
		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("msg","User is not login.");
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(result));

    	return null;
    }

    private String getIds(List<IfaClientVO> list){
    	List<String> ids=new ArrayList<String>();
    	if(null!=list && !list.isEmpty()){
    		Iterator<IfaClientVO> it=list.iterator();
    		while (it.hasNext()) {
				IfaClientVO ifaClientVO = (IfaClientVO) it.next();
				ids.add(ifaClientVO.getId());
			}
    	}
    	return StrUtils.arrayListToString(ids, ",");
    }
    
    /**
	 * 发送消息提醒
	 * @author mqzou
	 * @data 2017-02-21
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	private void strategyTodoRead(StrategyInfo info,MemberBase loginUser, String clientsDetail) {
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
		    			WebReadToDo webReadToDo = new WebReadToDo();
					 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_NORMAL);
					 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_STRATEGY);
					 	webReadToDo.setRelateId(info.getId());
					 	webReadToDo.setFromMember(loginUser);
					 	webReadToDo.setMsgUrl("/front/strategy/info/strategiesdetail.do");
					 	webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.index.activity.StrategyDetai");
					 	webReadToDo.setAppParam("id="+info.getId());
					 	webReadToDo.setMsgParam("id="+info.getId());
					 	webReadToDo.setIsApp("0");
					 	MemberBase memberBase = memberBaseService.findById(id);
					 	webReadToDo.setOwner(memberBase);
					 	webReadToDo.setCreateTime(new Date());
					 	webReadToDo.setIsValid("1");
					 	List<Object> params = new ArrayList<Object>();
					 	MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
					 	params.add(ifa.getMember().getNickName());
					 	params.add(info.getStrategyName());
					 	String titleSc = PropertyUtils.getSmsPropertyValue("strategy.info.push",CommonConstants.LANG_CODE_SC,params.toArray());
					 	String titleTc = PropertyUtils.getSmsPropertyValue("strategy.info.push",CommonConstants.LANG_CODE_TC,params.toArray());
					 	String titleEn = PropertyUtils.getSmsPropertyValue("strategy.info.push",CommonConstants.LANG_CODE_EN,params.toArray());
					 	webReadToDo.setTitle(titleEn);
					 	webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);
					 	
		    		}
		    	}
		    }
		}
	}	
    

   /* *//**
    *新建Proposal五步
    *//*
    @RequestMapping(value = "/createProposalSetOne", method = RequestMethod.GET)
	public String createProposalSetOne(CrmProposal proposal,HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	//CrmProposal proposal=new CrmProposal();
    	if(null!=proposal){
    		model.put("proposal", proposal);
    	}
		return "strategy/temp/createProposalSetOne";
	}*/
    
    /**
     *保存Proposal
     * @author mqzou
     * @date 2016-09-29
     */
     @RequestMapping(value = "/saveProposalSet")
    public void saveProposal(HttpServletRequest request, HttpServletResponse response, ModelMap model,CrmProposal proposal){
    	Map<String, Object> obj = new HashMap<String, Object>();
		if (null != proposal) {
			obj.put("result", true);
			obj.put("message", "保存成功");
		} else {
			obj.put("result", false);
			obj.put("message", "保存失败");
		}
		JsonUtil.toWriter(obj, response);
    }
    
    @RequestMapping(value = "/createProposalSetTwo")
	public String createProposalSetTwo(HttpServletRequest request, HttpServletResponse response, ModelMap model,CrmProposal proposal){
    	if(null!=proposal){
    		model.put("proposal", proposal);
    	}
    	//System.out.print(JsonUtil.toJson(proposal));
		return "strategy/temp/createProposalSetTwo";
	}
    @RequestMapping(value = "/createProposalSetThree", method = RequestMethod.GET)
	public String createProposalSetThree(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		return "strategy/temp/createProposalSetThree";
	}
    @RequestMapping(value = "/createProposalSetFour", method = RequestMethod.GET)
	public String createProposalSetFour(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		return "strategy/temp/createProposalSetFour";
	}
    @RequestMapping(value = "/createProposalSetFive", method = RequestMethod.GET)
	public String createProposalSetFive(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		return "strategy/temp/createProposalSetFive";
	}
    /******/
    /**
     *编辑 Proposal五步
     */
     @RequestMapping(value = "/editProposalSetOne", method = RequestMethod.GET)
 	public String editProposalSetOne(HttpServletRequest request, HttpServletResponse response, ModelMap model){
 		return "strategy/temp/editProposalSetOne";
 	}
     @RequestMapping(value = "/editProposalSetTwo", method = RequestMethod.GET)
 	public String editProposalSetTwo(HttpServletRequest request, HttpServletResponse response, ModelMap model){
 		return "strategy/temp/editProposalSetTwo";
 	}
    @RequestMapping(value = "/editProposalSetThree", method = RequestMethod.GET)
 	public String editProposalSetThree(HttpServletRequest request, HttpServletResponse response, ModelMap model){
 		return "strategy/temp/editProposalSetThree";
 	}
    @RequestMapping(value = "/editProposalSetFour", method = RequestMethod.GET)
 	public String editProposalSetFour(HttpServletRequest request, HttpServletResponse response, ModelMap model){
 		return "strategy/temp/editProposalSetFour";
 	}
     @RequestMapping(value = "/editProposalSetFive", method = RequestMethod.GET)
 	public String editProposalSetFive(HttpServletRequest request, HttpServletResponse response, ModelMap model){
 		return "strategy/temp/editProposalSetFive";
 	}
     /******/
    @RequestMapping(value = "/myAccountlist", method = RequestMethod.GET)
 	public String myAccountlist(HttpServletRequest request, HttpServletResponse response, ModelMap model){
 		return "strategy/temp/myAccountlist";
 	}
    /**
     * mqzou 实现功能
     * 2016-10-10
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/conservativePortfolio", method = RequestMethod.GET)
 	public String conservativePortfolio(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
    		String id=request.getParameter("id");
    		String currency=request.getParameter("currency");
    		String langCode=this.getLoginLangFlag(request);
    		if(null==currency || "".equals(currency)){
    			currency=loginUser.getDefCurrency();
    			if(null==currency || "".equals(currency)){
    				currency=CommonConstants.DEF_CURRENCY;
    			}
    		}
    		List<GeneralDataVO>  itemList=findSysParameters("currency_type", langCode);
 	        model.put("currencyType", itemList);
 	        
 	        
 	        String dateFormat=loginUser.getDateFormat();
 	        if(null==dateFormat || "".equals(dateFormat))
 	        	dateFormat=CommonConstants.FORMAT_DATE;
 	        model.put("dateTimeFormat", dateFormat+" "+CommonConstants.FORMAT_TIME);

			/*String check=request.getParameter("check");
			if(check==null || check=="0"){
				AccountValidVO validVO=iTraderSendService.saveCheckExistValid(request, loginUser.getId());
				if(null!=validVO){
					model.put("checkList", validVO.getValidAccountNo());
					if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR==loginUser.getMemberType())
					model.put("accountType", CommonConstantsWeb.WEB_ACCOUNTCHECK_INVESTOR);
					else {
						model.put("accountType", CommonConstantsWeb.WEB_ACCOUNTCHECK_IFA);
					}
				}
				
			}*/
			/*boolean b= portfolioHoldService.checkPortfolioAip(id);
			String hasAip=b?"1":"0";
			model.put("hasAip", hasAip);*/
    		
			/*OrderPlan plan=transactionService.findOrderPlanByHoldId(id);
			if(null!=plan)
				model.put("planId", plan.getId());*/
			
    		//PortfolioInfo info=portfolioInfoService.findById(id);
    		PortfolioHold hold=portfolioHoldService.findById(id);
    		if(null!=hold){
    			MemberIfa ifa=hold.getIfa();
    			ifa.setNameChn(getIfaName(ifa, langCode));
    			hold.setIfa(ifa);
    			model.put("info", hold);
    			
    			MemberIndividual individual=memberBaseService.findIndividualMember(hold.getMember().getId());
    			individual.setNameChn(getIndividualName(individual, langCode));
    			model.put("individual", individual);
    			
    			
    			AssetsVO assetsVO=new AssetsVO();
    			assetsVO.setTotalMarketValue(0);
    			/*assetsVO.setTotalReturnRate(0);
    			assetsVO.setTotalReturnValue(0);*/
    			
    			AssetsTotalVo totalVo=tradeCoreService.getPortfolioTotalAssets(id, currency);
    			
    			assetsVO.setTotalCashValue(totalVo.getTotalCash());
    			assetsVO.setTotalMarketValue(totalVo.getTotalMarketValue());
    			assetsVO.setTotalAssets(totalVo.getTotalAssets());
    			assetsVO.setTotalReturnRate(totalVo.getTotalReturnRate());
    			assetsVO.setTotalReturnValue(totalVo.getTotalReturnValue());
    			
    			assetsVO.setBaseCurrency(currency);
    			String curName=sysParamService.findNameByCode(currency, langCode);
    			assetsVO.setCurrencyName(curName);
    			
    			model.put("assetsVO", assetsVO);
    			model.put("currency", curName);
    			String numFormat="#,##0.00";
    			if("JPY".equals(currency)){
    				numFormat="#,##0";
    			}
    			model.put("numFormat", numFormat);
    			
    			
    			//List<CharDataVO> chatList=new ArrayList<CharDataVO>();
    			List<HoldChartVO> chartValueList=new ArrayList<HoldChartVO>();
    			
    			double fundMarket=0;
    			double fundPl=0;
    			Double fundYesterday = null;
    			double stockMarket=0;
    			double stockPl=0;
    			Double stockYesterday= null;
    			double bondMarket=0;
    			double bondPl=0;
    			Double bondYesterday= null;
    			double insureMarket=0;
    			double insurePl=0;
    			Double insureYesterday= null;
    			double fundUnit=0;
    			double stockUnit=0;
    			double bondUnit=0;
    			double insureUnit=0;
				
    			List<ProductType> productTypeList=portfolioHoldService.findProductType();
    			List<HoldProductVO> fundList=tradeCoreService.findHoldFundList(id, currency, langCode,"");
    			if(null!=fundList && !fundList.isEmpty()){
    				Iterator it=fundList.iterator();
    				double totalUnit=0;
    				while (it.hasNext()) {
    					HoldProductVO vo = (HoldProductVO) it.next();
    					double unit=Double.valueOf(vo.getHoldUnit());
    					double market=Double.valueOf(vo.getMarketValue());
    					//double pl=Double.valueOf(vo.getYesterdayPl());
    					fundMarket+=market;
    					//fundPl+=pl;
    					totalUnit+=unit;
    					if(null!=vo.getYesterdayPl()){
    						fundYesterday=null==fundYesterday?vo.getYesterdayPl():fundYesterday+vo.getYesterdayPl();
    						
    					}
    					//fundYesterday+=vo.getYesterdayPl();
    				}
    				fundUnit=totalUnit;
    				Iterator iterator=fundList.iterator();
    				while (iterator.hasNext()) {
    					HoldProductVO vo = (HoldProductVO) iterator.next();
    					double weight=0;
    					double unit=Double.valueOf(vo.getHoldUnit());
    					if(totalUnit!=0){
    						weight=unit/totalUnit;
    					}
    					vo.setWeight(String.valueOf(weight));
    				}
    			}
    			List<HoldProductVO>  stockList=tradeCoreService.findHoldStockList(id, currency, langCode,"");
    			if(null!=stockList && !stockList.isEmpty()){
    				Iterator it=stockList.iterator();
    				double totalUnit=0;
    				while (it.hasNext()) {
    					HoldProductVO vo = (HoldProductVO) it.next();
    					double unit=Double.valueOf(vo.getHoldUnit());
    					double market=Double.valueOf(vo.getMarketValue());
    					//double pl=Double.valueOf(vo.getYesterdayPl());
    					stockMarket+=market;
    					//stockPl+=pl;
    					totalUnit+=unit;
    					//stockYesterday+=vo.getYesterdayPl();
    					if(null!=vo.getYesterdayPl()){
    						stockYesterday=null==stockYesterday?vo.getYesterdayPl():stockYesterday+vo.getYesterdayPl();
    						
    					}
    				}
    				
    				stockUnit=totalUnit;
    				Iterator iterator=stockList.iterator();
    				while (iterator.hasNext()) {
    					HoldProductVO vo = (HoldProductVO) iterator.next();
    					double weight=0;
    					double unit=Double.valueOf(vo.getHoldUnit());
    					if(totalUnit!=0){
    						weight=unit/totalUnit;
    					}
    					vo.setWeight(String.valueOf(weight));
    				}
    			}
    			
    			List<HoldProductVO>  bondList=tradeCoreService.findHoldBond(id, currency, langCode,"");
    			if(null!=bondList && !bondList.isEmpty()){
    				Iterator it=bondList.iterator();
    				double totalUnit=0;
    				while (it.hasNext()) {
    					HoldProductVO vo = (HoldProductVO) it.next();
    					double unit=Double.valueOf(vo.getHoldUnit());
    					double market=Double.valueOf(vo.getMarketValue());
    					//double pl=Double.valueOf(vo.getYesterdayPl());
    					bondMarket+=market;
    				//	bondPl+=pl;
    					totalUnit+=unit;
    					//bondYesterday+=vo.getYesterdayPl();
    					if(null!=vo.getYesterdayPl()){
    						bondYesterday=null==bondYesterday?vo.getYesterdayPl():bondYesterday+vo.getYesterdayPl();
    						
    					}
    				}
    				bondUnit=totalUnit;
    				Iterator iterator=bondList.iterator();
    				while (iterator.hasNext()) {
    					HoldProductVO vo = (HoldProductVO) iterator.next();
    					double weight=0;
    					double unit=Double.valueOf(vo.getHoldUnit());
    					if(totalUnit!=0){
    						weight=unit/totalUnit;
    					}
    					vo.setWeight(String.valueOf(weight));
    				}
    			}
    			
    			List<HoldProductVO>  insureList=tradeCoreService.findHoldInsure(id, currency, langCode,"");
    			if(null!=insureList && !insureList.isEmpty()){
    				Iterator it=insureList.iterator();
    				double totalUnit=0;
    				while (it.hasNext()) {
    					HoldProductVO vo = (HoldProductVO) it.next();
    					double unit=Double.valueOf(vo.getHoldUnit());
    					double market=Double.valueOf(vo.getMarketValue());
    				//	double pl=Double.valueOf(vo.getYesterdayPl());
    					insureMarket+=market;
    				//	insurePl+=pl;
    					totalUnit+=unit;
    				//	insureYesterday+=vo.getYesterdayPl();
    					if(null!=vo.getYesterdayPl()){
    						insureYesterday=null==insureYesterday?vo.getYesterdayPl():insureYesterday+vo.getYesterdayPl();
    						
    					}
    				}
    				insureUnit=totalUnit;
    				Iterator iterator=insureList.iterator();
    				while (iterator.hasNext()) {
    					HoldProductVO vo = (HoldProductVO) iterator.next();
    					double weight=0;
    					double unit=Double.valueOf(vo.getHoldUnit());
    					if(totalUnit!=0){
    						weight=unit/totalUnit;
    					}
    					vo.setWeight(String.valueOf(weight));
    				}
    			}
    			
    			if(fundUnit!=0){
    				ProductType type=findProductType(productTypeList, CommonConstantsWeb.WEB_PRODUCT_TYPE_FUND);
    				HoldChartVO vo=new HoldChartVO();
    				vo.setName(type.getId());
    				vo.setDisplayColor(type.getDisplayColor());
    				vo.setMarketValue(String.valueOf(fundMarket));
    				vo.setMarketValueStr(StrUtils.getNumberString(fundMarket, "#,##0.00"));
    				vo.setPlValue(String.valueOf(fundPl));
    				vo.setHoldUnit(String.valueOf(fundUnit));
    				if(null!=fundYesterday)
    				  vo.setYesterdayPl(StrUtils.getNumberString(fundYesterday, "#,##0.00"));
    				else {
    					 vo.setYesterdayPl("-");
					}
    				chartValueList.add(vo);
    				
    			}
    			if(stockUnit!=0){
    				ProductType type=findProductType(productTypeList, CommonConstantsWeb.WEB_PRODUCT_TYPE_STOCK);
    				HoldChartVO vo=new HoldChartVO();
    				vo.setName(type.getId());
    				vo.setDisplayColor(type.getDisplayColor());
    				vo.setPlValue(String.valueOf(stockPl));
    				vo.setHoldUnit(String.valueOf(stockUnit));
    				vo.setMarketValue(String.valueOf(stockMarket));
    				vo.setMarketValueStr(StrUtils.getNumberString(stockMarket, "#,##0.00"));
    				if(null!=stockYesterday)
      				  vo.setYesterdayPl(StrUtils.getNumberString(stockYesterday, "#,##0.00"));
      				else {
      					 vo.setYesterdayPl("-");
  					}
    				//vo.setYesterdayPl(StrUtils.getNumberString(stockYesterday, "#,##0.00"));
    				chartValueList.add(vo);
    			}
    			if(bondUnit!=0){
    				ProductType type=findProductType(productTypeList, CommonConstantsWeb.WEB_PRODUCT_TYPE_BOND);
    				HoldChartVO vo=new HoldChartVO();
    				vo.setName(type.getId());
    				vo.setDisplayColor(type.getDisplayColor());
    				vo.setMarketValue(String.valueOf(bondMarket));
    				vo.setMarketValueStr(StrUtils.getNumberString(bondMarket, "#,##0.00"));
    				if(null!=bondYesterday)
        				  vo.setYesterdayPl(StrUtils.getNumberString(bondYesterday, "#,##0.00"));
        				else {
        					 vo.setYesterdayPl("-");
    					}
    			//	vo.setYesterdayPl(StrUtils.getNumberString(bondYesterday, "#,##0.00"));
    				vo.setPlValue(String.valueOf(bondPl));
    				vo.setHoldUnit(String.valueOf(bondUnit));
    				chartValueList.add(vo);
    			}
    			if(insureUnit!=0){
    				ProductType type=findProductType(productTypeList, CommonConstantsWeb.WEB_PRODUCT_TYPE_INSURE);
    				HoldChartVO vo=new HoldChartVO();
    				vo.setName(type.getId());
    				vo.setDisplayColor(type.getDisplayColor());
    				vo.setMarketValue(String.valueOf(insureMarket));
    				vo.setMarketValueStr(StrUtils.getNumberString(insureMarket, "#,##0.00"));
    				if(null!=insureYesterday)
      				  vo.setYesterdayPl(StrUtils.getNumberString(insureYesterday, "#,##0.00"));
      				else {
      					 vo.setYesterdayPl("-");
  					}
    				
    				//vo.setYesterdayPl(StrUtils.getNumberString(insureYesterday, "#,##0.00"));
    				vo.setPlValue(String.valueOf(insurePl));
    				vo.setHoldUnit(String.valueOf(insureUnit));
    				chartValueList.add(vo);
    			}
    			double totalCash=tradeCoreService.findPortfolioCash("", currency,loginUser.getId());
    			if(totalCash!=0){
    				ProductType type=findProductType(productTypeList, CommonConstantsWeb.WEB_PRODUCT_TYPE_CASH);
    				HoldChartVO vo=new HoldChartVO();
    				vo.setName(type.getId());
    				vo.setDisplayColor(type.getDisplayColor());
    				vo.setMarketValue(String.valueOf(totalCash));
    				vo.setMarketValueStr(StrUtils.getNumberString(totalCash, "#,##0.00"));
    				vo.setPlValue("");
    				chartValueList.add(vo);
    			}
    			model.put("chartListJson", JsonUtil.toJson(chartValueList));
    			model.put("chartList", chartValueList);
    			model.put("fundList", fundList);
    			model.put("stockList", stockList);
    			model.put("bondList", bondList);
    			model.put("insureList", insureList);
    			List<PortfolioProductPLVO> dayList=portfolioHoldService.findPortfolioPL(id, langCode);
    			model.put("dayList", dayList);
    			String displayColor=loginUser.getDefDisplayColor();
    			if(null==displayColor || "".equals(displayColor)){
    			    displayColor=CommonConstants.DEF_DISPLAY_COLOR;
    			}
    			model.put("displayColor", displayColor);
    			
    		}
    		
    	}
 		return "strategy/temp/conservativePortfolio";
 	}
    
    private ProductType findProductType(List<ProductType> list,String type){
		if(null!=list && !list.isEmpty()){
			Iterator<ProductType> it=list.iterator();
			while (it.hasNext()) {
				ProductType productType = (ProductType) it.next();
				if(type.equals(productType.getId())){
					return productType;
				}
				
			}
		}
		return null;
	}
    
    /**
     * mqzou 基金产品收益
     * 2016-10-11
     * */
    @RequestMapping(value = "/fundDetailIncome", method = RequestMethod.GET)
 	public String fundDetailIncome(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
    		String id=request.getParameter("id");
    		String currency=request.getParameter("currency");
    		String langCode=getLoginLangFlag(request);
    		if(null==currency || "".equals(currency)){
    			currency=loginUser.getDefCurrency();
    			if(null==currency || "".equals(currency)){
    				currency=CommonConstants.DEF_CURRENCY;
    			}
    		}
    		
    		//PortfolioHoldProduct holdProduct=portfolioHoldService.findPortfolioHoldProductById(id);
    		String dateFormat=loginUser.getDateFormat();
			if(null==dateFormat || "".equals(dateFormat)){
				dateFormat="yyyy-MM-dd";
			}
    		FundIncomDetailVO vo=portfolioHoldService.findFundIncomDetail(id,langCode);
    		if(null!=vo){
    			vo.setLastUpdate(DateUtil.getDateStr(vo.getLastUpdate(), "yyyy-MM-dd HH:mm:ss",dateFormat));
    			double latestNAVPrice=Double.valueOf(vo.getLatestNAVPrice());
    			double holdingUnit=Double.valueOf(vo.getHoldingUnit());
    			double availableUnit=Double.valueOf(vo.getAvailableUnit());
    			double cumperfRate=Double.valueOf(vo.getCumperfRate());
    			double totalPl=Double.valueOf(vo.getTotalPl());
    			String fundCurrency=vo.getCurrency();
    		    if(!currency.equals(fundCurrency)){
    		    	WebExchangeRate rate=fundInfoService.findExchangeRate(fundCurrency, currency);
    		    	if(null!=rate){
    		    		latestNAVPrice=latestNAVPrice*rate.getRate();
    		    		totalPl=totalPl*rate.getRate();
    		    		
    		    	}
    		    }
    		    vo.setCurrency(currency);
    			vo.setTotalPlValue(totalPl);
    			vo.setAvailableUnit(String.valueOf(availableUnit));
    			vo.setAverageCost(String.valueOf(holdingUnit/latestNAVPrice));
    			vo.setCumperfRate(String.valueOf(cumperfRate));
    			vo.setHoldingUnit(String.valueOf(holdingUnit));
    			vo.setLatestNAVPriceStr(String.valueOf(latestNAVPrice));
    			vo.setTotalAum(String.valueOf(latestNAVPrice*holdingUnit));
    			if(totalPl>0)
    				vo.setTotalPl(String.valueOf(totalPl));
    			else
    			    vo.setTotalPl(String.valueOf(totalPl));
        		vo.setId(id);
    		}else{
    			vo = new FundIncomDetailVO();
    		}
    		model.put("fundInfo", vo);
    		String displayColor=loginUser.getDefDisplayColor();
			if(null==displayColor || "".equals(displayColor)){
			    displayColor=CommonConstants.DEF_DISPLAY_COLOR;
			}
			model.put("displayColor", displayColor);
			model.put("currency", currency);
			
			List<GeneralDataVO>  itemList=findSysParameters("currency_type", langCode);
 	        model.put("currencyType", itemList);
			
    	}
 		return "strategy/info/fundDetailIncome";
 	}
    
    /**
     * mqzou 基金产品收益报表
     * 2016-10-12
     * */
    @RequestMapping(value = "/getFundIncomReport")
    public void getFundIncomReport(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	MemberBase loginUser=getLoginUser(request);
    	//获取发布日期条件
        String period = StrUtils.getString(request.getParameter("period"));
        String id=StrUtils.getString(request.getParameter("id"));
        String currency=StrUtils.getString(request.getParameter("currency"));
        if(null==currency || "".equals(currency))
        	currency=loginUser.getDefCurrency();
        if(null==currency || "".equals(currency))
        	currency=CommonConstants.DEF_CURRENCY;
        PortfolioHoldProduct product=portfolioHoldService.findPortfolioHoldProductById(id);
        if(null!=product){
        	String portfolioHoldId=product.getPortfolioHold().getId();
            String productId=product.getProduct().getId();
            String accountNo=product.getAccountNo();
    		String startDate = "";//DateUtil.getCurDateStr();
    		String endDate = DateUtil.getCurDateStr(Calendar.DATE, 1);//DateUtil.getCurDateStr();
    		if ("1W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
    		if ("2W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -14);
    		if ("1M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
    		if ("3M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
    		if ("6M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
    		if ("1Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
    		if ("3Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
    		if ("5Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
    		if ("YTD".equals(period)) startDate = DateUtil.getDateStr(DateUtil.getCurrYearFirst());	
    		else {
    			String[] dates = period.split("to");
    			if (null != dates && dates.length ==2) {
    				startDate = dates[0].trim();
    				endDate = dates[1].trim();
    			}
    		}
    		List<PortfolioHoldProductCumperf> list=portfolioHoldService.findPortfolioHoldProductCumperf(portfolioHoldId, productId, accountNo, startDate, endDate);
    		List<PortfolioHoldProductCumperfVO> resultList=new ArrayList<PortfolioHoldProductCumperfVO>();
    		if(null!=list && !list.isEmpty()){
    			String dateFormat=loginUser.getDateFormat();
    			if(null==dateFormat || "".equals(dateFormat)){
    				dateFormat="yyyy-MM-dd";
    			}
    			Iterator<PortfolioHoldProductCumperf> it=list.iterator();
    			while (it.hasNext()) {
					PortfolioHoldProductCumperf cumperf = (PortfolioHoldProductCumperf) it.next();
					//PortfolioHold hold=cumperf.getPortfolioHold();
					
					String curCurrency=null!=cumperf.getPortfolioHold()?cumperf.getPortfolioHold().getBaseCurrency():"";
					if(!currency.equals(curCurrency)){
						WebExchangeRate rate=fundInfoService.findExchangeRate(curCurrency, currency);
	    		    	if(null!=rate){
	    		    		cumperf.setDayPl(cumperf.getDayPl()*rate.getRate());
	    		    		cumperf.setTotalPl(cumperf.getTotalPl()*rate.getRate());
	    		    	}
					}
					PortfolioHoldProductCumperfVO vo=new PortfolioHoldProductCumperfVO(cumperf);
					vo.setValuationDate(DateUtil.getDateStr(vo.getValuationDate(), "yyyy-MM-dd", dateFormat));
					resultList.add(vo);
				}
    		}
    		ResponseUtils.renderHtml(response,JsonUtil.toJson(resultList));
        }
        
    }
   
    
    @RequestMapping(value = "/clientDetail", method = RequestMethod.GET)
	public String clientDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/clientDetail";
	    }else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
    @RequestMapping(value = "/clientDetailKyc", method = RequestMethod.GET)
	public String clientDetailKyc(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/clientDetailKyc";
	    }else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/portfolioDetail", method = RequestMethod.GET)
	public String portfolioDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/portfolioDetail";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/proposalMangement", method = RequestMethod.GET)
	public String proposalMangement(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/proposalMangement";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/portfolioMangement", method = RequestMethod.GET)
	public String portfolioMangement(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/portfolioMangement";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/transactionOne", method = RequestMethod.GET)
	public String transactionOne(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/transactionOne";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/transactionTwo", method = RequestMethod.GET)
	public String transactionTwo(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/transactionTwo";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/transactionThree", method = RequestMethod.GET)
	public String transactionThree(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/transactionThree";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/transactionRecord", method = RequestMethod.GET)
	public String transactionRecord(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "transaction/transactionRecord";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/confirmPortf", method = RequestMethod.GET)
	public String confirmPortfolio(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/confirmPortfolio";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	
	
	 /**
     * 临时开户流程,只放图片
     */
	@RequestMapping(value = "/openaccountOne", method = RequestMethod.GET)
	public String openaccountOne(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/openaccountOne";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/openaccountTwo", method = RequestMethod.GET)
	public String openaccountTwo(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/openaccountTwo";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/openaccountThree", method = RequestMethod.GET)
	public String openaccountThree(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
    		return "strategy/temp/openaccountThree";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/openaccountFour", method = RequestMethod.GET)
	public String openaccountFour(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/openaccountFour";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/openaccountFive", method = RequestMethod.GET)
	public String openaccountFive(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/openaccountFive";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/openaccountSix", method = RequestMethod.GET)
	public String openaccountSix(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/openaccountSix";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/openaccountSeven", method = RequestMethod.GET)
	public String openaccountSeven(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/openaccountSeven";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/openaccountEight", method = RequestMethod.GET)
	public String openaccountEight(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/openaccountEight";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	@RequestMapping(value = "/spaceManagement", method = RequestMethod.GET)
	public String spaceManagement(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
			return "strategy/temp/spaceManagement";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}

	/**
	 * 获取我的策略
	 * @author wwluo
	 * @date 2016-11-18
	 */
	@RequestMapping(value = "/getMyStrategys")
	public void getMyStrategys(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		List<StrategyInfoWebVO> strategyInfoWebVOs = new ArrayList<StrategyInfoWebVO>();
		MemberBase loginUser = this.getLoginUser(request);
		jsonPaging = this.getJsonPaging(request);
		String keyWord = request.getParameter("keyWord");
		String isPublic = request.getParameter("isPublic");
		String status = request.getParameter("status");
		jsonPaging = strategyInfoService.getMyStrategys(jsonPaging, loginUser, keyWord, isPublic, status);
		if(!jsonPaging.getList().isEmpty()){
			for (Object object : jsonPaging.getList()) {
				
				StrategyInfo strategyInfos = (StrategyInfo) object;
				StrategyInfoWebVO strategyInfoWebVO = new StrategyInfoWebVO(strategyInfos);
				//BeanUtils.copyProperties(strategyInfos, strategyInfoWebVO);
				/*List<WebInvestorVisit> wedInvestorVisits = strategyInfoService.getWebInvestorVisitByRelateId(strategyInfos.getId());
				if(wedInvestorVisits != null){
					strategyInfoWebVO.setVisitCount(wedInvestorVisits.size());
				}*/
				StrategyCount count = strategyInfoService.findStrategyCountById(strategyInfos.getId());
				if (null!=count && null!=count.getViews()) strategyInfoWebVO.setVisitCount(count.getViews());
				
				JsonPaging paging = new JsonPaging();
				jsonPaging.setRows(1000000000);
				paging = strategyInfoService.getCustomerVisitStrategy(paging,strategyInfos.getId(),"strategy",loginUser);
				strategyInfoWebVO.setCustomerVisitCount(paging.getTotal());
				strategyInfoWebVOs.add(strategyInfoWebVO);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(strategyInfoWebVOs);
		}
		JsonUtil.toWriter(jsonPaging, response);
		/*String jsonString=JsonUtil.toJson(jsonPaging);
		 ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));*/
	}
	
	/**
	 * 获取我的访客
	 * @author wwluo
	 * @param request
	 * @param response
	 * @param model
	 * @date 2016-11-18
	 */
	@RequestMapping(value = "/getMyVisitors")
	public void getMyStrategyVisitors(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		jsonPaging = this.getJsonPaging(request);
		String relateId = request.getParameter("relateId");
		String moduleType = request.getParameter("moduleType");
		jsonPaging = strategyInfoService.getCustomerVisitStrategy(jsonPaging,relateId,moduleType,loginUser);
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	
	
}
