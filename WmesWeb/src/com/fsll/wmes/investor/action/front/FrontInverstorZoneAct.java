package com.fsll.wmes.investor.action.front;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmProposalService;
import com.fsll.wmes.crm.vo.ProposalNumberVO;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.ProductType;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.AccountNumberVO;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.investor.vo.InvestorAccountCurrencyVO;
import com.fsll.wmes.investor.vo.MyAssetChartDataVO;
import com.fsll.wmes.investor.vo.MyAssetsVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.portfolio.vo.AssetsVO;
import com.fsll.wmes.portfolio.vo.HoldChartVO;
import com.fsll.wmes.portfolio.vo.HoldProductVO;
import com.fsll.wmes.portfolio.vo.InvestorPortfolioDataVO;
import com.fsll.wmes.portfolio.vo.PortfolioAssetsVO;
import com.fsll.wmes.portfolio.vo.PortfolioHoldCumperfSimpleVO;
import com.fsll.wmes.strategy.vo.CharDataVO;

/***
 * 投资人zoneAction
 * @author mqzou
 * @date 2016-10-9
 */
@Controller
@RequestMapping("/front/investor/zone")
public class FrontInverstorZoneAct extends WmesBaseAct {

	@Autowired
	private PortfolioHoldService portfolioHoldService;
	@Autowired
	private FundInfoService fundInfoService;
	@Autowired
	private InvestorService investorService;
	@Autowired
	private CrmProposalService crmProposalService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private SysParamService sysParamService;
	/**
	 * 我的资产（投资人）
	 * @author mqzou
	 * @date 2016-10-09
	 * @return
	 */
	@RequestMapping(value = "/myAssets", method = RequestMethod.GET)
	public String myAssetsPage(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		if(null!=loginUser){
			String defCurrency=request.getParameter("c");
			if(null==defCurrency || "".equals(defCurrency))defCurrency=loginUser.getDefCurrency();
			if(null==defCurrency || "".equals(defCurrency))defCurrency=CommonConstants.DEF_CURRENCY;
			String dateFormat=loginUser.getDateFormat();
			if(null==dateFormat || "".equals(dateFormat)){
				dateFormat=CommonConstants.FORMAT_DATE;
			}
			List<GeneralDataVO>  itemList=findSysParameters("currency_type", langCode);
 	        model.put("currencyType", itemList);
 	        
			MyAssetsVO myAssetsVO=tradeCoreService.findInvestorAssets(loginUser.getId(), defCurrency);
			if(null!=myAssetsVO){
				String currency=sysParamService.findNameByCode(myAssetsVO.getCurrency(), langCode);
				myAssetsVO.setCurrency(currency);
			}
			if(null==myAssetsVO)
				myAssetsVO=new MyAssetsVO();
			model.put("assetsVO", myAssetsVO);
			List<PortfolioAssetsVO> portfolioList=portfolioHoldService.getPortfolioAssets(loginUser.getId(),"","","1M",defCurrency);
			if(null!=portfolioList && !portfolioList.isEmpty()){
				double totalAssets=0;
				Iterator it=portfolioList.iterator();
				while (it.hasNext()) {
					PortfolioAssetsVO vo = (PortfolioAssetsVO) it.next();
					double assets=null!=vo.getTotalAssets() && !"".equals(vo.getTotalAssets())?Double.valueOf(vo.getTotalAssets()):0;
					totalAssets+=assets;
				}
				if(totalAssets>0){
					Iterator iterator=portfolioList.iterator();
					while (iterator.hasNext()) {
						PortfolioAssetsVO vo = (PortfolioAssetsVO) iterator.next();
						double assets=null!=vo.getTotalAssets() && !"".equals(vo.getTotalAssets())?Double.valueOf(vo.getTotalAssets()):0;
					    String weight=StrUtils.getNumberString(assets/totalAssets*100, "###0.00");
					    vo.setAssetRate(Double.valueOf(weight));
					}
				}
			}
			model.put("portfolioList", portfolioList);
			ProposalNumberVO proposalNumberVO=portfolioHoldService.findProposalNumberOfMember(loginUser.getId());
			proposalNumberVO.setTips(PropertyUtils.getPropertyValue(langCode,"assets.proposal.tips",new Object[]{proposalNumberVO.getTotalCount(),proposalNumberVO.getToConfirmCount()}));
			model.put("proposalNumber", proposalNumberVO);
			List proposalList=crmProposalService.findMyProposal(loginUser.getId(), defCurrency, langCode,dateFormat);
			model.put("proposalList", proposalList);
			
			AccountNumberVO accountNumberVO=portfolioHoldService.findAccountNumberOfMember(loginUser.getId());
			InvestorAccount account=new InvestorAccount();
			account.setMember(loginUser);
			account.setOpenStatus("");
			account.setBaseCurrency(defCurrency);
			List<AccountVO> list=investorService.findAllAccountList(account);
			model.put("accountList", list);
			accountNumberVO.setTips(PropertyUtils.getPropertyValue(langCode,"assets.account.tips",new Object[]{accountNumberVO.getTotalCount(),accountNumberVO.getAuditedCount()}));
			model.put("accountNumber", accountNumberVO);
			
			//List<CharDataVO> chatList=new ArrayList<CharDataVO>();
			List<HoldChartVO> chartValueList=new ArrayList<HoldChartVO>();
			
			String numFormat="#,##0.00";
			if("JPY".equals(defCurrency)){
				numFormat="#,##0";
			}
			
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
			List<HoldProductVO> fundList=tradeCoreService.findHoldFundList("", defCurrency, langCode,loginUser.getId());
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
					//fundYesterday+=vo.getYesterdayPl();
					if(null!=vo.getYesterdayPl()){
						fundYesterday=null==fundYesterday?vo.getYesterdayPl():fundYesterday+vo.getYesterdayPl();
						
					}
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
			List<HoldProductVO>  stockList=tradeCoreService.findHoldStockList("", defCurrency, langCode,loginUser.getId());
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
			
			List<HoldProductVO>  bondList=tradeCoreService.findHoldBond("", defCurrency, langCode,loginUser.getId());
			if(null!=bondList && !bondList.isEmpty()){
				Iterator it=bondList.iterator();
				double totalUnit=0;
				while (it.hasNext()) {
					HoldProductVO vo = (HoldProductVO) it.next();
					double unit=Double.valueOf(vo.getHoldUnit());
					double market=Double.valueOf(vo.getMarketValue());
					//double pl=Double.valueOf(vo.getYesterdayPl());
					bondMarket+=market;
					//bondPl+=pl;
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
			
			List<HoldProductVO>  insureList=tradeCoreService.findHoldInsure("", defCurrency, langCode,loginUser.getId());
			if(null!=insureList && !insureList.isEmpty()){
				Iterator it=insureList.iterator();
				double totalUnit=0;
				while (it.hasNext()) {
					HoldProductVO vo = (HoldProductVO) it.next();
					double unit=Double.valueOf(vo.getHoldUnit());
					double market=Double.valueOf(vo.getMarketValue());
					//double pl=Double.valueOf(vo.getYesterdayPl());
					insureMarket+=market;
					//insurePl+=pl;
					totalUnit+=unit;
					//insureYesterday+=vo.getYesterdayPl();
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
				/*CharDataVO chart=new CharDataVO();
				chart.setName(type.getId());
				chart.setValue(fundMarket);
				chatList.add(chart);*/
				
				HoldChartVO vo=new HoldChartVO();
				vo.setName(type.getId());
				vo.setDisplayColor(type.getDisplayColor());
				vo.setMarketValue(String.valueOf(fundMarket));
				vo.setMarketValueStr(StrUtils.getNumberString(fundMarket, numFormat));
				vo.setPlValue(String.valueOf(fundPl));
				vo.setHoldUnit(String.valueOf(fundUnit));
				//vo.setYesterdayPl(StrUtils.getNumberString(fundYesterday, "#,##0.00"));
				if(null!=fundYesterday)
    				  vo.setYesterdayPl(StrUtils.getNumberString(fundYesterday, numFormat));
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
				vo.setMarketValueStr(StrUtils.getNumberString(stockMarket, numFormat));
				//vo.setYesterdayPl(StrUtils.getNumberString(stockYesterday, "#,##0.00"));
				if(null!=stockYesterday)
  				  vo.setYesterdayPl(StrUtils.getNumberString(stockYesterday, numFormat));
  				else {
  					 vo.setYesterdayPl("-");
				}
				chartValueList.add(vo);
			}
			if(bondUnit!=0){
				ProductType type=findProductType(productTypeList, CommonConstantsWeb.WEB_PRODUCT_TYPE_BOND);
				HoldChartVO vo=new HoldChartVO();
				vo.setName(type.getId());
				vo.setDisplayColor(type.getDisplayColor());
				vo.setMarketValue(String.valueOf(bondMarket));
				vo.setMarketValueStr(StrUtils.getNumberString(bondMarket, numFormat));
				//vo.setYesterdayPl(StrUtils.getNumberString(bondYesterday, "#,##0.00"));
				if(null!=bondYesterday)
	  				  vo.setYesterdayPl(StrUtils.getNumberString(bondYesterday, numFormat));
	  				else {
	  					 vo.setYesterdayPl("-");
					}
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
				vo.setMarketValueStr(StrUtils.getNumberString(insureMarket, numFormat));
				//vo.setYesterdayPl(StrUtils.getNumberString(insureYesterday, "#,##0.00"));
				if(null!=insureYesterday)
	  				  vo.setYesterdayPl(StrUtils.getNumberString(insureYesterday, numFormat));
	  				else {
	  					 vo.setYesterdayPl("-");
					}
				vo.setPlValue(String.valueOf(insurePl));
				vo.setHoldUnit(String.valueOf(insureUnit));
				chartValueList.add(vo);
			}
			double totalCash=tradeCoreService.findPortfolioCash("", defCurrency,loginUser.getId());
			if(totalCash!=0){
				ProductType type=findProductType(productTypeList, CommonConstantsWeb.WEB_PRODUCT_TYPE_CASH);
				HoldChartVO vo=new HoldChartVO();
				vo.setName(type.getId());
				vo.setDisplayColor(type.getDisplayColor());
				vo.setMarketValue(String.valueOf(totalCash));
				vo.setMarketValueStr(StrUtils.getNumberString(totalCash, numFormat));
				vo.setPlValue("");
				chartValueList.add(vo);
			}
			model.put("chartListJson", JsonUtil.toJson(chartValueList));
			model.put("chartList", chartValueList);
			
			model.put("fundList", fundList);
			model.put("stockList", stockList);
			model.put("bondList", bondList);
			model.put("insureList", insureList);
			
			String displayColor=loginUser.getDefDisplayColor();
			if(null==displayColor || "".equals(displayColor)){
			    displayColor=CommonConstants.DEF_DISPLAY_COLOR;
			}
			model.put("displayColor", displayColor);
			
			model.put("currencyCode", defCurrency);
			
			
			model.put("numFormat", numFormat);
			
			defCurrency=sysParamService.findNameByCode(defCurrency, langCode);
			model.put("currency", defCurrency);
			
			
			
			model.put("dateFormat", dateFormat);
			return "ifa/discover/community/myAssets";
			//return "investor/zone/myAssets";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}		
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
	 * 获取时间差
	 * @author mqzou
	 * @date 2016-10-10
	 * @param beginDate
	 * @param endDate
	 * @return
	 *//*
	private String getDifferDateTimeStr(String beginDate,String endDate){
		long differTime=0;
		String differUnit="";
		long differ=DateUtil.compareDateStr(beginDate, endDate);
		long minute=differ/(1000*60);
		long hour=minute/60;
		long day=hour/24;
		long month=day/30;
		long year=month/12;
	    if (minute<60) {
	    	differUnit="Minute";
	    	if(minute<1){
	    		differTime=1;
	    	}else {
	    		differTime=minute;
			}
		}else if ( hour<24) {
			differUnit="Hour";
			differTime=hour;
		}else if ( day<30) {
			differUnit="Day";
			differTime=day;
			
		}else if (month<12) {
			differUnit="Month";
			differTime=month;
		}else  {
			differUnit="Year";
			differTime=year;
		}
	    if(1==differTime)
	    	differUnit=differUnit+"s";
	    return String.valueOf(differTime)+" "+differUnit;
	}*/
	
	/**
	 * 获取组合回报图表数据
	 * @author mqzou
	 * @date 2016-10-09
	 */
	@RequestMapping(value = "/getCumperfsData")
	public void getCumperfsData(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		//组合回报图表数据
		List<PortfolioHoldCumperfSimpleVO> list=countPortfolioHoldCumperfs(request, model);
		ResponseUtils.renderHtml(response,JsonUtil.toJson(list));
	}
	
	/**
	 * 获取资产收益图表数据
	 * @author mqzou
	 * @date 2017-06-06
	 */
	@RequestMapping(value = "/getAssetCumperfsData")
	public void getAssetCumperfsData(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		//获取发布日期条件
        String period = StrUtils.getString(request.getParameter("period"));
        String currency=StrUtils.getString(request.getParameter("currency"));
        if(null==currency || "".equals(currency))
        	currency=loginUser.getDefCurrency();
        if(null==currency || "".equals(currency))
        	currency=CommonConstants.DEF_CURRENCY;
		String startDate = "";//DateUtil.getCurDateStr();
		String endDate =DateUtil.getCurDateStr(); //DateUtil.getCurDateStr(Calendar.DATE, 1);//
		if ("1W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
		if ("2W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -14);
		if ("1M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
		if ("3M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
		if ("6M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
		if ("1Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
		if ("3Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
		if ("5Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
		if ("YTD".equals(period)) startDate = DateUtil.getDateStr(DateUtil.getCurrYearFirst());
		//if("".equals(period))startDate="";
		else {
			String[] dates = period.split("to");
			if (null != dates && dates.length ==2) {
				startDate = dates[0].trim();
				endDate = dates[1].trim();
			}
		}
		
		
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat))
			dateFormat=CommonConstants.FORMAT_DATE;
		List<MyAssetChartDataVO> list=investorService.findAssetCumperfsData(loginUser.getId(), startDate, endDate, dateFormat,currency);
		ResponseUtils.renderHtml(response,JsonUtil.toJson(list));
	}
	
	
	/**
	 * 组合回报图表数据(公共方法)
	 * @author mqzou
	 * @date 2016-10-09
	 * @return
	 */
	private List<PortfolioHoldCumperfSimpleVO>  countPortfolioHoldCumperfs(HttpServletRequest request, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		//获取发布日期条件
        String period = StrUtils.getString(request.getParameter("period"));
        String portfolioId=StrUtils.getString(request.getParameter("portfolioId"));
        String currency=StrUtils.getString(request.getParameter("currency"));
        if(null==currency || "".equals(currency))
        	currency=loginUser.getDefCurrency();
        if(null==currency || "".equals(currency))
        	currency=CommonConstants.DEF_CURRENCY;
		String startDate = "";//DateUtil.getCurDateStr();
		String endDate =DateUtil.getCurDateStr(); //DateUtil.getCurDateStr(Calendar.DATE, 1);//
		if ("1W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
		if ("2W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -14);
		if ("1M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
		if ("3M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
		if ("6M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
		if ("1Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
		if ("3Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
		if ("5Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
		if ("YTD".equals(period)) startDate = DateUtil.getDateStr(DateUtil.getCurrYearFirst());
		//if("".equals(period))startDate="";
		else {
			String[] dates = period.split("to");
			if (null != dates && dates.length ==2) {
				startDate = dates[0].trim();
				endDate = dates[1].trim();
			}
		}
		
		String memberId="";
		if(null==portfolioId || "".equals(portfolioId)){
			memberId=loginUser.getId();
		}
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat))
			dateFormat=CommonConstants.FORMAT_DATE;
		//List<PortfolioHoldCumperf> list=portfolioHoldService.getCountPortfolioHoldCumperfs(loginUser.getId(), startDate, endDate,portfolioId);
		String strCondition=" and r.`valuation_date` between '"+startDate+"' and '"+endDate+"'";
		InvestorPortfolioDataVO dataVo=portfolioHoldService.getPortfolioHoldCumperf(portfolioId, currency, strCondition,memberId,dateFormat);
		List<PortfolioHoldCumperfSimpleVO> cumperfList=new ArrayList<PortfolioHoldCumperfSimpleVO>();
		if(null!=dataVo){
			cumperfList=dataVo.getMyPortfolioList();
		}
		return cumperfList;
		
		/*List<PortfolioHoldCumperfVO> resultList=new ArrayList<PortfolioHoldCumperfVO>();
		if(null!=list && !list.isEmpty()){
			String dateFormat=loginUser.getDateFormat();
			if(null==dateFormat || "".equals(dateFormat)){
				dateFormat="yyyy-MM-dd";
			}
			Iterator<PortfolioHoldCumperf> it=list.iterator();
			while (it.hasNext()) {
				PortfolioHoldCumperf cumperf = (PortfolioHoldCumperf) it.next();
				PortfolioHold hold=cumperf.getPortfolioHold();
				String portfolioCur=null!=hold && null!=hold.getPortfolio()?hold.getPortfolio().getBaseCurrency():hold.getBaseCurrency();
				if(null!=portfolioCur && !"".equals(portfolioCur) && !currency.equals(portfolioCur)){
					WebExchangeRate rate=fundInfoService.findExchangeRate(portfolioCur, currency);
    		    	if(null!=rate){
    		    		cumperf.setDayPl(cumperf.getDayPl()*rate.getRate());
    		    		cumperf.setCumulativePl(cumperf.getCumulativePl()*rate.getRate());
    		    	}
				}
				PortfolioHoldCumperfVO vo=new PortfolioHoldCumperfVO(cumperf);
				vo.setValuationDate(DateUtil.getDateStr(vo.getValuationDate(), "yyyy-MM-dd", dateFormat));
				resultList.add(vo);
			}
		}*/
		
	}
	
	@RequestMapping(value = "/clientAssets", method = RequestMethod.GET)
	public String clientAssets(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String memberId=request.getParameter("memberId");
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		if(null!=ssoVO){
			MemberIndividual individual=memberBaseService.findIndividualMember(memberId);
			model.put("individual", individual);
			
			/*String check=request.getParameter("check");
			
			//验证账户登录
			if(check==null || check=="0"){
				AccountValidVO validVO=iTraderSendService.saveCheckExistValid(request, ssoVO.getId());
				if(null!=validVO){
					model.put("checkList", validVO.getValidAccountNo());
					model.put("accountType", CommonConstantsWeb.WEB_ACCOUNTCHECK_IFA);
				}
				
			}*/
			
			String defCurrency=ssoVO.getDefCurrency();
			if(null==defCurrency || "".equals(defCurrency)){
				defCurrency=CommonConstants.DEF_CURRENCY;
			}
			String dateFormat=ssoVO.getDateFormat();
			if(null==dateFormat || "".equals(dateFormat)){
				dateFormat="yyyy-MM-dd";
			}
			AssetsVO assetsVO=new AssetsVO();
			assetsVO.setTotalMarketValue(0);
			/*assetsVO.setTotalReturnRate(0);
			assetsVO.setTotalReturnValue(0);*/
			List<AssetsVO> assetsList=portfolioHoldService.getMyAssets(memberId,"",ssoVO.getIfaId());
			//转换为默认货币并合计
			if(null!=assetsList && !assetsList.isEmpty()){
				Iterator<AssetsVO> it=assetsList.iterator();
				while (it.hasNext()) {
					AssetsVO vo = (AssetsVO) it.next();
					
					if(!defCurrency.equals(vo.getBaseCurrency())){
						WebExchangeRate rate=fundInfoService.findExchangeRate(vo.getBaseCurrency(),defCurrency);
						if(null!=rate){
							assetsVO.setTotalReturnValue(assetsVO.getTotalReturnValue()+vo.getTotalReturnValue()*rate.getRate());
						}
						
					}else {
						assetsVO.setTotalReturnValue(assetsVO.getTotalReturnValue()+vo.getTotalReturnValue());
					}
					assetsVO.setTotalReturnRate(assetsVO.getTotalReturnRate()+vo.getTotalReturnRate());
				}
			}
			assetsVO.setTotalReturnRate(assetsVO.getTotalReturnRate()*100);
			
			assetsVO.setTotalReturnRateStr(StrUtils.getNumberString(assetsVO.getTotalReturnRate()));
			assetsVO.setTotalReturnValueStr(StrUtils.getNumberString(assetsVO.getTotalReturnValue()));
			

			InvestorAccountCurrencyVO accountCurrency=investorService.findInvestorCurrency(memberId, defCurrency,ssoVO.getIfaId());
			if(null!=accountCurrency){
				assetsVO.setTotalAssets(accountCurrency.getTotalAssets());
				assetsVO.setTotalCashValue(accountCurrency.getCashValue());
				assetsVO.setTotalMarketValue(accountCurrency.getMarketValue());
			}
			model.put("assetsVO", assetsVO);
			
			List<PortfolioAssetsVO> portfolioList=portfolioHoldService.getPortfolioAssets(memberId,"",ssoVO.getIfaId(),"",defCurrency);
			if(null!=portfolioList && !portfolioList.isEmpty()){
				Iterator<PortfolioAssetsVO> it=portfolioList.iterator();
				while (it.hasNext()) {
					PortfolioAssetsVO vo = (PortfolioAssetsVO) it.next();
					double assets=portfolioHoldService.findPortfolioAuM(vo.getPortfolioId(), defCurrency);
					vo.setTotalAssets(String.valueOf(assets));
					/*vo.setTotalCashValue("1000");
					vo.setTotalAssets(String.valueOf(Double.valueOf(vo.getTotalMarketValue())+Double.valueOf(vo.getTotalCashValue())));*/
					vo.setTotalReturnRate(Double.valueOf(vo.getTotalReturnRate())*100);
					vo.setTotalReturnRateStr(String.valueOf(vo.getTotalReturnRate()));
					if(assetsVO.getTotalAssets()!=0)
					 vo.setTotalAssetsRate(Double.valueOf(vo.getTotalAssets())/assetsVO.getTotalAssets()*100);
					else {
						vo.setTotalAssetsRate(0);
					}
					WebExchangeRate rate=fundInfoService.findExchangeRate(vo.getBaseCurrency(),defCurrency);
					if(null!=rate){
						vo.setTotalReturnValue(vo.getTotalReturnValue()*rate.getRate());
						vo.setTotalReturnRateStr(String.valueOf(vo.getTotalReturnValue()));
					}
				}
			}
			model.put("portfolioList", portfolioList);
			
			
			String displayColor=ssoVO.getDefDisplayColor();
			if(null==displayColor || "".equals(displayColor)){
			    displayColor=CommonConstants.DEF_DISPLAY_COLOR;
			}
			model.put("displayColor", displayColor);
			
			model.put("currency", defCurrency);
			return "investor/zone/clientAssets";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		
	}
	
	/**
	 * 获取IFA客户的收益图表
	 * @author mqzou 2017-02-20
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/clientHoldCumperfs")
	public void findClentPortfolioHoldCumperfs(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		//获取发布日期条件
        String period = StrUtils.getString(request.getParameter("period"));
        String portfolioId=StrUtils.getString(request.getParameter("portfolioId"));
        String currency=StrUtils.getString(request.getParameter("currency"));
        String memberId=StrUtils.getString(request.getParameter("memberId"));
        if(null==currency || "".equals(currency))
        	currency=loginUser.getDefCurrency();
        if(null==currency || "".equals(currency))
        	currency=CommonConstants.DEF_CURRENCY;
		String startDate = "";//DateUtil.getCurDateStr();
		String endDate =DateUtil.getCurDateStr(); //DateUtil.getCurDateStr(Calendar.DATE, 1);//
		if ("1W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
		if ("2W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -14);
		if ("1M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
		if ("3M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
		if ("6M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
		if ("1Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
		if ("3Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
		if ("5Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
		if ("YTD".equals(period)) startDate = DateUtil.getDateStr(DateUtil.getCurrYearFirst());
		//if("".equals(period))startDate="";
		else {
			String[] dates = period.split("to");
			if (null != dates && dates.length ==2) {
				startDate = dates[0].trim();
				endDate = dates[1].trim();
			}
		}
		
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat))
			dateFormat=CommonConstants.FORMAT_DATE;
		//List<PortfolioHoldCumperf> list=portfolioHoldService.getCountPortfolioHoldCumperfs(loginUser.getId(), startDate, endDate,portfolioId);
		String strCondition=" and r.`valuation_date` between '"+startDate+"' and '"+endDate+"'";
		InvestorPortfolioDataVO dataVo=portfolioHoldService.getPortfolioHoldCumperf(portfolioId, currency, strCondition,memberId,dateFormat);
		List<PortfolioHoldCumperfSimpleVO> cumperfList=new ArrayList<PortfolioHoldCumperfSimpleVO>();
		if(null!=dataVo){
			cumperfList=dataVo.getMyPortfolioList();
		}
		JsonUtil.toWriter(cumperfList, response);
	}
}
