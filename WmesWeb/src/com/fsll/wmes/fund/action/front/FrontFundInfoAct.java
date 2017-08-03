/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.fund.action.front;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.buscore.fund.service.CoreFundService;
import com.fsll.buscore.fund.vo.CoreFundNavVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.service.SysCountryService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.FundAnno;
import com.fsll.wmes.entity.FundHouseEn;
import com.fsll.wmes.entity.FundPortfolio;
import com.fsll.wmes.entity.FundReturn;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.WebBusLog;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebWatchlist;
import com.fsll.wmes.entity.WebWatchlistType;
import com.fsll.wmes.fund.service.FundAnnoService;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.service.FundPortfolioService;
import com.fsll.wmes.fund.vo.FundCompositionDataVO;
import com.fsll.wmes.fund.vo.FundHouseDataVO;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.fund.vo.FundMarketDataVO;
import com.fsll.wmes.fund.vo.FundPortfolioListDataVO;
import com.fsll.wmes.fund.vo.FundScreenerDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.web.service.WebFollowStatusService;
import com.fsll.wmes.web.service.WebWatchListService;


/**
 * 控制器:基金信息管理
 * 
 * @author Michael
 * @version 1.0.0 Created On: 2016-6-20
 */
@Controller
@RequestMapping("/front/fund/info")
public class FrontFundInfoAct extends WmesBaseAct {
    
    @Autowired
    private FundInfoService fundInfoService;

    @Autowired
    private FundPortfolioService fundPortfolioService;
    
    @Autowired
    private SysCountryService sysCountryService;
    
    @Autowired
    private DistributorService distributorService;

    public FundInfoDataVO fundInfoVO;
    public List<GeneralDataVO> fundFeesList;
    public List<GeneralDataVO> fundDocsList;
    public List<GeneralDataVO> fundBonusList;
    public List<GeneralDataVO> fundAnnoList;
    public List<GeneralDataVO> fundCumReturnList;
    public List<GeneralDataVO> fundYearReturnList;
    public List<GeneralDataVO> fundPriceList;
    public List<FundCompositionDataVO> fundPortfolioListByRegion;
    public List<FundCompositionDataVO> fundPortfolioListByIndustry;
    
    @Autowired
	private WebFollowStatusService webFollowStatusService;
    @Autowired
    private PortfolioHoldService portfolioHoldService;
    @Autowired
	private WebWatchListService webWatchListService;
    @Autowired
	private CoreFundService coreFundService;
    @Autowired
	private FundAnnoService fundAnnoService;
    /**
     * 基金筛选列表（第一版）
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model, String select, String showCart) {
        this.isMobileDevice(request, model);
        String lang = this.getLoginLangFlag(request);
        try {
            //List<SysParamConfig> currencyList = sysParamService.findParamConfigByType("currency_type");
        	List<GeneralDataVO> currencyList = findSysParameters("currency_type",lang);
            model.put("currencyList", currencyList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
	        List<FundHouseDataVO> fundHouseList = fundInfoService.loadFundHouseList(lang);
	        model.put("fundHouseList", fundHouseList);
		} catch (Exception e) {
			// TODO: handle exception
		}


		//select参数用于作为基金选择功能
    	select = StrUtils.getString(select);
    	if ("true".equals(select)) model.put("select", select);
		
    	//showCart参数用于控制是否显示购物车
    	showCart = StrUtils.getString(showCart);
    	if ("true".equals(showCart)) model.put("showCart", "true");
    	else model.put("showCart", "false");
    	
		String displayColor = "";
		MemberBase loginUser = getLoginUser(request);
		if(null != loginUser){ 
			MemberSsoVO ssoVO = getLoginUserSSO(request);
			String individualId = ssoVO.getIndividualId();
			if(null != individualId && !"".equals(individualId)){
				model.put("individualId", individualId);
			}
			
			String memberId = loginUser.getId();
			try {
		        List<PortfolioHold> portfolioHoldList = portfolioHoldService.findMyPortfolioHoldList(memberId);
		        model.put("portfolioHoldList", portfolioHoldList);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			displayColor = loginUser.getDefDisplayColor();
		}
		if(null==displayColor || "".equals(displayColor)){
		    displayColor=CommonConstants.DEF_DISPLAY_COLOR;
		}
		model.put("displayColor", displayColor);
		model.put("defCurrency", loginUser.getDefCurrency());
    	
		return "fund/info/fundsscreener";
    }
    
    /**
     * 分页获得基金记录（第一版，嵌入基金列表）--已用异步方法getFundListJson()替代
     * @author scshi
     * @param request
     * @param response
     * @param model
     * @param filters 筛选条件
     * @return
     */
    @RequestMapping(value = "/fundsForScreener", method = RequestMethod.GET)
    public String fundsForScreener(HttpServletRequest request, HttpServletResponse response, ModelMap model, FundScreenerDataVO filters) {
    	jsonPaging = queryFundsByFilters(request, model, filters);
        String lang = this.getLoginLangFlag(request);
        List<FundHouseDataVO> fundHouseList = fundInfoService.loadFundHouseList(lang);
        //this.toJsonString(response, jsonPaging);
        model.put("jsonPaging", jsonPaging);
        model.put("fundHouseList_in", fundHouseList);
        return "fund/info/fundList";
    }

    /**
     * 根据多条件筛选基金
     * @author michael
     * @param request
     * @param model
     * @param filters 筛选条件
     * @return
     */
    private JsonPaging queryFundsByFilters(HttpServletRequest request, ModelMap model, FundScreenerDataVO filters)
    {
        filters.setLoginUser(this.getLoginUser(request));
        filters.setKeyword(toUTF8String(filters.getKeyword()));
        filters.setFundName(toUTF8String(filters.getFundName()));
        filters.setDomicile(toUTF8String(filters.getDomicile()));
        filters.setCurrency(toUTF8String(filters.getCurrency()));
//        filters.setType(toUTF8String(fundType));
        filters.setFundType(toUTF8String(filters.getFundType()));
        filters.setSectorType(toUTF8String(filters.getSectorType()));
        filters.setGeoAllocation(toUTF8String(filters.getGeoAllocation()));
        filters.setFundHouseIds(StrUtils.getString(filters.getFundHouseIds()));
       // filters.setFundHouseIds("4A8A08F09D37B73795649038408B5F33,0CC175B9C0F1B6A831C399E269772661");
      //  filters.setFundHouse("贝莱");
        model.put("filters", filters);

        //测试条件
//        filters.setLipperCR_ID("3");
//        filters.setLipperCR("5");
        
        jsonPaging = this.getJsonPaging(request);
        // jsonPaging.setSort("convert_mine(" + jsonPaging.getSort() + ",gbk)");
        // 解决中英文混排问题，modified by michael @20160613
//        jsonPaging.setSort("ifnull(fristPinyin(" + jsonPaging.getSort() + "), pinyin(" + jsonPaging.getSort() + ")) ");
//        
        // 解决orderby字段排序问题，需转换成数字类型，modified by michael @20160613
//        if (jsonPaging.getSort() != null && jsonPaging.getSort().contains("orderBy")) {
//            jsonPaging.setSort("(orderBy+0)");
//        }
        if("riskLevel".equals(jsonPaging.getSort())){
        	jsonPaging.setSort("f.riskLevel");
        }
        jsonPaging = fundInfoService.findFundInfoList(jsonPaging, filters);
        
        return jsonPaging;
    }
    
    /**
     * 分页获得记录for ajax Json 调用
     * 	  与fundsscreener()一样
     * @author michael
     * @param request
     * @param response
     * @param model
     * @param filters 筛选条件
     * @return
     */
    @RequestMapping(value = "/getFundListJson", method = RequestMethod.POST)
    public void getFundListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model, FundScreenerDataVO filters) {
    	jsonPaging = queryFundsByFilters(request, model, filters);

        String lang = this.getLoginLangFlag(request);
        List<FundHouseDataVO> fundHouseList = fundInfoService.loadFundHouseList(lang);
        //this.toJsonString(response, jsonPaging);
        model.put("jsonPaging", jsonPaging);
//        model.put("filters", filters);
        model.put("fundHouseList_in", fundHouseList);
        
        ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
    }
    
    /**
     * 基金详细信息页面
     * @author Michael
     */
    @RequestMapping(value = "/fundsdetail", method = RequestMethod.GET)
    public String fundsDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model, String id, String toCurrency) {
    	MemberBase loginUser = getLoginUser(request);
    	String source = this.toUTF8String(request.getParameter("source"));
    	//String langCode = this.getLoginLangFlag(request);
		String displayColor = "";
		if(null != loginUser) displayColor = loginUser.getDefDisplayColor();
		if(null==displayColor || "".equals(displayColor)){
		    displayColor=CommonConstants.DEF_DISPLAY_COLOR;
		}
		model.put("displayColor", displayColor);
		
//    	String toCurrency = request.getParameter("toCurrency");
        String fundId = StrUtils.getString(id);
        model.put("id", fundId);
        fundInfoVO = fundInfoService.findFundFullInfoById(fundId, this.getLoginLangFlag(request), loginUser==null?null:loginUser.getId(),toCurrency);
        if(fundInfoVO == null)fundInfoVO = new FundInfoDataVO();
        model.put("fundInfoVO", fundInfoVO);
        
        fundFeesList = fundInfoService.findFundFees(fundId,this.getLoginLangFlag(request));
        model.put("fundFeesList", fundFeesList);
        //获取文档
        fundDocsList = fundInfoService.findFundDocs(fundId,this.getLoginLangFlag(request));
        model.put("fundDocsList", fundDocsList);
        
        fundBonusList = fundInfoService.findFundBonus(fundId);
        model.put("fundBonusList", fundBonusList);
        fundAnnoList = fundInfoService.findFundAnnouncement(fundId,this.getLoginLangFlag(request));
        model.put("fundAnnoList", fundAnnoList);
        
        //累积表现
        fundCumReturnList = fundInfoService.findFundReturnList(fundId, "heap", "", this.getLoginLangFlag(request));
        model.put("fundCumReturnList", fundCumReturnList);
        
        //年度表现
        fundYearReturnList = fundInfoService.findFundReturnList(fundId, "year", "", this.getLoginLangFlag(request));
        model.put("fundYearReturnList", fundYearReturnList);
        
        //add wwluo 20161116 往   web_investor_visit 插入浏览记录
    	WebInvestorVisit investorVisit = new WebInvestorVisit();
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DATE, -1);
        List<WebInvestorVisit> investorVisits = fundInfoService.getWebInvestorVisit(loginUser, fundId, calendar.getTime());
        if(investorVisits != null && !investorVisits.isEmpty()){
        	for (WebInvestorVisit webInvestorVisit : investorVisits) {
                webInvestorVisit.setVistiTime(new Date());
                fundInfoService.saveWebInvestorVisit(webInvestorVisit);
			}
        }else{
        	investorVisit.setMember(loginUser);
            investorVisit.setModuleType("fund");
            investorVisit.setRelateId(fundId);
            investorVisit.setVistiTime(new Date());
            fundInfoService.saveWebInvestorVisit(investorVisit);
        }
        //获取基金浏览历史
//        String langCode = this.getLoginLangFlag(request);
//        List<FundInfoDataVO> fundInfoList = new ArrayList<FundInfoDataVO>();
//        List<String> relateIds = fundInfoService.getFundBrowserHistory(loginUser);
//        if(relateIds != null && !relateIds.isEmpty()){
//        	String fundIds = null;
//        	StringBuffer fundIdBuffer = new StringBuffer();
//        	for (String relateId: relateIds) {
//        		fundIdBuffer.append(relateId + ",");
//			}
//        	if(fundIdBuffer.length()>0){
//        		fundIds = fundIdBuffer.substring(0, fundIdBuffer.length()-1);
//        	}
//        	fundInfoList = getFundsData(loginUser, toCurrency, langCode, fundInfoList, fundIds);
//    	}
//        model.put("fundHistoryList", fundInfoList);
//        //1 Month best performance
//        List<FundInfoDataVO> bestFundList = new ArrayList<FundInfoDataVO>();
//        List<FundReturn> fundReturns = fundInfoService.getBestFundReturn("return_period_code_1M",8);
//        if(fundReturns != null && !fundReturns.isEmpty()){
//        	String fundIds = null;
//        	StringBuffer fundIdBuffer = new StringBuffer();
//        	for (FundReturn fundReturn : fundReturns) {
//        		if(fundReturn.getFund() != null){
//        			fundIdBuffer.append(fundReturn.getFund().getId() + ",");
//        		}
//			}
//        	if(fundIdBuffer.length()>0){
//        		fundIds = fundIdBuffer.substring(0, fundIdBuffer.length()-1);
//        	}
//        	bestFundList = getFundsData(loginUser, toCurrency, langCode, bestFundList, fundIds);
//        	model.put("bestFundList", bestFundList);
//        }
        //插入业务日志
        WebBusLog busLog = new WebBusLog();
        String clientType = null;
		String requestHeader = request.getHeader("user-agent");
        String[] deviceArray = new String[]{"android","mac os","windows phone"};
        if(requestHeader != null){
        	requestHeader = requestHeader.toLowerCase();
            for(int i=0;i<deviceArray.length;i++){
                if(requestHeader.indexOf(deviceArray[i])>-1){
                	clientType = deviceArray[i];
                	break;
                }
            }
        }
        busLog.setClientType(clientType);
        busLog.setCreateTime(new Date());
        busLog.setIp(request.getRemoteAddr());
        busLog.setLoginCode((loginUser==null?null:loginUser.getLangCode()));
        busLog.setMember(loginUser);
        busLog.setModuleType("FundInfo");
        busLog.setRelateData(fundId);
        busLog.setNickName((loginUser==null?"":loginUser.getNickName()));
        fundInfoService.saveWebBusLog(busLog);
        //add end
        model.put("source", source);
        
        return "fund/info/fundsdetail";
    }
    
    
	 /**
     * 获取基金浏览历史/表现最好的基金
     */
    @RequestMapping(value = "/bestFundListJson", method = RequestMethod.POST)
    public String existingListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	MemberBase  loginUser = this.getLoginUser(request);    
//    	String memberId = loginUser.getId(); 
    	String langCode = this.getLoginLangFlag(request);
    	String toCurrency = request.getParameter("toCurrency");
    	String dataType = request.getParameter("dataType");
    	
        
        List<FundInfoDataVO> fundInfoList = new ArrayList<FundInfoDataVO>();
    	if("browsing".equalsIgnoreCase(dataType)){
    		//获取基金浏览历史
    		List<String> relateIds = fundInfoService.getFundBrowserHistory(loginUser, 5);
            if(relateIds != null && !relateIds.isEmpty()){
            	String fundIds = null;
            	StringBuffer fundIdBuffer = new StringBuffer();
            	for (String relateId: relateIds) {
            		fundIdBuffer.append(relateId + ",");
    			}
            	if(fundIdBuffer.length() > 0){
            		fundIds = fundIdBuffer.substring(0, fundIdBuffer.length() - 1);
            	}
            	fundInfoList = getFundsData(loginUser, toCurrency, langCode, fundInfoList, fundIds);
        	}
    	}else if ("best".equalsIgnoreCase(dataType)) {
            //1 Month best performance
            List<FundReturn> fundReturns = fundInfoService.getBestFundReturn("return_period_code_1M",8);
            if(fundReturns != null && !fundReturns.isEmpty()){
            	String fundIds = null;
            	StringBuffer fundIdBuffer = new StringBuffer();
            	for (FundReturn fundReturn : fundReturns) {
            		if(fundReturn.getFund() != null){
            			fundIdBuffer.append(fundReturn.getFund().getId() + ",");
            		}
    			}
            	if(fundIdBuffer.length()>0){
            		fundIds = fundIdBuffer.substring(0, fundIdBuffer.length()-1);
            	}
            	fundInfoList = getFundsData(loginUser, toCurrency, langCode, fundInfoList, fundIds);
            }
		}
    	    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	result.put("total",fundInfoList.size());
    	result.put("rows",fundInfoList);
		JsonUtil.toWriter(result, response);
		
        return null;
    }

    /**
     * 获取基金数据
     * @return
     */
	private List<FundInfoDataVO> getFundsData(MemberBase loginUser, String toCurrency,
			String langCode, List<FundInfoDataVO> fundInfoList, String fundIds) {
		if (StringUtils.isNotBlank(fundIds)) {
			if (StringUtils.isBlank(toCurrency) && null!=loginUser.getDefCurrency()) {
				toCurrency = loginUser.getDefCurrency();
			}else{
				toCurrency = CommonConstants.DEF_CURRENCY;
			}
			ArrayList checkList = new ArrayList();
			if(fundIds.indexOf(",") > -1){
				//多个基金
				String[] idArrStrings = fundIds.split(",");
		        if (idArrStrings != null && idArrStrings.length > 0){
		        	for(int k=0;k<idArrStrings.length;k++){
		        		String x = idArrStrings[k].toString();
		        		String fund = x;
		        		if(fund!=null && fund.length()>0 && !checkList.contains(fund)){
		        			checkList.add(fund);
		        			FundInfoDataVO fundInfoVO = fundInfoService.findFundFullInfoById(fund);
		        			fundInfoVO.setFundId(fund);
		        			fundInfoVO = fundInfoService.setFundInfos(fundInfoVO,langCode,(null==loginUser?"":loginUser.getId()),toCurrency,null);
		        			fundInfoList.add(fundInfoVO);
		        		}
		        	}
		        }
			}else{
				//单个基金
				checkList.add(fundIds);
				FundInfoDataVO fundInfoVO = fundInfoService.findFundFullInfoById(fundIds);
				fundInfoVO.setFundId(fundIds);
				fundInfoVO = fundInfoService.setFundInfos(fundInfoVO,langCode,loginUser.getId(),toCurrency,null);
				fundInfoList.add(fundInfoVO);
			}
		}
		return fundInfoList;
	}
    
    /**
     * 获取图标数据
     * @author Michael
     * @param request
     * @param response
     * @param model
     * @param id 基金id
     * @param ids 基金id列表
     * @param chart 图表类型
     * @param period 基金id列表
     * @return
     */
    @RequestMapping(value = "/getChartData", method = RequestMethod.POST)
    public void getChartData(HttpServletRequest request, HttpServletResponse response, ModelMap model, 
    		String id, String ids, String chart, String period) {
    	
        String fundId = StrUtils.getString(id);
        String fundIds = StrUtils.getString(ids);//用于基金比较
        String chartModel = StrUtils.getString(chart);
//        String startDate = StrUtils.getString(request.getParameter("startDate"));
//        String endDate = StrUtils.getString(request.getParameter("endDate"));
        period = StrUtils.getString(period);
        
        model.put("id", fundId);

		String startDate = DateUtil.getCurDateStr();
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

		//开始日期统一提前一天
		startDate = DateUtil.addDateToString(startDate,Calendar.DATE, -1);
		
        List data = new ArrayList();
        
        //获取单个基金的图表数据
        if (fundId.length()>0){
	        FundPortfolio queryPortfolio = new FundPortfolio();
			queryPortfolio.setFundId(fundId);
			if (null!=chartModel && "market".equals(chartModel)){
				//区域划分的投资组合饼图
				queryPortfolio.setType("market");
		        fundPortfolioListByRegion = fundPortfolioService.findPortfolioByType(queryPortfolio, this.getLoginLangFlag(request));
		        if (fundPortfolioListByRegion!=null)
		        	for (FundCompositionDataVO vo: fundPortfolioListByRegion ){
		        		JSONObject jsonObject = new JSONObject();
		                jsonObject.put("name", vo.getItemName());
		                jsonObject.put("value", vo.getValue());
		        		data.add(jsonObject);
		        	}
		        model.put("chartData", JSONArray.fromObject(data).toString());
			}else if (null!=chartModel && "sector".equals(chartModel)){
		        //行业划分的投资组合饼图
				queryPortfolio.setType("sector");
		        fundPortfolioListByIndustry = fundPortfolioService.findPortfolioByType(queryPortfolio, this.getLoginLangFlag(request));
		        
		        if (fundPortfolioListByIndustry!=null)
		        	for (FundCompositionDataVO vo: fundPortfolioListByIndustry ){
		        		JSONObject jsonObject = new JSONObject();
		                jsonObject.put("name", vo.getItemName()+"("+vo.getValue()+"%)");
		                jsonObject.put("value", vo.getValue());
		        		data.add(jsonObject);
		        	}
		        model.put("chartData", JSONArray.fromObject(data).toString());
			}else if (null!=chartModel && "price".equals(chartModel)){
		        //基金行情曲线图
		        fundPriceList = fundInfoService.findFundMarketList(fundId, "day", startDate, endDate, this.getLoginLangFlag(request));
		        JSONArray array = new JSONArray();
		        if (fundPriceList!=null)
		        	for (GeneralDataVO vo: fundPriceList ){
		        		JSONObject jsonObject = new JSONObject();
		        		jsonObject.put("id", vo.getFundId());
		        		jsonObject.put("name", vo.getName());
		                jsonObject.put("date", DateUtil.dateToDateString(vo.getUpdateTime(),CoreConstants.DATE_FORMAT));
		                jsonObject.put("open", vo.getOpenPrice());
		                jsonObject.put("low", vo.getLowPrice());
		                jsonObject.put("hight", vo.getHightPrice());
		                jsonObject.put("close", vo.getClosePrice());
		                jsonObject.put("nav", vo.getNav());
		                jsonObject.put("return", vo.getDayReturn());
		                array.add(jsonObject);
		                data.add(jsonObject);
		        	}
		        model.put("chartData", array);
			}
        }
        if (fundIds.length()>0 && "comparison".equals(chartModel)){
        	ArrayList checkList = new ArrayList();
            String[] idArrStrings = fundIds.split(",");
            if (idArrStrings!=null && idArrStrings.length>0){
            	JSONArray chartDataList = new JSONArray();
            	//for(String x: idArrStrings){
            	for(int m=0;m<idArrStrings.length;m++){
            		String x = idArrStrings[m].toString();
            		if(x==null || x.length()==0 || checkList.contains(x)) continue;
            		checkList.add(x);
            		
            		//基金行情曲线图
            		try {
	    		        fundPriceList = fundInfoService.findFundMarketList(x, "day", startDate, endDate, this.getLoginLangFlag(request));
	    		        JSONArray array = new JSONArray();
	    		        if (fundPriceList!=null){
	    		        	for (GeneralDataVO vo: fundPriceList ){
	    		        		JSONObject jsonObject = new JSONObject();
	    		        		jsonObject.put("id", vo.getFundId());
	    		        		jsonObject.put("name", vo.getName());
	    		                jsonObject.put("date", DateUtil.dateToDateString(vo.getUpdateTime(),CoreConstants.DATE_FORMAT));
	    		                jsonObject.put("open", vo.getOpenPrice());
	    		                jsonObject.put("low", vo.getLowPrice());
	    		                jsonObject.put("hight", vo.getHightPrice());
	    		                jsonObject.put("close", vo.getClosePrice());
	    		                jsonObject.put("nav", vo.getNav());
	    		                jsonObject.put("return", vo.getDayReturn());
	    		                array.add(jsonObject);
//	    		                data.add(jsonObject);
	    		        	}
	    		        }
	    		        chartDataList.add(array);
	    		        data.add(array);
					} catch (Exception e) {
						// TODO: handle exception
					}
            	}
		        model.put("chartDataList", chartDataList);
            }
        }
		ResponseUtils.renderHtml(response,JsonUtil.toJson(data));
    }
    
    /***
	 * 我关注的基金列表
	 * */
	@RequestMapping(value = "/collectionList", method = RequestMethod.GET)
	public String collectionList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);//获取当前登录用户
		jsonPaging = this.getJsonPaging(request);
		String memberId = null;;
//		if(null==loginMemberBase){//测试
//			memberId = "ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh";
//		}else{
		memberId = loginUser.getId();
//		}
		String lang = this.getLoginLangFlag(request);
		String moduleType = "fund";//基金类型
		jsonPaging = webFollowStatusService.getWebFollowList(memberId, moduleType,lang, jsonPaging);
		model.put("jsonPaging", jsonPaging);
		return "fund/info/fundcollection"; 
		
	}
	
	@RequestMapping(value = "/collectionListJson", method = RequestMethod.GET)
	public void collectionListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);//获取当前登录用户
		jsonPaging = this.getJsonPaging(request);
//		jsonPaging = new JsonPaging();
		String memberId = null;;
		if(null==loginUser){//测试
			memberId = "ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh";
		}else{
			memberId = loginUser.getId();
		}
		String lang = this.getLoginLangFlag(request);
		String moduleType = "fund";//基金类型
		jsonPaging = fundInfoService.getWebFollowList(memberId, moduleType, lang, jsonPaging);
		ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
	}
	
	
	
	/**
	 * 关注/取消关注基金
	 * @author scshi
	 * @param relateId 对应类型id
	 * @param memberId 网站会员ID
	 * @parame opType	Follow-设置关注;Delete-取消关注
	 * @param moduleType 对应模块,fund基金关注,ifa关注,article文章关注
	 * */
	@RequestMapping(value = "/setFoundCollection", method = RequestMethod.GET)
	public void  setFoundCollection(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberBase loginUser = this.getLoginUser(request);//获取当前登录用户

		if (null!=loginUser){
//			String relateId = request.getParameter("fundId");
//			String moduleType = "fund";
			String productId = request.getParameter("productId");
			String typeId = request.getParameter("typeId");
			String opType = request.getParameter("opType");
			String typeName=request.getParameter("typeName");
			String memberId = null;
	//		if(null==loginMemberBase){//测试
	//			memberId = "ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh";
	//		}else{
			memberId = loginUser.getId();
	//		}
			//WebFollow follow = webFollowStatusService.saveWebFollowStatus(relateId, memberId, opType, moduleType);
			if(null!=typeName && !"".equals(typeName)){
				typeName=toUTF8String(typeName);
			}
			if(!"Delete".equals(opType)){
				if(null==typeId || "".equals(typeId)){
	            	WebWatchlistType type=webWatchListService.saveWatchlistType(typeName, loginUser);
	            	obj.put("type", type);
	            	typeId=type.getId();
	            }else{
	            	WebWatchlistType type=webWatchListService.findTypeById(typeId);
	            	type.setName(typeName);
	            	type=webWatchListService.saveType(type);
	            }
			}
            
			
			WebWatchlist watch = webWatchListService.saveWebWatchlist(productId, memberId, opType, typeId);
			obj.put("watchId", watch==null?"":watch.getId());
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
			
		}else{
			obj.put("watchId", "");
			obj.put("result",false);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.fail",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	
	/**
     * 基金比较页面
     * @author Michael
     * @param request
     * @param response
     * @param model
     * @param id 	基金id
     * @param toCurrency 转换成货币
     * @return
     */
    @RequestMapping(value = "/fundscomparison", method = RequestMethod.GET)
    public String fundsComparison(HttpServletRequest request, HttpServletResponse response, ModelMap model, String id, String toCurrency) {
    	List<FundInfoDataVO> fundInfoList = new ArrayList<FundInfoDataVO>();
        List<GeneralDataVO> fundHouseList = new ArrayList<GeneralDataVO>();
        fundHouseList = fundInfoService.findFundHouseList(this.getLoginLangFlag(request));
        model.put("fundHouseList", fundHouseList);
    	
        MemberBase loginUser = this.getLoginUser(request);
        if (StringUtils.isBlank(toCurrency)) {
        	toCurrency = loginUser.getDefCurrency();
        	if (StringUtils.isBlank(toCurrency)) {
        		toCurrency = CommonConstants.DEF_CURRENCY;
    		}
		}
        
    	//获取基金集合
    	getFundListByIds(request, response, model, id, toCurrency);
    	List<FundInfoDataVO> fundList =  (List<FundInfoDataVO>)model.get("fundInfoList");
    	List<String> regionKeyList = new ArrayList<String>();
    	List<String> industryKeyList = new ArrayList<String>();
    	
    	if (null!=fundList && !fundList.isEmpty()){
    		for (FundInfoDataVO fundInfoVO: fundList){

                FundPortfolio queryPortfolio = new FundPortfolio();
                //区域划分的投资组合
	      		queryPortfolio.setFundId(fundInfoVO.getFundId());
	      		queryPortfolio.setType("market");
	      		List<FundCompositionDataVO> fundPortfolioListByRegion = fundPortfolioService.findPortfolioByType(queryPortfolio, this.getLoginLangFlag(request));
	      		fundInfoVO.setFundPortfolioListByRegion(fundPortfolioListByRegion);
	      		if (null!=fundPortfolioListByRegion && !fundPortfolioListByRegion.isEmpty()){
	      			for(FundCompositionDataVO v: fundPortfolioListByRegion)
	      				if (!regionKeyList.contains(v.getItemName())) regionKeyList.add(v.getItemName());
	      		}
	      		
	            //行业划分的投资组合
      			queryPortfolio.setType("sector");
      			List<FundCompositionDataVO> fundPortfolioListByIndustry = fundPortfolioService.findPortfolioByType(queryPortfolio, this.getLoginLangFlag(request));
      			fundInfoVO.setFundPortfolioListByIndustry(fundPortfolioListByIndustry);
      			if (null!=fundPortfolioListByIndustry && !fundPortfolioListByIndustry.isEmpty()){
	      			for(FundCompositionDataVO v: fundPortfolioListByIndustry)
	      				if (!industryKeyList.contains(v.getItemName())) industryKeyList.add(v.getItemName());
	      		}
      			
                fundInfoList.add(fundInfoVO);
    		}
    		
            //构建投资组合对象对比列表
            //--区域划分
            List<FundPortfolioListDataVO> portfolioListByRegion = new ArrayList<FundPortfolioListDataVO>();
            if (null!=regionKeyList && !regionKeyList.isEmpty()){
            	for(String key: regionKeyList){
            		FundPortfolioListDataVO listByKey = new FundPortfolioListDataVO();
            		listByKey.setType("market");
            		listByKey.setName(key);
            		for(FundInfoDataVO info: fundInfoList){
            			boolean exists = false;
            			if (info.getFundPortfolioListByRegion()!=null){
            				for(FundCompositionDataVO portfolio: info.getFundPortfolioListByRegion()){
            					if (portfolio.getItemName().equals(key)){
            						listByKey.addPortfolio(portfolio);
            						exists = true;
            						break;
            					}
            				}
            				if (!exists) listByKey.addPortfolio(new FundCompositionDataVO());
            			}else{
            				listByKey.addPortfolio(new FundCompositionDataVO());
            			}
            		}
            		portfolioListByRegion.add(listByKey);
            	}
            }
            model.put("portfolioListByRegion", portfolioListByRegion);
            
            //--行业划分
            List<FundPortfolioListDataVO> portfolioListByIndustry = new ArrayList<FundPortfolioListDataVO>();
            if (null!=industryKeyList && !industryKeyList.isEmpty()){
            	for(String key: industryKeyList){
            		FundPortfolioListDataVO listByKey = new FundPortfolioListDataVO();
            		listByKey.setType("sector");
            		listByKey.setName(key);
            		for(FundInfoDataVO info: fundInfoList){
            			boolean exists = false;
            			if (info.getFundPortfolioListByIndustry()!=null){
            				for(FundCompositionDataVO portfolio: info.getFundPortfolioListByIndustry()){
            					if (portfolio.getItemName().equals(key)){
            						listByKey.addPortfolio(portfolio);
            						exists = true;
            						break;
            					}
            				}
            				if (!exists) listByKey.addPortfolio(new FundCompositionDataVO());
            			}else{
            				listByKey.addPortfolio(new FundCompositionDataVO());
            			}
            		}
            		portfolioListByIndustry.add(listByKey);
            	}
            }
            model.put("portfolioListByIndustry", portfolioListByIndustry);
            
    	}
        
    	model.put("fundInfoList", fundInfoList);
    	
        return "fund/info/fundscomparison";
    }
    
    /**
     * 用基金id搜索基金信息，多个id以逗号分隔
     * @author michael
     * @param request
     * @param response
     * @param model
     * @param id 	基金id
     * @param toCurrency 转换成货币
     * @return
     */
    @SuppressWarnings("unchecked")
    private void getFundListByIds(HttpServletRequest request, HttpServletResponse response, ModelMap model, String id, String toCurrency) {
    	MemberBase loginUser = getLoginUser(request);//获取当前登录用户
//    	String toCurrency = request.getParameter("toCurrency");
    	
    	List<FundInfoDataVO> fundInfoList = new ArrayList<FundInfoDataVO>();
    	//比较页面的id允许有多个值，以逗号分隔
        String ids = StrUtils.getString(id);
//        ids=(ids==null?"":ids);
        ArrayList checkList = new ArrayList();
        String[] idArrStrings = ids.split(",");
        if (idArrStrings!=null && idArrStrings.length>0){
        	for(int k=0;k<idArrStrings.length;k++){
        		String x = idArrStrings[k].toString();
        		String fundId = x;
        		if(fundId!=null && fundId.length()>0 && !checkList.contains(fundId)){
        			checkList.add(fundId);
        			FundInfoDataVO fundInfoVO = fundInfoService.findFundFullInfoById(fundId, this.getLoginLangFlag(request), loginUser==null?null:loginUser.getId(), toCurrency);
        			fundInfoList.add(fundInfoVO);
        		}
        	}
        	
        }
        ids = StrUtils.arrayListToString(checkList, ",");
        model.put("id", ids);
        model.put("fundInfoList", fundInfoList);
    }
    
    /**
     * 基金选择页面对应的基金列表（嵌入） -- 以基金id作为传入参数。
     * @author michael
     * @param request
     * @param response
     * @param model
     * @param id 	基金id
     * @param toCurrency 转换成货币
     * @param view  只读状态，true只读
     * @return
     */
	@RequestMapping(value = "/getFundListForSelect", method = RequestMethod.GET)
    public String getFundListForSelect(HttpServletRequest request, HttpServletResponse response, ModelMap model, String id, String toCurrency, String view) {
		//获取基金集合
    	getFundListByIds(request, response, model, id, toCurrency);
//    	List<FundInfoDataVO> fundList =  (List<FundInfoDataVO>)model.get("fundInfoList");
//    	model.put("fundInfoList", fundInfoList);
    	model.put("view", StrUtils.getString(view,"true"));
    	model.put("showCart", StrUtils.getString(request.getParameter("showCart"),"false"));
    	model.put("fav", StrUtils.getString(request.getParameter("fav"),"true"));
    	model.put("select", StrUtils.getString(request.getParameter("select"),"false"));
    	MemberBase loginUser = getLoginUser(request);
    	String displayColor = loginUser.getDefDisplayColor();
    	if (StringUtils.isBlank(displayColor)) {
    		displayColor = CommonConstants.DEF_DISPLAY_COLOR;
		}
    	model.put("displayColor", displayColor);
    	return "fund/info/fundListForSelect";
    }
    
    /**
     * 获取基金列表数据(用于基金比较中的下拉选择)
     * @author Michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getFundList", method = RequestMethod.POST)
    public void getFundList(HttpServletRequest request, HttpServletResponse response, ModelMap model, String houseId, String keyword) {
        keyword = toUTF8String(keyword);

        model.put("houseId", houseId);
        model.put("keyword", keyword);
        List data = new ArrayList();
        data = fundInfoService.findFundListByHouse(houseId, keyword, this.getLoginLangFlag(request));
        
		ResponseUtils.renderHtml(response,JsonUtil.toJson(data));
    }
    
    /**
     * 基金汇率转换
     * @author stxxx
     * @date 20160704
     * */
    @RequestMapping(value = "/fundExchage", method = RequestMethod.GET)
    public void fundExchage(HttpServletRequest request, HttpServletResponse response, ModelMap model){
//    	String fromCurrency = request.getParameter("fromCurrency");
//    	String toCurrency = request.getParameter("toCurrency");
    	List<WebExchangeRate> exchangeList = fundInfoService.loadExchangeList();
    	Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("list", exchangeList);
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		JsonUtil.toWriter(obj, response);
    }
    
    /**
     * 获取图标数据
     * @author Michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/findCountry", method = RequestMethod.POST)
    public void findCountry(HttpServletRequest request, HttpServletResponse response, ModelMap model, String keyWord) {
    	
        keyWord = StrUtils.getString(keyWord);
        JsonPaging jsonpaging = new JsonPaging();
        jsonpaging.setPage(1);
        jsonpaging.setRows(null);
        jsonpaging = sysCountryService.findAll(jsonpaging, keyWord, keyWord);
        ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonpaging.getList()));
    }
    
    /**
     * 基金选择器
     * @author Michael
     * @param request
     * @param response
     * @param model
     * @param houseId 基金公司id
     * @param keyword 搜索关键词
     * @return
     */
	@RequestMapping(value = "/fundSelector", method = RequestMethod.GET)
    public String fundSelector(HttpServletRequest request, HttpServletResponse response, ModelMap model, String houseId, String keyword) {
        List<GeneralDataVO> fundHouseList = new ArrayList<GeneralDataVO>();
        fundHouseList = fundInfoService.findFundHouseList(this.getLoginLangFlag(request));
        model.put("fundHouseList", fundHouseList);
        
        model.put("houseId", houseId);
        keyword = toUTF8String(keyword);
        model.put("keyword", keyword);
        
		//基金选择器页面
    	return "fund/info/fundSelector";
    }
	
	
    /**
     * 获取基金列表数据(用于策略分配（第3版）对应的基金选择)
     * @author Michael
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getFundListForAllocationJson", method = RequestMethod.POST)
    public void getFundListForAllocationJson(HttpServletRequest request, HttpServletResponse response, ModelMap model, String view) {
    	String rows = StrUtils.filterNullString(request.getParameter("rows"),"10");
    	String page = StrUtils.filterNullString(request.getParameter("page"),"1");
    	jsonPaging.setRows(StrUtils.getInteger(rows));
    	jsonPaging.setPage(StrUtils.getInteger(page));
    	
    	jsonPaging = findFundListForAllocation(request, response, model);
    	model.put("fundInfoList", jsonPaging.getList());
        
    	view = StrUtils.getString(view);
    	if ("true".equals(view)) model.put("view", view);
        
//		ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging.getList()));
    	ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
    }
    
    /**
     * 策略分配对应的基金列表页面(显示或选择)
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
	@RequestMapping(value = "/getFundListForAllocation", method = RequestMethod.GET)
    public String getFundListForAllocation(HttpServletRequest request, HttpServletResponse response, ModelMap model, String view) {
		//获取基金集合
    	jsonPaging = findFundListForAllocation(request, response, model);
    	model.put("fundInfoList", jsonPaging.getList());
        
    	view = StrUtils.getString(view);
    	if ("true".equals(view)) model.put("view", "true");
    	else model.put("view", "false");
    	
//    	String select = StrUtils.getString(request.getParameter("select"));
//    	if ("true".equals(select)) model.put("select", "true");
//    	else model.put("select", "false");
    	model.put("select", "true");
    	
    	return "fund/info/fundListForAllocation";
    }
	
    /**
     * 投资策略中所选的基金列表页面(显示或编辑)
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
	@RequestMapping(value = "/getFundListForStrategy", method = RequestMethod.GET)
    public String getFundListForStrategy(HttpServletRequest request, HttpServletResponse response, ModelMap model, String view, String showCart) {
		//获取基金集合
		if (null!=jsonPaging) jsonPaging.setRows(9999);//不分页
    	jsonPaging = findFundListForAllocation(request, response, model);
    	model.put("fundInfoList", jsonPaging.getList());
        
    	view = StrUtils.getString(view);
    	if ("true".equals(view)) model.put("view", "true");
    	else model.put("view", "false");

    	showCart = StrUtils.getString(showCart);
    	if ("true".equals(showCart)) model.put("showCart", "true");
    	else model.put("showCart", "false");

    	
//    	String select = StrUtils.getString(request.getParameter("select"));
//    	if ("true".equals(select)) model.put("select", "true");
//    	else model.put("select", "false");
    	model.put("select", "false");
    	
    	return "fund/info/fundListForStrategy";
    }
	
    /**
     * 投资组合中所选的基金列表页面(显示或编辑)
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
	@RequestMapping(value = "/getFundListForPortfolio", method = RequestMethod.GET)
    public String getFundListForPortfolio(HttpServletRequest request, HttpServletResponse response, ModelMap model, String view, String showCart) {
		//获取基金集合
		if (null!=jsonPaging) jsonPaging.setRows(9999);//不分页
    	jsonPaging = findFundListForAllocation(request, response, model);
    	model.put("fundInfoList", jsonPaging.getList());
        
    	view = StrUtils.getString(view);
    	if ("true".equals(view)) model.put("view", "true");
    	else model.put("view", "false");

    	showCart = StrUtils.getString(showCart);
    	if ("true".equals(showCart)) model.put("showCart", "true");
    	else model.put("showCart", "false");
    	
//    	String select = StrUtils.getString(request.getParameter("select"));
//    	if ("true".equals(select)) model.put("select", select);
//    	else model.put("select", "false");
    	model.put("select", "false");
    	
    	return "fund/info/fundListForPortfolio";
    }
	
    /**
     * 查找策略分配页面对应的基金列表 -- 以基金类型等传入参数。
     * *** 此为公共方法，修改时请注意 ***
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
	private JsonPaging findFundListForAllocation(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//获取基金集合
    	MemberBase loginUser = getLoginUser(request);//获取当前登录用户
    	
   	    String houseId = request.getParameter("houseId");
        model.put("houseId", houseId);
        String keyword = toUTF8String(request.getParameter("keyword"));
        model.put("keyword", keyword);
        String strategyId = request.getParameter("strategyId");
        model.put("strategyId", strategyId);
    	String toCurrency = request.getParameter("toCurrency");
    	String regions = request.getParameter("regions");
        model.put("regions", regions);
    	String sectors = request.getParameter("sectors");
        model.put("sectors", sectors);
    	String types = request.getParameter("types");
        model.put("types", types);
        String distributors = request.getParameter("distributors");
        model.put("distributors", distributors);
    	
    	FundScreenerDataVO filters = new FundScreenerDataVO();
    	filters.setFundHouseIds(houseId);
    	filters.setKeyword(keyword);
    	filters.setGeoAllocation(regions);
    	filters.setSectorType(sectors);
    	filters.setFundType(types);
    	filters.setCurrency(toCurrency);
    	filters.setLoginUser(loginUser);
    	filters.setStrategyId(strategyId);
    	filters.setLangCode(this.getLoginLangFlag(request));
    	filters.setDistributorId(distributors);
    	return fundInfoService.findFundInfoListForAllocation(jsonPaging, filters);
    	
	}

    
	 /**
     * 基金选择器  -- 用于投资策略和组合的基金选择，以区域、板块、基金类型传入参数。
     * @author michael
     * @param request
     * @param response
     * @param model
     * @return
     */
	@RequestMapping(value = "/fundSelectorForAllocation", method = RequestMethod.GET)
    public String fundSelectorForAllocation(HttpServletRequest request, HttpServletResponse response, ModelMap model, String regions, String sectors, String types,String distributors) {
		//获取基金集合
        model.put("regions", regions);
        model.put("sectors", sectors);
        model.put("types", types);
        model.put("distributors", distributors);
    	
//    	jsonPaging = findFundListForAllocation(request, response, model);
//    	model.put("fundInfoList", jsonPaging.getList());

    	//基金公司列表
        List<GeneralDataVO> fundHouseList = new ArrayList<GeneralDataVO>();
        fundHouseList = fundInfoService.findFundHouseList(this.getLoginLangFlag(request));
        model.put("fundHouseList", fundHouseList);
        
    	//地区列表
        List<GeneralDataVO> itemList = findSysParameters("region", this.getLoginLangFlag(request));
        itemList = filterValues(itemList,regions);
        model.put("regionList", itemList);
        
        //主题分类
        itemList = findSysParameters("fund_sector", this.getLoginLangFlag(request));
        itemList = filterValues(itemList,sectors);
        model.put("sectorList", itemList);
        
        //基金分类
        itemList = findSysParameters("fund_type", this.getLoginLangFlag(request));
        itemList = filterValues(itemList,types);
        model.put("fundTypeList", itemList);
        
        //代理商列表
        List<MemberDistributor> dList = new ArrayList<MemberDistributor>();
    	if (StringUtils.isNotBlank(distributors)) {
    		String[] darray = distributors.split(",");
    		for(String dId : darray){
    			if(StringUtils.isNotBlank(dId)){
    				MemberDistributor each = distributorService.findDistributorById(dId);
    				dList.add(each);
    			}
    		}
    	}
    	model.put("distributorList", dList);
        
        //搜索框中的基金分类，显示全部
        itemList = findSysParameters("fund_type", this.getLoginLangFlag(request));
        model.put("searchFundTypeList", itemList);

    	return "fund/info/fundSelectorForAllocation";
    }
	
	private List<GeneralDataVO> filterValues(List<GeneralDataVO> itemList, String values){
		List<GeneralDataVO> result = new ArrayList<GeneralDataVO>();
		if (null!=itemList && !itemList.isEmpty() && StrUtils.getString(values).length()>0){
			for (GeneralDataVO vo: itemList){
				if (StrUtils.getString(vo.getItemCode()).length()>0 && values.indexOf(vo.getItemCode())>=0)
					result.add(vo);
			}
		}
		return result;
	}
	
    /**
     * 获取货币汇率
     * @author wwluo
     * @param request
     * @param response
     * @param model
     * @return
     */
	@RequestMapping(value = "/getExchangeRate", method = RequestMethod.POST)
    public void getExchangeRate(HttpServletRequest request, HttpServletResponse response, ModelMap model, String fromCurrency, String toCurrency) {
		Map<String, Object> result = new HashMap<String, Object>();
		WebExchangeRate exchangeRate = null;
		boolean flag = false;
		if (StringUtils.isNotBlank(fromCurrency) && 
				StringUtils.isNotBlank(toCurrency)) {
			exchangeRate = fundInfoService.findExchangeRate(fromCurrency, toCurrency);
			flag = true;
		}
		result.put("flag", flag);
		result.put("exchangeRate", exchangeRate);
		JsonUtil.toWriter(result, response);
    }
	
	/**
     * 汇总基金组合（Ecahrt饼图）
     * @author wwluo
     * @date 2017/2/2/23
     * @param request
     * @param response
     * @param model
     * @return
     */
	@RequestMapping(value = "/getPieData")
	public void getPieData(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String langCode = getLoginLangFlag(request);
		boolean flag = false;
		String funds = this.toUTF8String(request.getParameter("funds"));
		Map<String, Double> fundPortfolioMarket = null;
		Map<String, Double> fundPortfolioSector = null;
		if (StringUtils.isNotBlank(funds)) {
			fundPortfolioMarket = fundInfoService.getfundPortfolioWeight(funds,"market",langCode);
			fundPortfolioSector = fundInfoService.getfundPortfolioWeight(funds,"sector",langCode);
			flag = true;
		}
		result.put("flag", flag);
		result.put("market", fundPortfolioMarket);
		result.put("sector", fundPortfolioSector);
		JsonUtil.toWriter(result, response);
	}
	
    /**********************************
     * 
     *   getter and setter methods
     * 
     **********************************/
	public FundInfoDataVO getFundInfoVO() {
		return fundInfoVO;
	}

	public void setFundInfoVO(FundInfoDataVO fundInfoVO) {
		this.fundInfoVO = fundInfoVO;
	}
	
	/**
     * 获取基金详情管理中区域分布等二个饼图的图标数据（特殊处理）
     * @author wwlin
     * @param request
     * @param response
     * @param model
     * @param id 基金id
     * @param chart 图表类型
     * @return
     */
    @RequestMapping(value = "/getIndustrialChartData", method = RequestMethod.POST)
    public void getIndustrialChartData(HttpServletRequest request, HttpServletResponse response, ModelMap model, 
    		String id, String ids, String chart, String period) {
    	
        String fundId = StrUtils.getString(id);
        String chartModel = StrUtils.getString(chart);
        period = StrUtils.getString(period);
        
        model.put("id", fundId);

		String startDate = DateUtil.getCurDateStr();
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

		//开始日期统一提前一天
		startDate = DateUtil.addDateToString(startDate,Calendar.DATE, -1);
		
        List data = new ArrayList();
        
        //获取单个基金的图表数据
        if (fundId.length()>0){
	        FundPortfolio queryPortfolio = new FundPortfolio();
			queryPortfolio.setFundId(fundId);
			if (null!=chartModel && "market".equals(chartModel)){
				//区域划分的投资组合饼图
				queryPortfolio.setType("market");
		        fundPortfolioListByRegion = fundPortfolioService.findPortfolioByType(queryPortfolio, this.getLoginLangFlag(request));
		        if (fundPortfolioListByRegion!=null)
		        	for (FundCompositionDataVO vo: fundPortfolioListByRegion ){
		        		JSONObject jsonObject = new JSONObject();
		        		String name = vo.getItemName();
		        		String value = vo.getValue().toString();
		        		if(value.startsWith("."))name = name+"(0" +value +"%)";
		        		else name = name + "("+value+"%)";
		                jsonObject.put("name", name);
		                jsonObject.put("value", vo.getValue());
		        		data.add(jsonObject);
		        	}
		        model.put("chartData", JSONArray.fromObject(data).toString());
			}else if (null!=chartModel && "sector".equals(chartModel)){
		        //行业划分的投资组合饼图
				queryPortfolio.setType("sector");
		        fundPortfolioListByIndustry = fundPortfolioService.findPortfolioByType(queryPortfolio, this.getLoginLangFlag(request));
		        
		        if (fundPortfolioListByIndustry!=null)
		        	for (FundCompositionDataVO vo: fundPortfolioListByIndustry ){
		        		JSONObject jsonObject = new JSONObject();
		        		String name = vo.getItemName();
		        		String value = vo.getValue().toString();
		        		if(value.startsWith("."))name = name+"(0" +value +"%)";
		        		else name = name + "("+value+"%)";
		                jsonObject.put("name", name);
		                jsonObject.put("value", vo.getValue());
		        		data.add(jsonObject);
		        	}
		        model.put("chartData", JSONArray.fromObject(data).toString());
			}
        }

		ResponseUtils.renderHtml(response,JsonUtil.toJson(data));
    }
    
    /**
	 * 获取基金价格行情信息列表
	 * @author wwlin
	 * @data 2017-5-19
	 * @param request
	 * @param response
	 * @param model
	 */						
	@RequestMapping(value = "/findFundMarketList")
	public void findFundMarketList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String langCode = getLoginLangFlag(request);
		boolean flag = false;
		try {
			String fundIds = request.getParameter("fundIds");
			String period = request.getParameter("period");
			String type = request.getParameter("type");
			List<CoreFundNavVO> fundMarketDataVOs = new ArrayList<CoreFundNavVO>();
			if (StringUtils.isNotBlank(fundIds)) {
				fundMarketDataVOs = coreFundService.getFundNav(fundIds, period, "");
			}
			result.put("fundMarketDataVOs", fundMarketDataVOs);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取基金通告信息
	 * @author wwlin
	 * @data 2017-5-19
	 * @param request
	 * @param response
	 * @param model
	 */						
	@RequestMapping(value = "/findFundAnnoDetail")
	public void findFundAnnoDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		FundAnno anno = new FundAnno();
		String id = request.getParameter("id");
		if (StringUtils.isNotBlank(id)) {
			anno = fundAnnoService.fundAnncInfo(id);
		}
		JsonUtil.toWriter(anno, response);
	}
	
}
